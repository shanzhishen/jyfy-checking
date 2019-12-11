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
 * 业务学习
 * </p>
 *
 * @author Miracle
 * @since 2019-10-10
 */
@TableName("CS_BUSINESS_STUDY")
@Data
@KeySequence("S_CS_BUSINESS_STUDY")
public class BusinessStudyDO extends Model<BusinessStudyDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private String title;

    private String speakerJnum;

    private String speakerName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime speakDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private Integer num;

    private String remark;

    private Double perScore;

    private String state;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
