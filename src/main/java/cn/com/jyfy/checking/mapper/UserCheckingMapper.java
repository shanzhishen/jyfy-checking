package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.UserCheckingDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 互评表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface UserCheckingMapper extends BaseMapper<UserCheckingDO> {

    List<Map<String,Object>> getUserScore(@Param("checkId")Long checkId);


    List<Map<String,Object>> getOtherUserScore(@Param("checkId") Long checkId,@Param("jnum") String junm);


    List<Map<String,Object>> getUnCompleteUserCheck(@Param("checkId") Long checkId);

}
