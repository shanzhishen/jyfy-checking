package cn.com.jyfy.checking.aspect;

import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.utils.CheckException;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.LoginException;
import cn.com.jyfy.checking.utils.annotation.RequiresPermissions;
import cn.com.jyfy.checking.utils.annotation.RequiresRoles;
import com.baomidou.mybatisplus.generator.config.IFileCreate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Set;

@Aspect
@Component
@Slf4j
public class AuthAspect {


    @Around("execution(* cn.com.jyfy.checking.controller..*.*(..))" +
            "&& !execution(* cn.com.jyfy.checking.controller.LoginController.login(..))" +
            "&& !execution(* cn.com.jyfy.checking.controller.LoginController.regImage(..))")
    @Order(1)
    public Object checkLogin(ProceedingJoinPoint pjp) {
        UsersDO userObj = getUserDO(pjp);
        if (userObj == null) {
            return new JsonObject(JsonObject.UNLOGIN, "请先登录系统");
        }
        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (LoginException e) {
            return new JsonObject(JsonObject.UNLOGIN, e.getMessage());
        } catch (CheckException e) {
            return new JsonObject(e);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return obj;
    }

    @Around("@annotation(cn.com.jyfy.checking.utils.annotation.RequiresRoles)")
    @Order(2)
    public Object checkRoles(ProceedingJoinPoint pjp) {
        UsersDO usersDO = getUserDO(pjp);
        Method method = getMethod(pjp);
        String[] vals = method.getAnnotation(RequiresRoles.class).value();
        Set<String> roles = usersDO.getRoles();
        System.out.println(roles);
        Object obj = null;
        try {
            for (String val : vals) {
                System.out.println(roles.contains(val));
                if (roles.contains(val)) {
                    obj = pjp.proceed();
                    return obj;
                }
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return new JsonObject("您没有该角色权限");
    }

    @Around("@annotation(cn.com.jyfy.checking.utils.annotation.RequiresPermissions)")
    @Order(3)
    public Object checkPermissions(ProceedingJoinPoint pjp) {
        UsersDO usersDO = getUserDO(pjp);
        Method method = getMethod(pjp);
        String[] vals = method.getAnnotation(RequiresPermissions.class).value();
        Set<String> permissions = usersDO.getPermissions();
        Object obj = null;
        try {
            for (String val : vals) {
                if (permissions.contains(val)) {
                    obj = pjp.proceed();
                    return obj;
                }
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return new JsonObject("您没有该权限");
    }


    private UsersDO getUserDO(ProceedingJoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return null;
        }
        return (UsersDO) userObj;
    }

    private Method getMethod(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod();
    }
}
