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
    public JsonObject login(String jnum, String password, HttpServletRequest request);

    /**
     * 获取menu
     * @param usersDO
     * @param server
     * @return
     */
    public JsonObject getMenu(UsersDO usersDO, Integer server);

}
