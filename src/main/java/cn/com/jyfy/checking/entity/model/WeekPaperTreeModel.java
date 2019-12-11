package cn.com.jyfy.checking.entity.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Created by Miracle
 * @Date 2019/10/30 15:28
 */
@Data
public class WeekPaperTreeModel {

    private Long projectId;

    private String projectName;

    List<WeekPaperTreeModel> children;

    private List<Map<String,Object>> details;
}
