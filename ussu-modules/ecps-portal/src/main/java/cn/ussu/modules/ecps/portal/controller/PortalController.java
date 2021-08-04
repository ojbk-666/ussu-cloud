package cn.ussu.modules.ecps.portal.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.model.vo.SimplePageInfo;
import cn.ussu.common.core.model.vo.SimplePageResult;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.item.entity.*;
import cn.ussu.modules.ecps.item.model.param.SearchParam;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import cn.ussu.modules.ecps.member.entity.EbShipAddr;
import cn.ussu.modules.ecps.order.entity.EbOrder;
import cn.ussu.modules.ecps.portal.feign.order.RemoteOrderService;
import cn.ussu.modules.ecps.portal.feign.item.RemoteFeatureService;
import cn.ussu.modules.ecps.portal.feign.item.RemoteParaValueService;
import cn.ussu.modules.ecps.portal.feign.item.RemoteSkuIndexService;
import cn.ussu.modules.ecps.portal.feign.item.RemoveFeatureGroupService;
import cn.ussu.modules.ecps.portal.feign.member.RemoteCartService;
import cn.ussu.modules.ecps.portal.feign.member.RemoteShipAddrService;
import cn.ussu.modules.ecps.portal.service.ItemService;
import cn.ussu.modules.ecps.portal.service.PortalCartSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 前端页面访问控制层
 */
