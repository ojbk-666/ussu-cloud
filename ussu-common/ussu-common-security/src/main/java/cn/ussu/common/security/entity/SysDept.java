package cn.ussu.common.security.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDept implements Serializable {

    private static final long serialVersionUID = 5967768522474253219L;

    private String id;
    private String parentId;
    private String name;
    private String fullName;
    private String path;

}
