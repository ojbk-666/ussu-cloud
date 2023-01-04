package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.support.qinglong.dto.EnvListDTO;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.entity.SheepEnv;
import cc.ussu.modules.sheep.service.WsKeyToCookieService;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.wskey.JdWsKeyExpiredException;
import cc.ussu.modules.sheep.task.jd.util.JdCkWskUtil;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.wskey.JdWsKeyVO;
import cc.ussu.modules.sheep.task.service.QingLong5700Service;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 京东wskey转ck
 */
@Component
@DisallowConcurrentExecution
public class JdWskeyToCkTask extends SheepQuartzJobBean<JdWsKeyVO> {

    private ThreadLocal<JdWsKeyVO> threadLocal = new ThreadLocal<>();

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
    }

    @Override
    public String getTaskName() {
        return "京东wskey转ck";
    }

    @Override
    public List<JdWsKeyVO> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_WSCK).stream()
                .filter(StrUtil::isNotBlank).map(JdCkWskUtil::convertToWsKey).collect(Collectors.toList());
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/jd/wskey/" + fileName + ".log";
    }

    @Override
    public void doTask(List<JdWsKeyVO> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", params.size());
        for (JdWsKeyVO jdWsKeyVO : params) {
            loggerThreadLocal.get().info("账号 {} 开始执行", jdWsKeyVO.getPin());
            threadLocal.set(jdWsKeyVO);
            try {
                updateCkByWsKey();
                Thread.sleep(3000);
            } catch (JdWsKeyExpiredException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (RequestErrorException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (ResponseResultException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                loggerThreadLocal.get().info("未知异常：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 执行完毕", jdWsKeyVO.getPin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

    private WsKeyToCookieService getWsKeyToCookieService() {
        return SpringUtil.getBean(WsKeyToCookieService.class);
    }

    private QingLong5700Service getQingLongService() {
        return SpringUtil.getBean(QingLong5700Service.class);
    }

    private void updateCkByWsKey() throws JdWsKeyExpiredException, RequestErrorException, ResponseResultException {
        String pin = threadLocal.get().getPin();
        // 查询该ck
        if (StrUtil.isBlank(pin)) {
            loggerThreadLocal.get().info("wskey格式无效 跳过");
            return;
        }
        loggerThreadLocal.get().info("获取环境变量的ck信息");
        List<SheepEnv> collect = getEnvService().queryList(pin).stream().filter(r -> JdConstants.JD_COOKIE.equals(r.getName())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect)) {
            SheepEnv first = CollUtil.getFirst(collect);
            String value = first.getValue();
            JdCookieVO jdCookieVO = new JdCookieVO(value);
            // 验证有效性
            if (validateJdCookie(jdCookieVO)) {
                loggerThreadLocal.get().info("{} ck有效，跳过", pin);
                if (BooleanUtil.isTrue(first.getDisabled())) {
                    loggerThreadLocal.get().info("有效ck已被禁用，正在启用");
                    getEnvService().enable(first.getId());
                }
                updateQingLongEnv(jdCookieVO);
            } else {
                loggerThreadLocal.get().info("检测到ck失效，准备更新");
                JdCookieVO newCk = convertByWsKey();
                getEnvService().updateById(new SheepEnv().setId(first.getId()).setDisabled(false).setValue(newCk.toString()));
                updateQingLongEnv(jdCookieVO);
            }
        } else {
            // 环境中没有该ck 转换后添加
            JdCookieVO jdCookieVO = convertByWsKey();
            loggerThreadLocal.get().info("检测到新的ck，准备添加");
            getEnvService().save(new SheepEnv().setDisabled(false).setName(JdConstants.JD_COOKIE).setValue(jdCookieVO.toString()));
            updateQingLongEnv(jdCookieVO);
        }
    }

    private boolean validateJdCookie(JdCookieVO jdCookieVO) {
        loggerThreadLocal.get().info("检查ck有效性");
        return getWsKeyToCookieService().checkCk(jdCookieVO);
    }

    private JdCookieVO convertByWsKey() throws JdWsKeyExpiredException, RequestErrorException, ResponseResultException {
        loggerThreadLocal.get().info("账号 {} 开始转换", threadLocal.get().getPin());
        JdCookieVO jdCookieVO = getWsKeyToCookieService().getCkByWskey(threadLocal.get().toString());
        loggerThreadLocal.get().info("账号 {} ck转换成功：{}", threadLocal.get().getPin(), jdCookieVO.toString());
        return jdCookieVO;
    }

    private EnvListDTO.EnvDTO filterQingLongEnvVo(List<EnvListDTO.EnvDTO> envVoList) {
        if (envVoList == null) {
            return null;
        }
        for (EnvListDTO.EnvDTO envVo : envVoList) {
            if (JdConstants.JD_COOKIE.equals(envVo.getName())) {
                return envVo;
            }
        }
        return null;
    }

    /**
     * 更新青龙ck
     */
    private void updateQingLongEnv(JdCookieVO jdCookieVO) {
        String pin = jdCookieVO.getPt_pin();
        // 检查ck
        EnvListDTO envListDTO = getQingLongService().getEnvList(jdCookieVO.getPt_pin());
        List<EnvListDTO.EnvDTO> envList = envListDTO.getData();
        EnvListDTO.EnvDTO ck = filterQingLongEnvVo(envList);
        if (ck == null) {
            // 没有则添加
            getQingLongService().saveEnv(new EnvListDTO.EnvDTO().setName(JdConstants.JD_COOKIE).setValue(jdCookieVO.toString()));
            loggerThreadLocal.get().info("青龙京东ck已添加：{}", pin);
        } else {
            // 检查ck有效性，值相同的不变，不同表示已失效需要更新
            if (jdCookieVO.toString().equals(ck.getValue())) {
                // 仍然有效
                loggerThreadLocal.get().info("青龙京东ck未失效：{}", pin);
                // 是否被禁用
                enableQingLongEnv(ck);
            } else {
                enableQingLongEnv(ck);
                EnvListDTO.EnvDTO u = new EnvListDTO.EnvDTO().setId(ck.getId()).setName(JdConstants.JD_COOKIE)
                        .setValue(jdCookieVO.toString()).setRemarks(ck.getRemarks());
                getQingLongService().updateEnv(u);
                loggerThreadLocal.get().info("青龙京东ck已更新：{}", pin);
            }
        }
    }

    private void enableQingLongEnv(EnvListDTO.EnvDTO envDTO) {
        if (envDTO.getStatus().equals(EnvListDTO.STATUS_DISABLE)) {
            loggerThreadLocal.get().info("检测到青龙京东ck已被禁用，开始启用");
            getQingLongService().enableEnv(CollUtil.newArrayList(envDTO.getId()));
        }
    }

}
