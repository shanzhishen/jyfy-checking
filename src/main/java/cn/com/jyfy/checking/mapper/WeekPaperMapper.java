package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.WeekPaperDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目周报 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-10-21
 */
public interface WeekPaperMapper extends BaseMapper<WeekPaperDO> {

    List<Map<String,Object>> getUnHandlerPaper(@Param("monday")String monday);

    List<Map<String,Object>> getSafeProject(@Param("monday")String monday);
}
