package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.entity.WeekPaperDO;
import cn.com.jyfy.checking.entity.WeekPaperDetailDO;
import cn.com.jyfy.checking.mapper.WeekPaperDetailMapper;
import cn.com.jyfy.checking.mapper.WeekPaperMapper;
import cn.com.jyfy.checking.service.ProjectService;
import cn.com.jyfy.checking.utils.CheckException;
import cn.com.jyfy.checking.utils.FileUtil;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;

/**
 * @Created by Miracle
 * @Date 2019/10/23 17:42
 */
@Api("周报信息")
@RestController
@RequestMapping("weekPaper")
@Slf4j
public class WeekPaperController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WeekPaperMapper weekPaperMapper;

    @Autowired
    private WeekPaperDetailMapper weekPaperDetailMapper;


    @ApiOperation("增加或者更新周报信息")
    @PostMapping("setOnePaper")
    public JsonObject setOnePaper(@RequestBody WeekPaperDO weekPaperDO, HttpServletRequest request) {
        if (StringUtil.isNull(weekPaperDO.getWeek())) {
            return new JsonObject("周数据错误");
        }
        UsersDO usersDO = LoginController.getUser(request);
        StringUtil.printJson(weekPaperDO);
        LocalDateTime monday = StringUtil.getMonday(weekPaperDO.getWeek());
        weekPaperDO.setMonday(monday);
        weekPaperDO.setSunday(monday.plusDays(7).minusNanos(1));
        weekPaperDO.setModifiedJnum(usersDO.getJnum());
        weekPaperDO.setModifiedTime(LocalDateTime.now());
        weekPaperDO.setState(1);

        // 清理特殊字符
        List<WeekPaperDetailDO> detailDOS = weekPaperDO.getDetails();
        for (WeekPaperDetailDO detailDO : detailDOS) {
            detailDO.setContent(StringUtil.delSpecialChar(detailDO.getContent()));
            detailDO.setOperSituation(StringUtil.delSpecialChar(detailDO.getOperSituation()));
            detailDO.setOperProgress(StringUtil.delSpecialChar(detailDO.getOperProgress()));
            detailDO.setOperRate(StringUtil.delSpecialChar(detailDO.getOperRate()));
        }


        weekPaperDO.setWeekNum(monday.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear()));
        if (StringUtil.isNull(weekPaperDO.getWeekId())) {
            weekPaperDO.setEstablishJnum(usersDO.getJnum());
            weekPaperDO.setEstablishTime(LocalDateTime.now());
            weekPaperDO.setCreateTime(LocalDateTime.now());
            // 添加新的周报
            projectService.addWeekPaper(weekPaperDO);
            log.info(usersDO.getUserName() + ": 保存周报 " + weekPaperDO.getProjectName());
        } else {
            // 修改以前的周报
            projectService.updateWeekPaper(weekPaperDO);
            log.info(usersDO.getUserName() + ": 修改周报 " + weekPaperDO.getProjectName());
        }
        return new JsonObject(JsonObject.SUCCESS, "保存完成");
    }

    @ApiOperation("获取一个周报详情")
    @GetMapping("getOne")
    public JsonObject getOne(Integer projectId, String week) {
        if (StringUtil.isNull(projectId, week)) {
            return new JsonObject<>("部分参数不存在");
        }
        QueryWrapper<WeekPaperDO> queryWrapper = new QueryWrapper();

        LocalDateTime monday = StringUtil.getMonday(StringUtil.getDateTimeByString(week));
        queryWrapper.eq("project_id", projectId).eq("monday", monday);

        WeekPaperDO weekPaperDO = weekPaperMapper.selectOne(queryWrapper);
        if (weekPaperDO == null) {
            return new JsonObject();
        }
        QueryWrapper<WeekPaperDetailDO> detailDOQueryWrapper = new QueryWrapper<>();
        detailDOQueryWrapper.eq("week_id", weekPaperDO.getWeekId()).orderByAsc("ord");
        List<WeekPaperDetailDO> weekPaperDetailDOS = weekPaperDetailMapper.selectList(detailDOQueryWrapper);

        weekPaperDO.setDetails(weekPaperDetailDOS);

        return new JsonObject(weekPaperDO);
    }

    @ApiOperation("未交周报的人员")
    @GetMapping("unhandlerPaper")
    public JsonObject unhanderPaper(String week) {
        if (StringUtil.isNull(week)) {
            return new JsonObject<>("部分参数不存在");
        }
        LocalDateTime mondayLdt = StringUtil.getMonday(StringUtil.getDateTimeByString(week));
        String monday = mondayLdt.format(StringUtil.FORMATTER1);
        List<Map<String, Object>> result = weekPaperMapper.getUnHandlerPaper(monday);
        return new JsonObject(result);
    }

    @ApiOperation("批量下载周报")
    @GetMapping("downPaper")
    public void downPaper(HttpServletRequest request, HttpServletResponse response, String week) {
        if (StringUtil.isNull(week)) {
            throw new CheckException("部分参数不存在");
        }

        UsersDO usersDO = LoginController.getUser(request);
        String jnum = null;
        if (usersDO.getRoleId() == 13 || usersDO.getRoleId() == 12){
            jnum = usersDO.getJnum();
        }

        String zipPath = projectService.downloadPapers(week,jnum);
        if (StringUtil.isNull(zipPath)) {
            throw new CheckException("无数据传输");
        }
        FileUtil.downLoadFile(zipPath, response);
        FileUtil.deleteFiles(new File(zipPath));
    }


    @ApiOperation("下载周报汇总")
    @GetMapping("downloadSummary")
    public void downloadSummary(HttpServletResponse response,HttpServletRequest request, String week) {
        if (StringUtil.isNull(week)) {
            throw new CheckException("部分参数不存在");
        }
        String filePath = projectService.downloadSummary(week);
        if (StringUtil.isNull(filePath)) {
            throw new CheckException("数据错误");
        }
        FileUtil.downLoadFile(filePath, response);
        UsersDO usersDO = LoginController.getUser(request);
        log.info(usersDO.getUserName() + "：下载总结：" + week);

        FileUtil.deleteFiles(new File(filePath));
    }
}
