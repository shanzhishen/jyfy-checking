package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.MonthCheckingDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月差考核表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface MonthCheckingMapper extends BaseMapper<MonthCheckingDO> {

    List<Map<String,Object>> sumMonthScore(@Param("checkId") Long checkId);

    List<Map<String,Object>> getMonthScore(@Param("checkId") Long checkId,@Param("jnum") String junm);

    List<Map<String,Object>> getLeaderToEmpScore(@Param("checkId") Long checkId);
}
