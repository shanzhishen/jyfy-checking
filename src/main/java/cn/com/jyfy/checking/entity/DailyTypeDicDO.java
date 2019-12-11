package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 日常工作的类型
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_DAILY_TYPE_DIC")
@Data
public class DailyTypeDicDO extends Model<DailyTypeDicDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "TYPE", type = IdType.AUTO)
    private Integer type;

    private String typeName;



    @Override
    protected Serializable pkVal() {
        return this.type;
    }


}
