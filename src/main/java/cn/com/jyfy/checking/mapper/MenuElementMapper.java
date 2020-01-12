package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.MenuElementDO;
import cn.com.jyfy.checking.entity.RoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 页面权限 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2020-01-09
 */
public interface MenuElementMapper extends BaseMapper<MenuElementDO> {

    List<MenuElementDO> getMenuElementByRole(@Param("roles") List<RoleDO> roles);


}
