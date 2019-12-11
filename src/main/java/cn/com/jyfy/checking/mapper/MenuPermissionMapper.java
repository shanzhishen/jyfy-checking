package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.MenuPermissionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * menu权限
0无权限，1有权限 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface MenuPermissionMapper extends BaseMapper<MenuPermissionDO> {

}
