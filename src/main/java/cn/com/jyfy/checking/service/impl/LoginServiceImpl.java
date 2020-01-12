package cn.com.jyfy.checking.service.impl;


import cn.com.jyfy.checking.entity.MenuDO;
import cn.com.jyfy.checking.entity.MenuElementDO;
import cn.com.jyfy.checking.entity.RoleDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.MenuElementMapper;
import cn.com.jyfy.checking.mapper.MenuMapper;
import cn.com.jyfy.checking.mapper.RoleMapper;
import cn.com.jyfy.checking.mapper.UsersMapper;
import cn.com.jyfy.checking.service.LoginService;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.Md5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.util.*;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MenuElementMapper menuElementMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JsonObject login(String jnum, String password, HttpServletRequest request){
        String pwd = Md5Util.md5Password(password);
        UsersDO user = getFullUser(jnum);
        if (user == null) {
            return new JsonObject("用户名不存在");
        }
        System.out.println(user);
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
        Set<String> roles = usersDO.getRoles();
        if(roles==null || roles.size()==0){
            return new JsonObject(new ArrayList<MenuDO>());
        }
        // 管理员
        if (roles.contains("admin")){
            QueryWrapper<MenuDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("server",server).orderByAsc("ord");
            List<MenuDO> menuDOS = menuMapper.selectList(queryWrapper);
            return new JsonObject(menuDOS);
        }

        // 其他人员
        List<MenuDO> menuDOList = menuMapper.getMenu(roles,server);
        return new JsonObject(menuDOList);
    }

    /**
     * 获取用户所有权限
     *
     * @param jnum
     * @return
     */
    @Override
    public UsersDO getFullUser(String jnum) {
        UsersDO usersDO  = usersMapper.selectById(jnum);

        List<RoleDO> roleDOS = roleMapper.getRolesByJnum(jnum);
        Set<String> roles = new HashSet<>();
        for (RoleDO roleDO : roleDOS) {
            roles.add(roleDO.getRoleCode());
        }

        List<MenuElementDO> menuElements = menuElementMapper.getMenuElementByRole(roleDOS);
        Set<String> elements =  new HashSet<>();
        for (MenuElementDO menuElement : menuElements) {
            elements.add(menuElement.getElementCode());
        }

        usersDO.setRoles(roles);
        usersDO.setPermissions(elements);

        return usersDO;
    }


}
