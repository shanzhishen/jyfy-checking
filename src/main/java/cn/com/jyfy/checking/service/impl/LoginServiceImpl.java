package cn.com.jyfy.checking.service.impl;


import cn.com.jyfy.checking.entity.MenuDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.MenuMapper;
import cn.com.jyfy.checking.mapper.UsersMapper;
import cn.com.jyfy.checking.service.LoginService;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MenuMapper menuMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JsonObject login(String jnum, String password, HttpServletRequest request){
        String pwd = Md5Util.md5Password(password);
        UsersDO user = usersMapper.selectById(jnum);
        if (user == null) {
            return new JsonObject("用户名不存在");
        }
        if(pwd.equals(user.getPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            Map<String,Object> u = new HashMap<>();
            u.put("jnum",user.getJnum());
            u.put("name",user.getUserName());
            u.put("position",user.getPosition());

            session.setMaxInactiveInterval(60 * 60 * 10);
            int i = request.getSession().getMaxInactiveInterval();
            logger.info("登陆人员：" +  u);


            return new JsonObject(u);
        }else {
            return new JsonObject("用户名或者密码错误");
        }
    }

    /**
     * 获取menu
     *
     * @param usersDO
     * @param server
     * @return
     */
    @Override
    public JsonObject getMenu(UsersDO usersDO, Integer server) {
        List<MenuDO> menuDOList = menuMapper.getMenu(usersDO.getJnum(),server);
        return new JsonObject(menuDOList);
    }


}
