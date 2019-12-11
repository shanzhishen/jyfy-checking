package cn.com.jyfy.checking.controller;


import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.service.CalculateService;
import cn.com.jyfy.checking.utils.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api("月查考核计算成绩")
@RestController
@RequestMapping("cal")
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    @ApiOperation("计算班次成绩")
    @PostMapping("classScore")
    public JsonObject classScore(Long checkId){
        calculateService.calClassScore(checkId);
        return new JsonObject();
    }

    @ApiOperation("计算最终条目")
    @PostMapping("finalDetailScore")
    public JsonObject finalDetailScore(Long checkId, HttpServletRequest request){
        printWriter(request);
        if(checkId == null){
            return new JsonObject("参数错误");
        }
        // 计算排版分数
        calculateService.calClassScore(checkId);

        // 将指纹信息加入到本次考核当中
        calculateService.calFingerPrint(checkId);

        calculateService.calFinalDetailScore(checkId);
        return new JsonObject();
    }

    @ApiOperation("计算最终成绩")
    @PostMapping("finalScore")
    public JsonObject finalScore(Long checkId, HttpServletRequest request){
        printWriter(request);
        if(checkId == null){
            return new JsonObject("参数错误");
        }
        calculateService.calFinalScore(checkId);

        return new JsonObject();
    }


    private void printWriter(HttpServletRequest request){
        UsersDO usersDO = LoginController.getUser(request);
        System.out.println(usersDO.getUserName() + "========开始计算得分详情==========");
    }
}
