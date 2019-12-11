package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.FinalScoreDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 最终得分表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface FinalScoreMapper extends BaseMapper<FinalScoreDO> {

    List<Map<String,Object>> calFinalScore(@Param("checkId")Long checkId);


    List<Map<String,Object>> getFinalScore(@Param("checkId") Long checkId);

}
