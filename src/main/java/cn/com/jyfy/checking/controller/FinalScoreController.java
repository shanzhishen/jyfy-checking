package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.FinalScoreDO;
import cn.com.jyfy.checking.mapper.FinalScoreMapper;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("最终成绩信息")
@RestController
@RequestMapping("finalScore")
public class FinalScoreController {

    @Autowired
    private FinalScoreMapper finalScoreMapper;

    @ApiOperation("获取最终成绩信息by ID")
    @GetMapping("get")
    public JsonObject get(Long checkId) {
        if (StringUtil.isNull(checkId)) {
            return new JsonObject("参数错误");
        }
        List<Map<String, Object>> list = finalScoreMapper.getFinalScore(checkId);

        return new JsonObject(list);
    }

}
