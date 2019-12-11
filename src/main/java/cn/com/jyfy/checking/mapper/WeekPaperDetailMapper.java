package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.WeekPaperDetailDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 周报细节表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-10-21
 */
public interface WeekPaperDetailMapper extends BaseMapper<WeekPaperDetailDO> {

    List<Map<String,Object>> getDetailByDay(@Param("week") String week,@Param("types")String[] types);

}
