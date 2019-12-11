package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.ClassesDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 人员班次表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@Mapper
public interface ClassesMapper extends BaseMapper<ClassesDO> {

}
