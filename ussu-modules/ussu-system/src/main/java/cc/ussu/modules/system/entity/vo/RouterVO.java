package cc.ussu.modules.system.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RouterVO implements Serializable {

    private static final long serialVersionUID = -2158758215436083966L;

    /**
     * 控制路由是否显示在 sidebar
     */
    private Boolean hidden;

    /**
     * 重定向地址, 访问这个路由时,自定进行重定向
     */
    private String redirect;

    /**
     * 路由名称, 必须设置,且不能重名
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    private Boolean alwaysShow;

    /**
     * 路由元信息
     */
    private RouterMetaVO meta;

    /**
     * 子路由
     */
    private List<RouterVO> children;

}
