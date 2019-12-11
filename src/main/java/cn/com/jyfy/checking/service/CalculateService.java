package cn.com.jyfy.checking.service;


public interface CalculateService {

    /**
     * 计算排班分数
     */
    void calClassScore(Long checkId);

    /**
     * 计算分数细则
     */
    void calFinalDetailScore(Long checkId);

    /**
     * 将指纹得分加入到日常得分表中
     * @param checkId
     */
    void calFingerPrint(Long checkId);

    /**
     * 计算最终得分
     * @param checkId
     */
    void calFinalScore(Long checkId);

}
