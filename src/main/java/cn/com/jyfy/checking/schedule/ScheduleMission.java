package cn.com.jyfy.checking.schedule;

import cn.com.jyfy.checking.entity.UserMonthStatusDO;
import cn.com.jyfy.checking.mapper.UserMonthStatusMapper;
import cn.com.jyfy.checking.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
public class ScheduleMission {

    @Autowired
    private UserMonthStatusMapper userMonthStatusMapper;

    private final Logger logger = LoggerFactory.getLogger(ScheduleMission.class);


    @Scheduled(cron = "0 10 0 1 1/1 ?")
    @Async
    public void copyUserStatus(){
        LocalDate today = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        logger.info("开始复制本月状态数据到下月:" + LocalDateTime.now().toLocalDate());
        LocalDateTime next = StringUtil.getDateTimeByString(today.plusMonths(1));

        QueryWrapper<UserMonthStatusDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month",today);
        List<UserMonthStatusDO> statusDOList = userMonthStatusMapper.selectList(queryWrapper);

        QueryWrapper<UserMonthStatusDO> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("month",next);
        userMonthStatusMapper.delete(queryWrapper);


        for(UserMonthStatusDO statusDO : statusDOList){
            statusDO.setMonth(next);
            userMonthStatusMapper.insert(statusDO);
        }

    }


}
