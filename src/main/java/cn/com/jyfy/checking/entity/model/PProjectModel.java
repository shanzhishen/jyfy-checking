package cn.com.jyfy.checking.entity.model;

import lombok.Data;

import java.util.List;

/**
 * @Created by Miracle
 * @Date 2019/11/4 15:47
 */
@Data
public class PProjectModel {

    private Long projectId;

    private String projectName;

    private List<ProjectModel> projects;
 }
