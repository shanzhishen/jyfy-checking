package cn.com.jyfy.checking.utils;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public  class CommonValue {

    public static final String ZHU_1 = "行政主1";
    public static final String ZHU_2 = "行政主2";
    public static final String MEN_1 = "行政门1";
    public static final String MEN_2 = "行政门2";
    public static final String ZHI = "值";
    public static final String H24 = "24小时";
    public static final String H24_ZHI = "24小时/值";

    // role_code
    public static final String ROLE_WORKER = "worker";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_WORKPAPER = "weekpaper";

    public static final Long BASIC_TYPE = 1L;
    public static final Long COM_TYPE = 2L;
    public static final Long DAILY_TYPE =3L;
    public static final Long CLASS_TYPE = 4L;
    public static final Long USER_TYPE = 5L;
    public static final Long NEED_SCORE_TYPE = 8L;
    public static final Long ACTUAL_SCORE_TYPE = 9L;
    public static final Long DAILY_PLUS_TYPE = 10L;
    public static final Long DAILY_MINUS_TYPE = 11L;

    public static final Set<String>  LEADER_JNUM = new HashSet<>();

    public static final List<Long> NEED_SCORE = new ArrayList<>();
    public static final List<Long> ACTUAL_SCORE = new ArrayList<>();

    static {
        LEADER_JNUM.add("888");
        LEADER_JNUM.add("999");
        LEADER_JNUM.add("600");
        LEADER_JNUM.add("8452");

        NEED_SCORE.add(1L);
        NEED_SCORE.add(2L);
        NEED_SCORE.add(3L);
        NEED_SCORE.add(4L);
        NEED_SCORE.add(5L);

        ACTUAL_SCORE.add(1L);
        ACTUAL_SCORE.add(2L);
        ACTUAL_SCORE.add(3L);
        ACTUAL_SCORE.add(4L);
        ACTUAL_SCORE.add(5L);
        ACTUAL_SCORE.add(10L);
        ACTUAL_SCORE.add(11L);



    }


    public static void main(String[] args) {
        System.out.println(LEADER_JNUM);
    }
}
