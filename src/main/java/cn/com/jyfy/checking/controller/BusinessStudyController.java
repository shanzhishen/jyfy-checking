package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.*;
import cn.com.jyfy.checking.mapper.BusinessStudyDetailMapper;
import cn.com.jyfy.checking.mapper.BusinessStudyMapper;
import cn.com.jyfy.checking.mapper.DailyTypeDicMapper;
import cn.com.jyfy.checking.mapper.DailyWorkMapper;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Created by Miracle
 * @Date 2019/10/10 9:27
 */
@Api(value = "业务学习相关")
@RestController
@RequestMapping("businessStudy")
public class BusinessStudyController {

    @Autowired
    private BusinessStudyMapper businessStudyMapper;

    @Autowired
    private BusinessStudyDetailMapper businessStudyDetailMapper;

    @Autowired
    private DailyWorkMapper dailyWorkMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DailyTypeDicMapper dailyTypeDicMapper;

    @ApiOperation("获取列表")
    @GetMapping("getList")
    public JsonObject getList(Integer page, Integer pageSize,String jnum, String day) {
        if (StringUtil.isNull(page)) {
            page = 1;
        }
        if (StringUtil.isNull(pageSize)) {
            pageSize = 10;
        }

        Page<BusinessStudyDO> page1 = new Page<>(page, pageSize);
        QueryWrapper<BusinessStudyDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("CREATE_TIME");

        if(!StringUtil.isNull(jnum)){
            queryWrapper.eq("speaker_jnum",jnum);
        }
        if (!StringUtil.isNull(day)){
            queryWrapper.eq("speak_date",StringUtil.getDateTimeByString(day));
        }

        IPage<BusinessStudyDO> studyDOIPage = businessStudyMapper.selectPage(page1, queryWrapper);
        return new JsonObject(studyDOIPage);
    }

    @ApiOperation("添加新学习")
    @PostMapping("addOneStudy")
    public JsonObject addOneStudy(@ModelAttribute BusinessStudyDO businessStudyDO) {
        if (StringUtil.isNull(businessStudyDO.getSpeakerJnum(), businessStudyDO.getSpeakerName(), businessStudyDO.getTitle())) {
            return new JsonObject<>("部分参数不存在");
        }
        businessStudyDO.setCreateTime(LocalDateTime.now());
        businessStudyMapper.insert(businessStudyDO);
        return new JsonObject();
    }

    @ApiOperation("更新学习")
    @PostMapping("setOneStudy")
    public JsonObject setOneStudy(@ModelAttribute BusinessStudyDO businessStudyDO) {
        if (StringUtil.isNull(businessStudyDO.getId(), businessStudyDO.getSpeakerJnum(), businessStudyDO.getSpeakerName(), businessStudyDO.getTitle())) {
            return new JsonObject<>("部分参数不存在");
        }
        businessStudyDO.setCreateTime(LocalDateTime.now());
        businessStudyMapper.updateById(businessStudyDO);
        return new JsonObject();
    }

    @ApiOperation("删除一个学习")
    @PostMapping("delOneStudy")
    public JsonObject delOneStudy(Integer id) {
        if (StringUtil.isNull(id)) {
            return new JsonObject("id不存在");
        }
        int i = businessStudyMapper.deleteById(id);
        return new JsonObject(JsonObject.SUCCESS,"删除" + i + "条成功");
    }

    @ApiOperation("上传一个学习会议的成绩")
    @PostMapping("addOneScore")
    public JsonObject addOneScore(@ModelAttribute BusinessStudyDetailDO detailDO, HttpServletRequest request) {
        if (StringUtil.isNull(detailDO.getStudyId(), detailDO.getScore1(), detailDO.getScore2(), detailDO.getScore3(), detailDO.getScore4())) {
            return new JsonObject("部分参数不存在");
        }
        UsersDO usersDO = LoginController.getUser(request);

        QueryWrapper<BusinessStudyDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("STUDY_ID", detailDO.getStudyId()).eq("JNUM", usersDO.getJnum());
        businessStudyDetailMapper.delete(queryWrapper);

        detailDO.setJnum(usersDO.getJnum());
        detailDO.setUserName(usersDO.getUserName());
        detailDO.setCreateTime(LocalDateTime.now());
        detailDO.setTotalScore(detailDO.getScore1() + detailDO.getScore2() + detailDO.getScore3() + detailDO.getScore4());

        businessStudyDetailMapper.insert(detailDO);
        return new JsonObject();
    }

