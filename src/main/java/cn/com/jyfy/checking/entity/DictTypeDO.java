package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author Miracle
 * @since 2019-10-21
 */
@TableName("CS_DICT_TYPE")
@Data
public class DictTypeDO extends Model<DictTypeDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @TableId(value = "DICT_CODE", type = IdType.AUTO)
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（1正常0禁用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private Integer ord;


    @Override
    protected Serializable pkVal() {
        return this.dictCode;
    }


}
