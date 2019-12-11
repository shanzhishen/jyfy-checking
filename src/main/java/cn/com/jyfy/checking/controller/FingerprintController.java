package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.FingerprintDO;
import cn.com.jyfy.checking.mapper.FingerprintMapper;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Api("指纹信息")
@RestController
@RequestMapping("finger")
public class FingerprintController {

    @Autowired
    private  FingerprintMapper fingerprintMapper;

    @ApiOperation("获取指纹信息")
    @GetMapping("get")
    public JsonObject get(Integer page, Integer pageSize, String month) {
        if (StringUtil.isNull(page, pageSize)) {
            page = 1;
            pageSize = 10;
        }
        QueryWrapper<FingerprintDO> queryWrapper = new QueryWrapper<>();
        if(!StringUtil.isNull(month)){
            LocalDateTime monthTime = StringUtil.getDateTimeByString(month + "01");
            queryWrapper.eq("start_date",monthTime);
        }
        queryWrapper.orderByDesc("start_date","jnum");
        Page<FingerprintDO> page1 = new Page<>(page,pageSize);
        IPage<FingerprintDO> fingerprintDOIPage = fingerprintMapper.selectPage(page1,queryWrapper);

        return new JsonObject(fingerprintDOIPage);
    }


}
