package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 指纹按压
 * </p>
 *
 * @author Miracle
 * @since 2019-04-25
 */
@TableName("CS_FINGEPRINT")
@KeySequence("S_CS_FINGEPRINT")
@Data
public class FingerprintDO extends Model<FingerprintDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    private String jnum;

    private String name;

    private Integer needPrint;

    private Integer realPrint;

    private Double printRate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime endDate;

    private LocalDateTime createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
