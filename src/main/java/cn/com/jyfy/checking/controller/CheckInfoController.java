package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.CheckingInfoDO;
import cn.com.jyfy.checking.mapper.CheckingInfoMapper;
import cn.com.jyfy.checking.utils.CheckException;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Api("考核信息")
@RestController
@RequestMapping("checkInfo")
public class CheckInfoController {

    @Autowired
    private CheckingInfoMapper checkingInfoMapper;

    @ApiOperation("获取考核信息列表")
    @GetMapping("getList")
    public JsonObject getList(Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 10;
        }
        QueryWrapper<CheckingInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("CREATE_TIME");
        Page<CheckingInfoDO> infoDOPage = new Page<>(page, pageSize);
        IPage<CheckingInfoDO> infoDOIPage = checkingInfoMapper.selectPage(infoDOPage, queryWrapper);
        return new JsonObject(infoDOIPage);
    }

    @ApiOperation("获取一个考核详情")
    @GetMapping("get")
    public JsonObject get(Long checkId) {
        if (checkId == null) {
            throw new CheckException("获取考核号失败");
        }
        CheckingInfoDO checkingInfoDO = checkingInfoMapper.selectById(checkId);
        return new JsonObject(checkingInfoDO);
    }

    @ApiOperation("新增一个考核")
    @PostMapping("add")
    public JsonObject add(String checkName, String startDate, String endDate, String intro) {
        if (StringUtil.isNull(checkName, startDate, endDate)) {
            return new JsonObject("参数错误");
        }
        CheckingInfoDO checkingInfoDO = new CheckingInfoDO();
        checkingInfoDO.setCheckName(checkName);
        checkingInfoDO.setStartDate(StringUtil.getDateTimeByString(startDate));
        checkingInfoDO.setEndDate(StringUtil.getEndDateTimeByString(endDate));
        checkingInfoDO.setIntro(intro);
        checkingInfoDO.setCreateTime(LocalDateTime.now());
        checkingInfoDO.setState(1L);
        checkingInfoMapper.insert(checkingInfoDO);
        return new JsonObject(JsonObject.SUCCESS, "插入成功");
    }

    @ApiOperation("删除一个考核")
    @PostMapping("del")
    public JsonObject add(Long checkId) {
        if (StringUtil.isNull(checkId)) {
            return new JsonObject("参数错误");
        }
        int i = checkingInfoMapper.deleteById(checkId);
        return new JsonObject(JsonObject.SUCCESS, "删除了" + i + "条数据");
    }

    @PostMapping("changeState")
    @ApiOperation("修改考核状态")
    public JsonObject changeState(Long checkId, Long state) {
        if (StringUtil.isNull(checkId, state)) {
            return new JsonObject("参数错误");
        }
        CheckingInfoDO checkingInfoDO = new CheckingInfoDO();
        checkingInfoDO.setState(state);
        UpdateWrapper<CheckingInfoDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("check_id", checkId);
        int update = checkingInfoMapper.update(checkingInfoDO, updateWrapper);
        return new JsonObject(JsonObject.SUCCESS, "成功更新" + update + "条数据");
    }

}
