package cn.com.jyfy.checking.controller;

import cn.com.jyfy.checking.entity.DailyTypeDicDO;
import cn.com.jyfy.checking.entity.DailyWorkDO;
import cn.com.jyfy.checking.entity.UsersDO;
import cn.com.jyfy.checking.mapper.DailyTypeDicMapper;
import cn.com.jyfy.checking.mapper.DailyWorkMapper;
import cn.com.jyfy.checking.utils.JsonObject;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Api("日常工作信息")
@RestController
@RequestMapping("daily")
@Slf4j
public class DailyWorkController {

    @Autowired
    private DailyWorkMapper dailyWorkMapper;

    @Autowired
    private DailyTypeDicMapper dailyTypeDicMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("获取日常工作列表")
    @GetMapping("get")
    public JsonObject get(Integer page,Integer pageSize,String jnum,String start,String end,Long type){
        if(StringUtil.isNull(page,pageSize)){
            page = 1;
            pageSize = 10;
        }
        Page<DailyWorkDO> page1 = new Page<>(page,pageSize);
        QueryWrapper<DailyWorkDO> queryWrapper = new QueryWrapper();


//        if(checkId != null){
//            queryWrapper.eq("check_id",checkId);
//        }
        if (jnum != null) {
            queryWrapper.eq("jnum",jnum);
        }

        if(start != null){
            queryWrapper.ge("day",StringUtil.getDateTimeByString(start));
        }

        if (end != null) {
            queryWrapper.le("day",StringUtil.getEndDateTimeByString(end));
        }

        if (type != null) {
            queryWrapper.eq("type",type);
        }
        queryWrapper.orderByDesc("create_date","id");
        IPage<DailyWorkDO> workDOPage = dailyWorkMapper.selectPage(page1,queryWrapper);

        return new JsonObject(workDOPage);
    }

    @ApiOperation("新增一个日常工作")
    @PostMapping("add")
    public JsonObject add(@ModelAttribute DailyWorkDO dailyWork,String day1, HttpServletRequest request){
        LocalDateTime dateTime = StringUtil.getDateTimeByString(day1);
        dailyWork.setDay(dateTime);
        dailyWork.setCreateDate(LocalDateTime.now());
        dailyWorkMapper.insert(dailyWork);

        printWriter(request,"添加一条数据：" + dailyWork);
        return new JsonObject();
    }

    @ApiOperation("删除一个正常工作")
    @PostMapping("del")
    public JsonObject del(@ModelAttribute DailyWorkDO dailyWork, HttpServletRequest request){
        Long id = dailyWork.getId();
        dailyWorkMapper.deleteById(id);
        printWriter(request,"删除数据：" + id);
        return new JsonObject();
    }

    @ApiOperation("更新一个日常工作")
    @PostMapping("set")
    public JsonObject set(@ModelAttribute DailyWorkDO dailyWork,String day1, HttpServletRequest request){
        if(dailyWork == null || dailyWork.getId() == null){
            return new JsonObject("重置数据失败");
        }
        LocalDateTime dateTime = StringUtil.getDateTimeByString(day1);
        dailyWork.setDay(dateTime);
        dailyWork.setCreateDate(LocalDateTime.now());

        QueryWrapper<DailyWorkDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",dailyWork.getId());
        dailyWorkMapper.update(dailyWork,queryWrapper);

        printWriter(request,"修改一条数据：" + dailyWork);
        return new JsonObject();
    }


    @ApiOperation("获取日常工作类型")
    @GetMapping("getType")
    public JsonObject getType(){
        QueryWrapper<DailyTypeDicDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("type");
        List<DailyTypeDicDO> typeDicDOS = dailyTypeDicMapper.selectList(queryWrapper);
        return new JsonObject(typeDicDOS);
    }


    private void printWriter(HttpServletRequest request,String s){
        UsersDO usersDO = LoginController.getUser(request);
        logger.info(usersDO.getUserName() + "========" + s);
    }
}
