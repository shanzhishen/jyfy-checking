package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * <p>
 * 人员班次表
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_CLASSES")
@KeySequence("s_cs_classes")
@Data
public class ClassesDO extends Model<ClassesDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private Integer classType;

    private String jnum;

    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime day;

    private String className;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
