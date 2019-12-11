package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@TableName("CS_MENU")
@Data
public class MenuDO extends Model<MenuDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Integer menuId;

    @JsonProperty("pid")
    private Integer parentId;
    @JsonProperty("name")
    private String menuName;
    @JsonProperty("img")
    private String imageUrl;

    private String url;

    private LocalDateTime createTime;

    private Integer ord;

    private Long roleId;

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
