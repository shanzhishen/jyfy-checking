package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * menu权限
0无权限，1有权限
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_MENU_PERMISSION")
@Data
public class MenuPermissionDO extends Model<MenuPermissionDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Integer menuId;

    private Integer roleId;

    /**
     * 新增
     */
    private Integer cre;

    /**
     * 查看
     */
    private Integer sel;

    /**
     * 修改
     */
    private Integer upd;

    /**
     * 删除
     */
    private Integer del;

    /**
     * 下载
     */
    private Integer download;

    /**
     * 上传
     */
    private Integer upload;




    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }


}
