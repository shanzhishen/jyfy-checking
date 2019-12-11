package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.FingerprintDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 指纹按压 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface FingerprintMapper extends BaseMapper<FingerprintDO> {

    List<Map<String,Object>> getSumFingerPrint(@Param("start") String start, @Param("end") String end);

}
