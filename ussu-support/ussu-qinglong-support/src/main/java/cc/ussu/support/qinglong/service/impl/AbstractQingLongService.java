package cc.ussu.support.qinglong.service.impl;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.support.qinglong.dto.*;
import cc.ussu.support.qinglong.exception.*;
import cc.ussu.support.qinglong.service.EnvService;
import cc.ussu.support.qinglong.service.TaskService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * 青龙实现类抽象
 */
public abstract class AbstractQingLongService implements EnvService, TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQingLongService.class);

    @Autowired
    private RedisService redisService;

    private Map<String, Object> getNewMap() {
        return new HashMap<>();
    }

    /**
     * 基础路径
     */
    public String getQLUrl(String s, Map<String, Object> urlParam) {
        if (urlParam == null) {
            urlParam = getNewMap();
        }
        urlParam.put("t", new Date().getTime() + "");
        return getQingLongConfig().getUrl() + "/open/" + s + "?" + HttpUtil.toParams(urlParam);
    }

    public String getQLUrl(String s) {
        return getQLUrl(s, null);
    }

    /**
     * 获取青龙访问 token
     */
    @Override
    public String getToken() {
        String key = getCacheKey() + ":token";
        // 尝试从缓存获取值
        String token = redisService.getCacheObject(key);
        if (StrUtil.isBlank(token)) {
            synchronized (this) {
                token = redisService.getCacheObject(key);
                if (StrUtil.isBlank(token)) {
                    String url = getQingLongConfig().getUrl() + "/open/auth/token?client_id=" +
                            getQingLongConfig().getClientId() + "&client_secret=" + getQingLongConfig().getClientSecret();
                    MyHttpResponse execute = MyHttpRequest.createGet(url).disableRedirect().execute();
                    if (execute.isOk()) {
                        AuthTokenResultDTO authTokenResultVo = JSONUtil.toBean(execute.body(), AuthTokenResultDTO.class);
                        token = authTokenResultVo.getData().getToken();
                        redisService.setCacheObject(key, token, 60L * 60 * 24 * 29);
                    } else {
                        LOGGER.error("请求青龙Token失败：{}", execute.getStatus());
                        throw new GetTokenException(execute.getStatus() + "->" + execute.body());
                    }
                }
            }
        }
        return token;
    }

    /**
     * 处理异常
     */
    public void handleException(MyHttpResponse httpResponse) {
        if (!httpResponse.isOk()) {
            if (httpResponse.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                // token失效了
                redisService.deleteObject(getCacheKey() + ":token");
            }
        }
    }

    /**
     * 获取token
     */
    public Map<String, String> getHeader() {
        Map<String, String> r = new HashMap<>();
        String qlToken = getToken();
        if (StrUtil.isNotBlank(qlToken)) {
            r.put("Authorization", "Bearer " + qlToken);
        }
        return r;
    }

    public MyHttpRequest.HttpRequestBuilder httpGet(String url) {
        return MyHttpRequest.createGet(url).headerMap(getHeader(), true);
    }

    public MyHttpRequest.HttpRequestBuilder httpPost(String url) {
        return MyHttpRequest.createPost(url).headerMap(getHeader(), true);
    }

    public MyHttpRequest.HttpRequestBuilder httpPut(String url) {
        return MyHttpRequest.createPut(url).headerMap(getHeader(), true);
    }

    public MyHttpRequest.HttpRequestBuilder httpDelete(String url) {
        return MyHttpRequest.createDelete(url).headerMap(getHeader(), true);
    }

    /**
     * 获取环境变量
     */
    @Override
    public EnvListDTO getEnvList(String searchValue) {
        Map<String, Object> p = getNewMap();
        if (StrUtil.isNotBlank(searchValue)) {
            p.put("searchValue", searchValue);
        }
        MyHttpResponse execute = httpGet(getQLUrl("envs", p)).execute();
        if (execute.isOk()) {
            EnvListDTO data = JSONUtil.toBean(execute.body(), EnvListDTO.class);
            if (!data.isSuccess()) {
                throw new GetEnvException(data.getMessage());
            }
            LOGGER.info("查询环境变量成功");
            return data;
        } else {
            handleException(execute);
            throw new GetEnvException();
        }
    }

    /**
     * 保存环境
     */
    @Override
    public void saveEnv(EnvListDTO.EnvDTO envDTO) {
        Map<String, Object> p = getNewMap();
        p.put("name", envDTO.getName());
        p.put("value", envDTO.getValue());
        p.put("remarks", envDTO.getRemarks());
        List<Map> maps = new LinkedList<>();
        maps.add(p);
        MyHttpResponse execute = httpPost(getQLUrl("envs")).body(JSONUtil.toJsonStr(maps)).execute();
        if (execute.isOk()) {
            BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!baseResultDTO.isSuccess()) {
                throw new SaveEnvException(baseResultDTO.getMessage());
            }
            LOGGER.info("环境变量{}保存成功：{}", envDTO.getName(), envDTO.getValue());
        } else {
            handleException(execute);
            throw new SaveEnvException();
        }
    }


    /**
     * 编辑环境变量
     *
     * @param env
     */
    @Override
    public void updateEnv(EnvListDTO.EnvDTO env) {
        Assert.notNull(env.getId(), "id不能为空");
        MyHttpResponse execute = httpPut(getQLUrl("envs")).body(JSONUtil.toJsonStr(env)).execute();
        if (execute.isOk()) {
            BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!baseResultDTO.isSuccess()) {
                throw new UpdateEnvException(baseResultDTO.getMessage());
            }
            LOGGER.info("环境变量{}:{}修改成功：{}", env.getId(), env.getName(), env.getValue());
        } else {
            handleException(execute);
            throw new UpdateEnvException();
        }
    }

    /**
     * 删除环境变量
     *
     * @param ids
     */
    @Override
    public void deleteEnv(List<Integer> ids) {
        MyHttpResponse execute = httpDelete(getQLUrl("envs")).body(JSONUtil.toJsonStr(ids)).execute();
        if (execute.isOk()) {
            BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!baseResultDTO.isSuccess()) {
                throw new DeleteEnvException(baseResultDTO.getMessage());
            }
            LOGGER.info("删除环境变量成功");
        } else {
            handleException(execute);
            throw new DeleteEnvException();
        }
    }

    /**
     * 禁用环境变量
     *
     * @param ids
     */
    @Override
    public void disableEnv(List<Integer> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            MyHttpResponse execute = httpPut(getQLUrl("envs/disable")).body(JSONUtil.toJsonStr(ids)).execute();
            if (execute.isOk()) {
                BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
                if (!baseResultDTO.isSuccess()) {
                    throw new DisableEnvException(baseResultDTO.getMessage());
                }
                LOGGER.info("禁用环境变量成功：{}", ids);
            } else {
                handleException(execute);
                throw new DisableEnvException();
            }
        }
    }

    /**
     * 启用环境变量
     *
     * @param ids
     */
    @Override
    public void enableEnv(List<Integer> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            MyHttpResponse execute = httpPut(getQLUrl("envs/enable")).body(JSONUtil.toJsonStr(ids)).execute();
            if (execute.isOk()) {
                BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
                if (!baseResultDTO.isSuccess()) {
                    throw new EnableEnvException(baseResultDTO.getMessage());
                }
                LOGGER.info("启用环境变量成功：{}", ids);
            } else {
                handleException(execute);
                throw new EnableEnvException();
            }
        }
    }

    /**
     * 获取任务列表
     *
     * @param searchValue
     */
    @Override
    public TaskListDTO getTaskList(String searchValue) {
        Map<String, Object> p = getNewMap();
        if (StrUtil.isNotBlank(searchValue)) {
            p.put("searchValue", searchValue);
        }
        MyHttpResponse execute = httpGet(getQLUrl("crons", p)).execute();
        if (execute.isOk()) {
            TaskListDTO taskListVo = JSONUtil.toBean(execute.body(), TaskListDTO.class);
            if (!taskListVo.isSuccess()) {
                throw new GetTaskException(taskListVo.getMessage());
            }
            // 列出脚本
            LOGGER.info("查询任务成功:{}", taskListVo);
            return taskListVo;
        } else {
            handleException(execute);
            throw new GetTaskException();
        }
    }

    /**
     * 获取任务详情
     */
    @Override
    public TaskDetailDTO getTaskDetail(Integer id) {
        MyHttpResponse execute = httpGet(getQLUrl("crons/" + id)).execute();
        if (execute.isOk()) {
            TaskDetailDTO taskDetailDTO = JSONUtil.toBean(execute.body(), TaskDetailDTO.class);
            if (!taskDetailDTO.isSuccess()) {
                throw new GetTaskException(taskDetailDTO.getMessage());
            }
            LOGGER.info("获取任务详情成功：{}", taskDetailDTO);
            return taskDetailDTO;
        } else {
            handleException(execute);
            throw new GetTaskException();
        }
    }

    /**
     * 获取任务日志
     */
    @Override
    public TaskLogDTO getTaskLog(Integer taskId) {
        MyHttpResponse execute = httpGet(getQLUrl("crons/" + taskId + "/log")).execute();
        if (execute.isOk()) {
            TaskLogDTO taskLogDTO = JSONUtil.toBean(execute.body(), TaskLogDTO.class);
            if (!taskLogDTO.isSuccess()) {
                throw new GetTaskLogException(taskLogDTO.getMessage());
            }
            LOGGER.info("获取任务日志成功");
            return taskLogDTO;
        } else {
            handleException(execute);
            throw new GetTaskLogException();
        }
    }

    /**
     * 添加任务
     *
     * @param task
     */
    @Override
    public void saveTask(TaskDTO task) {
        Map<String, Object> p = getNewMap();
        p.put("name", task.getName());
        p.put("command", task.getCommand());
        p.put("schedule", task.getSchedule());
        MyHttpResponse execute = httpPost(getQLUrl("crons")).body(JSONUtil.toJsonStr(p)).execute();
        if (execute.isOk()) {
            BaseResultDTO resultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!resultDTO.isSuccess()) {
                throw new SaveTaskException(resultDTO.getMessage());
            }
            LOGGER.info("保存任务日志成功");
        } else {
            handleException(execute);
            throw new SaveTaskException();
        }
    }

    /**
     * 编辑任务
     *
     * @param task
     */
    @Override
    public void updateTask(TaskDTO task) {
        Assert.notNull(task.getId(), "id不能为空");
        Map<String, Object> p = getNewMap();
        p.put("_id", task.getId());
        p.put("name", task.getName());
        p.put("command", task.getCommand());
        p.put("schedule", task.getSchedule());
        MyHttpResponse execute = httpPut(getQLUrl("crons")).body(JSONUtil.toJsonStr(p)).execute();
        if (execute.isOk()) {
            BaseResultDTO resultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!resultDTO.isSuccess()) {
                throw new UpdateTaskException(resultDTO.getMessage());
            }
            LOGGER.info("修改任务成功");
        } else {
            handleException(execute);
            throw new UpdateTaskException();
        }
    }

    /**
     * 删除任务
     *
     * @param ids
     */
    @Override
    public void deleteTask(List<Integer> ids) {
        MyHttpResponse execute = httpDelete(getQLUrl("crons")).body(JSONUtil.toJsonStr(ids)).execute();
        if (execute.isOk()) {
            BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!baseResultDTO.isSuccess()) {
                throw new DeleteTaskException(baseResultDTO.getMessage());
            }
            LOGGER.info("删除任务成功");
        } else {
            handleException(execute);
            throw new DeleteTaskException();
        }
    }

    /**
     * 禁用任务
     *
     * @param ids
     */
    @Override
    public void disableTask(List<Integer> ids) {
        MyHttpResponse execute = httpPut(getQLUrl("crons/disable")).body(JSONUtil.toJsonStr(ids)).execute();
        if (execute.isOk()) {
            BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!baseResultDTO.isSuccess()) {
                throw new DisableTaskException(baseResultDTO.getMessage());
            }
            LOGGER.info("禁用任务成功");
        } else {
            handleException(execute);
            throw new DisableTaskException();
        }
    }

    /**
     * 启用任务
     *
     * @param ids
     */
    @Override
    public void enableTask(List<Integer> ids) {
        MyHttpResponse execute = httpPut(getQLUrl("crons/enable")).body(JSONUtil.toJsonStr(ids)).execute();
        if (execute.isOk()) {
            BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!baseResultDTO.isSuccess()) {
                throw new EnableTaskException(baseResultDTO.getMessage());
            }
            LOGGER.info("启用任务成功");
        } else {
            handleException(execute);
            throw new EnableTaskException();
        }
    }

    /**
     * 运行任务
     *
     * @param ids
     */
    @Override
    public void runTask(List<Integer> ids) {
        MyHttpResponse execute = httpPut(getQLUrl("crons/run")).body(JSONUtil.toJsonStr(ids)).execute();
        if (execute.isOk()) {
            BaseResultDTO baseResultDTO = JSONUtil.toBean(execute.body(), BaseResultDTO.class);
            if (!baseResultDTO.isSuccess()) {
                throw new RunTaskException(baseResultDTO.getMessage());
            }
            LOGGER.info("运行任务成功");
        } else {
            handleException(execute);
            throw new RunTaskException();
        }
    }

}
