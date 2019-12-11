package cn.com.jyfy.checking.service.impl;

import cn.com.jyfy.checking.entity.*;
import cn.com.jyfy.checking.mapper.*;
import cn.com.jyfy.checking.service.CalculateService;
import cn.com.jyfy.checking.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CalculateServiceImpl implements CalculateService {

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private CheckingInfoMapper checkingInfoMapper;

    @Autowired
    private ClassScoreMapper classScoreMapper;


    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private MonthCheckingMapper monthCheckingMapper;

    @Autowired
    private UserCheckingMapper userCheckingMapper;

    @Autowired
    private DailyWorkMapper dailyWorkMapper;

    @Autowired
    private FinalScoreDetailMapper finalScoreDetailMapper;

    @Autowired
    private FinalScoreMapper finalScoreMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private FingerprintMapper fingerprintMapper;

    @Autowired
    private UserMonthStatusMapper userMonthStatusMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final Double maxScore = 10D;


    /**
     * 计算排班分数
     *
     * @param checkId
     */
    @Override
    @Transactional
    public void calClassScore(Long checkId) {
        CheckingInfoDO checkingInfoDO = checkingInfoMapper.selectById(checkId);
        LocalDateTime start1 = checkingInfoDO.getStartDate();
        LocalDateTime end1 = checkingInfoDO.getEndDate();

        List<DateInMonthDO> monthList = StringUtil.getAllMonth(start1, end1);
        Map<String, Long> classPoint = commonUtil.getClassPoint();

        // 消除以前排班数据
        QueryWrapper<ClassScoreDO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ge("month", start1).le("month", end1);
        queryWrapper.eq("check_id", checkId);
        int i = classScoreMapper.delete(queryWrapper);
        logger.info("清除以前排班得分：" + i);


        Map<String, UsersDO> usersDOMap = commonUtil.usersNameMap();
        for (DateInMonthDO dateInMonthDO : monthList) {
            Double max = 0.1D;
            LocalDateTime start = dateInMonthDO.getStart();
            LocalDateTime end = dateInMonthDO.getEnd();
            // 重新计算排班数据
            String startStr = start.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String endStr = end.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            List<Map<String, Object>> datas = classScoreMapper.getAllScore(startStr, endStr);

            // 获取员工是否值班状态
            Map<String, UserMonthStatusDO> statusDOMap = commonUtil.getUserMonthStatus(start, end);

            Map<String, ClassScoreDO> scoreDOMap = new HashMap<>();
            for (Map<String, Object> map : datas) {
                String jnum = (String) map.get("jnum");
                Long num = StringUtil.getLongValue(map.get("num"));
                String userName = (String) map.get("userName");
                Long classType = StringUtil.getLongValue(map.get("classType"));
                String className = (String) map.get("className");

                ClassScoreDO scoreDO = scoreDOMap.get(jnum);
                if (scoreDO == null) {
                    scoreDO = new ClassScoreDO();
                    scoreDO.setCheckId(checkId);
                    scoreDO.setJnum(jnum);
                    scoreDO.setUserName(userName);
                    scoreDO.setMainPoints(0d);
                    scoreDO.setZhu1(0L);
                    scoreDO.setZhu2(0L);
                    scoreDO.setMen1(0L);
                    scoreDO.setMen2(0L);
                    scoreDO.setZhi(0L);
                    scoreDO.setH24(0L);
                    scoreDO.setPoints(0d);
                    scoreDO.setMainPoints(0d);
                    scoreDO.setCreateTime(LocalDateTime.now());
                    scoreDO.setMonth(start);
                    scoreDOMap.put(jnum, scoreDO);
                }
                String cname = "";
                Long val = 0L;
                if (CommonValue.ZHU_1.equals(className)) {
                    scoreDO.setZhu1(scoreDO.getZhu1() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.ZHU_1) * num);
                } else if (CommonValue.ZHU_2.equals(className)) {
                    scoreDO.setZhu2(scoreDO.getZhu2() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.ZHU_2) * num);
                } else if (CommonValue.MEN_1.equals(className)) {
                    scoreDO.setMen1(scoreDO.getMen1() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.MEN_1) * num);
                } else if (CommonValue.MEN_2.equals(className)) {
                    scoreDO.setMen2(scoreDO.getMen2() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.MEN_2) * num);
                } else if (CommonValue.H24.equals(className)) {
                    scoreDO.setH24(scoreDO.getH24() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.H24) * num);
                } else if (CommonValue.H24_ZHI.equals(className)) {
                    // 24小时值班 分为 24 和 值
                    scoreDO.setH24(scoreDO.getH24() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.H24) * num);

                    scoreDO.setZhi(scoreDO.getZhi() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.ZHI) * num);
                } else if (className.contains(CommonValue.ZHI)) {
                    scoreDO.setZhi(scoreDO.getZhi() + num);
                    scoreDO.setPoints(scoreDO.getPoints() + classPoint.get(CommonValue.ZHI) * num);
                }

                scoreDO.setOnDuty(statusDOMap.get(jnum).getOnDuty());

                if (scoreDO.getPoints() > max) {
                    max = scoreDO.getPoints();
                }
            }
            logger.info(startStr + " 班次得分的最大值： " + max);
            Double perScore = maxScore / max;

            for (Map.Entry<String, ClassScoreDO> entry : scoreDOMap.entrySet()) {
                ClassScoreDO scoreDO = entry.getValue();

                UsersDO usersDO = usersDOMap.get(scoreDO.getUserName());
                if (usersDO.getRoleId() < 10) {
                    continue;
                }

                Double main = StringUtil.left4double(perScore * scoreDO.getPoints());
                scoreDO.setMainPoints(main);
                classScoreMapper.insert(scoreDO);

            }

            calOnDutyScore(scoreDOMap, perScore);

            logger.info("================计算排班得分完成================");
        }
    }


    // 重新计算值班人员评分
    private void calOnDutyScore(Map<String, ClassScoreDO> scoreDOMap, Double perScore) {
        Long noDuty = 0L;
        Double noDutySum = 0D;
        List<ClassScoreDO> scoreDOList = new ArrayList<>();
        List<ClassScoreDO> restScoreDOList = new ArrayList<>();
        for (Map.Entry<String, ClassScoreDO> entry : scoreDOMap.entrySet()) {
            ClassScoreDO scoreDO = entry.getValue();
            if (scoreDO.getOnDuty() == 1) {
                scoreDOList.add(scoreDO);
            } else if (scoreDO.getOnDuty() == 3) {
                // 不计算休息人员的分数
                restScoreDOList.add(scoreDO);
                continue;
            } else {
                noDuty++;
                noDutySum += scoreDO.getPoints();
            }

        }
//        System.out.println("noDuty:" + noDuty + " , noDutySum: " + noDutySum);
        Double per = 0D;
        Double main = 0D;
        if (noDuty != 0) {
            per = StringUtil.left4double(noDutySum / noDuty);
            main = StringUtil.left4double(per * perScore);
        }
        for (ClassScoreDO classScoreDO : scoreDOList) {
            classScoreDO.setPoints(per);
            classScoreDO.setMainPoints(main);
            classScoreMapper.updateById(classScoreDO);
            logger.info("修正管理人员班次得分：" + classScoreDO);
        }

        for (ClassScoreDO classScoreDO : restScoreDOList) {
            classScoreDO.setPoints(0D);
            classScoreDO.setMainPoints(0D);
            classScoreMapper.updateById(classScoreDO);
            logger.info("修正长期休息人员班次等分：" + classScoreDO);
        }


    }


    /**
     * 计算全部分数详情
     *
     * @param checkId
     */
    @Override
    @Transactional
    public void calFinalDetailScore(Long checkId) {
        CheckingInfoDO checkingInfoDO = checkingInfoMapper.selectById(checkId);
        LocalDateTime start1 = checkingInfoDO.getStartDate();
        LocalDateTime end1 = checkingInfoDO.getEndDate();

        // 按月查询 =========================================
        List<DateInMonthDO> monthDOList = StringUtil.getAllMonth(start1, end1);

        for (DateInMonthDO dateInMonthDO : monthDOList) {
            LocalDateTime start = dateInMonthDO.getStart();
            LocalDateTime end = dateInMonthDO.getEnd();
            String startStr = start.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String endStr = end.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            int monthes = monthDOList.size();

            Map<String, UsersDO> userMap = checkedUsers();

            // 删除以前的数据
            QueryWrapper<FinalScoreDetailDO> checkQuery = new QueryWrapper();
            checkQuery.eq("check_id", checkId).eq("month", start);
            finalScoreDetailMapper.delete(checkQuery);

            // 月查考核成绩 1 2
            handlerMonthPoints(checkId, start);

            // 日常得分 3 10 11
            handlerDailyPoints(checkId, startStr, endStr, start);

            // 排班评分 4
            handlerClassPoints(checkId, start);

            // 员工互评成绩 5
            handlerUserChecking(checkId, start);

            // 计算应得分和实际得分
            handlerAllScore(checkId, start);

            // 重新计算长期休息人员的得分
            resetRestUser(checkId,start);
        }
    }

    private void resetRestUser(Long checkId, LocalDateTime start) {
        QueryWrapper<UserMonthStatusDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month",start).eq("on_duty",3);
        List<UserMonthStatusDO> statusDOList = userMonthStatusMapper.selectList(queryWrapper);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
        String mon = start.format(dtf);

        for (UserMonthStatusDO statusDO : statusDOList) {
            QueryWrapper<FinalScoreDetailDO> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("jnum",statusDO.getJnum()).eq("check_id",checkId).eq("month",start);
            int i  = finalScoreDetailMapper.delete(detailQueryWrapper);
            logger.info("重置长期休息人员：" + statusDO.getUserName() + " ,月份：" + mon + "数据量："+ i);
        }
    }

    private void handlerAllScore(Long checkId, LocalDateTime start) {
        String date = start.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 计算应得分
        List<Map<String, Object>> needPoints = finalScoreDetailMapper.getFinalScoreDetail(checkId, CommonValue.NEED_SCORE, date);
        insertDetailPoints(checkId, needPoints, CommonValue.NEED_SCORE_TYPE, start);

        // 计算实际得分
        List<Map<String, Object>> actualPoints = finalScoreDetailMapper.getFinalScoreDetail(checkId, CommonValue.ACTUAL_SCORE, date);
        insertDetailPoints(checkId, actualPoints, CommonValue.ACTUAL_SCORE_TYPE, start);
    }

    private void insertDetailPoints(Long checkId, List<Map<String, Object>> points, Long needScoreType, LocalDateTime start) {
        for (Map<String, Object> map : points) {
            Double point = StringUtil.left2double(StringUtil.getDoubleValue(map.get("point")));
            String jnum = (String) map.get("jnum");
            String userName = (String) map.get("userName");
            LocalDateTime month = StringUtil.getDataTime(map.get("month"));
            insertDetail(checkId, jnum, userName, needScoreType, point, start);
        }
    }

    /**
     * 将指纹得分加入到日常得分表中
     *
     * @param checkId
     */
    @Override
    @Transactional
    public void calFingerPrint(Long checkId) {
//        System.out.println("==============开始计算指纹信息==============");
        CheckingInfoDO checkingInfoDO = checkingInfoMapper.selectById(checkId);
        LocalDateTime start1 = checkingInfoDO.getStartDate();
        LocalDateTime end1 = checkingInfoDO.getEndDate();

        List<DateInMonthDO> monthDOList = StringUtil.getAllMonth(start1, end1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (DateInMonthDO monthDO : monthDOList) {
            String start = monthDO.getStart().format(dtf);
            String end = monthDO.getEnd().format(dtf);

            // 删除之前计算的信息
            QueryWrapper<DailyWorkDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("day", monthDO.getStart()).eq("type", 6L);
            int d = dailyWorkMapper.delete(queryWrapper);
            logger.info(start + ": 删除指纹信息：" + d + " 条");

            List<Map<String, Object>> fingerPrint = fingerprintMapper.getSumFingerPrint(start, end);
            for (Map<String, Object> finger : fingerPrint) {
                String jnum = (String) finger.get("jnum");
                String userName = (String) finger.get("name");
//                Double real = StringUtil.getDoubleValue(finger.get("real"));
//                Double need = StringUtil.getDoubleValue(finger.get("need"));
                Double percent = StringUtil.getDoubleValue(finger.get("rate"));
                long point = 0;
                if (percent < 0.9) {
                    if (percent < 0.7) {
                        point = -3;
                    } else if (percent < 0.8) {
                        point = -2;
                    } else {
                        point = -1;
                    }
                }
                DailyWorkDO dailyWorkDO = new DailyWorkDO();
                dailyWorkDO.setIntro(userName + "的指纹按压率：" + StringUtil.left4double(percent));
                dailyWorkDO.setPoints(1d * point);
                dailyWorkDO.setDay(monthDO.getStart());
                dailyWorkDO.setCreateDate(LocalDateTime.now());
                dailyWorkDO.setJnum(jnum);
                dailyWorkDO.setUserName(userName);
                dailyWorkDO.setType(6L);
                dailyWorkDO.setTypeName("指纹按压");
                dailyWorkMapper.insert(dailyWorkDO);
            }

        }


    }

    /**
     * 计算最终得分
     *
     * @param checkId
     */
    @Override
    public void calFinalScore(Long checkId) {
        logger.info("=============开始计算最终得分=============");
        QueryWrapper<FinalScoreDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id", checkId);
        finalScoreMapper.delete(queryWrapper);


        List<Map<String, Object>> finalScores = finalScoreMapper.calFinalScore(checkId);
        for (Map<String, Object> scoreMap : finalScores) {
            String jnum = (String) scoreMap.get("jnum");
            String userName = (String) scoreMap.get("userName");
            Long type = StringUtil.getLongValue(scoreMap.get("type"));
            Double point = StringUtil.getDoubleValue(scoreMap.get("point"));
            Double sum = StringUtil.getDoubleValue(scoreMap.get("sum"));

            FinalScoreDO finalScoreDO = new FinalScoreDO();
            finalScoreDO.setCheckId(checkId);
            finalScoreDO.setJnum(jnum);
            finalScoreDO.setUserName(userName);
            if (type == 10 || type == 11) {
                finalScoreDO.setPoint(sum);
            } else {
                finalScoreDO.setPoint(point);
            }
            finalScoreDO.setCreateTime(LocalDateTime.now());
            finalScoreDO.setType(type);
//            System.out.println(finalScoreDO);
            finalScoreMapper.insert(finalScoreDO);


        }
        logger.info("==================计算最终得分结束================");
    }


    private void handlerClassPoints(Long checkId, LocalDateTime start) {
//        logger.info("---------------------开始计算班次分-----------------");
        QueryWrapper<ClassScoreDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month", start).eq("check_id", checkId);
        List<ClassScoreDO> scoreDOList = classScoreMapper.selectList(queryWrapper);
        for (ClassScoreDO score : scoreDOList) {
            String jnum = score.getJnum();
            String userName = score.getUserName();
            Double point = score.getMainPoints();
            insertDetail(checkId, jnum, userName, CommonValue.CLASS_TYPE, point, start);
        }

    }


    private void handlerDailyPoints(Long checkId, String startStr, String endStr, LocalDateTime start) {
//        logger.info("---------------------开始计算日常得分-----------------");
        // 日常评分20分 3
        List<Map<String, Object>> dailyPionts = dailyWorkMapper.sumDailyWork(startStr, endStr);
        insertDailyDetail(checkId, dailyPionts, CommonValue.DAILY_TYPE, start);

        // 插入加分项 10
        List<Map<String, Object>> plusPionts = dailyWorkMapper.sumPlusDailyWork(startStr, endStr);
        insertDailyDetail(checkId, plusPionts, CommonValue.DAILY_PLUS_TYPE, start);

        // 插入减分项 11
        List<Map<String, Object>> minusPionts = dailyWorkMapper.sumMinusDailyWork(startStr, endStr);
        // 将指纹信息加入到减分项
//        handlerFingerPrint(minusPionts, checkId, startStr, endStr);

        insertDailyDetail(checkId, minusPionts, CommonValue.DAILY_MINUS_TYPE, start);

    }

    private void handlerFingerPrint(List<Map<String, Object>> minusPionts, Long checkId, String startStr, String endStr) {
//        logger.info("---------------------开始计算指纹信息-----------------");
        List<Map<String, Object>> fingerPrint = fingerprintMapper.getSumFingerPrint(startStr, endStr);
        for (Map<String, Object> finger : fingerPrint) {
            String jnum = (String) finger.get("jnum");
            Double real = StringUtil.getDoubleValue(finger.get("real"));
            Double need = StringUtil.getDoubleValue(finger.get("need"));
            Double percent = real / need;
            if (percent < 0.9) {
                long point = 0;
                if (percent < 0.7) {
                    point = -3;
                } else if (percent < 0.8) {
                    point = -2;
                } else {
                    point = -1;
                }
                for (Map<String, Object> minus : minusPionts) {
                    String mjnum = (String) minus.get("jnum");
                    if (jnum != null && jnum.equals(mjnum)) {
                        Double m = StringUtil.getDoubleValue(minus.get("point"));
                        minus.put("point", m + point);
                        break;
                    }
                }
            }
        }


    }

    private void insertDailyDetail(Long checkId, List<Map<String, Object>> dailyPionts, Long dailyType, LocalDateTime start) {
        for (Map<String, Object> score : dailyPionts) {
            Double point = StringUtil.getDoubleValue(score.get("point"));
            if (dailyType == 3) {
                point = 20D;
            }
            String jnum = (String) score.get("jnum");
            String userName = (String) score.get("userName");
            insertDetail(checkId, jnum, userName, dailyType, point, start);
        }
    }

    // 员工互评成绩
    private void handlerUserChecking(Long checkId, LocalDateTime start) {
//        logger.info("---------------------开始计算员工互评得分-----------------");
        List<Map<String, Object>> scores = userCheckingMapper.getUserScore(checkId);
        if (scores == null || scores.size() == 0) {
            throw new CheckException("员工互评成绩不存在");
        }
        for (Map<String, Object> score : scores) {
            Double point = StringUtil.getDoubleValue(score.get("perPoint"));
            String jnum = (String) score.get("jnum");
            String userName = (String) score.get("userName");
            insertDetail(checkId, jnum, userName, CommonValue.USER_TYPE, point, start);
        }
    }

    // 月查考核成绩
    private void handlerMonthPoints(Long checkId, LocalDateTime start) {
        List<Map<String, Object>> perScore = monthCheckingMapper.sumMonthScore(checkId);
        for (Map<String, Object> map : perScore) {
            Double perBasic = StringUtil.getDoubleValue(map.get("perBasic"));
            Double perCom = StringUtil.getDoubleValue(map.get("perCom"));
            String jnum = (String) map.get("jnum");
            String userName = (String) map.get("userName");

            // 注入基础评分
            insertDetail(checkId, jnum, userName, CommonValue.BASIC_TYPE, perBasic, start);

            // 注入综合评分法
            insertDetail(checkId, jnum, userName, CommonValue.COM_TYPE, perCom, start);

        }
    }

    private Map<String, UsersDO> checkedUsers() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ge("role_id", 2);
        queryWrapper.orderByAsc("ord");
        List<UsersDO> usersDOList = usersMapper.selectList(queryWrapper);
        Map<String, UsersDO> usersDOMap = new HashMap<>();
        for (UsersDO usersDO : usersDOList) {
            usersDOMap.put(usersDO.getUserName(), usersDO);
        }
        return usersDOMap;
    }


    private FinalScoreDetailDO insertDetail(Long checkId, String jnum, String userName, Long type, Double point, LocalDateTime start) {
        FinalScoreDetailDO detailDO1 = new FinalScoreDetailDO();
        detailDO1.setCheckId(checkId);
        detailDO1.setJnum(jnum);
        detailDO1.setUserName(userName);
        detailDO1.setType(type);
        detailDO1.setPoint(StringUtil.left4double(point));
        detailDO1.setCreateTime(LocalDateTime.now());
        detailDO1.setMonth(start);
        finalScoreDetailMapper.insert(detailDO1);
        return detailDO1;
    }


}
