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
 * 计算所有加减分项
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_FINAL_SCORE_DETAIL")
@KeySequence("S_CS_FINAL_SCORE_DETAIL")
@Data
public class FinalScoreDetailDO extends Model<FinalScoreDetailDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private Long checkId;

    private String jnum;

    private String userName;

    private Long type;

    private Double point;

    private String content;

    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private LocalDateTime month;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
