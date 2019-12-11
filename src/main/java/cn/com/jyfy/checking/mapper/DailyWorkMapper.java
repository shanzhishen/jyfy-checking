package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.DailyWorkDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日常工作得分 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface DailyWorkMapper extends BaseMapper<DailyWorkDO> {

    List<Map<String,Object>> sumDailyWork(@Param("start") String start,@Param("end") String end);

    List<Map<String,Object>> sumPlusDailyWork(@Param("start") String start,@Param("end") String end);

    List<Map<String,Object>> sumMinusDailyWork(@Param("start") String start,@Param("end") String end);
}
