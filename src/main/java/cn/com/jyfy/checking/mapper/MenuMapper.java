package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.MenuDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {

    List<MenuDO> getMenu(@Param("roles") Set<String> roles, @Param("server") Integer server);
}
