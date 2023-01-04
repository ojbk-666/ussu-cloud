package cc.ussu.modules.system.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RouterMetaVO implements Serializable {

    private static final long serialVersionUID = 228901040923624541L;

    /**
     * 路由标题, 用于显示面包屑, 页面标题 *推荐设置
     */
    private String title;

    /**
     * 路由在 menu 上显示的图标
     */
    private String icon;

    /**
     * 是否不缓存该路由
     */
    private boolean noCache;

    /**
     * 菜单链接跳转目标（参考 html a 标记）
     */
    private String target;

    private String link;

}
