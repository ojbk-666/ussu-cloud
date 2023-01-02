package cc.ussu.modules.dczx.service.impl;

import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.exception.GetAllStudyPlanFailedException;
import cc.ussu.modules.dczx.exception.LoginFailedException;
import cc.ussu.modules.dczx.model.vo.AllStudyPlanVO;
import cc.ussu.modules.dczx.model.vo.CurrentStudyPlanVO;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.service.DczxService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DczxServiceImpl implements DczxService {

    public static final int TIMEOUT = 3000;
    @Autowired
    private RedisService redisService;

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    @Override
    public DczxLoginResultVo login(String username, String password) throws LoginFailedException {
        DczxLoginResultVo loginResultVo = redisService.getCacheObject(DczxConstants.CACHE_JSESSIONID_PREFIX + username);
        if (loginResultVo != null) {
            // 验证ck有效性
            if (verifyCookieExpired(loginResultVo)) {
                return loginResultVo;
            } else {
                loginResultVo = null;
            }
        } else {
            synchronized (this) {
                if (loginResultVo != null) {
                    return loginResultVo;
                } else {
                    // 开始登录
                    Map<String, Object> param = new HashMap<>();
                    param.put("loginName", username);
                    param.put("password", password);
                    param.put("callerId", "0003");
                    param.put("customerCode", "1");
                    String url = "https://classroom.edufe.com.cn/api/v1/myclassroom/login";
                    HttpResponse response = HttpUtil.createPost(url)
                        .form(param).contentType(ContentType.FORM_URLENCODED.getValue())
                        .execute();
                    if (response.isOk()) {
                        String body = response.body();
                        String jsessionid = response.getCookieValue(DczxConstants.JSESSIONID);
                        try {
                            loginResultVo = JSONUtil.toBean(body, DczxLoginResultVo.class);
                        } catch (Exception e) {
                            throw new LoginFailedException("登录失败：响应体格式化失败");
                        }
                        if (loginResultVo != null) {
                            loginResultVo.setJsessionid(jsessionid).setLoginName(username).setPassword(password);
                            SpringUtil.getBean(RedisService.class).setCacheObject(DczxConstants.CACHE_JSESSIONID_PREFIX + username, loginResultVo, 60L * 60 * 2);
                            return loginResultVo;
                        }
                    } else if (response.getStatus() == 401) {
                        String body = response.body();
                        if (StrUtil.isNotBlank(body) && response.header(Header.CONTENT_TYPE).contains("application/json")) {
                            JSONObject jsonObject = JSONUtil.parseObj(body);
                            throw new LoginFailedException("登录东财在线网站失败：" + jsonObject.getStr("msg"));
                        } else {
                            throw new LoginFailedException("登录失败：401");
                        }
                    } else {
                        throw new LoginFailedException("登录失败：网络错误：" + response.getStatus());
                    }
                }
            }
        }
        throw new LoginFailedException("登录失败，未知错误");
    }

    /**
     * 验证ck有效性
     * @param loginResultVo
     * @return
     */
    @Override
    public boolean verifyCookieExpired(DczxLoginResultVo loginResultVo) {
        HttpResponse execute = HttpUtil.createPost("https://classroom.edufe.com.cn/api/v1/myclassroom/refreshMainInfo")
            .headerMap(loginResultVo.getHeaderMap(), true)
            .header(Header.ORIGIN, "https://classroom.edufe.com.cn")
            .header(Header.REFERER, "https://classroom.edufe.com.cn/?a=1670576575")
            .disableCookie()
            .cookie(loginResultVo.getRequestCookie())
            .timeout(5000)
            .execute();
        if (execute.isOk()) {
            return true;
        } else if (401 == execute.getStatus()) {
            return false;
        }
        return false;
    }

    /**
     * 获取全部学习计划
     *
     * @param loginResultVo
     */
    @Override
    public List<AllStudyPlanVO> getAllStudyPlan(DczxLoginResultVo loginResultVo) {
        HttpResponse execute = HttpRequest.post("https://classroom.edufe.com.cn/api/v1/myclassroom/allStudyPlan?userId=" + loginResultVo.getUserId())
                .headerMap(loginResultVo.getHeaderMap(), true)
                .cookie(loginResultVo.getRequestCookie())
                .timeout(TIMEOUT)
                .execute();
        if (execute.isOk()) {
            return JSONUtil.toList(execute.body(), AllStudyPlanVO.class);
        } else {
            throw new GetAllStudyPlanFailedException("获取全部学习计划失败：" + execute.getStatus());
        }
    }

    /**
     * 获取当前学习计划
     */
    @Override
    public List<CurrentStudyPlanVO> getCurrentStudyPlan(DczxLoginResultVo loginResultVo) {
        HttpResponse execute = HttpUtil.createPost("https://classroom.edufe.com.cn/api/v1/myclassroom/currentStudyPlan")
                .headerMap(loginResultVo.getHeaderMap(), true)
                .cookie(loginResultVo.getRequestCookie())
                .timeout(TIMEOUT)
                .execute();
        if (execute.isOk()) {
            return JSONUtil.toList(execute.body(), CurrentStudyPlanVO.class);
        }
        return null;
    }
}
