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
 *
 * </p>
 *
 * @author Miracle
 * @since 2019-10-10
 */
@TableName("CS_BUSINESS_STUDY_DETAIL")
@KeySequence("S_CS_BUSINESS_STUDY_DETAIL")
@Data
public class BusinessStudyDetailDO extends Model<BusinessStudyDetailDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private Long studyId;

    private Integer score1;

    private Integer score2;

    private Integer score3;

    private Integer score4;

    private String jnum;

    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private Integer totalScore;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
