package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.UserCheckingDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.UserCheckingMapper;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Api("员工互评信息")
@RestController
@RequestMapping("userChecking")
public class UserCheckingController {

    @Autowired
    private UserCheckingMapper userCheckingMapper;

    @ApiOperation("获取员工互评的人员名单")
    @RequestMapping("get")
    public JsonObject get(Long checkId, HttpServletRequest request){
        UsersDO usersDO = LoginController.getUser(request);
        List<Map<String,Object>> userCheckingDOList = userCheckingMapper.getOtherUserScore(checkId,usersDO.getJnum());
        return new JsonObject(userCheckingDOList);
    }

    @ApiOperation("上传所有的互评成绩")
    @RequestMapping("add")
    @Transactional
    public JsonObject add(Long checkId, HttpServletRequest request,@RequestParam("userScores")String userScores){
        UsersDO usersDO = LoginController.getUser(request);
        JSONArray array = JSONArray.parseArray(userScores);
        
        QueryWrapper<UserCheckingDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id",checkId);
        queryWrapper.eq("check_jnum",usersDO.getJnum());
        userCheckingMapper.delete(queryWrapper);

        for(Object obj : array){
            if(obj != null){
                JSONObject o = (JSONObject) obj;
                UserCheckingDO u = new UserCheckingDO();
                u.setCheckId(checkId);
                u.setCheckJnum(usersDO.getJnum());
                u.setCheckName(usersDO.getUserName());
                u.setCheckedJnum(o.getString("jnum"));
                u.setCheckedName(o.getString("userName"));
                u.setPoints(o.getLong("points"));
                u.setCreateTime(LocalDateTime.now());
                userCheckingMapper.insert(u);
            }
        }
        return new JsonObject();
    }


    @ApiOperation("删除一个互评")
    @RequestMapping("del")
    public JsonObject del(Long checkId, HttpServletRequest request){
        UsersDO usersDO = LoginController.getUser(request);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("check_id",checkId);
        queryWrapper.eq("check_jnum",usersDO.getJnum());
        userCheckingMapper.delete(queryWrapper);
        return new JsonObject();
    }

    @ApiOperation("还未互评的人员信息")
    @RequestMapping("unComplete")
    public JsonObject unComplete(Long checkId){
        if(StringUtil.isNull(checkId)){
            return new JsonObject("参数错误");
        }
        List<Map<String,Object>> list = userCheckingMapper.getUnCompleteUserCheck(checkId);
        return new JsonObject(list);
    }
}
