package cc.ussu.support.qinglong.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TaskDTO implements Serializable {

    private static final long serialVersionUID = -7117089925556001843L;

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_STOP = 1;

    @Deprecated
    private String _id;
    private Integer id;

    private String command;
    private Long created;
    private Integer isDisabled;
    private Integer isPinned;
    private Integer isSystem;
    private Long last_execution_time;
    private Integer last_running_time;
    private Boolean saved;
    private String name;
    private String schedule;
    private String log_path;
    /**
     * 状态 0运行中 1已停止
     */
    private Integer status;
    private String timestamp;

}
