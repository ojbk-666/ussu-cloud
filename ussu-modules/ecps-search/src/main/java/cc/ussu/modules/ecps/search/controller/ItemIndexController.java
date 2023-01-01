package cc.ussu.modules.ecps.search.controller;

import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.modules.ecps.item.model.param.SearchParam;
import cc.ussu.modules.ecps.search.service.ItemIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/item")
public class ItemIndexController extends BaseController {

    @Autowired
    private ItemIndexService itemService;

    @GetMapping
    public Object searchSku(SearchParam searchParam) {
        itemService.search(searchParam);
        return null;
    }
}
