package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 周报细节表
 * </p>
 *
 * @author Miracle
 * @since 2019-10-21
 */
@TableName("CS_WEEK_PAPER_DETAIL")
@Data
@KeySequence("S_CS_WEEK_PAPER_DETAIL")
public class WeekPaperDetailDO extends Model<WeekPaperDetailDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "DETAIL_ID", type = IdType.INPUT)
    private Long detailId;

    private Long weekId;

    /**
     * 1,本周工作(计划内)，2,其他工作(计划外)，3,问题，4下周工作计划，5需协调工作
     */
    private String type;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 执行情况
     */
    private String operSituation;

    /**
     * 执行过程
     */
    private String operProgress;

    /**
     * 完成率
     */
    private String operRate;

    /**
     * 计划完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime planFinishTime;

    /**
     * 级别
     */
    private String degree;

    /**
     * 排序
     */
    private Integer ord;

    /**
     * 解决办法
     */
    private String solution;




    @Override
    protected Serializable pkVal() {
        return this.detailId;
    }


}
