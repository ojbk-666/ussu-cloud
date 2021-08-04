<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0040)https://shop.10086.cn/order/shoplist.php -->
<html xmlns="http://www.w3.org/1999/xhtml" class="">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="shortcut icon" type="image/x-icon" href="https://img0.shop.10086.cn/favicon.ico__0.ico">
    <meta name="WT.location" content="431">
    <title>我的购物车-移动商城_移动商城</title>
    <link href="${IMAGE_SERVER}cloud/static/jiesuan/css/list.css" rel="stylesheet" type="text/css">
    <!--[if lt IE 9]>
    <script r='m'>document.createElement("section")</script><![endif]-->
    <script charset="utf-8" src="${IMAGE_SERVER}cloud/static/jiesuan/js/jquery.placeholder.js"></script>
    <meta name="WT.mobile" content="">
</head>
<body style="">

<#include "../common/script_screenResize.ftlh">

<script type="text/html" id="login_box">
    <div class="minloginbox loadingBlock clearfix" style="width:350px;">
        <iframe src="https://login.10086.cn/SSOCheck.action?channelID=12002&backUrl=https%3A%2F%2Fshop.10086.cn%2Fsso%2Fminilogincallback.php%3Faction%3Dforcelogin"
                width="350" height="400" allowTransparency="true" frameborder="0" scrolling="no"></iframe>
    </div>
</script>

<#include "../common/topNav.ftlh">

<iframe src="${IMAGE_SERVER}cloud/static/jiesuan/我的购物车-移动商城_移动商城_files/saved_resource.html" id="ssocheckframe"
        style="display:none"></iframe>
<#include "../common/script_allcity.ftlh">

<div class="globalMain clearfix" id="header">

    <div class="search_mlogo clearfix">

        <#include "../common/logoWhere.ftlh">

        <!--[if IE]>
        <style>
            /*#header .search .txt{padding-top:6px;}*/
        </style>
        <![endif]-->
        <div class="search">
            <form class="clearfix" action="https://search.10086.cn/shop/list" autocomplete="off" method="get"
                  target="_blank">
                <input id="skeywords" type="text" name="key" autocomplete="off" disableautocomplete="" class="txt"
                       onfocus=" this.style.color = &quot;#333333&quot;" onblur=" this.style.color =&quot;#ccc&quot;">
                <ul id="searchKeyWord1598367246265" class="search_toplist" style="display: none;"></ul>
                <ul id="searchHistory1598367246265" class="search_toplist" style="display: none;"></ul>
                <input type="submit" class="b" value="">
                <input type="hidden" class="txt" name="province_id" value="431">
                <input type="hidden" class="txt" name="city_id" value="431">
                <input type="hidden" class="txt" name="from" value="10603">
                <script type="text/javascript">window.searchDomain = "https://search.10086.cn/";</script>
            </form>
            <ul id="hotsearch" class="floatleft search_hotlist clearfix">
                <li><a target="_blank" dcs_id="MALL_RCSS_1_5G正式商用" type="" class="search_red ac_dcs_base"
                       href="http://www.10086.cn/5Gplus/?WT.ac=hot_search_010_1_5G%E6%AD%A3%E5%BC%8F%E5%95%86%E7%94%A8">5G正式商用</a>
                </li>
                <li><a target="_blank" dcs_id="MALL_RCSS_2_信用购机" type="" class="search_red ac_dcs_base"
                       href="https://shop.10086.cn/list/101_431_431_1_0_0_0_0_0_1_0.html?WT.ac=hot_search_010_2_%E4%BF%A1%E7%94%A8%E8%B4%AD%E6%9C%BA">信用购机</a>
                </li>
                <li><a target="_blank" dcs_id="MALL_RCSS_3_积分兑换" type="" class="search_red ac_dcs_base"
                       href="https://search.10086.cn/shop/list?key=%E7%A7%AF%E5%88%86%E5%85%91%E6%8D%A2&provinceId=431&cityId=431&WT.ac=hot_search_010_3_%E7%A7%AF%E5%88%86%E5%85%91%E6%8D%A2&from=10602">积分兑换</a>
                </li>
                <li><a target="_blank" dcs_id="MALL_RCSS_4_华为P30" type="" class="search_red ac_dcs_base"
                       href="https://search.10086.cn/shop/list?key=%E5%8D%8E%E4%B8%BAP30&provinceId=431&cityId=431&WT.ac=hot_search_010_4_%E5%8D%8E%E4%B8%BAP30&from=10602">华为P30</a>
                </li>
                <li><a target="_blank" dcs_id="MALL_RCSS_5_4G流量卡" type="" class="search_blue ac_dcs_base"
                       href="https://search.10086.cn/shop/list?key=4G%E6%B5%81%E9%87%8F%E5%8D%A1&provinceId=431&cityId=431&WT.ac=hot_search_010_5_4G%E6%B5%81%E9%87%8F%E5%8D%A1&from=10602">4G流量卡</a>
                </li>
                <li><a target="_blank" dcs_id="MALL_RCSS_6_服务密码" type="" class="search_red ac_dcs_base"
                       href="https://search.10086.cn/shop/list?key=%E6%9C%8D%E5%8A%A1%E5%AF%86%E7%A0%81&provinceId=431&cityId=431&WT.ac=hot_search_010_6_%E6%9C%8D%E5%8A%A1%E5%AF%86%E7%A0%81&from=10602">服务密码</a>
                </li>
            </ul>

        </div>
    </div>

    <#assign topMenuNav="shop">
    <#include "../common/topMenuNav.ftlh">

</div>


<div class="header_area_template" style="display:none;">
    <!-- <li style="height:24px;overflow:hidden;"><a href="javascript:void(0)" class="ac_city_choose" location="{province_id}_{city_id}" title="{city_name}">{city_name}</a></li> --> </div>

