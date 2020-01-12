package cn.com.jyfy.checking.utils.annotation;

import java.lang.annotation.*;

/**
 * @Create by Miracle
 * @Date 2020/1/9
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRoles {

    String[] value();

}
