package cc.ussu.support.qinglong.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务详情响应
 */
@Data
@Accessors(chain = true)
public class TaskDetailDTO extends BaseResultDTO<TaskDTO> {
}
