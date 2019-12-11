package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@TableName("CS_CLASS_POINTS")
@Data
public class ClassPointsDO  extends Model<ClassPointsDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "POINT_TYPE", type = IdType.AUTO)
    private String pointType;

    private Long point;


    @Override
    protected Serializable pkVal() {
        return this.pointType;
    }

}
