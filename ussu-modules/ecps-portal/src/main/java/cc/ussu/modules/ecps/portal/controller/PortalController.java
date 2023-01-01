package cc.ussu.modules.ecps.portal.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.*;
import cc.ussu.modules.ecps.item.model.param.SearchParam;
import cc.ussu.modules.ecps.member.entity.EbCartSku;
import cc.ussu.modules.ecps.member.entity.EbShipAddr;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import cc.ussu.modules.ecps.portal.feign.item.RemoteFeatureService;
import cc.ussu.modules.ecps.portal.feign.item.RemoteParaValueService;
import cc.ussu.modules.ecps.portal.feign.item.RemoteSkuIndexService;
import cc.ussu.modules.ecps.portal.feign.item.RemoveFeatureGroupService;
import cc.ussu.modules.ecps.portal.feign.member.RemoteCartService;
import cc.ussu.modules.ecps.portal.feign.member.RemoteShipAddrService;
import cc.ussu.modules.ecps.portal.feign.order.RemoteOrderService;
import cc.ussu.modules.ecps.portal.service.ItemService;
import cc.ussu.modules.ecps.portal.service.PortalCartSkuService;
import cc.ussu.modules.ecps.skill.entity.EbSessionSkuRelation;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;
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
    @Autowired
    private RedisService redisService;

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
        JsonResult<Map<String, Object>> jr = remoteSkuIndexService.searchSkuForPage(searchParam.getCatId(),
                searchParam.getBrandId(),
                searchParam.getPrice(),
                searchParam.getParas(),
                searchParam.getKeywords(),
                searchParam.getHasStore(),
                searchParam.getSort(),
                searchParam.getPageNum());
        Map<String, Object> data = jr.getData();
        model.addAttribute("skuList", data.get("list"));
        model.addAttribute("skuListPageInfo", data.get("total"));
        return "list/list";
    }

    /**
     * 商品详情页
     */
    @GetMapping("/details")
    public String details(Integer skuId, Model model) {
        Assert.notNull(skuId);
        EbSku ebSku = remoteSkuIndexService.detail(skuId);
        model.addAttribute("skuDetail", ebSku);
        // 获取规格
        EbItem item = ebSku.getItem();
        List<EbFeature> featureList = remoteFeatureService.getFeaturelistByCatId(item.getCatId(), 1, 0);
        model.addAttribute("featureList", featureList);
        model.addAttribute("featureGroup", removeFeatureGroupService.getFeatureListByCatId(item.getCatId(), true, 0));
        model.addAttribute("paraList", remoteParaValueService.allByItemId(item.getItemId()));
        return "details/details";
    }

    /**
     * 购物车
     */
    @GetMapping("/shoplist")
    public ModelAndView shoplist(Model model) {
        model.addAttribute("cartList", remoteCartService.getCartListByUserId());
        return new ModelAndView("shoplist/shoplist");
    }

    /**
     * 创建订单
     */
    @GetMapping("/create_order")
    public ModelAndView createOrder(Model model, String cartIds) {
        injectAllParamToRequestScope();
        // 用户收货地址
        List<EbShipAddr> shipAddrList = remoteShipAddrService.allByUserId(SecurityUtil.getUserId());
        model.addAttribute("shipAddrList", shipAddrList);
        // 默认的收货地址
        List<EbShipAddr> collect1 = shipAddrList.stream().filter(item -> item.getDefaultAddr().equals(1)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect1)) {
            model.addAttribute("defaultShipAddr", collect1.get(0));
        }
        // 勾选的购物车
        // 筛选已勾选购物车列表
        List<Integer> collect = Arrays.stream(StrUtil.splitToInt(cartIds, StrPool.COMMA)).boxed().collect(Collectors.toList());
        List<EbCartSku> cartList = filterSelectedCartList(remoteCartService.getCartListByUserId(), collect);
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
        return new ModelAndView("create_order/create_order");
    }

    /**
     * 提交订单
     */
    @PostMapping("/submit_order")
    public ModelAndView submitOrder(EbOrder order, Model model) {
        JsonResult jr = null;
        EbOrder ebOrder = remoteOrderService.submitOrder(order);
        model.addAttribute("order", ebOrder);
        model.addAttribute("orderTimeStr", DateUtil.formatDateTime(ebOrder.getOrderTime()));
        return new ModelAndView("submit_order/submit_order");
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 跳转到秒杀列表页
     */
    @GetMapping("/skill")
    public ModelAndView toSkill(Model model) {
        Collection<String> keys = redisService.keys(ConstantsEcps.CACHE_SKILL_SESSIONS_PREFIX + "*");
        long now = new Date().getTime();
        List<Integer> skuIdList = new LinkedList<>();
        for (String key : keys) {
            String key1 = key.replace(ConstantsEcps.CACHE_SKILL_SESSIONS_PREFIX, StrUtil.EMPTY);
            long[] longs = StrUtil.splitToLong(key1, StrPool.UNDERLINE);
            if (now >= longs[0] && now <= longs[1]) {
                // 取出skuId
                List<Integer> skuIds = redisService.getCacheList(key);
                skuIdList.addAll(skuIds);
            }
        }
        if (CollUtil.isNotEmpty(skuIdList)) {
            // 获取所有商品信息
            // List<String> relationList = redisService.getMultiCacheMapValue(ConstantsEcps.CACHE_SKILL_SKU, skuIdList);
            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(ConstantsEcps.CACHE_SKILL_SKU);
            BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(ConstantsEcps.CACHE_SKILL_SKU);
            List<String> tempList = ops.multiGet(skuIdList.stream().map(i -> i.toString()).collect(Collectors.toList()));
            List<EbSessionSkuRelation> relationList = new LinkedList<>();
            for (String s : tempList) {
                relationList.add(JSON.parseObject(s, EbSessionSkuRelation.class));
            }
            model.addAttribute("relationList", relationList);
        }
        return new ModelAndView("skill/list");
    }

    @GetMapping("/skilldetails")
    public ModelAndView toSkillDetails(Integer skuId, Model model) {
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(ConstantsEcps.CACHE_SKILL_SKU);
        String s = ops.get(skuId.toString());
        EbSessionSkuRelation relation = JSON.parseObject(s, EbSessionSkuRelation.class);
        model.addAttribute("relation", relation);
        model.addAttribute("skuDetail", relation.getEbSku());
        Integer stock = redisService.getCacheObject(ConstantsEcps.CACHE_SKILL_STOCK_PREFIX + relation.getRandomCode());
        model.addAttribute("stock", stock);
        return new ModelAndView("skill/details");
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
