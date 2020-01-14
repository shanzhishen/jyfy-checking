package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.FinalScoreDO;
import cn.com.jyfy.checking.entity.FinalScoreDetailDO;
import cn.com.jyfy.checking.mapper.FinalScoreDetailMapper;
import cn.com.jyfy.checking.mapper.UsersMapper;
import cn.com.jyfy.checking.utils.CheckException;
import cn.com.jyfy.checking.utils.CommonValue;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api("考核成绩细节信息")
@RestController
@RequestMapping("detailScore")
public class FinalDetailScoreController {

    @Autowired
    private FinalScoreDetailMapper finalScoreDetailMapper;

    @Autowired
    private UsersMapper usersMapper;

    @ApiOperation("获取最终得分表")
    @GetMapping("get")
    public JsonObject get(Long checkId,String types,String month){
        if(StringUtil.isNull(checkId)){
            return new JsonObject("参数错误");
        }
        List<String> list = null;
        if(types != null && !"".equals(types)){
            String[] type1 = types.split(",");
            list = new ArrayList<>();
            for (String s : type1) {
                list.add(s);
            }
        }
        List<Map<String,Object>> finalScoreDetailDOList = finalScoreDetailMapper.getAllDetailScore(checkId,list);
        return new JsonObject(finalScoreDetailDOList);
    }





}
