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
 * 日常工作得分
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_DAILY_WORK")
@KeySequence("S_CS_DAILY_WORK")
@Data
public class DailyWorkDO extends Model<DailyWorkDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private String intro;

    private Double points;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime day;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDate;

    private String jnum;

    private String userName;

    private Long type;

    private String typeName;

    private String inf1;

    private String inf2;

    private String applyJnum;

    private String applyName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
