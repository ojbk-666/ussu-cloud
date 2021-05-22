package cn.ussu.modules.system.parser;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.ussu.modules.system.entity.SysDept;

/**
 * 简单树
 */
public class SysDeptTreeSelectParser implements NodeParser<SysDept, String> {

    @Override
    public void parse(SysDept menu, Tree<String> treeNode) {
        treeNode.setId(menu.getId());
        treeNode.setParentId(menu.getParentId());
        treeNode.setName(menu.getName());
        treeNode.putExtra("label", menu.getName());
    }

}
