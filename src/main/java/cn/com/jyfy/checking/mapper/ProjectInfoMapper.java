package cn.com.jyfy.checking.mapper;

import cn.com.jyfy.checking.entity.ProjectInfoDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author Miracle
 * @since 2019-10-21
 */
public interface ProjectInfoMapper extends BaseMapper<ProjectInfoDO> {

    List<Map<String,Object>> getProjectId(@Param("pid") String pid);

}
