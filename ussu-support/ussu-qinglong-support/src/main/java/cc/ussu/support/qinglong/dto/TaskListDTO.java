package cc.ussu.support.qinglong.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 任务列表相应
 */
@Data
@Accessors(chain = true)
public class TaskListDTO extends BaseResultDTO<List<TaskDTO>> {
}
