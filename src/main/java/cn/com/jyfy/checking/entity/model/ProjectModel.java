package cn.com.jyfy.checking.entity.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Created by Miracle
 * @Date 2019/11/4 15:48
 */
@Data
public class ProjectModel {

    private Long projectId;

    private String projectName;

    private List<Map<String,Object>> details;

}
