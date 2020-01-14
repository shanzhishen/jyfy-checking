package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.UsersDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface UsersMapper extends BaseMapper<UsersDO> {

    List<Map<String,Object>> getUserByRoleCode(@Param("roleCode") String roleCode);


}