<style>
    a.disable {
        filter: alpha(opacity=60);
        -moz-opacity: 0.6;
        -khtml-opacity: 0.6;
        opacity: 0.6;
    }
</style>
<div class="globalMain">
    <ul class="filter-bar">
        <li class="current">全部商品<span>1</span></li>
    </ul>
</div>
<div class="globalMain cart-warp">

    <div class="cart-main">
        <div class="cart-thead">
            <dl>
                <dd class="t-checkbox alignleft"><input type="checkbox" class="ac_cart_checkall"> 全选</dd>
                <dd class="t-goods alignleft ">商品信息</dd>
                <dd class="t-props">&nbsp;</dd>
                <dd class="t-price">单价</dd>
                <dd class="t-quantity">数量</dd>
                <dd class="t-sum">金额</dd>
                <dd class="t-action">操作</dd>
            </dl>
        </div>
        <div class="cart-list ">
            <div class="cartgroup cartgroup_1000049">
                <div class="shop"><input type="checkbox" class="ac_cart_checkgroup"> <span>终端公司</span></div>
                <div class="cart-item-list globalGrayBorder">
                    <#--购物车循环开始-->
                    <#list cartList as cartItem>
                    <div class="item-list  single selected">
                        <div class="item-item last" cartid="${cartItem.cartSkuId}">
                            <dl>
                                <dd class="p-checkbox">
                                    <input class="ac_cart_select" name="cartid" value="${cartItem.cartSkuId}" num="${cartItem.quantity}"
                                           price="${cartItem.price*100}" total_price="${cartItem.totalPrice*100}" type="checkbox">
                                    <span class="line"></span>
                                </dd>
                                <dd class="p-goods">
                                    <a target="_blank" href="./details?skuId=${cartItem.skuId}" class="blue ac_dcs_base" dcs_id="">
                                        <img src="${IMAGE_SERVER}${cartItem.skuImg}" class="floatleft imgBorder" width="50" height="50">
                                        <span title="【优惠购机】 iPhone11（A2223）双卡公开版4G手机">【优惠购机】 ${cartItem.skuName}</span><br>
                                        <span class="red" title="信用购机享双重优惠">信用购机享双重优惠...</span>
                                    </a>
                                </dd>
                                <dd class="p-props"><span class="gray">红色  64G</span></dd>
                                <dd class="p-price">¥ ${cartItem.price}</dd>
                                <dd class="p-quantity aligncenter">
                                    <a href="javascript:void(0)" class="bt minus ac_cart_decr ac_dcs_base disable " dcs_id="SHOPLIST_GOODS_DECR">-</a>
                                    <input type="text" num="1" value="${cartItem.quantity}" class="txt-normal ac_cart_update ">
                                    <a href="javascript:void(0)" class="bt plus ac_cart_incr ac_dcs_base disable" dcs_id="SHOPLIST_GOODS_INCR">+</a> <br>
                                    <span class="red">限购1件</span>
                                </dd>
                                <dd class="p-sum">¥ ${cartItem.totalPrice}</dd>
                                <dd class="p-action">
                                    <a href="javascript:void(0)" class="ac_cart_delete ac_dcs_base" dcs_id="SHOPLIST_DELETE" cartid="${cartItem.cartSkuId}">删除</a>
                                </dd>
                            </dl>
                            <div class="item-line"></div>
                        </div>
                    </div>
                    </#list>
                    <#--购物车循环结束-->
                </div>
            </div>
        </div>
    </div>

    <div class="cart-floatbar" id="" style="position:static">
        <dl>
            <dd class="t-checkbox floatleft">
                <input type="checkbox" class="ac_cart_checkall"> <span>全选</span>
                <a href="javascript:void(0)" class="ac_cart_deleteall">删除选中的商品</a>
            </dd>
            <dd class="bt floatright">
                <a class="ac_cart_settlement ac_dcs_base" dcs_id="SHOPLIST_SETTLEMENT" href="javascript:void(0)">去结算</a>
            </dd>
            <dd class="price-sum floatright">
                <div class="black">总价（不含运费）：
                    <span class="price totalprice">¥ <label id="totalprice">4399.00</label></span>
                </div>
                <!-- div >已节省：<span class="price reprice">- ¥ <label id="discount">0.00</label></span></div -->
            </dd>
            <dd class="amount-sum floatright black">已选择<em class="red" id="cartcount">1</em>件商品</dd>
        </dl>
    </div>
