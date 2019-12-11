package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.UserMonthStatusDO;
import cn.com.jyfy.checking.mapper.UserMonthStatusMapper;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Api("用户状态信息")
@RestController
@RequestMapping("userStatus")
public class UserStatusController {

    @Autowired
    private UserMonthStatusMapper userMonthStatusMapper;

    @ApiOperation("每个月用户状态信息")
    @GetMapping("get")
    public JsonObject get(String month) {
        if (StringUtil.isNull(month)) {
            return new JsonObject("参数错误");
        }

        QueryWrapper<UserMonthStatusDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month", StringUtil.getDateTimeByString(month + "01"));
        List<UserMonthStatusDO> userMonthStatusDOList = userMonthStatusMapper.selectList(queryWrapper);
        return new JsonObject(userMonthStatusDOList);
    }

    @ApiOperation("更新一个人的某月的状态")
    @PostMapping("set")
    public JsonObject set(Long id, Long status) {
        if (StringUtil.isNull(id, status)) {
            return new JsonObject("参数错误");
        }
        UserMonthStatusDO userMonthStatusDO = new UserMonthStatusDO();
        userMonthStatusDO.setOnDuty(status);
        UpdateWrapper<UserMonthStatusDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        int update = userMonthStatusMapper.update(userMonthStatusDO, updateWrapper);
        return new JsonObject(JsonObject.SUCCESS, "成功更新" + update + "条数据");
    }
}
