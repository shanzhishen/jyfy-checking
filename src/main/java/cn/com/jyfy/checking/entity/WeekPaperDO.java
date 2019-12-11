package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 项目周报
 * </p>
 *
 * @author Miracle
 * @since 2019-10-21
 */
@TableName("CS_WEEK_PAPER")
@Data
@KeySequence("S_CS_WEEK_PAPER")
public class WeekPaperDO extends Model<WeekPaperDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "WEEK_ID", type = IdType.INPUT)
    private Long weekId;

    private Long projectId;

    private String projectName;

    /**
     * 工程师
     */
    private String engineerName;

    /**
     * 工时
     */
    private Double workHours;

    @TableLogic
    private Integer state;

    /**
     * 本周日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime sunday;

    /**
     * 以周一的时间表示本周
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime monday;

    // 仅用于接受week数据，不做插入操作
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(exist=false)
    private  LocalDateTime week;

    /**
     * 本年度第几周
     */
    private Integer weekNum;

    /**
     * 创建时间
     */
    private LocalDateTime establishTime;

    /**
     * 创建人
     */
    private String establishJnum;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;

    /**
     * 修改人
     */
    private String modifiedJnum;

    /**
     * 第一次建立时间
     */
    private LocalDateTime createTime;

    /**
     * 运行情况：1稳定运行，2出现问题
     */
    private String situation;

    @TableField(exist=false)
    private List<WeekPaperDetailDO> details;


    @Override
    protected Serializable pkVal() {
        return this.weekId;
    }


}
