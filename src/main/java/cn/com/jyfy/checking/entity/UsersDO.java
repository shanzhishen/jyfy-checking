package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_USERS")
@Data
public class UsersDO extends Model<UsersDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "JNUM", type = IdType.AUTO)
    private String jnum;

    private Integer roleId;

    private String userName;

    private String password;

//    @TableField(exist = false)
    private LocalDateTime createTime;


    /**
     * 1 可用，0 不可用
     */
    @TableLogic
    private Long state;

    /**
     * 1是超级管理员,2系统管理员3普通用户
     */
    private Long isAdmin;

    private Long ord;

    private  Long position;


    @Override
    protected Serializable pkVal() {
        return this.jnum;
    }


}
