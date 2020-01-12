package cn.com.jyfy.checking.service;

import cn.com.jyfy.checking.entity.MenuElementDO;
import cn.com.jyfy.checking.entity.RoleDO;
import cn.com.jyfy.checking.entity.UsersDO;

import java.util.List;

/**
 * @Create by Miracle
 * @Date 2020/1/9
 */
public interface UserService {


    /**
     * 获取用户所有的Role
     *
     * @return
     */
    List<RoleDO> getUserRole(String jnum);

    /**
     * 根据角色获得所有的MenuElement
     *
     * @param roles
     * @return
     */
    List<MenuElementDO> getMenuElement(List<RoleDO> roles);
}
