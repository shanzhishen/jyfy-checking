package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.ClassesDO;
import cn.com.jyfy.checking.mapper.ClassesMapper;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Api("排班信息")
@RestController
@RequestMapping("classes")
public class ClassesController {

    @Autowired
    private ClassesMapper classesMapper;

    @ApiOperation("获取一周的排班信息")
    @GetMapping("get")
    public JsonObject get(String monday) {
        if(StringUtil.isNull(monday)){
            return new JsonObject(new ArrayList());
        }
        LocalDateTime startTime = LocalDateTime.parse(monday, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        LocalDate mon = startTime.toLocalDate().with(DayOfWeek.MONDAY);
        String monStr = mon.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        startTime = StringUtil.getDateTimeByString(monStr);

        LocalDateTime endTime = startTime.plusDays(7).minusNanos(1);
        QueryWrapper<ClassesDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("day", startTime).lt("day", endTime).orderByAsc("jnum", "day");
        List<ClassesDO> classesDOList = classesMapper.selectList(queryWrapper);
        return new JsonObject(classesDOList);
    }

}
