package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("CS_CLASS_TYPE_DIC")
@Data
public class ClassTypeDicDO extends Model<ClassTypeDicDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CLASS_TYPE", type = IdType.AUTO)
    private Integer classType;

    private String className;

    private Long points;


    @Override
    protected Serializable pkVal() {
        return this.classType;
    }


}
