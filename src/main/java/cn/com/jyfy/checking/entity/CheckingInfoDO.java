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
 * @since 2019-04-25
 */
@TableName("CS_CHECKING_INFO")
@KeySequence("s_cs_checking_info")
@Data
public class CheckingInfoDO extends Model<CheckingInfoDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CHECK_ID", type = IdType.INPUT)
    private Long checkId;

    private String checkName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime endDate;

    private String intro;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private Long state;

    @Override
    protected Serializable pkVal() {
        return this.checkId;
    }

}
