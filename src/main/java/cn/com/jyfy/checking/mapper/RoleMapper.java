package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.RoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

    List<RoleDO> getRolesByJnum(@Param("jnum") String jnum);
}
