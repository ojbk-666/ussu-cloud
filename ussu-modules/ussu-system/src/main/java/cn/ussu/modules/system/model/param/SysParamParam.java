package cn.ussu.modules.system.model.param;

import cn.ussu.modules.system.entity.SysParam;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysParamParam extends SysParam {

    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

}
