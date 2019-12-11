package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.ClassScoreDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 班次得分
 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface ClassScoreMapper extends BaseMapper<ClassScoreDO> {


    List<Map<String,Object>> getAllScore(@Param("start") String start,@Param("end") String end);
}
