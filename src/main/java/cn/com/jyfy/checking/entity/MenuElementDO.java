package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 页面权限
 * </p>
 *
 * @author Miracle
 * @since 2020-01-09
 */
@TableName("CS_MENU_ELEMENT")
@Data
public class MenuElementDO extends Model<MenuElementDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 操作id
     */
    @TableId(value = "ELEMENT_ID", type = IdType.INPUT)
    private Long elementId;

    /**
     * 操作名称
     */
    private String elementName;

    /**
     * 操作代码
     */
    private String elementCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 状态：1在用0弃用
     */
    private String state;

    private LocalDateTime delTime;


    @Override
    protected Serializable pkVal() {
        return this.elementId;
    }


}
