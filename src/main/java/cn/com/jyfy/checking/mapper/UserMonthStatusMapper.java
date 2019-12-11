package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.UserMonthStatusDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户每月状态 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-05-10
 */
@Mapper
public interface UserMonthStatusMapper extends BaseMapper<UserMonthStatusDO> {

//    List<UserMonthStatusDO> getMonthStatus(LocalDateTime start,LocalDateTime end);

}
