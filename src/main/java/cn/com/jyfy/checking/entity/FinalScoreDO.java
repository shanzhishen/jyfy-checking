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
 * 最终得分表
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_FINAL_SCORE")
@KeySequence("S_CS_FINAL_SCORE")
@Data
public class FinalScoreDO extends Model<FinalScoreDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private Long checkId;

    private String jnum;

    private String userName;

    private Double point;

    private LocalDateTime createTime;

    private Long type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
