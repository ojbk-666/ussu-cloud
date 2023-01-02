package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.entity.DcUserInfo;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.service.IDcUserInfoService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;

/**
 * 采集用户信息线程
 */
public class SaveUserInfoThread extends Thread {

    private String dcUn;
    private String dcPwd;
    private String ip;

    public SaveUserInfoThread(String username, String pwd, String ip) {
        this.dcUn = username;
        this.dcPwd = pwd;
        this.ip = ip;
    }

    @Override
    public void run() {
        if (StrUtil.isBlank(dcUn) || StrUtil.isBlank(dcPwd)) {
            return;
        }
        // 测试旧cookie是否仍有效
        // DcUserInfo one = SpringUtil.getBean(IDcUserInfoService.class).getOne(Wrappers.lambdaQuery(DcUserInfo.class).eq(DcUserInfo::getLoginName, dcUn));
        // if (one != null) {
        // }
        // 获取一个cookie
        DczxLoginResultVo loginResultVo = DczxUtil.login(dcUn, dcPwd);
        if (loginResultVo != null) {
            saveUserInfo(loginResultVo);
            SaveDczxCourseByAllStudyPlanThread saveDczxCourseByAllStudyPlanThread = new SaveDczxCourseByAllStudyPlanThread(loginResultVo);
            ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean(ThreadPoolTaskExecutor.class);
            threadPoolTaskExecutor.submit(saveDczxCourseByAllStudyPlanThread);
        }
        // 获取并保存用户信息
        // 采集课程信息
    }

    /**
     * 用户信息入库
     */
    private synchronized void saveUserInfo(DczxLoginResultVo loginResultVo) {
        DcUserInfo userInfoFromRemote = null;
        Date now = new Date();
        DcInterfaceLog dcInterfaceLog = new DcInterfaceLog().setDelFlag(false).setResult(true).setUserid(loginResultVo.getUserId())
                .setRemarks("采集用户信息线程").setUrl("https://classroom.edufe.com.cn/UserProfile").setCreateTime(now);
        try {
            userInfoFromRemote = getUserInfoFromRemote(loginResultVo, dcInterfaceLog);
        } catch (Exception e) {
            e.printStackTrace();
            dcInterfaceLog.setResult(false).setReason("接口解析异常或json格式化失败");
        } finally {
            DczxUtil.saveInterfaceLog(dcInterfaceLog);
        }
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        IDcUserInfoService userInfoService = SpringUtil.getBean(IDcUserInfoService.class);
        DcUserInfo one = userInfoService.getById(loginResultVo.getUserId());
        if (userInfoFromRemote != null) {
            userInfoFromRemote.setInterfaceLogId(dcInterfaceLog.getId()).setLoginIp(ip).setUpdateTime(now);
            if (one == null) {
                userInfoFromRemote.setCreateTime(now);
                userInfoService.save(userInfoFromRemote);
            } else {
                userInfoService.updateById(userInfoFromRemote);
            }
        }
    }

    /**
     * 从接口获取用户信息
     */
    private DcUserInfo getUserInfoFromRemote(DczxLoginResultVo loginResultVo, DcInterfaceLog dcInterfaceLog) {
        String url = "https://classroom.edufe.com.cn" + "/api/v1/myclassroom/basicInfo";
        MyHttpResponse response = MyHttpRequest.createPost(url).disableCookie().cookie(loginResultVo.getRequestCookie())
                .headerMap(loginResultVo.getHeaderMap(), true).execute();
        if (response.isOk()) {
            String body = response.body();
            dcInterfaceLog.setResponseBody(body);
            DcUserInfo dcUserInfo = JSONUtil.toBean(body, DcUserInfo.class);
            dcUserInfo.setStuPic(loginResultVo.getStuPic()).setJsessionid(loginResultVo.getJsessionid())
                    .setLoginName(loginResultVo.getLoginName()).setPassword(loginResultVo.getPassword());
            return dcUserInfo;
        }
        return null;
    }

}
