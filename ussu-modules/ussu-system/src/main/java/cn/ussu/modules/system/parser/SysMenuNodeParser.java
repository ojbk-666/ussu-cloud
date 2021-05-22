package cn.ussu.modules.system.parser;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.ussu.modules.system.entity.SysMenu;

/**
 * 构造菜单树 转换器 依赖HuTool
 *
 * @author liming
 * @date 2020-05-06 23:27
 */
public class SysMenuNodeParser implements NodeParser<SysMenu, String> {

    @Override
    public void parse(SysMenu menu, Tree<String> treeNode) {
        treeNode.setId(menu.getId());
        treeNode.setParentId(menu.getParentId());
        treeNode.setName(menu.getName());

        treeNode.putExtra("components", menu.getComponent());
        treeNode.putExtra("", menu.getIcon());
    }

}
