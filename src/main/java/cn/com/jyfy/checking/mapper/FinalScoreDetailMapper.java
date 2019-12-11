package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.FinalScoreDetailDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计算所有加减分项 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface FinalScoreDetailMapper extends BaseMapper<FinalScoreDetailDO> {

    List<Map<String,Object>> getAllDetailScore(@Param("checkId") Long checkId,@Param("list") List<String> list);

    List<Map<String,Object>> getFinalScoreDetail(@Param("checkId") Long checkId, @Param("types") List<Long> types, @Param("month")String month);
}
