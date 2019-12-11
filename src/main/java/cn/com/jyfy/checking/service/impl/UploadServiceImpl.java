package cn.com.jyfy.checking.service.impl;

import cn.com.jyfy.checking.entity.*;
import cn.com.jyfy.checking.excel.ClassExcelListener;
import cn.com.jyfy.checking.mapper.ClassesMapper;
import cn.com.jyfy.checking.mapper.DailyWorkMapper;
import cn.com.jyfy.checking.mapper.FingerprintMapper;
import cn.com.jyfy.checking.service.UploadService;
import cn.com.jyfy.checking.utils.*;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private FingerprintMapper fingerprintMapper;

    @Autowired
    private DailyWorkMapper dailyWorkMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public List<List<String>> readExcel(MultipartFile file) {
        InputStream inputStream = null;
        List<List<String>> data = null;
        ClassExcelListener listener = new ClassExcelListener();
        ExcelTypeEnum typeEnum = ExcelTypeEnum.XLSX;
        String fname = file.getOriginalFilename();
        if (fname.endsWith("xls")) {
            typeEnum = ExcelTypeEnum.XLS;
        }
        try {
            inputStream = file.getInputStream();
            ExcelReader excelReader = new ExcelReader(inputStream, typeEnum, null, listener);
            excelReader.read();
            data = listener.getData();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            listener.clearData();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 处理排班数据
     *
     * @param data
     */
    @Transactional
    @Override
    public JsonObject handlerClassInfo(List<List<String>> data) {
        int rowNum = data.size();
        int colNum = data.get(0).size();
        List<LocalDateTime> days = new ArrayList<>();
        Map<String, UsersDO> nameMap = commonUtil.usersNameMap();
        Map<String, ClassTypeDicDO> classTypeDicDOMap = commonUtil.classTypeMap();
        // 获取周一的日期
/*        String monday;
        if(data.get(0).get(1)!= null && data.get(0).get(1).length()==1){
            monday = data.get(0).get(0) + "0" +  data.get(0).get(1);
        }else {
            monday = data.get(0).get(0) + data.get(0).get(1);
        }
        LocalDate ld = LocalDate.parse(monday,DateTimeFormatter.ofPattern("yyyyMMdd")).with(DayOfWeek.MONDAY);
        monday = ld.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "000000";
        LocalDateTime md = LocalDateTime.parse(monday, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));*/
        if (data.get(0).get(1) == null || data.get(0).get(1).length() == 0) {
            throw new CheckException("数据错误: " + data.get(0).get(1));
        }
        String firstDay = data.get(0).get(1);
        String first = firstDay.substring(firstDay.indexOf("\n") + 1).replaceAll("-", "");
        LocalDateTime md = LocalDateTime.parse(first + "000000", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 计算实际列数
        for (int k = 0; k < colNum; k++) {
            if (data.get(0).get(k) == null) {
                colNum = k;
                break;
            }
        }
        boolean flag_del = true;
        // 获取所有日期
        for (int i = 0; i < rowNum; i++) {
            List<String> d = data.get(i);
            String name = null;
            if (d.get(0) == null) {
                name = "";
            } else {
                name = d.get(0).replaceAll(" ", "").replaceAll("　", "");
            }
            if (name.contains("(")) {
                name = name.substring(0, name.indexOf("("));
            }


            for (int j = 0; j < colNum; j++) {
                if (i == 0) {
                    LocalDateTime ldt = md.plusDays(j);
                    // 加入日期
                    days.add(ldt);
                } else {
                    // 删除数据表中存在的日期
                    if (flag_del) {
                        LocalDateTime start = days.get(0);
                        LocalDateTime end = days.get(days.size() - 1);
                        QueryWrapper<ClassesDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.ge("day", start).lt("day", end);
                        int x = classesMapper.delete(queryWrapper);
                        logger.info("删除的数据：  " + x);
                        flag_del = false;
                    }
                    String c = d.get(j);
                    UsersDO usersDO = nameMap.get(name);
                    if (c == null || "".equals(c)) {
                        c = "0";
                    }
                    if(c.contains("（")){
                        c = c.substring(0,c.indexOf("（"));
                    }

                    // 删除领导信息
                    if (CommonValue.LEADER_JNUM.contains(usersDO.getJnum())) {
                        continue;
                    }

                    ClassTypeDicDO classTypeDicDO = classTypeDicDOMap.get(c);
                    if (usersDO != null && classTypeDicDO != null) {
                        ClassesDO classesDO = new ClassesDO();
                        classesDO.setClassType(classTypeDicDO.getClassType());
                        classesDO.setClassName(classTypeDicDO.getClassName());
                        classesDO.setDay(days.get(j - 1));
                        classesDO.setJnum(usersDO.getJnum());
                        classesDO.setUserName(usersDO.getUserName());
                        classesMapper.insert(classesDO);
                    }
                }
            }
        }

        return new JsonObject(JsonObject.SUCCESS, "更新班次数据成功");
    }

    /**
     * 处理指纹数据
     *
     * @param data
     * @param start1
     * @return
     */
    @Transactional
    @Override
    public JsonObject handlerFingerPrint(List<List<String>> data, String start1) {
        Map<String, UsersDO> nameMap = commonUtil.usersNameMap();
        int rowNum = data.size();

        // 获取第一天和最后一天的日期
        LocalDateTime start = StringUtil.getDateTimeByString(start1 + "01");

        // 清除相同月份的数据
        QueryWrapper<FingerprintDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("start_date", start);
        int t = fingerprintMapper.delete(queryWrapper);
        logger.info("清除指纹信息： " + t);

        for (int i = 1; i < rowNum; i++) {
            if (checkUserAndJnum(data.get(i), nameMap)) {
                List<String> row = data.get(i);
                // 删除领导信息
                if (CommonValue.LEADER_JNUM.contains(row.get(0))) {
                    continue;
                }
                FingerprintDO fingerprintDO = new FingerprintDO();
                fingerprintDO.setJnum(row.get(0));
                fingerprintDO.setName(row.get(1));
                fingerprintDO.setRealPrint(Integer.valueOf(row.get(3)));
                fingerprintDO.setNeedPrint(Integer.valueOf(row.get(4)));
                fingerprintDO.setPrintRate(Double.valueOf(row.get(5)));
                fingerprintDO.setStartDate(start);
                fingerprintDO.setCreateTime(LocalDateTime.now());
                fingerprintMapper.insert(fingerprintDO);
            }
        }

        return new JsonObject(JsonObject.SUCCESS, "更新指纹数据成功");
    }

    private final String titleReg = "\\关于(.*?)\\替班";
    private final String hourReg = "\\d*(\\.\\d*)?小时";
    private final String minuteReg = "(\\d*)\\分钟";

    @Transactional
    @Override
    public JsonObject handerChangeClass(List<List<String>> data) {
        Map<String, UsersDO> map = commonUtil.usersNameMap();
        List<String> errorLine = new ArrayList<>();
        int rowNum = data.size();
        for (int i = 1; i < rowNum; i++) {
            List<String> d = data.get(i);
            String docId = d.get(0);
            String title = d.get(1);
            String nameStr = StringUtil.regString(title, titleReg);
            if (StringUtil.isNull(nameStr)) {
                errorLine.add(title);
                continue;
            }
            String name = nameStr.replace("关于", "").replace("替班", "");

            UsersDO usersDO = map.get(name);
            if (usersDO == null) {
                errorLine.add(title);
                continue;
            }
            // 删除领导信息
            if (CommonValue.LEADER_JNUM.contains(usersDO.getJnum())) {
                continue;
            }


            String hourStr = StringUtil.regString(title, hourReg);
            double hour = -1;
            if (!StringUtil.isNull(hourStr)) {
                hour = Double.valueOf(hourStr.replace("小时", ""));
            }
            double minute = -1;
            if (hour < 0) {
                String minuteStr = StringUtil.regString(title, minuteReg);
                if (!StringUtil.isNull(minuteStr)) {
                    minute = Double.valueOf(minuteStr.replace("分钟", ""));
                }
            }
            double point = 0.0;
            if (hour < 2 && hour >= 0) {
                point = 0.5;
            } else if (hour >= 2) {
                point = 1;
            } else if (minute > 0) {
                point = 0.5;
            } else {
                errorLine.add(title);
                continue;
            }
            LocalDateTime day = LocalDateTime.parse(d.get(8) + ":00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // 删除出现过的docId
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("INF1", docId);
            dailyWorkMapper.delete(queryWrapper);

            UsersDO applier = map.get(d.get(4));


            DailyWorkDO dailyWorkDO = new DailyWorkDO();
            dailyWorkDO.setIntro(title);
            dailyWorkDO.setPoints(point);
            dailyWorkDO.setDay(day);
            dailyWorkDO.setCreateDate(LocalDateTime.now());
            dailyWorkDO.setType(1L);
            dailyWorkDO.setTypeName("替班");
            dailyWorkDO.setJnum(usersDO.getJnum());
            dailyWorkDO.setUserName(usersDO.getUserName());
            dailyWorkDO.setApplyName(applier.getUserName());
            dailyWorkDO.setApplyJnum(applier.getJnum());
            dailyWorkDO.setInf1(docId);
            dailyWorkMapper.insert(dailyWorkDO);
        }
        return new JsonObject(errorLine);
    }

    private boolean checkUserAndJnum(List<String> info, Map<String, UsersDO> nameMap) {
        if (info.size() >= 6) {
            String name = info.get(1);
            if (!StringUtil.isNull(name) && nameMap.get(name) != null) {
                return true;
            }
        }
        return false;
    }

}
