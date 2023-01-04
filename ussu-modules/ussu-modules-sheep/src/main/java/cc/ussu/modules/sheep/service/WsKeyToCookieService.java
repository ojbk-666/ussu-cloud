package cc.ussu.modules.sheep.service;

import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.task.jd.exception.wskey.JdWsKeyExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.wskey.WsKeyGenTokenCheckApiResponseVO;

public interface WsKeyToCookieService {

    /**
     * getUa
     */
    String getUserAgent();

    /**
     * 检查ck有效性
     */
    boolean checkCk(JdCookieVO ck);

    /**
     * 获取远程参数
     */
    WsKeyGenTokenCheckApiResponseVO getRemoteParam();
    WsKeyGenTokenCheckApiResponseVO getRemoteParam(boolean fromProSeasmallTop);

    /**
     * 根据wskey请求ck
     */
    JdCookieVO getCkByWskey(String wskey) throws JdWsKeyExpiredException, RequestErrorException, ResponseResultException;

}
