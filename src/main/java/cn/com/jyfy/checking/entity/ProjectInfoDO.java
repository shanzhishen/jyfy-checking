package cn.com.jyfy.checking.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author Miracle
 * @since 2019-10-21
 */
@TableName("CS_PROJECT_INFO")
@Data
@KeySequence("S_CS_PROJECT_INFO")
public class ProjectInfoDO extends Model<ProjectInfoDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "PROJECT_ID", type = IdType.INPUT)
    private Long projectId;

    private String projectName;

    private Long pid;

    private String pname;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 目前阶段：1需求分析 2开发3测试4实施验收5维护
     */
    private Integer stage;

    /**
     * 项目进度：1提前  2按进度 3稍滞后 4严重滞后  □其它
     */
    private Integer progress;

    /**
     * 简介
     */
    private String info;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1在用0废止
     */
    @TableLogic
    private Integer state;

    /**
     * D:目录,P:实际项目
     */
    private String type;

    private String jnum1;

    private String jnum2;

    private String jnum3;



    @Override
    protected Serializable pkVal() {
        return this.projectId;
    }


}
