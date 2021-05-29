package cn.ussu.modules.system.model.result;

import cn.ussu.modules.system.entity.SysLog;
import lombok.Data;

@Data
public class SysLogResult extends SysLog {

    private String executeTimeStr;

}
