package cn.com.jyfy.checking.service.impl;

import cn.com.jyfy.checking.entity.MenuElementDO;
import cn.com.jyfy.checking.entity.RoleDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.MenuElementMapper;
import cn.com.jyfy.checking.mapper.RoleMapper;
import cn.com.jyfy.checking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Create by Miracle
 * @Date 2020/1/9
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuElementMapper menuElementMapper;


    /**
     * 获取用户所有的Role
     *
     * @param jnum
     * @return
     */
    @Override
    public List<RoleDO> getUserRole(String jnum) {
        return roleMapper.getRolesByJnum(jnum);
    }

    /**
     * 根据角色获得所有的MenuElement
     *
     * @param roles
     * @return
     */
    @Override
    public List<MenuElementDO> getMenuElement(List<RoleDO> roles) {
        if (roles==null || roles.size()==0) return new ArrayList<>();
        return menuElementMapper.getMenuElementByRole(roles);
    }
}
