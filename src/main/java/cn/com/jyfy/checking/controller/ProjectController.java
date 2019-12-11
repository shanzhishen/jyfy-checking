package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.ProjectInfoDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.ProjectInfoMapper;
import cn.com.jyfy.checking.service.ProjectService;
import cn.com.jyfy.checking.utils.CommonUtil;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Created by Miracle
 * @Date 2019/10/21 14:54
 */
@Api("项目信息")
@RestController
@RequestMapping("project")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ProjectService projectService;

    @ApiOperation("获取项目列表")
    @GetMapping("getList")
    public JsonObject getList(HttpServletRequest req,Integer page,Integer pageSize,String projectName) {
        UsersDO usersDO = LoginController.getUser(req);
        String jnum = usersDO.getJnum();
        IPage<ProjectInfoDO > projectInfoDOS = null;
        QueryWrapper<ProjectInfoDO> queryWrapper = new QueryWrapper<>();

        if (page==null || pageSize ==null){
            page = 1;
            pageSize = 1000;
        }

        Page<ProjectInfoDO> page1 = new Page<>(page,pageSize);
        if (!StringUtil.isNull(projectName)){
            queryWrapper.like("project_name",projectName);
        }
//        if (usersDO.getIsAdmin() == 1L || usersDO.getIsAdmin() == 2L) {
//        }else {
//            // 只能查询和自己相关的项目
//            queryWrapper.eq("jnum1",jnum).or().eq("jnum2",jnum).or().eq("jnum3",jnum);
//        }
        projectInfoDOS = projectInfoMapper.selectPage(page1,queryWrapper);
        return new JsonObject(projectInfoDOS);
    }

    @ApiOperation("获取项目详情")
    @GetMapping("getProjectId")
    public JsonObject getProjectId(String pid){
        List<Map<String,Object>> projectInfoDOS = projectInfoMapper.getProjectId(pid);
        return new JsonObject(projectInfoDOS);
    }

    @ApiOperation("增加一个新项目")
    @PostMapping("addOneProject")
    public JsonObject addOneProject(@ModelAttribute ProjectInfoDO projectInfoDO,HttpServletRequest request){
        if (StringUtil.isNull(projectInfoDO.getProjectName())){
            return new JsonObject("部分参数不存在");
        }
        if(projectInfoDO.getPid() == 0){
            projectInfoDO.setPname(null);
        }else {
            ProjectInfoDO projectInfoDO1 = projectInfoMapper.selectById(projectInfoDO.getPid());
            projectInfoDO.setPname(projectInfoDO1.getProjectName());
        }
        projectInfoDO.setCreateTime(LocalDateTime.now());

        projectInfoMapper.insert(projectInfoDO);

        UsersDO usersDO = LoginController.getUser(request);
        log.info(usersDO.getUserName() + "：添加一个项目 " + projectInfoDO.getProjectName());
        return new JsonObject(JsonObject.SUCCESS,"添加一个新项目");
    }

    @ApiOperation("更新一个项目信息")
    @PostMapping("setOneProject")
    public JsonObject setOneProject(@ModelAttribute ProjectInfoDO projectInfoDO,HttpServletRequest request){
        if (StringUtil.isNull(projectInfoDO.getProjectName(),projectInfoDO.getProjectId(),projectInfoDO.getJnum1())){
            return new JsonObject("部分参数不存在");
        }
        if(projectInfoDO.getPid() == 0){
            projectInfoDO.setPname(null);
        }else {
            ProjectInfoDO projectInfoDO1 = projectInfoMapper.selectById(projectInfoDO.getPid());
            projectInfoDO.setPname(projectInfoDO1.getProjectName());
        }
        projectInfoMapper.updateById(projectInfoDO);

        UsersDO usersDO = LoginController.getUser(request);
        log.info(usersDO.getUserName() + "：更新一个项目 " + projectInfoDO.getProjectName());

        return new JsonObject(JsonObject.SUCCESS,"修改一个新项目");
    }

    @ApiOperation("删除一个项目")
    @PostMapping("delOneProject")
    public JsonObject delOneProject(Integer id,HttpServletRequest request){
        if (StringUtil.isNull(id)){
            return new JsonObject("部分参数不存在");
        }

        projectInfoMapper.deleteById(id);

        UsersDO usersDO = LoginController.getUser(request);
        log.info(usersDO.getUserName() + "：删除一个项目 " + id);
        return new JsonObject();
    }

}
