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
 * 互评表
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_USER_CHECKING")
@Data
@KeySequence("s_cs_user_checking")
public class UserCheckingDO extends Model<UserCheckingDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private Long checkId;

    private String checkJnum;

    private String checkName;

    private String checkedJnum;

    private String checkedName;

    private Long points;

    private LocalDateTime createTime;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
