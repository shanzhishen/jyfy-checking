package cn.com.jyfy.checking.utils;

import cn.com.jyfy.checking.entity.*;
import cn.com.jyfy.checking.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommonUtil {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ClassTypeDicMapper classTypeDicMapper;

    @Autowired
    private CheckingInfoMapper checkingInfoMapper;

    @Autowired
    private ClassPointsMapper classPointsMapper;

    @Autowired
    private UserMonthStatusMapper userMonthStatusMapper;

    public Map<String, UsersDO> usersNameMap(){
        List<UsersDO> dos = usersMapper.selectList(null);
        Map<String, UsersDO> usersDOMap = new HashMap<>();
        for(UsersDO usersDO : dos){
            usersDOMap.put(usersDO.getUserName(),usersDO);
        }
        return usersDOMap;
    }

    public Map<String, UsersDO> usersNameMapByJnum(){
        List<UsersDO> dos = usersMapper.selectList(null);
        Map<String, UsersDO> usersDOMap = new HashMap<>();
        for(UsersDO usersDO : dos){
            usersDOMap.put(usersDO.getJnum(),usersDO);
        }
        return usersDOMap;
    }


    public Map<String, ClassTypeDicDO> classTypeMap(){
        List<ClassTypeDicDO> classTypeDicDOS = classTypeDicMapper.selectList(null);
        Map<String, ClassTypeDicDO> map = new HashMap<>();
        for (ClassTypeDicDO dicDO: classTypeDicDOS){
            map.put(dicDO.getClassName(),dicDO);
        }
        return map;
    }


    public CheckingInfoDO getCheckingInfo(Long checkId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("check_id",checkId);
        CheckingInfoDO checkingInfoDO = checkingInfoMapper.selectOne(queryWrapper);
        return checkingInfoDO;
    }

    public Map<String,Long> getClassPoint(){
        Map<String,Long> map = new HashMap<>();
        QueryWrapper<ClassTypeDicDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("points",0);
        List<ClassTypeDicDO> classPointsDOS = classTypeDicMapper.selectList(queryWrapper);
        for (ClassTypeDicDO typeDicDO : classPointsDOS){
            map.put(typeDicDO.getClassName(),typeDicDO.getPoints());
        }
        return map;
    }


    public Map<String,UserMonthStatusDO> getUserMonthStatus(LocalDateTime start,LocalDateTime end){
        QueryWrapper<UserMonthStatusDO> monthStatusDOQueryWrapper = new QueryWrapper<>();
        monthStatusDOQueryWrapper.ge("month",start).lt("month",end);
        List<UserMonthStatusDO> statusDOLists = userMonthStatusMapper.selectList(monthStatusDOQueryWrapper);
        Map<String,UserMonthStatusDO> map = new HashMap<>();
        for(UserMonthStatusDO statusDO : statusDOLists){
            map.put(statusDO.getJnum(),statusDO);
        }
        return map;
    }
}
