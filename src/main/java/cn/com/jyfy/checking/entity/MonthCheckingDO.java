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
 * 月差考核表
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_MONTH_CHECKING")
@KeySequence("S_CS_MONTH_CHECKING")
@Data
public class MonthCheckingDO extends Model<MonthCheckingDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private Long checkId;

    private String leaderJnum;

    private String leaderName;

    private String checkedJnum;

    private String checkedName;

    private Long basicPoints;

    private Long comPoints;

    private Long total;

    private LocalDateTime createTime;

    private Long state;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
