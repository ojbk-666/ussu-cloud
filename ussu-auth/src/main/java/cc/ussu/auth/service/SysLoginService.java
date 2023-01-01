package cc.ussu.auth.service;

import cc.ussu.auth.vo.LoginParamVO;
import cc.ussu.common.core.exception.UsernamePasswordInvalidException;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.security.service.TokenService;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.system.api.RemoteSystemUserService;
import cc.ussu.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLoginService {

    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private TokenService tokenService;

    public String login(LoginParamVO loginParamVO) {
        String username = loginParamVO.getUsername();
        String password = loginParamVO.getPassword();
        // 验证账户
        JsonResult<LoginUser> jr = remoteSystemUserService.getUserByUsername(username);
        if (jr.isError() || jr.getData() == null || jr.getData().getUser() == null) {
            // 用户不存在
            throw new UsernamePasswordInvalidException();
        }
        LoginUser loginUser = jr.getData();
        // 验证密码
        if (!SecurityUtil.matchesPassword(password, loginUser.getUser().getPassword())) {
            throw new UsernamePasswordInvalidException();
        }
        // 更新登录信息
        // remoteSystemUserService.updateUserLastLoginInfo();
        return convertToLoginUser(loginUser);
    }

    /**
     * 转换请求结果并生成token
     */
    public String convertToLoginUser(LoginUser loginUser) {
        loginUser.getUser().setPassword(null);
        String token = tokenService.createToken(loginUser);
        return token;
    }

}
