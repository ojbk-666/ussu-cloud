package cn.ussu.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.ussu.auth.model.param.login.system.LoginParam;
import cn.ussu.auth.feign.RemoteSystemUserService;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.exception.UsernamePasswordInvalidException;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.entity.SysRole;
import cn.ussu.common.security.entity.SysUser;
import cn.ussu.common.security.service.TokenService;
import cn.ussu.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SysLoginService {

    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private TokenService tokenService;

    public LoginUser login(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        Assert.notBlank(username, "用户名或密码不能为空");
        Assert.notBlank(password, "用户名或密码不能为空");
        // 验证码
        // 验证账户
        JsonResult jr = remoteSystemUserService.getUserByUsername(username);
        Map map = jr.getData();
        SysUser sysUser = BeanUtil.toBean(map, SysUser.class);
        if (sysUser == null) {
            // 用户名不存在
            throw new UsernamePasswordInvalidException();
        }
        // 验证密码
        if (!SecurityUtils.matchesPassword(password, sysUser.getPassword())) {
            throw new UsernamePasswordInvalidException();
        }
        LoginUser loginUser = convertToLoginUser(sysUser);
        // 更新登录信息
        // remoteSystemUserService.updateUserLastLoginInfo();
        return loginUser;
    }

    /**
     * 转换请求结果并生成token
     */
    public LoginUser convertToLoginUser(SysUser sysUser) {
        LoginUser loginUser = new LoginUser();
        List<SysRole> roleList = sysUser.getRoleList();
        Set<String> perms = new LinkedHashSet<>();
        for (SysRole sysRole : roleList) {
            for (String perm : sysRole.getPerms()) {
                if (StrUtil.isNotBlank(perm)) {
                    perms.add(perm);
                }
            }
        }
        loginUser.setPerms(perms);
        loginUser.setSysUser(sysUser.setPassword(null));
        String token = tokenService.createToken(loginUser);
        return loginUser;
    }

}
