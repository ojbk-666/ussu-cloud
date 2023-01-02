package cc.ussu.modules.dczx.model.vo.homework;

import cc.ussu.modules.dczx.model.vo.SimulatedQuestionResponse;
import lombok.Data;

/**
 * 单元作业详情的响应
 */
@Data
public class ShowHistoryPapermcapiResponse extends SimulatedQuestionResponse {

    private String code;

    private String errorMessage;

}
