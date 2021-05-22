package cn.ussu.modules.system.parser;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.ussu.modules.system.entity.SysMenu;

/**
 * 构造角色菜单树
 *
 * @author liming
 * @date 2020-05-06 23:27
 */
public class SysRoleMenuNodeParser implements NodeParser<SysMenu, String> {

    @Override
    public void parse(SysMenu menu, Tree<String> treeNode) {
        treeNode.setId(menu.getId());
        treeNode.setParentId(menu.getParentId());
        treeNode.setName(menu.getName());

        treeNode.putExtra("title", menu.getName()); //显示名称
        boolean checked = menu.getChecked() == null ? false : true;
        treeNode.putExtra("spread", checked);  //是否展开
        treeNode.put("checked", checked); //是否选中
        treeNode.put("open", checked);
        // treeNode.putExtra("url", menu.getUrl());
        // treeNode.putExtra("icon", menu.getIcon());
    }

}
