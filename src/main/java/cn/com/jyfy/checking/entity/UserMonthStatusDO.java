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
 * 用户每月状态
 * </p>
 *
 * @author Miracle
 * @since 2019-05-10
 */
@TableName("CS_USER_MONTH_STATUS")
@KeySequence("S_CS_USER_MONTH_STATUS")
@Data
public class UserMonthStatusDO extends Model<UserMonthStatusDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private String jnum;

    private String userName;

    private Long onDuty;

    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private LocalDateTime month;

    private LocalDateTime createTime;



    @Override
    protected Serializable pkVal() {
        return this.jnum;
    }


}
