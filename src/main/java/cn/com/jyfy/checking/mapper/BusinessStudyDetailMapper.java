package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.BusinessStudyDetailDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-10-10
 */
@Mapper
public interface BusinessStudyDetailMapper extends BaseMapper<BusinessStudyDetailDO> {

    List<Map<String,Object>> notSendScore(@Param("id") Integer id);


}