</div>
<!--
<div class="globalMain cart-warp">
    <ul class="filter-bar">
        <a class="floatright" href="https://shop.10086.cn/order/footprint.php">查看更多</a>
        <li class="current">
            <a href="https://shop.10086.cn/order/footprint.php">我的足迹</a>
        </li>
    </ul>
    <div class="footprint-container">
        <div class="goodsList">
            <ul class="clearfix withhover">
                <li>
                    <div class="body">
                        <a href="javascript:void(0)" class="floatright ac_footprint_delete zj_del_bt"
                           ref="1012520">删除</a>
                        <p class="img"><a target="_blank"
                                          href="https://shop.10086.cn/goods/100_100_1012520_1067523.html"
                                          title="【优惠购机】 iPhone11（A2223）双卡公开版4G手机"><img
                                        alt="【优惠购机】 iPhone11（A2223）双卡公开版4G手机" class="lazy"
                                        src="${IMAGE_SERVER}cloud/static/jiesuan/images/ths5jdmp7njd4rs4_170x170.png"
                                        data-original="//img1.shop.10086.cn/goods/ths5jdmp7njd4rs4_170x170.png"
                                        style="display: inline;"></a></p>
                        <p class="price red"><em class="fontYaHei size14">￥</em>4399</p>
                        <p class="name" title="【优惠购机】 iPhone11（A2223）双卡公开版4G手机 信用购机享双重优惠，优惠购机最高优惠300元，具体下拉见详情页"><a
                                    target="_blank" href="https://shop.10086.cn/goods/100_100_1012520_1067523.html">【优惠购机】
                                iPhone11（A2223）双卡公开版4G手机<span class="red">
													 信用购机享双重优惠，优惠购机最高优惠300元，具体下拉见详情页
											</span></a></p>

                        <p class="marginTop10">
                            <a target="_blank" href="https://shop.10086.cn/goods/100_100_1012520_1067523.html"
                               class="btn btnSmall btnGray marginRight10"><i></i><b></b>
                                立即购买

                            </a>
                        </p>


                    </div>
                </li>
                <li>
                    <div class="body">
                        <a href="javascript:void(0)" class="floatright ac_footprint_delete zj_del_bt"
                           ref="1006550">删除</a>
                        <p class="img"><a target="_blank"
                                          href="https://shop.10086.cn/goods/100_100_1006550_1063410.html"
                                          title="【优惠购机】iPhone11 Pro双卡 公开版4G手机"><img alt="【优惠购机】iPhone11 Pro双卡 公开版4G手机"
                                                                                    class="lazy"
                                                                                    src="${IMAGE_SERVER}cloud/static/jiesuan/images/thncs7asp9bttqpen_170x170.png"
                                                                                    data-original="//img1.shop.10086.cn/goods/thncs7asp9bttqpen_170x170.png"
                                                                                    style="display: inline;"></a></p>
                        <p class="price red"><em class="fontYaHei size14">￥</em>6599</p>
                        <p class="name" title="【优惠购机】iPhone11 Pro双卡 公开版4G手机信用购机最高优惠3780元 "><a target="_blank"
                                                                                              href="https://shop.10086.cn/goods/100_100_1006550_1063410.html">【优惠购机】iPhone11
                                Pro双卡 公开版4G手机<span class="red">
													信用购机最高优惠3780元
											</span></a></p>

                        <p class="marginTop10">
                            <a target="_blank" href="https://shop.10086.cn/goods/100_100_1006550_1063410.html"
                               class="btn btnSmall btnGray marginRight10"><i></i><b></b>
                                立即购买

                            </a>
                        </p>


                    </div>
                </li>
                <li>
                    <div class="body">
                        <a href="javascript:void(0)" class="floatright ac_footprint_delete zj_del_bt"
                           ref="1077764">删除</a>
                        <p class="img"><a target="_blank"
                                          href="https://shop.10086.cn/goods/100_100_1077764_1063382.html"
                                          title="【优惠购机】iPhone11 Pro Max（A2220）公开版"><img
                                        alt="【优惠购机】iPhone11 Pro Max（A2220）公开版" class="lazy"
                                        src="${IMAGE_SERVER}cloud/static/jiesuan/images/thnxsp7tfbzmtt9ck3_170x170.png"
                                        data-original="//img1.shop.10086.cn/goods/thnxsp7tfbzmtt9ck3_170x170.png"
                                        style="display: inline;"></a></p>
                        <p class="price red"><em class="fontYaHei size14">￥</em>7299</p>
                        <p class="name" title="【优惠购机】iPhone11 Pro Max（A2220）公开版信用购机最高优惠3780元 *购机享好礼"><a target="_blank"
                                                                                                        href="https://shop.10086.cn/goods/100_100_1077764_1063382.html">【优惠购机】iPhone11
                                Pro Max（A2220）公开版<span class="red">
													信用购机最高优惠3780元 *购机享好礼
											</span></a></p>

                        <p class="marginTop10">
                            <a target="_blank" href="https://shop.10086.cn/goods/100_100_1077764_1063382.html"
                               class="btn btnSmall btnGray marginRight10"><i></i><b></b>
                                立即购买

                            </a>
                        </p>


                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
-->


<style type="text/css">
    .lowerIEtips {
        display: none;
        _display: block;
        _position: absolute;
    }
</style>

<#include "../common/footerNav.ftlh">
<#include "../common/footer.ftlh">

<div class="mask" id="mask"></div>
<div class="lowerIEtips" style="position: fixed;_position: absolute;bottom:0px;display:none;_display:block;">
    <div class="tipcon"><span><img src="${IMAGE_SERVER}cloud/static/jiesuan/images/logo2.png__240.png"
                                   style="width: 160px; height: 29px;"></span>
        <p>您的浏览器版本过低，如果页面显示不正常，建议将浏览器升级到最新版本。</p><!-- <a class="close" id="closeTips"></a> --></div>
</div>

