package cc.ussu.common.security.aspect;

import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.auth.AuthUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限检查切面类
 */
@Aspect
@Component
public class PermCheckAspect {

    /**
     * 切入点为自定义注解
     */
    @Pointcut(value = "@annotation(cc.ussu.common.security.annotation.PermCheck)")
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
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取目标方法上的注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        checkMethodAnnotation(signature.getMethod());

        return point.proceed(point.getArgs());
    }

    /**
     * 对一个Method对象进行注解检查
     */
    public void checkMethodAnnotation(Method method) {
        // 校验 @RequiresLogin 注解
        /*RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);
        if (requiresLogin != null) {
            AuthUtil.checkLogin();
        }*/

        // 校验 @RequiresRoles 注解
        /*RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        if (requiresRoles != null) {
            AuthUtil.checkRole(requiresRoles);
        }*/

        // 校验 @RequiresPermissions 注解
        PermCheck permCheck = method.getAnnotation(PermCheck.class);
        if (permCheck != null) {
            AuthUtil.checkPerm(permCheck);
        }
    }

}
