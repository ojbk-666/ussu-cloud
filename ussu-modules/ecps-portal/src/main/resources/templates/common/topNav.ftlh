<div class="topNav">
    <div class="clearfix globalMain">
        <p class="leftMenu clearfix">
            <a href="http://www.10086.cn/gx/" class="current">个人客户</a>
            <a href="http://zhengqi.10086.cn/?WT.ac_id=190620ZQKH_shop">政企客户</a>
        </p>
        <p class="floatleft">
            欢迎来到中国移动！
        </p>
        <div class="rightMenu clearfix">
            <a href="./shoplist" class="cart ac_cart1_count ac_dcs_base"
               dcs_id="NAV_TOP_CART">购物车<span class="red">1</span><i></i></a>
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
                        <p><span class="cartcount">共<b style="font-weight: normal">1</b>件商品</span> 共计：<span class="red">¥ <b
                                        style="font-weight: normal"></b></span></p>
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
                function getQueryString(name) {
                    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
                    var r = window.location.search.substr(1).match(reg);
                    if (r != null) return unescape(r[2]); return null;
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