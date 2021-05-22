package cn.ussu.modules.system.parser;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.ussu.modules.system.entity.SysMenu;

/**
 * 简单树
 */
public class SysMenuTreeSelectParser implements NodeParser<SysMenu, String> {

    @Override
    public void parse(SysMenu menu, Tree<String> treeNode) {
        treeNode.setId(menu.getId());
        treeNode.setParentId(menu.getParentId());
        treeNode.setName(menu.getName());
        treeNode.putExtra("label", menu.getName());
    }

}
