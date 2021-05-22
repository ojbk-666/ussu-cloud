package cn.ussu.modules.system.model.result;

import cn.ussu.modules.system.entity.SysDict;
import cn.ussu.modules.system.entity.SysDictType;
import lombok.Data;

import java.util.List;

@Data
public class SysDictTypeResult extends SysDictType {

    private List<SysDict> sysDictList;

}
