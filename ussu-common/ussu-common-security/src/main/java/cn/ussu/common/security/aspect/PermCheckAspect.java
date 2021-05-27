package cn.ussu.common.security.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.exception.PermCheckException;
import cn.ussu.common.security.service.TokenService;
import cn.ussu.common.security.util.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * 权限检查切面类
 */
@Aspect
@Component
public class PermCheckAspect {

    @Autowired
    private TokenService tokenService;

    /**
     * 切入点为自定义注解
     */
    @Pointcut(value = "@annotation(cn.ussu.common.security.annotation.PermCheck)")
    public void cutService() {
    }

    /**
     * 环绕通知
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around(value = "cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        // 获取目标方法上的注解
        Signature signature = point.getSignature();
        MethodSignature msig = (MethodSignature) signature;
        PermCheck permCheckAnnotation = msig.getMethod().getAnnotation(PermCheck.class);
        String[] value = permCheckAnnotation.value();
        // 如果需要检查则检查所有权限
        if (value != null && value.length != 0) {
            if (!hasPerm(permCheckAnnotation.type(), value)) {
                throw new PermCheckException();
            }
        }
        Object[] args = point.getArgs();
        Object proceed = point.proceed(args);
        return proceed;
    }

    /**
     * 检查权限
     */
    private boolean hasPerm(PermCheck.PermCheckType permCheckType, String[] perm) {
        // 获取权限
        LoginUser loginUser = tokenService.getLoginUser();
        if (SecurityUtils.isSuperAdmin()) {
            return true;
        }
        Set<String> perms = loginUser.getPerms();
        if (PermCheck.PermCheckType.AND.equals(permCheckType)) {
            // every
            return CollectionUtil.containsAll(perms, new ArrayList<>(Arrays.asList(perm)));
        } else {
            // any
            return CollectionUtil.containsAny(perms, Arrays.asList(perm));
        }
    }

    /**
     * 检查角色
     */


}
