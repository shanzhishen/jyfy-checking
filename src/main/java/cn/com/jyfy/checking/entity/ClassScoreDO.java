package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 班次得分

 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_CLASS_SCORE")
@KeySequence("s_cs_class_score")
@Data
public class ClassScoreDO extends Model<ClassScoreDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private String jnum;

    private String userName;

    private Long checkId;

    private Long zhu1;

    private Long zhu2;

    private Long men1;

    private Long men2;

    private Long zhi;

    private Long h24;

    private Double points;

    private Double mainPoints;

    private LocalDateTime createTime;

    private LocalDateTime month;

    private Long onDuty;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