    @ApiOperation("获取一个学习会议的成绩")
    @GetMapping("getOneScore")
    public JsonObject getOneScore(Integer id, HttpServletRequest request) {
        if (StringUtil.isNull(id)) {
            return new JsonObject("id不存在");
        }
        UsersDO usersDO = LoginController.getUser(request);
        QueryWrapper<BusinessStudyDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("study_id", id).eq("jnum", usersDO.getJnum());
        BusinessStudyDetailDO detailDO = businessStudyDetailMapper.selectOne(queryWrapper);
        return new JsonObject(detailDO);

    }

    @ApiOperation("获取学习会议的细节")
    @GetMapping("getDetailInfo")
    public JsonObject getDetailInfo(Integer id) {
        if (StringUtil.isNull(id)) return new JsonObject("ID不存在");

        List<Map<String, Object>> notSendScore = businessStudyDetailMapper.notSendScore(id);

        QueryWrapper<BusinessStudyDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("study_id", id);
        List<BusinessStudyDetailDO> list = businessStudyDetailMapper.selectList(queryWrapper);
        StringBuilder allScore = new StringBuilder();
        for (BusinessStudyDetailDO detailDO : list) {
            allScore.append(detailDO.getTotalScore()).append(", ");
        }
        if (allScore.length() >= 2)
            allScore.subSequence(0, allScore.length() - 2);

        Map<String, Object> result = new HashMap<>();
        result.put("notSendUser", notSendScore);
        result.put("num", list.size());
        result.put("scores", allScore);
        return new JsonObject(result);
    }


    @ApiOperation("计算学习会议的成绩")
    @PostMapping("calStudyScore")
    @Transactional
    public JsonObject calStudyScore(Integer id) {
        if (StringUtil.isNull(id)) return new JsonObject("ID不存在");

        QueryWrapper<BusinessStudyDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("study_id", id);
        List<BusinessStudyDetailDO> list = businessStudyDetailMapper.selectList(queryWrapper);
        if (list.size() == 0) return new JsonObject(JsonObject.SUCCESS, "还没有人打分");

        BusinessStudyDO studyDO = businessStudyMapper.selectById(id);
        if (StringUtil.isNull(studyDO)) {
            return new JsonObject("会议不存在");
        }

        Integer total = 0;
        for (BusinessStudyDetailDO detailDO : list) {
            total += detailDO.getTotalScore();
        }
        Double d = StringUtil.left2double(total.doubleValue() / list.size());

        // 超过90分加1分
        Double point;
        if (d >= 90) {
            point = 1d;
        } else {
            point = 0d;
        }

        studyDO.setPerScore(d);
        studyDO.setNum(list.size());

        businessStudyMapper.updateById(studyDO);

        // 新增一条日常分数
        QueryWrapper<DailyWorkDO> dailyWorkDOQueryWrapper = new QueryWrapper<>();
        dailyWorkDOQueryWrapper.eq("type", 9).eq("inf1", studyDO.getId());
        dailyWorkMapper.delete(dailyWorkDOQueryWrapper);

        DailyTypeDicDO dailyTypeDicDO = dailyTypeDicMapper.selectById(9L);

        DailyWorkDO dailyWorkDO = new DailyWorkDO();
        dailyWorkDO.setIntro(studyDO.getTitle() + "，得分：" + studyDO.getPerScore());
        dailyWorkDO.setPoints(point);
        dailyWorkDO.setDay(studyDO.getSpeakDate());
        dailyWorkDO.setCreateDate(LocalDateTime.now());
        dailyWorkDO.setJnum(studyDO.getSpeakerJnum());
        dailyWorkDO.setUserName(studyDO.getSpeakerName());
        dailyWorkDO.setType(9L);
        dailyWorkDO.setTypeName(dailyTypeDicDO.getTypeName());
        dailyWorkDO.setInf1(studyDO.getId().toString());
        dailyWorkMapper.insert(dailyWorkDO);

        return new JsonObject(JsonObject.SUCCESS, "计算成功，并且日常得分中加分");
    }
}
