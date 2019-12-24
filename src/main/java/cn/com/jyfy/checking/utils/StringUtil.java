package cn.com.jyfy.checking.utils;

import cn.com.jyfy.checking.entity.DateInMonthDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMATTER3 = DateTimeFormatter.ofPattern("yyyyMMdd");
    // 密码中必须包含大小字母、数字、特称字符，至少6个字符，最多30个字符。
    public static final String PWD_REG = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,30}";

    /**
     * 判断是否为空
     *
     * @param strs
     * @return
     */
    public static boolean isNull(String... strs) {
        for (String s : strs) {
            if (isNull(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNull(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object... strs) {
        for (Object s : strs) {
            if (s == null) {
                return true;
            } else if (s instanceof String && ((String) s).length() == 0) {
                return true;
            }
        }
        return false;
    }


    public static boolean isNum(String str) {
        if (str == null) {
            return false;
        }
        String reg = "^\\d+$";
        return str.matches(reg);
    }


    public static LocalDateTime getDateTimeByString(String date) {
        if (date.length() == 10) {
            return LocalDateTime.parse(date + " 00:00:00", FORMATTER1);
        } else if (date.length() == 19) {
            return LocalDateTime.parse(date, FORMATTER1);
        }
        return LocalDateTime.parse(date + "000000", FORMATTER);
    }

    public static LocalDateTime getDateTimeByString(LocalDate date) {
        return LocalDateTime.parse(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "000000", FORMATTER);
    }

    public static LocalDateTime getEndDateTimeByString(String date) {
        return LocalDateTime.parse(date + "235959", FORMATTER);
    }

    public static String getDay(LocalDateTime ldt) {
        return ldt.format(FORMATTER2);
    }

    // 查找匹配的字符串
    public static String regString(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        String re = "";
        while (matcher.find()) {
            re = matcher.group(0);
            break;
        }
        return re;
    }

    // 验证是否符合字符串
    public static boolean regBoolean(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static Long getLongValue(Object str) {
        if (str instanceof Long) {
            return (Long) str;
        } else if (str instanceof BigDecimal) {
            return ((BigDecimal) str).longValue();
        } else {
            return 0L;
        }
    }

    public static Double getDoubleValue(Object str) {
        if (str instanceof Double) {
            return (Double) str;
        } else if (str instanceof BigDecimal) {
            return ((BigDecimal) str).doubleValue();
        } else {
            return 0d;
        }
    }


    public static double left4double(Double a) {
        DecimalFormat df = new DecimalFormat("#.0000");
        return Double.valueOf(df.format(a));
    }

    public static double left2double(Double a) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(a));
    }

    public static boolean startBeforeEnd(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }

    public static List<DateInMonthDO> getAllMonth(LocalDateTime start, LocalDateTime end) {
        List<DateInMonthDO> monthes = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDateTime s = start.plusMonths(i);
            if (startBeforeEnd(s, end)) {
                DateInMonthDO dateInMonthDO = new DateInMonthDO();
                LocalDateTime ldt = dateToDateTime(s.toLocalDate().with(TemporalAdjusters.firstDayOfMonth()));
                dateInMonthDO.setStart(ldt);
                dateInMonthDO.setEnd(ldt.plusMonths(1).minusNanos(1));
                monthes.add(dateInMonthDO);
            } else {
                break;
            }
        }

        return monthes;
    }

    public static LocalDateTime dateToDateTime(LocalDate ld) {
        String date = ld.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "000000";
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public static boolean isThisDay(LocalDateTime ldt, DayOfWeek dayOfWeek) {
        LocalDate now = ldt.toLocalDate();
        LocalDate mon = now.with(dayOfWeek);
        return now.isEqual(mon);
    }

    public static LocalDateTime getDataTime(Object obj) {
        if (obj instanceof LocalDateTime) {
            return (LocalDateTime) obj;
        } else if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toLocalDateTime();
        }
        return null;
    }

    public static LocalDateTime getMonday(LocalDateTime date) {
        LocalDate mon = date.toLocalDate().with(DayOfWeek.MONDAY);
        String monStr = mon.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return StringUtil.getDateTimeByString(monStr);
    }

    // 打印json
    public static void printJson(Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(obj);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static String delSpecialChar(String str){
        if(isNull(str)){
            return str;
        }
        return str.trim().replaceAll("\\n","")
                .replaceAll("\\t","")
                .replaceAll("\\r","");
    }


    public static void main(String[] args) {
        String str = "A13042\n" +
                "\n" +
                "Process finished with exit code 0";
        System.out.println(delSpecialChar(str));
    }
}
