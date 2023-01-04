package cc.ussu.modules.sheep.task.jdbeans.service;

import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.entity.JdUserInfo;
import cc.ussu.modules.sheep.service.IJdUserInfoService;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jdbeans.vo.TotalBeanResponseVo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 更新数据库的京东用户信息
 */
@Component
@DisallowConcurrentExecution
public class UpdateDBJdUserInfoTask extends SheepQuartzJobBean<String> {

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "更新数据库的京东用户信息";
    }

    /**
     * 获取参数
     */
    @Override
    public List<String> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_COOKIE);
    }

    /**
     * 获取日志存放的相对路径
     *
     * @param fileName 文件名
     * @return 相对路径地址 例 /a/b/c/2022-10-16.log
     */
    @Override
    public String getLogRelativePath(String fileName) {
        return "/db/jd_userinfo/" + fileName + ".log";
    }

    /**
     * 执行任务
     *
     * @param params
     */
    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params));
        IJdUserInfoService jdUserInfoService = getJdUserInfoService();
        JdBeansService beansService = getJdBeansService();
        for (String ck : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(ck);
            loggerThreadLocal.get().info("账号 {} 开始执行", jdCookieVO.getPt_pin());
            try {
                loggerThreadLocal.get().info("请求 totalBean 接口");
                checkStop();
                TotalBeanResponseVo.TotalBeanResponseDataVo totalBeanResponseDataVo = beansService.totalBean(ck);
                TotalBeanResponseVo.UserInfo userInfo = totalBeanResponseDataVo.getUserInfo();
                TotalBeanResponseVo.AssetInfo assetInfo = totalBeanResponseDataVo.getAssetInfo();
                JdUserInfo jdUserInfo = new JdUserInfo();
                BeanUtil.copyProperties(assetInfo, jdUserInfo, false);
                BeanUtil.copyProperties(userInfo, jdUserInfo, false);
                TotalBeanResponseVo.UserInfoBaseInfo baseInfo = userInfo.getBaseInfo();
                BeanUtil.copyProperties(baseInfo, jdUserInfo, false);
                jdUserInfo.setJdUserId(baseInfo.getCurPin());
                checkStop();
                Thread.sleep(1000);
                checkStop();
                loggerThreadLocal.get().info("请求 totalBean2 接口");
                checkStop();
                // totalBean2接口下线
                try {
                    // TotalBean2ResponseVo totalBean2 = beansService.totalBean2(ck);
                    // TotalBean2ResponseVo.TotalBean2UserVo user2 = totalBean2.getUser();
                    // BeanUtil.copyProperties(user2, jdUserInfo, false);
                    /*MyWalletInfoResponse myWalletInfoResponse = beansService.myWalletInfo(ck);
                    if (myWalletInfoResponse.isOk()) {
                        MyWalletInfoResponse.MyWalletInfo myWalletInfo = myWalletInfoResponse.getResultData().getData();
                        List<MyWalletInfoResponse.MyWalletInfo.Floor> floors = myWalletInfo.getFloors();
                        if (CollUtil.isNotEmpty(floors)) {
                            for (MyWalletInfoResponse.MyWalletInfo.Floor floor : floors) {
                                if ("app_assets".equals(floor.getFloorId())) {
                                    List<MyWalletInfoResponse.MyWalletInfo.Floor.Node> nodes = floor.getNodes();
                                    if (CollUtil.isNotEmpty(nodes)) {
                                        for (MyWalletInfoResponse.MyWalletInfo.Floor.Node node : nodes) {

                                        }
                                    }
                                }
                            }
                        }
                    }*/
                } catch (Exception e) {
                    loggerThreadLocal.get().debug("请求 totalBean2 接口失败 {}", e.getMessage());
                }
                jdUserInfoService.saveOrUpdate(jdUserInfo);
                checkStop();
                Thread.sleep(1000);
                checkStop();
            } catch (JdCookieExpiredException e) {
                loggerThreadLocal.get().info("ck失效");
            } catch (RequestErrorException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (ResponseResultException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                e.printStackTrace();
                loggerThreadLocal.get().info("未知异常：{}, {}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 完成", jdCookieVO.getPt_pin()).newline();
                }
            }
        }
    }

    /**
     * 获取任务数据库id
     */
    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

    private JdBeansService getJdBeansService() {
        return SpringUtil.getBean(JdBeansService.class);
    }

    private IJdUserInfoService getJdUserInfoService() {
        return SpringUtil.getBean(IJdUserInfoService.class);
    }

}
