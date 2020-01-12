package cn.com.jyfy.checking.service;

import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.utils.JsonObject;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    /**
     * 验证登录
     * @param jnum
     * @param password
     * @param request
     * @return
     */
    JsonObject login(String jnum, String password, HttpServletRequest request);

    /**
     * 获取menu
     * @param usersDO
     * @param server
     * @return
     */
    JsonObject getMenu(UsersDO usersDO, Integer server);

    /**
     * 获取用户所有权限
     * @param jnum
     * @return
     */
    UsersDO getFullUser(String jnum);


}
