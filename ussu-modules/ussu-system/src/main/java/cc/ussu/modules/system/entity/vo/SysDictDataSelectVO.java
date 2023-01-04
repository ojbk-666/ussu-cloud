package cc.ussu.modules.system.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SysDictDataSelectVO implements Serializable {

    private static final long serialVersionUID = -2588133715365954840L;

    private String label;

    private String value;

}
