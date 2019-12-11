package cn.com.jyfy.checking.service;


import cn.com.jyfy.checking.entity.WeekPaperDO;

/**
 * @Created by Miracle
 * @Date 2019/10/21 15:24
 */
public interface ProjectService {


    void addWeekPaper(WeekPaperDO weekPaperDO);

    void updateWeekPaper(WeekPaperDO weekPaperDO);

    String downloadPapers(String week,String jnum);

    String downloadSummary(String week);
}