<script type="text/javascript" src="${IMAGE_SERVER}cloud/static/jiesuan/js/loader.js__357.js"></script>
<script src="${IMAGE_SERVER}cloud/static/jiesuan/js/sdc_mall.js__463.js"></script>
<script type="text/javascript">
    window.ENVOBJ = {
        "loc": {"province_id": 431, "city_id": 431},
        "loginurl": "https:\/\/login.10086.cn\/SSOCheck.action?channelID=12002&backUrl=https%3A%2F%2Fshop.10086.cn%2Forder%2Fshoplist.php%3Fforcelogin%3D1"
    };
    LOADJS('//img0.shop.10086.cn/combo/__577/script/jquery-1.10.2.js__script/cookie/jquery.cookie.js__script/iprompt/jquery.iprompt.js__script/formater.js__script/pubsub/jquery.pubsub.js__script/interactive/jquery.interactive.js__script/jquery.lazyload.js__www/core/js/global.js__www/core/js/fav.js__www/core/js/dcs_tracker.js__www/mod/search/js/shop_jsonp_search.js__script/mousewheel/jquery.mousewheel.js__script/customscrollbar/jquery.mCustomScrollbar.js__script/form/jquery.form.v318.js__script/city/jquery.city.v1.js__script/validate/jquery.validate.js__script/placeholder/jquery.placeholder.js', function () {
//	$.domains = window.ENVOBJ.domains;


        $
            .sub('click_login_mini', __mini_login)
            .sub('login_mini', __mini_login)
            .sub('login_mini_msg', __mini_login_msg)
        ;
        var _options = null
        var _spritebox = null;

        function __mini_login(event, options) {
            _options = options;
            var miniframe = $('#login_box').html();
            if (miniframe) {
                _spritebox = $('#jquery-interactive-box2').hide();
                $('#jquery-interactive-mask').hide();
                var $box = $.alert(miniframe, {
                    onLoad: function ($box) {
                        $box.find('.interactive-content').css({
                            'background-color': '#ECECEC'
                        });
                    },
                    onClose: function () {
                        $.pub('login_mini_cancel');
                        _options = null;
                        _spritebox = null;
                        $.UI.hide();
                    },
                    liveTime: null,
                    noCloser: false
                });
                return false;
            } else {
                return true;
            }
        }

        function __mini_login_msg(event, r) {
            if (window.initUser) {
                window.initUser(r);
            }
            $.UI.hide('alert');
            if (r.code == 0) {
                if (_options && typeof _options == 'object') $.ajax(_options);
                _options = null;
                if (_spritebox && _spritebox.length) {
                    $('#jquery-interactive-mask').show();
                    _spritebox.show();
                }
                _spritebox = null;
            } else {
                $.UI.hide();
            }
        }


        ;
        $(function () {
            $(".cart_list").mCustomScrollbar();
        });


        ;
        $(function () {
            $.getJSON('/ajax/cartshop/amount.json', function (r) {
                if (r.code) {
                    $.alert(r.msg);
                } else {
                    $('.cart .red').text(r.data.num);
                    $('#returntop .cart-quan em').text(r.data.num);
                    $('.have_goods .btm .cartcount b').text(r.data.num);
                }
                ;
            });
            $
                .sub('mouseover_cart1_count', __cart1_count)
                .sub('msg_cart', __cart1_show, false)
            ;
            var list_lock = true;

            function __cart1_count() {
                if (list_lock) {
                    $.getJSON('/ajax/cartshop/list.json', function (r) {
                        if (r.code) {
                            $.alert(r.msg);
                        } else {
                            $.pub('msg_cart', r.data);
                        }
                    });
                }
                list_lock = false;
            }


            function __cart1_show(event, r) {
                var cartdata = r;
                var $cart_list = [];
                var $cartcount = null;
                if (cartdata && cartdata.cartcount) {
                    var total = 0, count = 0;
                    $('.dorpdown-layer .cart_list .mCSB_container').html(cartdata.content);
                    $('.have_goods .cartcount b,.rightMenu .cart .red,.rightfloatbox .cart-quan em').html(cartdata.cartcount);
                    $cart_list = $('.rightMenu .cart_list').find('li');
                    if ($cart_list.length > 5) {
                        $('.cart_list').addClass('height268');
                    } else {
                        $('.cart_list').removeClass('height268');
                    }
                    ;
                    $($cart_list).each(function () {
                        var $t = $(this);
                        total += parseInt($t.attr('num')) * parseInt($t.attr('price') * 100) || 0;
                        //count +=parseInt($t.attr('num'))||0;
                    });
                    $('.have_goods .btm .red b').text((total / 100).toFixed(2));
                    //$('.have_goods .btm .cartcount b').text(count);
                    $cartcount = $('.cart_list ul').attr('cartcount');
                    if ($cart_list.length == 0 && $cartcount) {
                        $('.dorpdown-layer .loadingBlock').hide();
                        $('.dorpdown-layer .nogoods_p').hide();
                        $('.dorpdown-layer .abate').show();
                        $('.dorpdown-layer .nogoods').show();
                        $('.dorpdown-layer .have_goods').hide();
                    } else {
                        $('.dorpdown-layer .nogoods').hide();
                        $('.dorpdown-layer .loadingBlock').hide();
                        $('.dorpdown-layer .have_goods').show();
                        $('.dorpdown-layer .cart_goodslist').show();
                        $('.dorpdown-layer .have_goods .btm').show();
                    }
                } else {
                    $('.have_goods .cartcount b,.rightMenu .cart .red,.rightfloatbox .cart-quan em').html('0');
                    $('.dorpdown-layer .loadingBlock').hide();
                    $('.dorpdown-layer .abate').hide();
                    $('.dorpdown-layer .nogoods_p').show();
                    $('.dorpdown-layer .nogoods').show();
                    $('.dorpdown-layer .have_goods').hide();
                }
                ;

            }

            var timer = null;
            $('.cart').hover(function () {
                var is_cart = '1';
                if (is_cart) return false;
                clearTimeout(timer);
                $(".rightMenu .dorpdown-layer").show();
                //__hover_get_cart();
                //$.pub("hover_get_cart");
            }, function () {
                timer = setTimeout(function () {
                    list_lock = true;
                    $('.cart').removeClass('current');
                    $(".rightMenu .dorpdown-layer").hide();
                }, 100);
            });
            $('.dorpdown-layer').hover(function () {
                list_lock = false;
                clearTimeout(timer);
                $('.cart').addClass('current');
                $(this).show();
            }, function () {
                timer = setTimeout(function () {
                    list_lock = true;
                    $('.cart').removeClass('current');
                    $('.rightMenu .dorpdown-layer').hide();
                }, 100);
            });


            $.sub('click_cartlist_delete', __cartlist_delete, false).sub('cartlist_del', cartlist_del, false);

            function __cartlist_delete() {
                var $t = $(this);
                var cartcount = $t.closest('ul').attr('cartcount');
                $.getJSON('/ajax/cartshop/delete.json', {selected: $t.closest('li').attr('cartid')}, function (r) {
                    if (r.code) {
                        $.alert(r.msg);
                    } else {
                        $t.closest('li').remove();
                        $cart_list = $('.rightMenu .cart_list').find('li');
                        if ($cart_list.length > 5) {
                            $('.cart_list').addClass('height268');
                        } else {
                            $('.cart_list').removeClass('height268');
                        }
                        ;
                        $.pub('cartlist_del');
                    }
                    ;
                });
            }

            function cartlist_del() {
                var c_num = $('.cart .red').text();
                $('.have_goods .cartcount b,.rightMenu .cart .red,.rightfloatbox .cart-quan em').text(parseInt(c_num - 1));
                $('#returntop .cart-quan em').text(parseInt(c_num - 1));
                var total = 0;
                var $cart_list = $('.rightMenu .cart_list').find('li');
                $($cart_list).each(function () {
                    var $t = $(this);
                    total += parseInt($t.attr('num')) * parseInt($t.attr('price') * 100) || 0;
                });
                $('.have_goods .btm .red b').text((total / 100).toFixed(2));
                if (c_num && c_num - 1 != 0) {
                    $('.dorpdown-layer .nogoods').hide();
                    $('.dorpdown-layer .have_goods').show();
                } else {
                    $('.dorpdown-layer .nogoods').show();
                    $('.dorpdown-layer .have_goods').hide();
                }
                ;
            }
        });


        $(document).ready(function () {
            var timers = null;

            $(".cmccclient").hover(function () {
                clearTimeout(timers);
                var cmcc_left_1 = $('.cmccclient').position().left + 2;
                $('.ewm').css('left', cmcc_left_1);
                $('.ewm').show();
            }, function () {
                timers = setTimeout(function () {
                    $('.ewm').hide();
                }, 100);
            });
            $('.ewm').hover(function () {
                var cmcc_left_2 = $('.cmccclient').position().left + 2;
                clearTimeout(timers);
                $('.cmccclient').addClass('cmccclienthover');
                $(this).css('left', cmcc_left_2);
                $(this).show();
            }, function () {
                var $t = $(this);
                timers = setTimeout(function () {
                    $('.cmccclient').removeClass('cmccclienthover');
                    $t.hide();
                }, 100);
            });
        });


        window.initUser = function (r) {
            if (r.code == 0) {
                window.loginUser = r.data;
                var nickname = r.data.nick_name || r.data.user_name;
                var tmpnick = nickname.split('@')[0];
                var noread = (r.data.msg_remind || {}).noread || 0;
                var showeye = r.data.is_china_mobile ? '' : 'eye-hide'; // 互联网用户不显示眼睛
                var tmpstr =
                    '<span class="tel floatleft marginRight10" title="' + nickname + '">' + tmpnick + '</span><i class="floatleft marginRight10 icon_eye ' + showeye + ' ac_handle_mobile"></i>' +
                    '<span class="floatleft noticearea" >' +
                    ' <a href="https://shop.10086.cn/i/?f=mynews"  target="_blank" class="red floatleft">消息 ' + noread + '</a> </span>' +
                    '<a class="red bold" href="javascript:void(logout())" title="退出">[退 出]</a><em>|</em>';
                document.getElementById('userinfo').innerHTML = tmpstr;
                if ($.pub) {
                    $.pub('login_success', r.data);
                }
                // 当前登陆人插码 ,如果是手机号登录，写入手机号，否则清空
                if (_tag) {
                    if (r.data.is_china_mobile) {
                        _tag.setMobile(r.data.wtmobile);
                    } else {
                        _tag.setMobile();
                    }
                }
            } else if (r.code == 3) {
                $('#ssocheckframe').attr('src', r.data);
            } else {
                $.pub('user_not_login_after_query');
                $('.btnOnestepbuy').hide();
                // 当前登陆人插码 没登陆清空
                if (_tag) {
                    _tag.setMobile();
                }
            }
        }
        var userParam = {
            'province_id': '431',
            'city_id': '431'
        }
        $.getJSON('/ajax/user/userinfo.json', userParam, initUser);


        ;
        $(function () {
            var provinceId = "431";	//省区编码
            var cityId = "431";	//市区编码
            var charset = "utf-8";

            $("#skeywords").search({
                type: 1,
                maxResult: 10,
                provinceId: provinceId,
                cityId: cityId,
                tabType: 1,
                tabName: '',
                showDefaultWord: 1,
                showHotSearchWord: 1,
                charset: charset
            }).change(function () {
                $(this).siblings('[name="from"]').val('10601');
            });
        });


        $(function () {
            $('.shopSearch .mainmenu').hover(function () {
                $(this).addClass('hover');
            }, function () {
                $(this).removeClass('hover');
            });
            var dcs_map = {
                '101': 'Home_FirstScreen_PHONE',
                '140': 'Home_FirstScreen_EXPENSE',
                '146': 'Home_FirstScreen_VAS',
                '134': 'Home_FirstScreen_NUMBER',
                '128': 'Home_FirstScreen_ACCESORY',
                'jiaohuafei': 'Home_FirstScreen_HUAFEI',
                'home_firstscreen_grzx': 'Home_FirstScreen_PERSONALCENTER',
                'home_firstscreen_ad_1': 'Home_FirstScreen_AD_1',
                'home_firstscreen_ad_2': 'Home_FirstScreen_AD_2',
                'home_firstscreen_ad_3': 'Home_FirstScreen_AD_3',
                'home_firstscreen_ad_4': 'Home_FirstScreen_AD_4',
                'home_firstscreen_ljcz': 'Home_FirstScreen_RECHARGE',
                'home_firstscreen_hfcz': 'Home_FirstScreen_HF_RECHARGE',
                'home_firstscreen_llcz': 'Home_FirstScreen_LL_RECHARGE',
                'home_firstscreen_30': 'Home_FirstScreen_30',
                'home_firstscreen_50': 'Home_FirstScreen_50',
                'home_firstscreen_100': 'Home_FirstScreen_100',
                'home_firstscreen_200': 'Home_FirstScreen_200',
                'home_firstscreen_500': 'Home_FirstScreen_500'
            };
            $.sub('click_dcstracker_homefirstscreen', __dcstracker_homefirstscreen);

            function __dcstracker_homefirstscreen() {
                var $t = $(this);
                $.dcstracker({
                    category: 'Home',
                    action: 'FirstScreen',
                    event: dcs_map[$t.attr('dcs_id')] || $t.attr('dcs_id')
                });
            }
        });


        $(document).ready(function () {
            var timer = null;
            $('#nav .title').hover(function () {
                clearTimeout(timer);
                $(".spnav .smore").css('display', 'none');
                $(".spnav .goodsNav").show();
            }, function () {
                $(".spnav .smore").css('display', 'none');
                timer = setTimeout(function () {
                    $(".spnav .goodsNav").hide();
                }, 100);
            });
            $(".spnav .goodsNav").hover(function () {
                clearTimeout(timer);
                $(this).show();
            }, function () {
                timer = setTimeout(function () {
                    $(".spnav .goodsNav").hide();
                }, 100);
            });
        });


        // $('.ac_dcs_base').not('.first .ac_dcs_base').mouseenter(function() {
        // 	$(this).css('border-top-color', '#0085D0');
        // 	$(this).closest('li').siblings().find('.ac_dcs_base').not('.shop_ini').css('border-top-color', '#EAEAEA');
        // }).eq(0).mouseenter();
        // $('.ac_dcs_base').not('.shop_ini').mouseout(function() {
        // 	$(this).css('border-top-color', '#EAEAEA');
        // })


        ;
        $(function () {
            $
                .sub('click_show_areabar', __show_areabar)
                .sub('click_show_city', __show_city)
                .sub('click_city_choose', __gotocity)
            ;

            //清除浮层
            function __clear_areabar() {
                $('#header').find('.area ul').hide();
            }

            //展开浮层
            function __show_areabar() {
                __clear_areabar();
                var $this = $(this);
                $this.next().show();
                return false;
            }

            function __gotocity(loc) {
                if (typeof loc != "string") {
                    loc = $(this).attr('location');
                }
                var locarray = loc.split('_');
                var pid = locarray[0], cid = locarray[1];
                var url = location.href;
                if (url.indexOf('/list/') > 0 || url.indexOf('/tags/') > 0) {
                    url = url.replace(/_\d{3}_\d{3,4}/i, "_" + loc);
                    setTimeout(function () {
                        location.href = url;
                    });
                } else {
                    setTimeout(function () {
                        location.href = "/mall_{loc}.html".replace("{loc}", loc);
                    });
                }
            }

            function getCityByProvince(province_id) {
                if (!window.allcity[province_id]) {
                    $.ajax({
                        async: false,
                        url: "/ajax/region/singleregion.json",
                        data: {province_id: province_id},
                        success: function (r) {
                            if (r.code) {
                                $.alert(r.msg)
                            } else {
                                window.allcity[province_id] = r.data || [];
                            }
                        },
                        dataType: 'json'
                    });
                }
                return window.allcity[province_id];
            }

            var $province = $('#header .province'),
                $city = $('#header .city'),
                current_province_id = $province.find('span').attr('province_id'),
                city_id = '431',
                reg = new RegExp('<!--|-->', 'g'),
                template = new Formater($('.header_area_template').html().replace(reg, '') || '<li><a href="#" target="_blank">{city_name}</a></li>'),
                city_name = '长春'
            ;

            //点击省下拉项展开市浮层
            function __show_city() {
                var $this = $(this),
                    province_id = $this.attr('province_id'),
                    tmp = ''
                ;
                if ($this.attr('iscity')) {
                    __gotocity(province_id + "_" + province_id);
                    return false;
                }
                var data = getCityByProvince(province_id);
                $city.find('span').text('请选择');
                //var cityAreaWidth = (data.length < 4) ? 40*(data.length + 1) + 5 : 245;//根据市的个数，判断外框宽度
                $.each(data, function (inx, item) {
                    tmp += template.exec(item);
                });
                __clear_areabar();
                $this.closest('ul').prev().attr('province_id', $this.attr('province_id')).text($this.text());
                $city.find('ul').html(tmp).show();
                return false;
            }

            //点击其他dom收起浮层
            $(document).bind('click.area', function (e) {
                if ($(e.target).closest('.area').length == 0) {
                    __clear_areabar();
                }
                if ($(e.target).closest('.fs_box').length === 0) {
                    $('.fs_box ul').hide();
                }
            });

        })
        ;


        ;
        $(function () {
            var _mobileFuzzy = mobileFuzzy();

            $
                .sub('login_success', _mobileFuzzy.loginSuccess)
                .sub('click_send_msgcode4eye', _mobileFuzzy.sendMsgCode, false)
                .sub('click_handle_mobile', _mobileFuzzy.handleMobile, false)
            ;

            mobileFuzzy.$sms_mobile = $('#smsContent');

            function mobileFuzzy() {
                var mobile = null, isLogin = false, isShow = 0;
                var time = 60;
                var timer;

                // 填写mobile，并切换小眼睛的显示与隐藏
                function _handleMobile() {
                    var $eye = $('.userinfo .icon_eye');
                    isLogin && $('.userinfo .tel').text(mobile).attr('title', mobile);
                    mobile && (isShow ? $eye.removeClass('eye-close') : $eye.addClass('eye-close'));
                }

                // 发送验证码倒计时
                function _timeCountDown() {
                    var $msg_code = $('.sms-content .msg-code');
                    if (time === 0) {
                        clearInterval(timer);
                        $msg_code.removeClass('btnGray').addClass('ac_send_msgcode4eye btnBlue').find('span').text("重发验证码");
                        time = 60;
                        return false;
                    }
                    $msg_code.removeClass('btnBlue ac_send_msgcode4eye').addClass('btnGray').find('span').text(time + "s后发送");
                    time--;
                }

                // 弹出短信验证框
                function _popSmsBox() {
                    $.confirm($('#smsContent').html(), {
                        direction: null,
                        align: null,
                        mask: true,
                        noCloser: false,
                        preButtons: true,
                        onLoad: function ($box) {
                            $box.find('.mobile').html(mobile)
                        },
                        onAccept: _submitCheck
                    });
                }

                // 校验短信验证码是否正确，并设置返回值中的mobile和小眼睛状态等
                function _submitCheck() {
                    var flag = false;
                    $.ajax({
                        url: '/ajax/shownumber/submitcheck.json',
                        data: {msg_verify: $('.sms-content input[name="msg_verify"]').val()},
                        success: function (r) {
                            if (r.code) {
                                $.alert(r.msg);
                            } else if (r.data) {
                                mobile = r.data.user_name;
                                isShow = r.data.num_show;
                                _handleMobile();
                                flag = true;
                            }
                        },
                        dataType: 'json',
                        async: false
                    });
                    return flag;
                }

                // 点击小眼睛，控制手机号是否模糊化
                function _queryStatus() {
                    if (!isLogin) return;
                    $.getJSON('/ajax/shownumber/shownumber.json', {is_show: isShow}, function (r) {
                        if (r.code) {
                            $.alert(r.msg);
                        } else if (r.data) {
                            if (r.data.need_frame) {  // 后台告诉是否需要弹出短验窗口，need_frame=1表示当前登录状态会话下已经短信未校验过
                                _popSmsBox();
                            } else {
                                mobile = r.data.user_name;
                                isShow = r.data.num_show;
                                _handleMobile();
                            }
                        }
                    });
                }

                return {
                    // 校验是否登录，并填写用户名
                    loginSuccess: function () {
                        var user = arguments[0] instanceof jQuery.Event ? arguments[1] : arguments[0];
                        var nickname = user.nick_name || user.user_name;
                        // 如果是手机号用户，则设置isShow
                        if (user.is_china_mobile) {
                            isShow = user.num_show;
                            mobile = nickname;
                            isLogin = true;
                            _handleMobile();
                        }
                    },
                    handleMobile: _queryStatus,
                    // 发送短信验证码
                    sendMsgCode: function () {
                        $.getJSON('/ajax/shownumber/msg_send.json', function (r) {
                            if (r.code) {
                                $.alert(r.msg);
                            } else {
                                timer = setInterval(_timeCountDown, 1000);
                            }
                        });
                    }
                }
            }
        });


        $(function () {
            $.sub('click_cart_settlement', __cart_settlement, false)
                .sub('click_cart_delete', __cart_delete, false)
                .sub('click_cart_deleteall', __cart_deleteall, false)
                .sub('click_cart_select', __cart_select, false)
                .sub('click_cart_checkall', __cart_check, false)
                .sub('click_cart_checkgroup', __cart_check, false)
                .sub('change_cart_update', __cart_update, false)
                .sub('click_cart_incr', __cart_incr, false)
                .sub('click_cart_decr', __cart_decr, false)
                .sub('login_mini_msg', __settle_unlock, true)
                .sub('login_mini_cancel', __settle_unlock, true)
                .sub('login_success', __settle_unlock, true)
            ;
            var settlelock = false;

            function __settle_unlock() {
                settlelock = false;
            }
            // 去结算
            function __cart_settlement() {
                var tmp = [];
                var $t = $(this);
                $('.cart-list input[name="cartid"]:checked').each(function () {
                    tmp.push(this.value);
                });
                if (tmp.length == 0) {
                    $t.errormsg('请选择要结算的商品', {direction: 'down', align: 'center'});
                    return false;
                }
                if (settlelock) {
                    return false;
                }
                settlelock = true;
                window.location = './create_order?cartIds=' + tmp.join(',');
                /*request('/order/create/' + tmp.join(','), {}, 'put').then(res => {
                    if (r.data.to_url) {
                        location.href = r.data.to_url;
                    }
                }).catch(err => {
                    $t.errormsg(err.msg, {direction: 'down', align: 'center'});
                })*/
                /*$.post('/ajax/cartshop/settlement.json', {
                    selected: tmp
                }, function (r) {
                    settlelock = false;
                    if (r.code) {
                        $t.errormsg(r.msg, {direction: 'down', align: 'center'});
                    } else {
                        if (r.data.to_url) {
                            location.href = r.data.to_url;
                        }
                    }
                }, 'json');*/
            }

            function __cart_delete() {
                var $t = $(this);
                $.confirm("您确认要删除所选商品？", {
                    title: "删除",
                    onAccept: function () {
                        $.getJSON('/ajax/cartshop/delete.json', {
                            selected: $t.attr('cartid')
                        }, function (r) {
                            $t.successmsg(r.msg, {
                                direction: 'down',
                                align: 'center',
                                liveTime: 1000,
                                onClose: function () {
                                    if (r.code == 0) {
                                        location.href = location.href;
                                    }
                                }
                            });
                        });
                    }
                });

            }

            function __cart_deleteall() {
                var temp = [];
                var $t = $(this);
                $('.cart-list ').find('input[name="cartid"]:checked').each(function () {
                    temp.push(this.value);
                });
                if (temp.length == 0) {
                    $t.errormsg('请至少选择一件商品！', {direction: 'down', align: 'center'});
                    return false;
                }
                $.confirm("您确认要删除所选商品？", {
                    title: "删除",
                    onAccept: function () {
                        $.getJSON('/ajax/cartshop/delete.json', {
                            selected: temp.join(',')
                        }, function (r) {
                            $t.notice(r.msg, {
                                onClose: function () {
                                    location.href = location.href;
                                }
                            });
                        });
                    }
                });

            }

            function __cart_select() {
                var count = 0, total = 0, discount = 0;
                $(".item-list").removeClass("selected");
                $('.cart-list input[name="cartid"]:checked').each(function () {
                    var $t = $(this);
                    count += parseInt($t.attr('num')) || 0;
                    total += parseInt($t.attr('total_price')) || 0;
                    $t.parents(".item-list").addClass("selected");
                });
                $('#cartcount').text(count);
                $('#totalprice').text((total / 100).toFixed(2));
            }

            function __cart_check() {
                var $t = $(this);
                if ($t.is('.ac_cart_checkall')) {
                    $('.ac_cart_checkgroup , input[name="cartid"],.ac_cart_checkall').prop('checked', $t.prop('checked'));
                } else {
                    $t.closest('.cartgroup').find('input[name="cartid"]').prop('checked', $t.prop('checked'));
                }
                __cart_select();
            }

            var timerMap = {}, updatelock = false;

            function __cart_update() {
                var $t = $(this), val = parseInt($t.val()), $itemList = $t.closest(".item-list");
                if (isNaN(val) || val <= 0) {
                    $t.val($t.attr('num'));
                    return false;
                }
                var cartid = $t.closest('.item-list').find('input[name="cartid"]').val();
                clearTimeout(timerMap[cartid]);
                timerMap[cartid] = setTimeout(function () {
                    updatelock = true;
                    $.post('/ajax/cartshop/update.json', {selected: cartid, purpose_num: $t.val()}, function (r) {

                        if (r.code) {
                            $t.errormsg(r.msg, {direction: 'down', align: 'center'});
                            $t.val($t.attr('num'));
                        } else {
                            $t.successmsg(r.msg, {direction: 'down', align: 'center'});
                            if (!isNaN(r.data)) $t.val(r.data);
                            $t.attr('num', $t.val());
                            var $itemList = $t.parents(".item-list"), itemList = $itemList.get(0),
                                itemHeader = $itemList.find(".item-header").get(0),
                                checked = $itemList.find(".ac_cart_select").prop("checked"), newDom = $(r.data.tpl);
                            newDom.find(".ac_cart_select").prop("checked", !checked);
                            if (r.data.res.promo_type == 7 || r.data.res.promo_type == 8) {
                                itemList.replaceChild(newDom.find(".item-header").get(0), itemHeader);
                                $(itemList).find(".ac_cart_select").trigger("click");
                                var goodsnum = $itemList.find(".ac_cart_update").val();
                                $itemList.find(".purpose_num").html(goodsnum);
                                var item = $itemList.find(".item-item");
                                item.each(function () {
                                    var $this = $(this), originPrice = $this.find(".p-price").html().slice(1);
                                    $this.find(".p-sum").html("¥ " + (originPrice * goodsnum).toFixed(2));
                                })
                            } else {
                                itemList.parentNode.replaceChild(newDom.get(0), itemList);
                                newDom.find(".ac_cart_select").trigger("click");
                            }
                        }
                        updatelock = false;
                    }, 'json');
                }, 200);
            }

            function __cart_incr() {
                var $t = $(this);
                if ($t.hasClass("disable") || updatelock) {
                    return false;
                }
                $t.siblings(".ac_cart_decr").removeClass("disable");
                var $input = $t.siblings('input');
                var num = parseInt($input.val());
                $input.val(num + 1).change();
            }

            function __cart_decr() {
                var $t = $(this);
                if ($t.hasClass("disable") || updatelock) {
                    return false;
                }
                $t.siblings(".ac_cart_incr").removeClass("disable");
                var $input = $t.siblings('input');
                var num = parseInt($input.val());
                num = num - 1;
                if (num > 0) {
                    $input.val(num).change();
                }
                if (num <= 1) {
                    $t.addClass("disable");
                }
            }

            $('input[name="cartid"]').eq(0).click();
            /* 添加足迹 */
            $.getJSON("/ajax/detail/getfootprint.json", {count: 5}, function (r) {
                if (r.code == 0)
                    $(r.data).find('li').appendTo($('.footprint-container ul')).find('img.lazy').lazyload({effect: 'fadeIn'});
            });
        });


        ;
        $(function () {

            $(window).scroll(function () {
                if (sTop >= 0) {
                    if (isIE6()) {
                        var wheight = $(window).height;
                        var sTop = $(window).scrollTop();
                        var h = parseInt(wheight + sTop);
                        $('.lowerIEtips').stop().animate({marginTop: h}, 1).show();
                    }
                }
            }).scroll();

        });


    }, 'utf-8');
</script>


<div class="xl-chrome-ext-bar" id="xl_chrome_ext_{4DB361DE-01F7-4376-B494-639E489D19ED}" style="display: none;">
    <div class="xl-chrome-ext-bar__logo"></div>

    <a id="xl_chrome_ext_download" href="javascript:;" class="xl-chrome-ext-bar__option">下载视频</a>
    <a id="xl_chrome_ext_close" href="javascript:;" class="xl-chrome-ext-bar__close"></a>
</div>
</body>
</html>