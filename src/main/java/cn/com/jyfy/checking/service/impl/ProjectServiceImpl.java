package cn.com.jyfy.checking.service.impl;

import cn.com.jyfy.checking.entity.ProjectInfoDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.entity.WeekPaperDO;
import cn.com.jyfy.checking.entity.WeekPaperDetailDO;
import cn.com.jyfy.checking.entity.model.PProjectModel;
import cn.com.jyfy.checking.entity.model.ProjectModel;
import cn.com.jyfy.checking.entity.model.WeekPaperTreeModel;
import cn.com.jyfy.checking.mapper.ProjectInfoMapper;
import cn.com.jyfy.checking.mapper.UsersMapper;
import cn.com.jyfy.checking.mapper.WeekPaperDetailMapper;
import cn.com.jyfy.checking.mapper.WeekPaperMapper;
import cn.com.jyfy.checking.service.ProjectService;
import cn.com.jyfy.checking.utils.CheckException;
import cn.com.jyfy.checking.utils.FileUtil;
import cn.com.jyfy.checking.utils.StringUtil;
import cn.com.jyfy.checking.word.WordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * @Created by Miracle
 * @Date 2019/10/21 15:25
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WeekPaperMapper weekPaperMapper;

    @Autowired
    private WeekPaperDetailMapper weekPaperDetailMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private WordUtil wordUtil;

    // 将生成的文件生成的位置
    @Value("${checking.weekpaper.save.path}")
    private String fileSavePath;

    @Value("${checking.weekpaper.zip.path}")
    private String zipSavePath;

    private String templatePath = "weekpaper.ftl";

    @Override
    public void addWeekPaper(WeekPaperDO weekPaperDO) {
        List<WeekPaperDetailDO> detailDOS = weekPaperDO.getDetails();
        weekPaperMapper.insert(weekPaperDO);
        if (detailDOS != null) {
            for (int i = 0; i < detailDOS.size(); i++) {
                // 内容为空或者无，则无需增加
                String content = detailDOS.get(i).getContent().trim();
                if (StringUtil.isNull(content) || "无".equals(content)) {
                    continue;
                }
                WeekPaperDetailDO detail = detailDOS.get(i);
                detail.setWeekId(weekPaperDO.getWeekId());
                detail.setOrd(i + 1);
                weekPaperDetailMapper.insert(detail);
            }
        }
    }


    @Override
    public void updateWeekPaper(WeekPaperDO weekPaperDO) {
        weekPaperMapper.updateById(weekPaperDO);

        QueryWrapper<WeekPaperDetailDO> detailDOQueryWrapper = new QueryWrapper<>();
        detailDOQueryWrapper.eq("week_id", weekPaperDO.getWeekId());
        weekPaperDetailMapper.delete(detailDOQueryWrapper);

        List<WeekPaperDetailDO> detailDOS = weekPaperDO.getDetails();
        if (detailDOS != null) {
            for (int i = 0; i < detailDOS.size(); i++) {
                // 内容为空或者无，则无需增加
                String content = detailDOS.get(i).getContent().trim();
                if (StringUtil.isNull(content) || "无".equals(content)) {
                    continue;
                }
                WeekPaperDetailDO detail = detailDOS.get(i);
                detail.setWeekId(weekPaperDO.getWeekId());
                detail.setOrd(i + 1);
                weekPaperDetailMapper.insert(detail);
            }
        }
    }


    @Override
    public String downloadPapers(String week, String jnum) {
        String savePath = fileSavePath + "temp" + File.separator;
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        List<WeekPaperDO> allWeekPaper = getAllWeekPaper(week, jnum);
//        StringUtil.printJson(allWeekPaper);
        if (allWeekPaper == null || allWeekPaper.size()==0){
            return null;
        }

        String templateName = "weekPaper.ftl";

        List<UsersDO> usersDOS = usersMapper.selectList(null);
        for (WeekPaperDO paperDO : allWeekPaper) {
            // 创建每个项目的周报 .doc
            String fileFullPath = createOnePaper(paperDO, templateName, savePath, usersDOS);
        }
        // 将生成的周报进行打包
        String zipName = zipSavePath + "项目周报" + week.substring(0, 10).replaceAll("-", "") + ".zip";
        wordUtil.createZip(zipName, savePath, true);
        FileUtil.deleteFiles(new File(savePath));
        return zipName;
    }


    private String createOnePaper(WeekPaperDO paperDO, String templateName, String savePath, List<UsersDO> usersDOS) {
        ProjectInfoDO projectInfoDO = projectInfoMapper.selectById(paperDO.getProjectId());
        Map<String, Object> map = new HashMap<>();
        map.put("paper", paperDO);
        map.put("project", projectInfoDO);
        map.put("users", usersDOS);
        String monday = paperDO.getMonday().format(StringUtil.FORMATTER3);
        String sunday = paperDO.getSunday().format(StringUtil.FORMATTER3);

        String fileName = savePath + projectInfoDO.getPname() + "-" + projectInfoDO.getProjectName() + monday + "-" + sunday + ".doc";
        wordUtil.createWord(map, templateName, fileName);
        return fileName;
    }

    private List<WeekPaperDO> getAllWeekPaper(String week, String jnum) {
        LocalDateTime monday = StringUtil.getMonday(StringUtil.getDateTimeByString(week));

        QueryWrapper<WeekPaperDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("monday", monday);

        // role!=13如果只找项目负责人
        if (jnum != null) {
            QueryWrapper<ProjectInfoDO> projectWrapper = new QueryWrapper<>();
            projectWrapper.and(i-> i.eq("jnum1", jnum).or().eq("jnum2", jnum).or().eq("jnum3", jnum));
            List<ProjectInfoDO> projectInfoDOS = projectInfoMapper.selectList(projectWrapper);
            List<Long> projectList = new ArrayList<>();
            for (ProjectInfoDO infoDO : projectInfoDOS) {
                projectList.add(infoDO.getProjectId());
            }
            if (projectList.size() == 0){
               return new ArrayList<>();
            }
            queryWrapper.in("project_id", projectList);
        }

        List<WeekPaperDO> paperDOList = weekPaperMapper.selectList(queryWrapper);
        for (WeekPaperDO paperDO : paperDOList) {
            QueryWrapper<WeekPaperDetailDO> detailDOQueryWrapper = new QueryWrapper<>();
            detailDOQueryWrapper.eq("week_id", paperDO.getWeekId()).orderByAsc("ord");

            List<WeekPaperDetailDO> detailDOS = weekPaperDetailMapper.selectList(detailDOQueryWrapper);
            // 补全所有类型的数据，至少有一个
            createOneDetail(detailDOS);
            paperDO.setDetails(detailDOS);
        }
        StringUtil.printJson(paperDOList);
        return paperDOList;
    }

    private void createOneDetail(List<WeekPaperDetailDO> details) {
        int thisWeek = 0, otherWork = 0, nextWeek = 0, question = 0, fitted = 0;
        for (WeekPaperDetailDO detail : details) {
            switch (detail.getType()) {
                case "1":
                    thisWeek++;
                    break;
                case "2":
                    otherWork++;
                    break;
                case "3":
                    nextWeek++;
                    break;
                case "4":
                    question++;
                    break;
                case "5":
                    fitted++;
                    break;
            }
        }
        if (thisWeek == 0) {
            details.add(getOneDetail("1"));
        }
        if (otherWork == 0) {
            details.add(getOneDetail("2"));
        }
        if (question == 0) {
            details.add(getOneDetail("3"));
        }
        if (nextWeek == 0) {
            details.add(getOneDetail("4"));
        }
        if (fitted == 0) {
            details.add(getOneDetail("5"));
        }
    }

    private WeekPaperDetailDO getOneDetail(String type) {
        WeekPaperDetailDO weekPaperDetailDO = new WeekPaperDetailDO();
        weekPaperDetailDO.setType(type);
        weekPaperDetailDO.setContent("");
        weekPaperDetailDO.setOperSituation("");
        weekPaperDetailDO.setOperProgress("");
        weekPaperDetailDO.setOperRate("");
        return weekPaperDetailDO;
    }


    /**
     * 构建周报总结所有的数据结构
     *
     * @param week
     * @return
     */
    public String downloadSummary(String week) {
        LocalDateTime monday = StringUtil.getMonday(StringUtil.getDateTimeByString(week));

        String mondayStr = monday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 2019.10.07-2019.10.13（2019年第41周）
        String startDate = monday.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String endDate = monday.plusDays(6).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String year = monday.format(DateTimeFormatter.ofPattern("yyyy"));
        int week1 = monday.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear());

        //11大类
        QueryWrapper<ProjectInfoDO> oneClassWrapper = new QueryWrapper<>();
        oneClassWrapper.eq("pid", 0);
        List<ProjectInfoDO> oneClass = projectInfoMapper.selectList(oneClassWrapper);
        //共计63个信息系统
        QueryWrapper<ProjectInfoDO> notOneClassWrapper = new QueryWrapper<>();
        notOneClassWrapper.ne("pid", 0);
        int notOneClass = projectInfoMapper.selectCount(notOneClassWrapper);

        // 稳定运行系统：（无新需求或问题）
        List<Map<String, Object>> safeSystemList = getSafeSyatem(mondayStr);

        List<WeekPaperDO> weekPapers = getWeekPaper(monday);
        /**
         *  (三)主要工作完成情况：
         */
        List<PProjectModel> treeModels = getTreeModel(mondayStr, null);

        // 有问题的数据
        String[] types1 = {"3"};
        List<PProjectModel> questionModels = getTreeModel(mondayStr, types1);

        // 下周计划的数据
        String[] types2 = {"4"};
        List<PProjectModel> nextWeekModels = getTreeModel(mondayStr, types2);

        // 组合所有周报的数据
        Map<String, Object> result = new HashMap<>();
        result.put("paperTree", treeModels);
        result.put("paperList", weekPapers);
        result.put("safeSystem", safeSystemList);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("year", year);
        result.put("week", week1);
        result.put("oneClassNum", oneClass.size());
        result.put("oneClass", oneClass);
        result.put("notOneClassNum", notOneClass);
        result.put("questionTree", questionModels);
        result.put("nextWeekTree", nextWeekModels);

        String fileFullPath = fileSavePath + "test" + (int) (Math.random() * 1000) + ".doc";
        wordUtil.createWord(result, "07weekPaperSummary.ftl", fileFullPath);

        return fileFullPath;
    }

    private List<PProjectModel> getTreeModel(String mondayStr, String[] types) {
        List<Map<String, Object>> detailDOS = weekPaperDetailMapper.getDetailByDay(mondayStr, types);
        return convertToTree(detailDOS);
    }

    private List<WeekPaperDO> getWeekPaper(LocalDateTime monday) {
        QueryWrapper<WeekPaperDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("monday", monday);
        List<WeekPaperDO> paperDOList = weekPaperMapper.selectList(queryWrapper);

        paperDOList.forEach(item -> {
            QueryWrapper<WeekPaperDetailDO> detailWrapper = new QueryWrapper<>();
            detailWrapper.eq("week_id", item.getWeekId()).orderByAsc("ord");
            List<WeekPaperDetailDO> detailDOS = weekPaperDetailMapper.selectList(detailWrapper);
            item.setDetails(detailDOS);
        });

        return paperDOList;
    }


    private List<Map<String, Object>> getSafeSyatem(String startDate) {
        List<Map<String, Object>> safeSystemList = weekPaperMapper.getSafeProject(startDate);
        Map<String, Map<String, Object>> safeSystemMap = new HashMap<>();
        for (Map<String, Object> system : safeSystemList) {
            String pname = (String) system.get("pname");
            Map<String, Object> temp = safeSystemMap.get(pname);
            if (temp == null) {
                safeSystemMap.put(pname, system);
            } else {
                String projectName = (String) temp.get("projectName");
                projectName += "，" + system.get("projectName");
                system.put("projectName", projectName);
                safeSystemMap.put(pname, system);
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        safeSystemMap.forEach((key, val) -> {
            String str = (String) val.get("projectName");
            if (str.startsWith("，")) {
                str = str.substring(1);
                val.put("projectName", str);
            }
            result.add(val);
        });
        return result;
    }


    /**
     * 这个方法只能看一遍，不建议看第二遍，写的太丑陋了
     *
     * @param projectInfoMap
     * @param weekPapers
     * @param pid
     */
    private List<PProjectModel> convertToTree(List<Map<String, Object>> detailDOS) {
        List<PProjectModel> pProjectModels = new ArrayList<>();
        for (Map<String, Object> map : detailDOS) {
            // 第一层
            Long pid = StringUtil.getLongValue(map.get("pid"));
            PProjectModel pProjectModel = getPProjectModel(pProjectModels, pid);
            if (pProjectModel == null) {
                pProjectModel = new PProjectModel();
                pProjectModel.setProjectId(pid);
                pProjectModel.setProjectName((String) map.get("pname"));

                List<ProjectModel> projectModels = new ArrayList<>();
                ProjectModel projectModel = new ProjectModel();
                projectModel.setProjectId(StringUtil.getLongValue(map.get("projectId")));
                projectModel.setProjectName((String) map.get("projectName"));
                List<Map<String, Object>> details = new ArrayList<>();
                details.add(map);
                projectModel.setDetails(details);
                projectModels.add(projectModel);

                pProjectModel.setProjects(projectModels);

                pProjectModels.add(pProjectModel);
            } else {
                // 第二层
                Long projectId = StringUtil.getLongValue(map.get("projectId"));
                List<ProjectModel> list = pProjectModel.getProjects();
                ProjectModel projectModel = getProjectModel(list, projectId);

                if (projectModel == null) {
                    projectModel = new ProjectModel();
                    projectModel.setProjectId(projectId);
                    projectModel.setProjectName((String) map.get("projectName"));
                    List<Map<String, Object>> details = new ArrayList<>();
                    details.add(map);
                    projectModel.setDetails(details);
                    list.add(projectModel);
                } else {
                    List<Map<String, Object>> details = projectModel.getDetails();
                    if (details == null) {
                        details = new ArrayList<>();
                    }
                    details.add(map);
                }

            }
        }
        return pProjectModels;
    }

    private PProjectModel getPProjectModel(List<PProjectModel> pProjectModels, Long pid) {
        for (PProjectModel pProjectModel : pProjectModels) {
            if (pid.equals(pProjectModel.getProjectId())) {
                return pProjectModel;
            }
        }
        return null;
    }

    private ProjectModel getProjectModel(List<ProjectModel> projectModels, Long projectId) {
        for (ProjectModel projectModel : projectModels) {
            if (projectId.equals(projectModel.getProjectId())) {
                return projectModel;
            }
        }
        return null;
    }
}
