package cn.ussu.modules.system.model.param;

import cn.ussu.modules.system.entity.SysLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysLogParam extends SysLog {

    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

}
