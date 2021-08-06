<div class="topNav">
    <div class="clearfix globalMain">
        <p class="leftMenu clearfix">
            <a href="javascript:void(0)" class="current">个人客户</a>
            <a href="javascript:void(0)">政企客户</a>
        </p>
        <p class="floatleft">
            欢迎来到中国移动！
        </p>
        <div class="rightMenu clearfix">
            <a href="./shoplist" class="cart ac_cart1_count ac_dcs_base"
               dcs_id="NAV_TOP_CART">购物车<span class="red">0</span><i></i></a>
            <div class="dorpdown-layer" style="display:none">
                <style type="text/css">
                    .cart-layer .cart_list {
                        width: 350px;
                    }

                    .height268 {
                        height: 268px;
                    }

                    .cart-layer .loadingBlock {
                        height: 75px;
                    }
                </style>
                <div class="have_goods">
                    <div class="cart-layer" style="border-bottom: 1px solid #ECECEC">
                        <div class="loadingBlock"></div>
                        <div class="cart_goodslist" style="display: none;">
                            <p class="smt top">最新加入的商品</p>
                            <p class="smt right">购物车</p>
                            <div id="content-1" class="cart_list mCustomScrollbar _mCS_1 mCS_no_scrollbar">
                                <div id="mCSB_1" class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside"
                                     style="max-height: 0px;" tabindex="0">
                                    <div id="mCSB_1_container" class="mCSB_container mCS_y_hidden mCS_no_scrollbar_y"
                                         style="position:relative; top:0; left:0;" dir="ltr">
                                    </div>
                                    <div id="mCSB_1_scrollbar_vertical"
                                         class="mCSB_scrollTools mCSB_1_scrollbar mCS-light mCSB_scrollTools_vertical"
                                         style="display: none;">
                                        <div class="mCSB_draggerContainer">
                                            <div id="mCSB_1_dragger_vertical" class="mCSB_dragger"
                                                 style="position: absolute; min-height: 30px; top: 0px;">
                                                <div class="mCSB_dragger_bar" style="line-height: 30px;"></div>
                                            </div>
                                            <div class="mCSB_draggerRail"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class=" btm black" style="display: none;">
                        <p>
                            <span class="cartcount">共<b style="font-weight: normal">0</b>件商品</span>
                            共计：<span class="red">¥ <b style="font-weight: normal"></b></span>
                        </p>
                        <a href="./shoplist" class="btn btnGraySmall btnRed ac_dcs_base"
                           dcs_id="NAV_CART_GO">去购物车<em></em><i></i><b></b></a>
                    </div>
                </div>
                <!--<img class="ajax_loading" style="margin-left: 155px;" src="//img0.shop.10086.cn/www/core/css/global/ajax_loading_max.gif__81.gif" />-->
                <div id="" class="nogoods btm black" style="display: none;">
                    <p class="nogoods_p">购物车内暂时还没有商品</p>
                    <p class="abate" style="display:none">您购物车里的商品均已失效</p>
                    <a href="./shoplist"
                       class="btn btnGraySmall btnRed">去购物车<em></em><i></i><b></b></a>
                </div>
            </div>
            <em>|</em>
            <span id="userinfo" class="floatleft userinfo">
                <a href="javascript:toLogin()" class=" red login">登录</a><em>|</em>
            </span>
            <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/common/common.js"></script>
            <script src="https://cdn.bootcdn.net/ajax/libs/layer/3.5.1/layer.js"></script>
            <script src="https://img.seasmall.top/cloud/static/login/js/jquery.cookie.js"></script>
            <script type="text/javascript">
                let loginHtml = '<a href="javascript:toLogin()" class=" red login">登录</a><em>|</em>';
                (function (){
                    let token = $.cookie('token');
                    let isLogin = $.cookie('isLogin');
                    let nickname = $.cookie('nickname');
                    if (isLogin && isLogin == 'true') {
                        $('#userinfo').html('<span style="float: none;">'+nickname+'</span><a href="javascript:logout2()">退出</a>');
                    } else {
                        $('#userinfo').html(loginHtml);
                    }
                })();
                function toLogin() {
                    let backUrl = window.location.href;
                    window.location = "./login?backUrl=" + backUrl;
                }
                function logout2() {
                    $.removeCookie('token', {path: '/', domain: window.location.domain });
                    $.removeCookie('nickname', {path: '/', domain: window.location.domain });
                    $.cookie('isLogin', false, {path: '/', domain: window.location.domain});
                    $('#userinfo').html(loginHtml);
                    window.location.reload();
                }
                // 检查登录状态
                function checkLogin() {
                    let isLogin = $.cookie('isLogin');
                    if (isLogin && isLogin == 'true') {
                        return true;
                    } else {
                        toLogin();
                        return false;
                    }
                }
                function isLogin() {
                    let isLogin = $.cookie('isLogin');
                    return isLogin && isLogin == 'true';
                }
                function getQueryString(name) {
                    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
                    var r = window.location.search.substr(1).match(reg);
                    if (r != null) return unescape(r[2]); return null;
                }

                $(function () {
                    if (!isLogin()) {
                        return;
                    }
                    request('member/cart/amount', {}, 'get').then(r => {
                        $('.cart .red').text(r.data);
                        $('#returntop .cart-quan em').text(r.data);
                        $('.have_goods .btm .cartcount b').text(r.data);
                    })
                })

                $(document).ready(function () {
                    $.sub('mouseover_cart1_count', __cart1_count).sub('msg_cart', __cart1_show, false);
                    var list_lock = true;
                    // 获取购物车商品数量
                    function __cart1_count() {
                        if (list_lock) {
                            /*$.getJSON('/ajax/cartshop/list.json', function (r) {
                                if (r.code) {
                                    $.alert(r.msg);
                                } else {
                                    $.pub('msg_cart', r.data);
                                }
                            });*/
                            request('member/cart/user', {}, 'get').then(res => {
                                // $.pub('msg_cart', res.data);
                                __cart1_show(res.data);
                            }).catch(res => {
                                $.alert(res.msg);
                            })
                        }
                        list_lock = false;
                    }
                    function __cart1_show(r) {
                        var cartdata = r;
                        var $cart_list = [];
                        var $cartcount = null;
                        if (cartdata && cartdata.length > 0) {
                            var total = 0, count = 0;
                            let content = '<ul cartcount="'+cartdata.length+'">';
                            for (const cart of cartdata) {
                                total += cart.totalPrice;
                                count += cart.quantity;
                                let skuNameShort = cart.skuName.length > 20 ? cart.skuName.substring(0, 20) + '...' : cart.skuName;
                                let specStr = getObjValueArr(JSON.parse(cart.skuSpec)).join(' ');
                                content += '<li cartid="'+cart.cartSkuId+'" price="'+cart.skuPrice+'" price="'+cart.quantity+'">' +
                                    '<div class="p-goods floatleft"><a target="_blank" href="./details?skuId='+cart.skuId+'" class="black ac_dcs_base" dcs_id="" title="'+cart.skuName+'">' +
                                    '<img src="${IMAGE_SERVER}'+cart.skuImg+'" class="floatleft imgBorder">'+skuNameShort+'</br>' +
                                    '<span class="gray">'+specStr+'</span></a></div><div class="p-price floatright"><span class="black">' +
                                    '<strong class="red">￥ '+cart.price+'</strong> X '+cart.quantity+'</span></br>' +
                                    '<a href="javascript:deleteCart(\''+cart.cartSkuId+'\')" class="gray ac_dcs_base">删除</a></div>' +
                                    '</li>'
                            }
                            content += '</ul>';
                            // $('.dorpdown-layer .cart_list .mCSB_container').html(cartdata.content);
                            $('.dorpdown-layer .cart_list .mCSB_container').html(content);
                            $('.have_goods .cartcount b,.rightMenu .cart .red,.rightfloatbox .cart-quan em').html(count);
                            $cart_list = $('.rightMenu .cart_list').find('li');
                            if ($cart_list.length > 5) {
                                $('.cart_list').addClass('height268');
                            } else {
                                $('.cart_list').removeClass('height268');
                            }
                            // $('.have_goods .btm .red b').text((total / 100).toFixed(2));
                            $('.have_goods .btm .red b').text(total);
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
                    }

                    // 删除购物车中得商品
                    deleteCart = function (cartId) {
                        if (!cartId || cartId == '') {
                            return;
                        }
                        request('member/cart/'+cartId,{},'delete').then(res=>{
                            list_lock = true;
                            __cart1_count();
                        }).catch(err=>{
                            layer.msg(err.msg);
                        })
                    }

                })

                var deleteCart;

                function getObjValueArr(obj) {
                    let arr = [];
                    for(let k in obj){
                        arr.push(obj[k]);
                    }
                    return arr;
                }

            </script>
            <a class="cmccclient" href="http://www.10086.cn/cmccclient/">手机营业厅</a>
            <em>|</em> <a href="http://jf.10086.cn/">积分商城</a>
            <em>|</em> <a href="http://www.10086.cn/aboutus/gx/">关于中国移动</a>
            <a href="http://www.10086.cn/zzxx/gx" class="red" target="_blank">证照信息</a>
            <div class="ewm" style="display:none;">
                <img src="${IMAGE_SERVER}cloud/static/store/images/ewm.jpg__240.jpg">
                <p>扫一扫，随时查话费！</p>
            </div>
        </div>
    </div>
</div>