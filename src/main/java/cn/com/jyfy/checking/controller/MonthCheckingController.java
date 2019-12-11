package cn.com.jyfy.checking.controller;


import cn.com.jyfy.checking.entity.CheckingInfoDO;
import cn.com.jyfy.checking.entity.MonthCheckingDO;
import cn.com.jyfy.checking.entity.UserCheckingDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.MonthCheckingMapper;
import cn.com.jyfy.checking.mapper.UsersMapper;
import cn.com.jyfy.checking.utils.CommonUtil;
import cn.com.jyfy.checking.utils.CommonValue;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
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

@Api("月查考核成绩")
@RestController
@RequestMapping("monthChecking")
public class MonthCheckingController {

    @Autowired
    private MonthCheckingMapper monthCheckingMapper;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private UsersMapper usersMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("获取已存在的月查考核")
    @GetMapping("get")
    public JsonObject get(Long checkId, HttpServletRequest request) {
        UsersDO usersDO = LoginController.getUser(request);
        List<Map<String,Object>> monthCheckingDOList = monthCheckingMapper.getMonthScore(checkId, usersDO.getJnum());
        return new JsonObject(monthCheckingDOList);
    }

    @ApiOperation("保存所有的月查成绩")
    @PostMapping("add")
    @Transactional
    public JsonObject add(Long checkId, @RequestParam("monthScores") String monthScores, HttpServletRequest request) {
        CheckingInfoDO checkingInfoDO = commonUtil.getCheckingInfo(checkId);
        UsersDO leader = LoginController.getUser(request);

        QueryWrapper<MonthCheckingDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id",checkId);
        queryWrapper.eq("leader_jnum",leader.getJnum());
        monthCheckingMapper.delete(queryWrapper);

        JSONArray jsonArray = JSONArray.parseArray(monthScores);
        for(Object object : jsonArray){
            JSONObject obj = (JSONObject) object;
            MonthCheckingDO monthCheckingDO = new MonthCheckingDO();
            monthCheckingDO.setCheckId(checkId);
            monthCheckingDO.setLeaderJnum(leader.getJnum());
            monthCheckingDO.setLeaderName(leader.getUserName());
            monthCheckingDO.setCheckedJnum(obj.getString("jnum"));
            monthCheckingDO.setCheckedName(obj.getString("userName"));
            monthCheckingDO.setBasicPoints(obj.getLong("basicPoints"));
            monthCheckingDO.setComPoints(obj.getLong("comPoints"));
            monthCheckingDO.setTotal(obj.getLong("total"));
            monthCheckingDO.setCreateTime(LocalDateTime.now());
            monthCheckingDO.setState(1L);
            monthCheckingMapper.insert(monthCheckingDO);
        }
        return new JsonObject(JsonObject.SUCCESS,"保存成功");
    }

    @ApiOperation("删除月查考核的成绩")
    @PostMapping("del")
    public JsonObject del(Long id) {
        monthCheckingMapper.deleteById(id);
        return new JsonObject();
    }


    @ApiOperation("增加一条月查考核成绩")
    @PostMapping("addOne")
    @Transactional
    public JsonObject addOne(@ModelAttribute MonthCheckingDO monthCheckingDO){
        QueryWrapper<MonthCheckingDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id",monthCheckingDO.getCheckId())
                .eq("leader_jnum",monthCheckingDO.getLeaderJnum())
                .eq("checked_jnum",monthCheckingDO.getCheckedJnum());
        monthCheckingMapper.delete(queryWrapper);

        monthCheckingDO.setState(1L);
        monthCheckingDO.setCreateTime(LocalDateTime.now());

        monthCheckingMapper.insert(monthCheckingDO);
        logger.info("添加一条考核：" + monthCheckingDO.toString());

        return new JsonObject(JsonObject.SUCCESS,"保存成功");
    }


    @ApiOperation("获取所有的成绩")
    @GetMapping("getAllScore")
    public JsonObject getAllScore(Long checkId){
        List<Map<String,Object>> result = monthCheckingMapper.getLeaderToEmpScore(checkId);

        QueryWrapper<UsersDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",1).orderByAsc("ord");
        List<UsersDO> leaders = usersMapper.selectList(queryWrapper);
        for (UsersDO leader : leaders) {
            leader.setPassword("");
        }

        Map<String,Object> a = new HashMap<String,Object>();
        a.put("scores",result);
        a.put("leaders",leaders);

        return new JsonObject(a);
    }
}