@Controller
public class PortalController extends BasePortalController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private RemoteSkuIndexService remoteSkuIndexService;
    @Autowired
    private RemoteFeatureService remoteFeatureService;
    @Autowired
    private RemoveFeatureGroupService removeFeatureGroupService;
    @Autowired
    private RemoteParaValueService remoteParaValueService;
    @Autowired
    private RemoteCartService remoteCartService;
    @Autowired
    private RemoteShipAddrService remoteShipAddrService;
    @Autowired
    private PortalCartSkuService portalCartSkuService;
    @Autowired
    private RemoteOrderService remoteOrderService;

    /**
     * 首页
     */
    @GetMapping({"/", "/index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("store/index");
        List<EbCat> catTree = itemService.getCatTree();
        modelAndView.addObject("cats", catTree);
        return modelAndView;
    }

    /**
     * 分类 商品列表
     */
    @GetMapping("/list")
    public String toList(SearchParam searchParam, Model model) {
        Integer catId = searchParam.getCatId();
        if (catId != null) {
            // 品牌
            List<EbBrand> brandList = itemService.getBrandListByCatId(catId);
            model.addAttribute("brandList", brandList);
            // 获取属性
            List<EbFeature> featureList = itemService.getFeatureListByCatId(catId);
            model.addAttribute("featureList", featureList);
        }
        JsonResult jr = remoteSkuIndexService.searchSkuForPage(searchParam.getCatId(),
                searchParam.getBrandId(),
                searchParam.getPrice(),
                searchParam.getParas(),
                searchParam.getKeywords(),
                searchParam.getHasStore(),
                searchParam.getSort(),
                searchParam.getPageNum());
        SimplePageResult skuListPageResult = jr.getData(SimplePageResult.class);
        SimplePageInfo pageInfo = skuListPageResult.getPageInfo();
        List<EbSku> skuList = skuListPageResult.getRecords();
        model.addAttribute("skuList", skuList);
        model.addAttribute("skuListPageInfo", pageInfo);
        return "list/list";
    }

    /**
     * 商品详情页
     */
    @GetMapping("/details")
    public String details(Integer skuId, Model model) {
        Assert.notNull(skuId);
        JsonResult jr = remoteSkuIndexService.detail(skuId);
        Map skuDetail = jr.getData();
        if (jr.isSuccess()) {
            model.addAttribute("skuDetail", skuDetail);
            // 获取规格
            EbSku ebSku = BeanUtil.toBean(skuDetail, EbSku.class);
            EbItem item = ebSku.getItem();
            JsonResult featureJr = remoteFeatureService.getFeaturelistByCatId(item.getCatId(), 1, 0);
            List<EbFeature> featureList = featureJr.getData();
            model.addAttribute("featureList", featureList);
            JsonResult featureListByCatId = removeFeatureGroupService.getFeatureListByCatId(item.getCatId(), true, 0);
            model.addAttribute("featureGroup", featureListByCatId.getData());
            JsonResult itemParaJr = remoteParaValueService.allByItemId(item.getItemId());
            model.addAttribute("paraList", itemParaJr.getData());
        }
        return "details/details";
    }

    /**
     * 购物车
     */
    @GetMapping("/shoplist")
    public ModelAndView shoplist(Model model) {
        JsonResult cartListJr = remoteCartService.getCartListByUserId();
        if (!cartListJr.isSuccess()) {
            return toLogin(request.getRequestURI());
        }
        model.addAttribute("cartList", cartListJr.getData());
        return new ModelAndView("shoplist/shoplist");
    }

    /**
     * 创建订单
     */
    @GetMapping("/create_order")
    public ModelAndView createOrder(Model model, String cartIds) {
        injectAllParamToRequestScope();
        // 用户收货地址
        JsonResult shipAddrListJr = remoteShipAddrService.allByUserId(SecurityUtils.getUserId());
        if (!shipAddrListJr.isSuccess()) {
            return toLogin(request.getRequestURI());
        }
        model.addAttribute("shipAddrList", shipAddrListJr.getData());
        // 默认的收货地址
        List<Map> tempList = shipAddrListJr.getData();
        List<EbShipAddr> shipAddrList = BeanUtil.copyToList(tempList, EbShipAddr.class);
        List<EbShipAddr> collect1 = shipAddrList.stream().filter(item -> item.getDefaultAddr().equals(1)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect1)) {
            model.addAttribute("defaultShipAddr", collect1.get(0));
        }
        // 勾选的购物车
        JsonResult cartListJr = remoteCartService.getCartListByUserId();
        if (!cartListJr.isSuccess()) {
            return toLogin(request.getRequestURI());
        } else {
            // 筛选已勾选购物车列表
            List<Map> all = cartListJr.getData();
            List<Integer> collect = Arrays.stream(StrUtil.splitToInt(cartIds, StrPool.COMMA)).boxed().collect(Collectors.toList());
            List<EbCartSku> cartList = filterSelectedCartList(BeanUtil.copyToList(all, EbCartSku.class), collect);
            model.addAttribute("cartList", cartList);
            // 商品总价格
            BigDecimal skuTotalPrice = new BigDecimal("0");
            // 商品总数量
            int skuTotalQuantity = 0;
            for (EbCartSku cartSku : cartList) {
                skuTotalPrice = NumberUtil.add(skuTotalPrice, cartSku.getTotalPrice());
                skuTotalQuantity += cartSku.getQuantity();
            }
            model.addAttribute("skuTotalPrice", skuTotalPrice.setScale(2).toString());
            model.addAttribute("skuTotalQuantity", skuTotalQuantity);
        }
        return new ModelAndView("create_order/create_order");
    }

    /**
     * 提交订单
     */
    @PostMapping("/submit_order")
    public ModelAndView submitOrder(EbOrder order, Model model) {
        JsonResult jr = remoteOrderService.submitOrder(order);
        if (!jr.isSuccess()) {
            // error
            model.addAttribute("msg", jr.getMsg());
            return new ModelAndView("submit_order/error");
        }
        EbOrder ebOrder = jr.getData(EbOrder.class);
        model.addAttribute("order", ebOrder);
        model.addAttribute("orderTimeStr", DateUtil.formatDateTime(ebOrder.getOrderTime()));
        return new ModelAndView("submit_order/submit_order");
    }

    /**
     * 路径跳转
     */
    @GetMapping("/{p}")
    public ModelAndView toFtlh(@PathVariable String p) {
        injectAllParamToRequestScope();
        return new ModelAndView(p + StrPool.SLASH + p);
    }

    /**
     * 从购物车列表筛选已选择的
     *
     * @param list
     * @param selectedIds
     * @return
     */
    private List<EbCartSku> filterSelectedCartList(List<EbCartSku> list, List<Integer> selectedIds) {
        LinkedList<EbCartSku> r = new LinkedList<>();
        for (EbCartSku cartSku : list) {
            if (selectedIds.contains(cartSku.getCartSkuId())) {
                r.push(cartSku);
            }
        }
        return r;
    }

}
