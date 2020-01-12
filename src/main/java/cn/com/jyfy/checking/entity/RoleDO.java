package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_ROLE")
@Data
public class RoleDO extends Model<RoleDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    private Integer roleId;

    private String roleName;

    private LocalDateTime createTime;

    private Integer state;

    private String roleCode;

    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }


}
