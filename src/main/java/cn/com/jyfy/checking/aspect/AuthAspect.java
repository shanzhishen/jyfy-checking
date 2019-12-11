package cn.com.jyfy.checking.aspect;

import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.utils.CheckException;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.LoginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class AuthAspect {


    @Around("execution(* cn.com.jyfy.checking.controller..*.*(..))" +
            "&& !execution(* cn.com.jyfy.checking.controller.LoginController.login(..))" +
            "&& !execution(* cn.com.jyfy.checking.controller.LoginController.regImage(..))")
    @Order(1)
    public Object checkLogin(ProceedingJoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return new JsonObject(JsonObject.UNLOGIN, "请先登录系统");
        }
        Object obj = null;
        try {
            obj = pjp.proceed();
        }catch (LoginException e){
            return new JsonObject(JsonObject.UNLOGIN,e.getMessage());
        }catch (CheckException e) {
            return new JsonObject(e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }

//    @Around("execution(* cn.com.jyfy.checking.controller.MonthCheckingController.*(..))")
    public Object leaderLogin(ProceedingJoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpSession session = request.getSession();
        UsersDO usersDO = (UsersDO)session.getAttribute("user");
        Object obj = null;
        try {
            if(usersDO.getPosition() == 1 || usersDO.getIsAdmin() == 1){
                obj = pjp.proceed();
            }else {
                return new JsonObject<>("非领导职务，无权使用该功能");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }


}
