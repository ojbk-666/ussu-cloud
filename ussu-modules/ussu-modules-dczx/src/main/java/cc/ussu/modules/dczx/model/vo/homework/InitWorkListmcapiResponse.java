package cc.ussu.modules.dczx.model.vo.homework;

import lombok.Data;

import java.util.List;

/**
 * 单元作业列表响应类
 *
 * https://classroom.edufe.com.cn/TKGL/initWorkListmcapi.action
 */
@Data
public class InitWorkListmcapiResponse {

    // 课程名称
    private String courseName;

    /**
     * 作业列表
     */
    private List<HomeWork> homeWorkList;

    /**
     * 目前分数
     */
    private float exampaperScore;

    private String code;

    private String errorMessage;

}
