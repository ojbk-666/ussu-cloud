<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0056)https://shop.10086.cn/goods/771_771_1082982_1071935.html -->
<html xmlns="http://www.w3.org/1999/xhtml" class="">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="shortcut icon" type="image/x-icon" href="https://img0.shop.10086.cn/favicon.ico__0.ico">
    <meta name="WT.location" content="771">
    <meta name="WT.category" content="youhui">
    <meta name="WT.goods_type" content="70000">
    <meta name="WT.goods_id" content="1082982">
    <meta name="WT.sku_id" content="1071935">
    <title>【中国移动】_移动商城</title>
    <link href="${IMAGE_SERVER}cloud/static/details/jscrollpane.css" rel="stylesheet" type="text/css">
    <!--[if lt IE 9]>
    <script r='m'>document.createElement("section")</script><![endif]-->
    <#--<script src="${IMAGE_SERVER}cloud/static/details/120.js" type="text/javascript"></script>-->
    <script type="text/javascript">
        (function () {
            var CmChannelID = document.cookie.match(/CmChannelID=([^;]+)/i)
            var CmChannel = (CmChannelID && CmChannelID[1]) ? CmChannelID[1].toLowerCase() : '';
            var u = navigator.userAgent;
            var browserDetect = {
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios系统
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或系统
                wp: u.indexOf('IEMobile') > -1,//Windows Phone系统
                iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                Safari: u.indexOf("Safari") > -1,//判断是否Safari浏览器
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') > -1 //是否web应该程序，没有头部与底部
            };
            if (CmChannel != 'pc' && CmChannel != 'pad' && browserDetect.mobile && !browserDetect.iPad) {
                var urlmap = {
                    touch: "https://touch.10086.cn/goods/771_771_1082982_1071935.html",
                    wap: ''
                }
                if (CmChannel == 'wap' && urlmap.wap) {
                    location.href = urlmap.wap;
                } else {
                    location.href = urlmap.touch;
                }
            }

        })();
    </script>
    <script charset="utf-8" src="${IMAGE_SERVER}cloud/static/details/qrcode.js"></script>
    <meta name="WT.mobile" content="">
    <script src="${IMAGE_SERVER}cloud/static/details/rp.sdk.h5.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery-zoom/1.7.9/jquery.zoom.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/axios/0.21.1/axios.min.js"></script>
</head>

<body style="">

<#include "../common/script_screenResize.ftlh">

<script type="text/html" id="login_box">
    <div class="minloginbox loadingBlock clearfix" style="width:350px;">
        <#--<iframe src="https://login.10086.cn/SSOCheck.action?channelID=12002&backUrl=https%3A%2F%2Fshop.10086.cn%2Fsso%2Fminilogincallback.php%3Faction%3Dforcelogin"
                width="350" height="400" allowTransparency="true" frameborder="0" scrolling="no"></iframe>-->
    </div>
</script>

<#include "../common/topNav.ftlh">

<iframe src="${IMAGE_SERVER}cloud/static/details/saved_resource.html" id="ssocheckframe" style="display:none"
        __idm_frm__="9"></iframe>
<#include "../common/script_allcity.ftlh">

<div class="globalMain clearfix" id="header">

    <div class="search_mlogo clearfix">

        <#--区域选择-->
        <#include "../common/logoWhere.ftlh">

        <#include "../common/search.ftlh">
    </div>

    <#assign topMenuNav="shop">
    <#include "../common/topMenuNav.ftlh">

</div>

<style type="text/css">
    .floatnav .btnGreen em {
        padding-left: 0px;
        background-image: none;
    }

    .onestepbuymask {
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        z-index: 99999;
        min-width: 1000px;
        display: none;
    }

    .onestepbuybg {
        position: fixed;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        z-index: 99999;
        background: #000;
        filter: alpha(opacity=75);
        -moz-opacity: 0.75;
        -khtml-opacity: 0.75;
        opacity: 0.75;
    }

    .onestepbuypic {
        width: 1300px;
        height: 700px;
        position: absolute;
        left: 50%;
        margin-left: -650px;
        z-index: 999999;
    }

    .onestepbuypic .close {
        display: block;
        position: fixed;
        width: 30px;
        height: 30px;
        top: 70px;
        right: 150px;
        color: #fff;
        font-size: 30px;
        text-decoration: none;
        background: url(//img0.shop.10086.cn/www/goods/css/close.png__285.png) 0 0 no-repeat;
    }

    .w1k .onestepbuypic .pic_fav {
        position: absolute;
        left: 50%;
        margin-left: -520px;
        bottom: 30px;
        width: 427px;
        height: 116px;
        background: url(//img0.shop.10086.cn/www/goods/css/shoucang.png__289.png) 0 0 no-repeat;
    }

    .w1k .onestepbuypic .pic_btn {
        position: absolute;
        left: 50%;
        margin-left: 150px;
        width: 454px;
        height: 153px;
        background: url(//img0.shop.10086.cn/www/goods/css/onestepbuy.png__285.png) 0 0 no-repeat;
    }

    .onestepbuypic .pic_fav {
        position: absolute;
        left: 50%;
        margin-left: -620px;
        bottom: 30px;
        width: 427px;
        height: 116px;
        background: url(//img0.shop.10086.cn/www/goods/css/shoucang.png__289.png) 0 0 no-repeat;
    }

    .onestepbuypic .pic_btn {
        position: absolute;
        left: 50%;
        margin-left: 80px;
        width: 454px;
        height: 153px;
        background: url(//img0.shop.10086.cn/www/goods/css/onestepbuy.png__285.png) 0 0 no-repeat;
    }

    .onestepbuypic .pic_btn .text {
        display: block;
        position: absolute;
        width: 130px;
        height: 25px;
        top: 48px;
        left: 223px;
        color: #47a6a6;
        text-decoration: underline;
        font-size: 18px;
    }
</style>

<div class="content clearfix">
    <#--商品信息开始-->
    <div class="goodsInfo" style="position:relative;z-index:1">


        <div class="preview">
            <div class="pic clearfix" id="oldp">
                <a href="javascript:void(0);" class="jqzoom" style="outline-style: none; text-decoration: none;">
                    <div class="zoomPad">
                        <img src="${IMAGE_SERVER}${skuDetail.skuImg}" class="middle" style="opacity: 1;"/>
                    </div>
                </a>
            </div>
            <div class="list">
                <div class="hiddenbox">
                    <ul class="clearfix" id="thumblist" style="left: 0px;">
                        <#--图片预览图循环-->
                        <#list skuDetail.skuImgList! as skuImgItem>
                        <li class="">
                            <a href="javascript:void(0);" bigimg="${IMAGE_SERVER}${skuImgItem.imgUrl}" class="">
                                <img src="${IMAGE_SERVER}${skuImgItem.imgUrl}">
                            </a>
                        </li>
                        </#list>
                        <#--<li class="current">
                            <a href="javascript:void(0);"
                               rel="{gallery: 'gal1', smallimage: '//img1.shop.10086.cn/goods/ts869cr7ygm3mq69_700x700.png',largeimage: '//img1.shop.10086.cn/file/ts869cr7ygm3mq69.png'}"
                               class="zoomThumbActive"><img
                                        src="${IMAGE_SERVER}cloud/static/details/ts869cr7ygm3mq69_90x90.png"></a>
                        </li>-->
                    </ul>
                </div>

            </div>
            <div style="position:relative;top:-55px;">
                <a href="https://shop.10086.cn/goods/771_771_1082982_1071935.html#" class="prev ac_prev noprev"></a>
                <a href="https://shop.10086.cn/goods/771_771_1082982_1071935.html#" class="next ac_next"></a>
            </div>

            <a href="javascript:void(0);" class="btnFav floatleft btnAppend ">
                <em class="ac_fav_add iconBtnFavStar" goods_id="1082982" goods_type="70000"><strong>收藏商品</strong></em>
            </a>

        </div>

        <style>
            .hide {
                display: none !important;
            }
        </style>

        <div class="phone_module">
            <#--sku名称-->
            <h1>${skuDetail.skuName}</h1>
            <#--在线客服-->
            <a href="https://shop.10086.cn/ac/livechat-web-client/robertIndex?tenantId=1000049&code=BS&channelType=1011&commodityId=1082982&skuId=1071935"
               target="_blank" dcs_id="detail_zxkf" class="ac_dcs_base f_zxkf MT_zxkf" style="display: block;">
                <img style="width: 80px;height: 30px;" src="${IMAGE_SERVER}cloud/static/details/zxkf.png__211.png">
            </a>
            <style type="text/css">
                .cost_price {
                    padding-left: 10px;
                    font-size: 12px;
                    color: #666;
                    font-weight: normal;
                }

                .befor_price {
                    padding-left: 10px;
                    text-decoration: line-through;
                    font-size: 12px;
                    color: #666;
                }

                .presale_wkje p {
                    display: inline-block;
                    margin-right: 40px;
                }
            </style>
            <#--sku价格-->
            <dl class="clearfix margin5" id="sku_price">
                <dt>价　　格：</dt>
                <dd class="red bold size20">
                    <em class="fontYaHei">￥</em>
                    <label sku_price="${relation.seckillPrice}">${relation.seckillPrice}</label>
                </dd>
            </dl>
            <dl class="clearfix margin5">
                <dt>配送范围：</dt>
                <dd>全国（港澳台地区除外）
                </dd>
            </dl>
            <#--sku规格循环开始-->
            <#list featureList! as feature>
            <dl class="clearfix btnChose skulist" skutype="color">
                <dt>${feature.featureName}：</dt>
                <dd featureid="${feature.featureId?c}">
                    <#list feature.selectValueList as selectValueItem>
                    <a skuindex="315" href="javascript:void(0)"
                       <#list skuDetail.specList as skuSpec>
                           <#if (feature.featureId?c == skuSpec.featureId?c && selectValueItem == skuSpec.specValue)>
                       class="current"
                           </#if>
                       </#list>
                       sv="${selectValueItem}" featureid="${feature.featureId?c}" title="${selectValueItem}">${selectValueItem}</a>
                    </#list>
                </dd>
            </dl>
            </#list>
            <#--sku规格循环结束-->

            <!--<dl class="clearfix btnChose skulist" skutype="mem">
                <dt>手机内存：</dt>
                <dd>
                    <a skuindex="299" href="javascript:void(0);" class="current  " title="64G">64G</a>
                    <a skuindex="1000150" href="https://shop.10086.cn/goods/771_771_1082982_1071942.html" class=" "
                       title="128G">128G</a>
                    <a skuindex="1000340" href="https://shop.10086.cn/goods/771_771_1082982_1071947.html" class=" "
                       title="256G">256G</a>

                </dd>
            </dl>-->

            <dl class="clearfix margin5 service">
                <dt>承　　诺：</dt>
                <dd>
                    <img align="absmiddle" src="${IMAGE_SERVER}cloud/static/details/zhengpin.png__81.png">
                    <img align="absmiddle" src="${IMAGE_SERVER}cloud/static/details/free.png__81.png">
                </dd>
            </dl>

            <style text="text/css">
                a.g_tips {
                    width: 110px;
                }

                .wangting_buy .graybtn {
                    background: #f4f4f4;
                    border-top: 1px solid #dadada;
                    border-bottom: 1px solid #dadada;
                    color: #707070;
                    height: 38px;
                    line-height: 38px;
                }

                .wangting_buy .graybtn:hover {
                    color: #707070;
                }

                .wangting_buy {
                    display: none;
                }
            </style>
            <div class="margin15 clearfix" id="buy_area">
                <a href="javascript:void(0);" class="btn btnBig btnPink floatleft marginRight10 btnBuy click_float_buy" rc="${relation.randomCode!}" id="buyNowBtn">
                    <em>秒杀</em><i></i><b></b>
                </a>
                <span class="state_lack floatleft marginRight10 size14 btnSuspended">已抢完</span>
            </div>
            <div class="margin15 clearfix">
            </div>

        </div>


        <div class="popbox" style="top:50px;  left:50%; margin-left:-425px; display:none; width:850px;" id="popNumber">

            <div class="template_brand">
                <!--<option value="{obj_brand}">{obj_brand}</option>-->
            </div>
            <div class="template_feature">
                <!--<option value="{obj_feature}">{obj_feature}</option>-->
            </div>
            <div class="template_system">
                <!--<option value="{value}">{name}</option>-->
            </div>
            <div class="template_selected">
                <!--<option value="" selected>{choose}</option>-->
            </div>
        </div>

        <div id="errortips_whiteagenet" style="display:none">
            <div class="fontYaHei" style="width:300px;_width:300px;padding:20px;line-height:24px;">
                <p style="font-size:16px;" class="msg"></p>
                <p style="text-align:center;margin-top:30px;display:none" class="btnBox">
                    <a class="btn btnBlue" href="javascript:void(0);"><em>前往查看</em><b></b><i></i></a>
                </p>
            </div>
        </div>


    </div>
    <#--商品信息结束-->

    <#--比对栏-->
    <div class="comparebox" style="display: none;">
        <p class="pop-compare-tips" style="display: none;">对比栏已满，您可以删除不需要的栏内商品再继续添加哦！</p>
        <div class="diff-hd">
            <ul class="tab-btns">
                <li class="current"><a href="javascript:void(0);">对比栏</a></li>
            </ul>
            <div class="operate">
                <a href="javascript:void(0);" class="comparehide">隐藏</a>
            </div>
        </div>
        <div class="diff-bd tab-cons">
            <div class="tab-con">
                <div id="diff-items" class="diff-items clearfix">
                </div>
                <div class="diff-operate">
                    <a id="goto-contrast" href="javascript:void(0);"
                       class="btn-compare-b compare-active ac_send_compare">对比</a>
                    <a class="del-items ac_clear_comitem" href="javascript:void(0);">清空对比栏</a>
                </div>
            </div>
        </div>
    </div>

    <div class="clearfix globalMargin" style="position:relative;z-index:0;">

        <#--左侧热门商品-->
        <div class="sidebar floatleft">

            <div class="globalBorder sideItem marginBottom10">
                <h2 class="clearfix"><span class="name">热销产品</span></h2>
                <div class="sideGoods">
                    <#--热门商品循环开始-->
                    <dl class="clearfix first">
                        <dt class="position">
                            <a title="【新品超值购】iPhone 12 64/128/256G" dcs_id="phone_Product_Hot_1" class="ac_dcstracker"
                               target="_blank" href="https://shop.10086.cn/goods/771_771_1082982_1071935.html"><img
                                        src="${IMAGE_SERVER}cloud/static/details/ts7x7sdtttvpe9xdf_170x170.png"
                                        alt="【新品超值购】iPhone 12 64/128/256G"></a>
                        </dt>
                        <dd>
                            <a title="【新品超值购】iPhone 12 64/128/256G" dcs_id="phone_Product_Hot_1" class="ac_dcstracker"
                               target="_blank" href="https://shop.10086.cn/goods/771_771_1082982_1071935.html">
                                <p>【新品超值购】iPhone 12 64/128/256G</p>
                            </a>
                        </dd>
                        <dd class="size14 red"><em class="fontYaHei">￥</em>5299</dd>
                    </dl>
                    <#--热门商品循环结束-->
                </div>
            </div>

        </div>
        <#--左侧热门商品-->


        <div class="maincolumn floatright">
            <#--主信息导航-->
            <div class="tabs clearfix" id="detail_tabs">
                <ul class="clearfix">
                    <li class="first ac_detail_tab current" rel="goods_desc">
                        <a href="javascript:void(0)">商品介绍</a>
                    </li>
                    <li class="ac_detail_tab" rel="expense_desc">
                        <a href="javascript:void(0)">规格参数</a>
                    </li>
                    <li class="ac_detail_tab" rel="evaluate_desc">
                        <a href="javascript:void(0)">商品评价</a>
                    </li>
                    <li class="ac_detail_tab" rel="qa_desc">
                        <a href="javascript:void(0)">常见问题</a>
                    </li>
                    <li class="ac_detail_tab" rel="pack_desc">
                        <a href="javascript:void(0)">配送及售后</a>
                    </li>
                </ul>
            </div>

            <#--主体信息-->
            <div class="goods_detail clearfix">
                <div id="goods_desc" style="display: block;">
                    <#--<p>
                        <span style="color: rgb(255, 0, 0);">
                            <span style="font-size:16px;"><strong>${skuDetail.item.pageDesc!}</strong></span>
                        </span>
                    </p>-->
                    <#--富文本-->
                    <div id="richTextDiv"></div>

                </div>
                <style>
                    .itemParaTable {
                        border: 1px solid rgb(229, 229, 229);
                        border-image: none;
                        width: 848px;
                        color: rgb(51, 51, 51);
                        font-family: 'Microsoft Yahei', tahoma, HELVETICA;
                        font-size: 16px;
                        border-collapse: collapse;
                        orphans: 2;
                        widows: 2;
                        font-variant-ligatures: normal;
                    }
                </style>
                <div id="expense_desc" style="display: none;">
                    <#list featureGroup! as featureGroupItem>
                    <table class="dtparams-table itemParaTable" id="Jbasicparams3" cellspacing="0" cellpadding="0">
                        <thead style="background: rgb(249, 249, 249); line-height: 24px;">
                        <tr>
                            <th style="margin: 0px; padding: 0px 0px 0px 10px; height: 50px; border-right-color: rgb(242, 242, 242); border-right-width: 2px; border-right-style: solid;">
                                ${featureGroupItem.groupName!}
                            </th>
                            <td style="margin: 0px; padding: 0px; border-right-color: rgb(242, 242, 242); border-right-width: 1px; border-right-style: solid;">
                                &nbsp;
                            </td>
                        </tr>
                        </thead>
                        <tbody style="line-height: 24px; font-size: 14px; border-bottom-color: rgb(242, 242, 242); border-bottom-width: 2px; border-bottom-style: solid;">
                        <#list featureGroupItem.featureList! as feature>
                        <tr itemid="18437365">
                            <th style="margin: 0px; padding: 8px 0px 8px 10px; width: 202px; font-weight: normal; border-right-color: rgb(242, 242, 242); border-bottom-color: rgb(242, 242, 242); border-right-width: 2px; border-bottom-width: 1px; border-right-style: solid; border-bottom-style: solid;">
                                ${feature.featureName!}
                            </th>
                            <td style="margin: 0px; padding: 8px 0px 8px 20px; color: rgb(153, 153, 153); border-right-color: rgb(242, 242, 242); border-bottom-color: rgb(242, 242, 242); border-right-width: 1px; border-bottom-width: 1px; border-right-style: solid; border-bottom-style: solid; -ms-word-break: break-all;">
                                <div class="out" style="margin: 0px; padding: 0px;">
                                    <a class="poptxt" style="color: rgb(153, 153, 153); text-decoration: none;" href="javascript:void(0)">
                                        <#list paraList as para>
                                            <#if para.featureId == feature.featureId>
                                                ${para.paraValue}
                                            </#if>
                                        </#list>
                                    </a>
                                </div>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    </#list>

                    <p><br></p>
                    <p>&nbsp;<span style="font-family:Arial, Sun;font-size:16px;color:#ff0000;line-height: 21.6px; orphans: 2; widows: 2;">注：以上参数信息仅供参考，实际参数以官方发布为准。</span><br>
                    </p>

                </div>
                <div id="evaluate_desc" style="display: none;">
                    <iframe src="${IMAGE_SERVER}cloud/static/details/commentdetail.html" width="100%" height="1410"
                            allowtransparency="true" frameborder="0" scrolling="no" __idm_frm__="10"></iframe>
                </div>
                <div id="qa_desc" style="display: none;">
                    <img alt="" src="${IMAGE_SERVER}cloud/static/details/tsuhjj57zzc3mrcg_940x7200"
                         data-original="//img1.shop.10086.cn/goods/tsuhjj57zzc3mrcg_940x7200" class="lazy"
                         style="display: block;"><img
                            src="${IMAGE_SERVER}cloud/static/details/ts869zc8zv88vrjr_940x7200"
                            data-original="//img1.shop.10086.cn/goods/ts869zc8zv88vrjr_940x7200" alt="" class="lazy"
                            style="display: block;"><img
                            src="${IMAGE_SERVER}cloud/static/details/ts869zzttsaj8ftyv_940x7200"
                            data-original="//img1.shop.10086.cn/goods/ts869zzttsaj8ftyv_940x7200" alt="" class="lazy"
                            style="display: block;"><img
                            src="${IMAGE_SERVER}cloud/static/details/ts86duv34qq32b3s_940x7200"
                            data-original="//img1.shop.10086.cn/goods/ts86duv34qq32b3s_940x7200" alt="" class="lazy"
                            style="display: block;">

                </div>
                <div id="pack_desc" style="display: none;">
                    <img alt="" src="${IMAGE_SERVER}cloud/static/details/thz39ubwcr6tuq9an_940x7200"
                         data-original="//img1.shop.10086.cn/goods/thz39ubwcr6tuq9an_940x7200" class="lazy"
                         style="display: block;"><img
                            src="${IMAGE_SERVER}cloud/static/details/thznv93rkzb24zgj_940x7200"
                            data-original="//img1.shop.10086.cn/goods/thznv93rkzb24zgj_940x7200" alt="" class="lazy"
                            style="display: block;"><img
                            src="${IMAGE_SERVER}cloud/static/details/ts86du2fxam5pskv_940x7200"
                            data-original="//img1.shop.10086.cn/goods/ts86du2fxam5pskv_940x7200" alt="" class="lazy"
                            style="display: block;">

                </div>
            </div>
            <#--主体信息-->
        </div>

    </div>
</div>


<style>
    .jspVerticalBar {
        width: 5px;
    }

    .quanitem *:focus {
        outline: none !important;
    }

    .quanbox {
        overflow: auto;
        position: fixed;
        top: 240px;
        bottom: 30px;
        width: 250px;
    }
</style>
<div class="quanitem" style="display:none">
    <div class="title">
        <h3>优惠券</h3>
        <a herf="javascript:void(0);" class="close_quanitem ac_close_quanitem">close</a>
    </div>
    <div class="quan_icon"></div>
    <div class="quan_tip"></div>
    <div class="quanbox" style="height: 675px; width: 250px; overflow: hidden; padding: 0px;">

        <div class="jspContainer" style="width: 250px; height: 675px;">
            <div class="jspPane" style="padding: 0px; top: 0px; left: 0px; width: 250px;">
                <ul class="quanlist">
                </ul>
            </div>
        </div>
    </div>
</div>

<style>

    .pop_holdon .title {
        height: 105px;
        background: url(//img0.shop.10086.cn/m/mod/pop/css/icon_pop_holdon_pc.png__631.png) 27px 33px no-repeat;
        padding: 33px 27px 0 103px;
        box-sizing: border-box;
    }

    .pop_holdon .title h3 {
        font-size: 16px;
        height: 30px;
        line-height: 30px;
        font-weight: 600;
        text-align: left;
        color: #333333;
    }

    .pop_holdon .title p {
        font-size: 15px;
        text-align: left;
        color: #666666;
        line-height: 25px;
        height: 25px;
        position: relative;
        overflow: hidden;
        text-overflow: ellipsis;
        overflow: hidden;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
    }

    .ie .pop_holdon .title p:after {
        content: '...';
        text-align: right;
        position: absolute;
        bottom: 0;
        right: 0;
        width: 1.8em;
        height: 25px;
        background: linear-gradient(to right, rgba(255, 255, 255, 0), rgba(255, 255, 255, 1) 50%);
    }

    .pop_holdon .img, .pop_holdon .img img {
        width: 450px;
        height: 140px;;
        margin: 0 auto;
    }

    .pop_holdon .bt a {
        display: inline-block;
        width: 170px;
        height: 42px;
        margin: .20px .25px 0 25px;
        font-size: 0;
    }

    .pop_holdon .bt a.ac_close_pop_holdon {
        background: url(//img0.shop.10086.cn/m/mod/pop/css/bt_pop_holdon_close.png__631.png) 0 0 no-repeat;
    }

    .pop_holdon .bt a.continue {
        background: url(//img0.shop.10086.cn/m/mod/pop/css/bt_pop_holdon_continue.png__631.png) 0 0 no-repeat;
    }

    .pop_holdon .bt {
        text-align: center;
    }
</style>

<#--详情tab开始-->
<div class="floatnav" style="display: none;" id="floatnav">
    <div class="tabs clearfix">
        <ul class="floatleft">
            <li class="first detail_tab current" rel="goods_desc"><a href="javascript:void(0)">
                    商品介绍
                </a></li>
            <li class="detail_tab" rel="expense_desc"><a href="javascript:void(0)">
                    规格参数
                </a></li>
            <li class="detail_tab" rel="evaluate_desc"><a href="javascript:void(0)">
                    商品评价
                </a></li>
            <li class="detail_tab" rel="qa_desc"><a href="javascript:void(0)">
                    常见问题
                </a></li>
            <li class="detail_tab" rel="pack_desc"><a href="javascript:void(0)">
                    配送及售后
                </a></li>
        </ul>

        <#--<div class="floatright">
            <a href="javascript:void(0);" class="btn  btnGreen  btnSuspended floatleft btnAppend g_tips ac_dcs_base"
               dcs_id="float_goods_remind" style="margin-right: 10px;">
                <em class="ac_goods_notice" goods_id="1082982" goods_type="70000">
                    <strong class="" style="font-size: 14px;">到货通知</strong></em><i></i><b></b>
            </a>
            <a href="javascript:void(0);" class="btn btnGray marginRight10 floatleft jump_shoucang ac_dcs_base" dcs_id="float_shoucang">
                <em><strong class="iconBtnFav">收藏</strong></em><i></i><b></b>
            </a>
            <a href="javascript:void(0);" class="btn btnRed floatleft marginRight10 jump_buy ac_dcs_base" dcs_id="float_buy">
                <em>立即购买</em><i></i><b></b>
            </a>
            &lt;#&ndash;在线客服&ndash;&gt;
            <a href="javascript:void(0)" class="ac_dcs_base service_url" dcs_id="floatnav_zxkf" target="_blank">
                <img style="width: 80px;height: 30px;" src="${IMAGE_SERVER}cloud/static/details/zxkf.png__211.png">
            </a>
        </div>-->
    </div>
</div>
<#--详情tab结束-->

<div class="popbox tipBox popbroadband"
     style="width:670px; top:30%; left:50%; margin-left:-335px;z-index:99999;display: none; ">
    <div class="popTitle clearfix"><span>请选择地址</span><a href="javascript:void(0)"
                                                        class="iconClose popCloseTips ac_ui_hide">关闭</a></div>
    <div class="popMain">
        <p class="marginBottom20">安装地址： <span class="">安徽省</span>&nbsp;&nbsp;-&nbsp;&nbsp;<span class="pop_city"
                                                                                                city_id="771">合肥市</span>&nbsp;&nbsp;-&nbsp;&nbsp;<span
                    class="pop_district" district_id="-1"></span></p>
        <div class="" style="width:600px; height:216px;border:1px solid #ccc">
            <div class="toptxt">
                <input type="text" class="detail_addr" value="" placeholder="请输入小区名称关键词，如幸福家园，根据提示选择具体地址">
            </div>
            <div class="listtxt">
                <div class="listtxt_left">
                    <ul id="addressUlDetail">
                    </ul>
                </div>
                <div class="listtxt_right">
                    <ul id="buildingUl">
                    </ul>
                </div>
            </div>
        </div>
        <p class="globalMargin20 aligncenter"><a href="javascript:void(0)"
                                                 class="btn btnBig btnBlue ac_selected"><em>提交</em><b></b><i></i></a>
        </p>
    </div>
</div>


<style type="text/css">
    .logo_bg {
        width: 180px;
        height: 180px;
        background: url(//img0.shop.10086.cn/www/hd/heduohao/qr_bg.png__349.png) no-repeat center;
        position: relative;
        margin: auto;
    }

    #qrcode {
        width: 160px;
        height: 160px;
        /*position: fixed;*/
        /*z-index: 999;*/
        padding: 10px;
    }

    #qrcode > table {
        width: 160px \9;
        height: 160px \9;
        margin: 0;
    }

    .qrcode_bar {
        height: 20px;
        padding: 10px 10px 0 10px;
    }

    .qrcode_warp {
        width: 330px;
        background-color: #fff;
        border: 5px solid #ececec;
        position: fixed;
        z-index: 999;
        left: 50%;
        top: 50%;
        margin-left: -150px;
        margin-top: -200px;
    }

    .qrcode_warp p {
        padding: 0 20px;
        line-height: 1.5;
        /*text-align: center;*/
        /*font-size: 14px;*/
    }

    .qr_close {
        font-size: 16px;
        cursor: pointer;
        text-align: right;
    }

    .tips {
        padding: 20px 0;
    }

    .qrlogo {
        width: 26px;
        height: 26px;
        position: absolute;
        background: #fff url(//img0.shop.10086.cn/www/hd/heduohao/logo.png__349.png) no-repeat center;
        left: 50%;
        top: 50%;
        margin-left: -13px;
        margin-top: -13px;
    }

    .qrtitle {
        color: #0085d0;
    }
</style>

<div class="qrcode_warp" style="display:none;">
    <div class="qrcode_bar">
        <span class="qr_close floatright">X</span>
    </div>
    <div class="marginBottom20">
        <p class="qrtitle">请使用“中国移动”客户端-右上角“扫一扫”享受信用购机优惠哦！</p>
    </div>
    <div class="logo_bg">
        <div class="qrlogo"></div>
        <div id="qrcode">
        </div>
    </div>
    <div class="tips aligncenter">
        <p>请扫二维码下单</p>
    </div>
</div>


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
    <div class="tipcon"><span><img src="${IMAGE_SERVER}cloud/static/details/logo2.png__240.png"
                                   style="width: 160px; height: 29px;"></span>
        <p>您的浏览器版本过低，如果页面显示不正常，建议将浏览器升级到最新版本。</p><!-- <a class="close" id="closeTips"></a> --></div>
</div>

<script type="text/javascript" src="${IMAGE_SERVER}cloud/static/details/loader.js__357.js"></script>
<#--<script src="${IMAGE_SERVER}cloud/static/details/sdc_mall.js__635.js"></script>-->
<script type="text/javascript">
    var HtmlUtil = {
        /*1.用浏览器内部转换器实现html编码（转义）*/
        htmlEncode:function (html){
            //1.首先动态创建一个容器标签元素，如DIV
            var temp = document.createElement ("div");
            //2.然后将要转换的字符串设置为这个元素的innerText或者textContent
            (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
            //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
            var output = temp.innerHTML;
            temp = null;
            return output;
        },
        /*2.用浏览器内部转换器实现html解码（反转义）*/
        htmlDecode:function (text){
            //1.首先动态创建一个容器标签元素，如DIV
            var temp = document.createElement("div");
            //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
            temp.innerHTML = text;
            //3.最后返回这个元素的innerText或者textContent，即得到经过HTML解码的字符串了。
            var output = temp.innerText || temp.textContent;
            temp = null;
            return output;
        },
        /*3.用正则表达式实现html编码（转义）*/
        htmlEncodeByRegExp:function (str){
            var temp = "";
            if(str.length == 0) return "";
            temp = str.replace(/&/g,"&");
            temp = temp.replace(/</g,"&lt;");
            temp = temp.replace(/>/g,"&gt;");
            temp = temp.replace(/\s/g,"&nbsp;");
            temp = temp.replace(/\'/g,"&#39;");
            temp = temp.replace(/\"/g,"'");
            return temp;
        },
        /*4.用正则表达式实现html解码（反转义）*/
        htmlDecodeByRegExp:function (str){
            var temp = "";
            if(str.length == 0) return "";
            temp = str.replace(/&/g,"&");
            temp = temp.replace(/&lt;/g,"<");
            temp = temp.replace(/&gt;/g,">");
            temp = temp.replace(/&nbsp;/g," ");
            temp = temp.replace(/&#39;/g,"\'");
            temp = temp.replace(/'/g,"\"");
            return temp;
        },
        /*6.用正则表达式实现html解码（反转义）（另一种写法）*/
        escape2Html:function (str) {
            var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
            return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
        }
    };

    <#--let richtextstr = "${skuDetail.item.itemClob.itemDesc!}";-->
    // $('#richTextDiv').html(HtmlUtil.htmlDecode(richtextstr));
</script>
<script type="text/javascript">

    $(document).ready(function () {
        // 放大镜
        $('.zoomPad').zoom({
            target: '.zoomPad'
        });
        // 加入购物车
        $('#addToCartBtn').click(function () {
            let skuId = getQueryString('skuId');
            request('member/cart/add/' + skuId, {}, 'put').then(res => {
                // $.alert('加入购物车成功');
                layer.msg('加入购物车成功');
            }).catch(err => {
                $.alert(err.msg);
            })
        })
        // 立即购买按钮
        $('#buyNowBtn').click(function () {
            let skuId = getQueryString('skuId');
            let randomCode = $(this).attr('rc');
            request('/skill/seckill', {skuId: skuId, randomCode: randomCode, seckillLimit: 1}, 'post').then(res => {
                // 获取购物车id
                window.location = './create_order?cartIds=' + res.data;
            }).catch(err => {
                $.alert(err.msg);
            })
        })
    });

    window.ENVOBJ = {
        "loc": {"province_id": 771, "city_id": 771},
        "loginurl": "",
        "choice": {
            "info": {
                "third_id": "1001931986999991413332",
                "flag": "u2",
                "purchase_volume": 1,
                "ex_device_giveaway": "",
                "contracts": [1079377, 1079507, 1079571, 1079573, 1079511, 1080154, 1079516, 1080092, 1080350, 1080640, 1079618, 1080711, 1079371, 1079564, 1080560, 1079987, 1079413, 1079605, 1079417, 1082685, 1079615, 1079551, 1079845, 1079594, 1079722, 1079338, 1079404],
                "text_price_flag": 0,
                "pay_time_end": "",
                "with_number": 0,
                "presale_price": 0,
                "ex_device_mobile_version": 1002051,
                "price": 529900,
                "channels_show": "1",
                "product_id": 1006437,
                "supplier": "",
                "ex_device_color": 315,
                "sale_time_end": "",
                "ex_device_inventory": 0,
                "reserved1": 0,
                "cost": 0,
                "default_sku": 0,
                "goods_id": 1082982,
                "ex_device_alarm_inventory": 1,
                "model_id": 1071935,
                "priority": 50,
                "pay_time_begin": "",
                "buyer_count_limit": -1,
                "contract_category": 2,
                "goods_version": 0,
                "name": "\u9ed1\u8272  64G",
                "market_price": 0,
                "comment": "",
                "boss_id": "_#_#1071935",
                "sale_time_begin": "",
                "ex_device_memory": 299,
                "status": 1,
                "promotion": [],
                "jump_url": "https:\/\/shop.10086.cn\/goods\/771_771_1082982_1071935.html"
            }
        },
        "env": {"province_id": 771, "city_id": 771}
    };
    // LOADJS('//img0.shop.10086.cn/combo/__623/script/jquery-1.10.2.js__script/cookie/jquery.cookie.js__script/iprompt/jquery.iprompt.js__script/formater.js__script/pubsub/jquery.pubsub.js__script/interactive/jquery.interactive.js__script/jquery.lazyload.js__www/core/js/global.js__www/core/js/fav.js__www/core/js/dcs_tracker.js__www/mod/search/js/shop_jsonp_search.js__script/mousewheel/jquery.mousewheel.js__script/customscrollbar/jquery.mCustomScrollbar.js__script/rp/rploader.js__script/jqzoom/jquery.jqzoom-core.js__script/jplayer/jquery.jplayer.js__www/core/js/quantity.js__www/core/js/cart.js__www/core/js/phone.js__www/core/js/countdown.js__script/form/jquery.form.v318.js__script/pagination/jquery.pagination.js__www/core/js/page.js__script/jscrollpane/jquery.jscrollpane.js__script/jscrollpane/jquery.mousewheel.js__script/qrcode.js', function () {
    LOADJS('//img.seasmall.top/cloud/static/details/loadjsbak.js', function () {
//	$.domains = window.ENVOBJ.domains;
//	$.domains = window.ENVOBJ.domains;


        $.sub('click_login_mini', __mini_login).sub('login_mini', __mini_login).sub('login_mini_msg', __mini_login_msg);
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
        };
        $(function () {
            $(".cart_list").mCustomScrollbar();
        });

        ;
        $(function () {

            var timer = null;
            $('.cart').hover(function () {
                var is_cart = '';
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
            'province_id': '771',
            'city_id': '771'
        }
        $.getJSON('/ajax/user/userinfo.json', userParam, initUser);

        ;
        $(function () {
            var provinceId = "771";	//省区编码
            var cityId = "771";	//市区编码
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


        ;
        $(function () {

            $('.video-preview-play').click(function () {
                $(this).parent().remove();
                $('#jp_container_1').show();
                $("#intro_video").jPlayer("play");
            });
            $("#intro_video").jPlayer({
                ready: function () {
                    $(this).jPlayer("setMedia", {
                        // title: "Big Buck Bunny",
                        m4v: "//img1.shop.10086.cn/video/20210427153742082_1000049_133_-912469560.mp4",
                        // m4v: "http://www.jplayer.org/video/m4v/Big_Buck_Bunny_Trailer.m4v",
                        /*				ogv: "http://www.jplayer.org/video/ogv/Big_Buck_Bunny_Trailer.ogv",
				webmv: "http://www.jplayer.org/video/webm/Big_Buck_Bunny_Trailer.webm",*/
                        poster: "//img1.shop.10086.cn/goods/ts7x7sdtttvpe9xdf_350x350.png"
                    });
                    // }).jPlayer("play"); //自动播放
                    $('.jp-gui').hide();  //隐藏播放栏
                },

                swfPath: "//img0.shop.10086.cn/script/jplayer/jquery.jplayer.swf__436.swf",
                solution: "html, flash",
                supplied: "webmv, ogv, m4v",
                size: {
                    width: "340px",
                    height: "340px",
                    cssClass: "jp-video-340p"
                },
                useStateClassSkin: true,
                autoBlur: false,
                smoothPlayBar: true,
                keyEnabled: true,
                remainingDuration: false,
                toggleDuration: true,
                volume: .5
            });

            var moveTimer = null;
            var beginTimer = null;


            $("#intro_video").bind($.jPlayer.event.play, function (event) {
                $('.jp-gui').show();
                beginTimer = setTimeout(function () {
                    $('.jp-gui').hide();
                }, 3000);
                $('#intro_video').bind('mousemove', function (event) {
                    $('.jp-gui').show();
                    clearTimeout(beginTimer);
                    clearTimeout(moveTimer);
                    moveTimer = setTimeout(
                        function () {
                            $('.jp-gui').stop().fadeOut();
                        }
                        , 3000);
                });
            });

            $("#intro_video").bind($.jPlayer.event.ended, function (event) {
                clearTimeout(moveTimer);
                $('#intro_video').unbind('mousemove');
                $('.jp-gui').stop().show();
            });

            // 放大镜
            /*$('.jqzoom').jqzoom({
                preloadImages: false,
                xOffset: 20,
                yOffset: 0,
                zoomWidth: 350,
                zoomHeight: 350,
                title: false
            });*/


            $
                .sub('click_next', __next)
                .sub('click_prev', __prev)
            ;

            function __next() {
                var $this = $(this),
                    $ul = $this.closest('.preview').find('.list ul'),
                    ul_position = $ul.position(),
                    ul_position_left = ul_position.left,
                    val = (parseInt(ul_position_left) - 62),
                    li_length = $ul.find('li').length,
                    hide_len = 0,
                    max_left = 0
                ;
                if (li_length > 5) {
                    hide_len = li_length - 5;
                    max_left = hide_len * (-62);
                    if (max_left < ul_position_left) {
                        $ul.css({'left': val});
                        if (max_left == (ul_position_left - 62)) {
                            $this.addClass('nonext');
                        }
                        if (ul_position_left == 0) {
                            $this.prev().removeClass('noprev');
                        }
                    }
                }
                return false;
            }

            function __prev() {
                var $this = $(this),
                    $ul = $this.closest('.preview').find('.list ul'),
                    ul_position = $ul.position(),
                    ul_position_left = ul_position.left,
                    val = (parseInt(ul_position_left) + 62),
                    li_length = $ul.find('li').length,
                    hide_len = 0,
                    max_left = 0
                ;
                if (ul_position_left < 0) {
                    hide_len = li_length - 5;
                    max_left = hide_len * (62);
                    if (max_left > ul_position_left) {
                        $ul.css({'left': val});
                        if ((ul_position_left + 62) == 0) {
                            $this.addClass('noprev');
                        }
                        if (ul_position_left == (-(max_left))) {
                            $this.next().removeClass('nonext');
                        }
                    }
                }
                return false;
            }

            // 图片预览
            $('.preview .list ul li img').mouseenter(function (e) {
                var $this = $(e.target),
                    $li = $this.closest('li')
                ;
                // $('.preview .pic img').attr('src',$li.attr('rel'));
                $li.addClass('current').siblings().removeClass('current');
                $('.video-preview').remove();
                if ($li.hasClass('video')) {
                    $('.preview>.video').show();
                    $('.preview>#oldp').hide();
                    // $("#intro_video").jPlayer("pause");
                    // $("#intro_video").jPlayer("pause", 0);
                    // $("#intro_video").jPlayer("play", 0);

                } else {
                    // 图片
                    $('.preview>#oldp').show();
                    $('.preview>.video').hide();
                    $("#intro_video").jPlayer("pause");
                    // 改变主图
                    $('.preview .pic img').attr('src', $this.parent().attr('bigimg'));
                }
            });

        })
        ;


        $(function () {
            $.sub('click_shipping_range', function () {
                $(this).closest('.publishRange').toggleClass('current');
            }).sub('click_shipping_close', function () {
                $(this).closest('.publishRange').removeClass('current');
            });
        });


        ;$(function () {
            $
                .sub('click_goods_notice', __goods_notice, false)
                .sub('click_notice_hide', __notice_hide, false)
                .sub('mouseenter_onestepbuy', __onestepbuy_hover, false)
                .sub('mouseleave_onestepbuy', __onestepbuy_hover, false)
                .sub('mouseenter_noticeShow', __noticeShow_hover, false)
                .sub('mouseleave_noticeShow', __noticeShow_hover, false)
                .sub('login_success', __osbShow)
            ;
            var model_id = '1071935', province_id = '771', city_id = '771';
            var mobile = null;
            var success_code = 0;
            if (window.loginUser) {
                mobile = window.loginUser.user_name;
            }

            function __goods_notice() {
                $.sprite('/ajax/goods/subscriberemind.html', {
                    title: '到货通知',
                    direction: null,
                    align: null,
                    mask: true,
                    noCloser: true,
                    preButtons: false,
                    onLoad: function (box, r) {
                        var code = box.find('.sub').attr('code');
                        if (code == 0 || code == 1) success_code = 1;
                    },
                    onComplete: function (box, r) {
                        if (r.code) {
                            $.alert(r.msg);
                        }
                        return false;
                    },
                    data: {
                        model_id: model_id,
                        province_id: province_id,
                        city_id: city_id
                    }
                });
            }

            function __notice_hide() {
                if (success_code) {
                    $('.g_tips em').removeClass('ac_goods_notice');
                    $('.g_tips').css({
                        background: '#dadada',
                        color: '#545353'
                    });
                    $('.g_tips i,.g_tips b,.g_tips a.btnGreen').css('background', '#dadada');
                    $('.g_tips strong').text('已订阅');
                }
                ;
                $('.notice_form').hide();
                $.UI.hide();
            }

            var onestepbuyTimer = null, onestepbuyNotice = null;

            function __onestepbuy_hover(event) {
                var $t = $(this);
                if (event.type == "mouseover" || event.type == "mouseenter") {
                    clearTimeout(onestepbuyTimer);
                    $.getJSON('/ajax/detail/getonestepbuy.json', function (r) {
                        var $onestepbuyData = r.data[0];
                        if (r.code) {
                            $.alert(r.msg);
                        } else {
                            if (!onestepbuyNotice) {
                                if ($onestepbuyData) {
                                    onestepbuyNotice = $t.notice(
                                        '<div class="txt_otb ac_noticeShow"><span class="bold">' + $onestepbuyData.rec_name + '&nbsp;&nbsp;' + $onestepbuyData.rec_phone_no + '&nbsp;&nbsp;' + $onestepbuyData.rec_tel + '</span> <br/>' + $onestepbuyData.province_name + $onestepbuyData.city_name + $onestepbuyData.region_name + $onestepbuyData.street + '<br/><img src="//img0.shop.10086.cn/www/goods/css/icon_help.png__289.png" /><span>使用一键购，将按上述信息直接提交订单</span></div>'
                                        , {liveTime: null}
                                    )
                                } else {
                                    var $address_url = "https://shop.10086.cn/i/?f=recaddrmag";
                                    onestepbuyNotice = $t.notice(
                                        '<div class="txt_otb ac_noticeShow"><span class="bold">一键购，帮您快速一键下单</span> <br/>初次使用需设置<a href=' + $address_url + '>收货地址</a>，继续使用即可完成一键购买！</div>'
                                        , {liveTime: null}
                                    )
                                }
                            }
                        }
                    })
                } else {
                    onestepbuyTimer = setTimeout(function () {
                        $.UI.hide();
                        onestepbuyNotice = null;
                    }, 2000);
                }
            };

            function __noticeShow_hover(event) {
                var $t = $(this);
                if (event.type == "mouseover" || event.type == "mouseenter") {

                    clearTimeout(onestepbuyTimer);
                    if (!onestepbuyNotice) {
                        onestepbuyNotice = $t.notice(
                            '<div class="txt_otb ac_noticeShow"><span class="bold">一键购，帮您快速一键下单</span> <br/>初次使用需设置<a href=' + $address_url + '>收货地址</a>，继续使用即可完成一键购买！</div>'
                            , {
                                liveTime: null
                            })
                    }
                } else {
                    onestepbuyTimer = setTimeout(function () {
                        $.UI.hide();
                        onestepbuyNotice = null;
                    }, 1000);
                }
            };

            //登陆成功一键购按钮显示
            function __osbShow() {
                var $osb = $('.btnOnestepbuy').attr('osb');
                if ($osb) {
                    $('.btnOnestepbuy').show();
                }
            }
        });

    $.sub('errortips_whiteagenet', errortips_whiteagenet, false);

    function errortips_whiteagenet() {
        // 如果是白名单网龄校验出错
        if (arguments.length > 1 && arguments[1].code == 1001031) {
            var r = arguments[1];
            $('#errortips_whiteagenet .msg').html(r.msg);
            if (r.data && r.data.to_url) {
                $('#errortips_whiteagenet .btnBox').show().children().attr('href', r.data.to_url);
            }
            $.alert($('#errortips_whiteagenet').html(), {title: '温馨提示', liveTime: null});
        }
    };

    // 获取商品库存
    /*$(function () {
        $.phone({
            goods_id: '1082982',
            goods_type: '70000',
            isheyueji: '2',
            province_id: '771',
            city_id: '771',
            merchant_id: '1000049',
            sale_type: '1',
            slave_promotion: null
        });
        $('.ac_heyue_type_selected').eq(0).click();
        $.getJSON('/ajax/detail/getstock.json', {
            goods_id: "1082982",
            merchant_id: "1000049",
            sale_type: "1",
            sku_id: "1071935"
        }, function (r) {
            var stock = {};
            if (r.code == 0) {
                var main_stock_status;
                $.each(r.data, function (index, item) {
                    stock[index] = item;
                    if ((parseInt(item.stock) - parseInt(item.lock)) <= 0) {
                        main_stock_status = 0
                    } else {
                        main_stock_status = 1;
                    }
                });
                if ($('.taogou').length > 0) {
                    $('.taogou .tg_list').each(function (index, item) {
                        var $item = $(item);
                        if (($item.attr('stock_state') * main_stock_status) == 0) {
                            $item.find('.ac_taogou_buy').replaceWith('<a href="javascript:void(0)" class="bt_gray">购买套装<b></b><i></i></a>');
                        } else {
                            $item.find('.ac_taogou_buy').show();
                        }
                    });
                }
            }
            //$skulist.find('a.current').eq(0).syncsku();
            $.addToCart({
                'goods_id': 1082982,
                'goods_type': 70000,
                'stock': stock
            });
            window.ENVOBJ.cur_stock = stock;
        });
    });*/
    $.sub('login_mini_msg', __revert_buy, true).sub('login_mini_cancel', __revert_buy, true)
        .sub('click_wbuy_versionm', __wbuy_versionm);

    function __revert_buy() {
        $('#buy_area').removeClass('checking');
    }

    function __wbuy_versionm() {
        if ($('.versionm_wu').eq(0).html() == '无') {
            window.location.href = $('.versionm_wu').eq(0).attr('href');
        }
    }

    var mvalis = $("#version_map dd a").length;
    if (mvalis == 1 && $("#version_map dd a").text() == "无") {
        $("#version_map").hide();
    }
    if (mvalis == 2 && $("#version_map dd a").eq(0).text() == "无" && $("#version_map dd a").eq(1).text() == "无") {
        $("#version_map").hide();
    }

    $('.click_float_buy').click(function () {
        if (!$('.ac_popheyue').attr('index')) {
            $.dcstracker({'event': '200722LJGM_WXZFA_PC'});
        }
    })


    ;
    $(function () {
        $.sub('click_del_comitem', __del_comitem);
        $.sub('click_clear_comitem', __clear_comitem);
        $.sub('click_send_compare', __send_compare);

        $('.comparehide').click(function () {
            $('.comparebox').toggle();
        });

        function __del_comitem() {
            var $t = $(this), sku_id = $t.attr('sku_id'), index = $t.closest('dl').attr('index'),
                str = '<dt>' + index + '</dt><dd>您还可以继续添加</dd>';
            $.getJSON('/ajax/goods/deletegoodsdiff.json', {sku_id: sku_id}, function (r) {
                if (r.code == 0) {
                    $t.closest('dl').attr('class', 'item-empty').html(str);
                }
            })
        };

        function __clear_comitem() {
            var $t = $(this), str = '';
            $.getJSON('/ajax/goods/deleteallgoodsdiff.json', function (r) {
                if (r.code == 0) {
                    for (var i = 1; i <= 4; i++) {
                        str += '<dl class="item-empty"><dt>' + i + '</dt><dd>您还可以继续添加</dd></dl>';
                    }
                    $t.parent().prev().html(str);
                }
            })
        };

        function __send_compare() {
            if ($('#diff-items').children('.hasItem').length < 2) {
                $('.pop-compare-tips').html('请至少添加两项商品才能对比！').show();
            } else {
                window.open('/goods/compare.html');
            }

        }

    })
    ;


    $(function () {
        var ShopApi = {};
        ShopApi.setCommentBoxSize = function (options) {
            if (options.height) {
                $('#evaluate_desc iframe').attr('height', parseInt(options.height));
            }
        }
        window.ShopApi = ShopApi;
        $.sub('click_detail_tab', __detail_tab, false);

        var $details = $('.goods_detail > div');
        var $tabs = $('#detail_tabs li');

        function __detail_tab() {
            var $t = $(this).addClass('current');
            $t.siblings().removeClass('current');
            var index = $tabs.index($t);
            $details.hide().eq(index).show();
            $.pub('detail_tabs_change', index);
            return false;
        }

        $(window).bind('scroll.goodsdetail', function () {
            if ($(window).scrollTop() >= $('.goods_detail').offset().top - $(window).height()) {
                $(window).unbind('scroll.goods_detail');
                $('.goods_detail > div').each(function () {
                    var $t = $(this);
                    if ($t.data('inited')) {
                        return;
                    }
                    $t.data('inited', 1);
                    $t.html($t.find('script').html());
                    $('img.lazy', $t).lazyload({effect: 'fadeIn'});
                });
                $tabs.filter('.current').click();
            }
        }).trigger('scroll.goodsdetail');
        var hash = location.hash;
        if (hash.indexOf('pack_desc') >= 0) {
            var $packtab = $tabs.filter('[rel="pack_desc"]');
            if ($packtab.length == 1) {
                $packtab.click();
                $(window).scrollTop($packtab.offset().top);
            }
        }
        $.getJSON('/ajax/detail/addfootprint.json', {
            goods_id: "1082982",
            goods_type: '70000'
        });
    });


    //优惠券信息弹层
    $.sub('click_showquan', __showquan)
    $.sub('click_close_quanitem', __close_quanitem)
    $.sub('click_ljlq', __ljlq)
    ;
    var $quanbox = $(".quanbox")
    var panelApi = $quanbox.jScrollPane().data('jsp')
    var busiact_id = $('.yh_quan').attr('busiact_id')
    var showyhLock = false;

    function __showquan() {
        var cururl = window.location.href;
        sessionStorage.setItem("quan_window", "open");
        sessionStorage.setItem("now", cururl);
        $('.quanitem').show();

    }

    function __close_quanitem() {
        showyhLock = false
        $('.quanitem').hide();
        sessionStorage.setItem("quan_window", "close");
    }

    if (sessionStorage.getItem("quan_window") == 'open' && sessionStorage.getItem("now") == window.location.href) {
        __showquan();
        sessionStorage.removeItem("quan_window");
        sessionStorage.removeItem("now");
    }

    var lqLock = false;

    function __ljlq() {
        if (lqLock) {
            return false;
        }
        lqLock = true;
        var $t = $(this)
        var batch_id = $t.parents('li').attr('batch_id')
        var $cur = $t.attr('index')
        $.post('/ajax/coupon/goodsdetailgetcoupon.json', {
            batch_id: batch_id,
        }, function (r) {
            if (r.code == 0) {
                //$t.removeClass('ljlq').removeClass('ac_ljlq').addClass('ylq')
                $.post('/ajax/coupon/usercanreceivecoupon.json', {
                    busiact_id: busiact_id,
                    //phone

                    type: 'sw',
                }, function (r) {
                    if (r.code == 0) {
                        if (r.data.coupon_data[$cur].pcardStatus == 1) {
                            $('.kaquanbt_' + $cur).html('<span class="bt jxlq ac_ljlq" index="' + $cur + '">继续领取</span><span class="zt ylq">已领取</span>')
                        } else {
                            $('.kaquanbt_' + $cur).html('<span class="zt ylq ac_close_quanitem">已领取</span>')
                        }
                    } else {
                        $.message(r.msg)
                    }
                }, 'JSON');
                $.message(r.msg)
            } else {
                $.message(r.msg)
            }
            lqLock = false;
        }, 'JSON');
    }


    $.sub('click_close_pop_holdon', __close_pop_holdon);

    function __close_pop_holdon() {
        holdonLock = false
        $('.pop_holdon_mask').hide();
        $('#pop_holdon').hide();
    }

    var ms_ie = false;
    var ua = window.navigator.userAgent;
    var old_ie = ua.indexOf('MSIE ');
    var new_ie = ua.indexOf('Trident/');
    if ((old_ie > -1) || (new_ie > -1)) {
        ms_ie = true;
    }

    if (ms_ie) {
        document.documentElement.className += " ie";
    }


    ;$(function () {
        $
            .sub('click_ui_hide', __ui_hide)
            .sub('click_select_buildingUl', __select_buildingUl)
            .sub('click_buildingUl_selected', __buildingUl_selected)
            .sub('click_door_selected', __door_selected)
            .sub('click_selected', __selected)
        ;

        function __ui_hide() {
            $.UI.hide();
            $('.popbroadband').hide();
        }

        var timer = null;
        var merchant_id = '1000049';
        var lock = 1;
        var str = "";
        var address_id_parent = -1;
        $('.popbroadband .detail_addr').keyup(function () {
            var $t = $(this), val = $t.val();
            var area_code = $('.popbroadband .pop_city').attr('city_id');
            clearTimeout(timer);
            if (val) {
                $('.popbroadband #addressUlDetail').html('');
                $('.popbroadband #buildingUl').html('');
                if (val.length > 1) {
                    timer = setTimeout(function search_addr() {
                        $.getJSON('/ajax/detail/getbroadbandaddress.json', {
                            address_name: val,
                            merchant_id: merchant_id,
                            type: 1,
                            area_code: area_code
                        }, function (r) {
                            var $addressUlDetail = $('.popbroadband #addressUlDetail').html('');
                            if (r.code) {
                                str = "";
                                var msg = r.msg || '没有找到你要的地址，请尝试其他关键词搜索';
                                str = '<li><a href="javascript:void(0)">' + msg + '</a></li>';
                                $addressUlDetail.html(str);
                            } else {
                                var $data = r.data;
                                str = "";
                                if ($data) {
                                    $.each($data, function (index, item) {
                                        str = str + '<li><a href="javascript:void(0)" class="ac_select_buildingUl" address_id="' + item.address_id + '"is_standard="' + item.is_standard + '">' + item.address_name + '</a></li>';
                                    })
                                } else {
                                    if (val.length > 1) {
                                        str = '<li><a href="javascript:void(0)">没有找到你要的地址，请尝试其他关键词搜索</a></li>';
                                    }
                                    ;
                                }
                                ;
                                $addressUlDetail.html(str);
                            }
                            ;
                        });
                    }, 1000);
                }
                ;
            } else {
                $('.popbroadband #addressUlDetail').html('');
                $('.popbroadband #buildingUl').html('');
            }

        });

        function __select_buildingUl() {
            var $t = $(this), address_id = $t.attr('address_id'), is_standard = $t.attr('is_standard');
            $t.addClass('current').parent().siblings().find('a').removeClass('current');
            if (is_standard == 0) {
                $.getJSON('/ajax/detail/getbroadbandaddress.json', {
                    address_id: address_id,
                    type: 2,
                    merchant_id: merchant_id
                }, function (r) {
                    var $buildingUl = $('.popbroadband #buildingUl').html('');
                    if (r.code) {
                        str = "";
                        var msg = r.msg || '没有找到你要的地址，请尝试其他关键词搜索';
                        str = '<li><a href="javascript:void(0)">' + msg + '</a></li>';
                        $buildingUl.html(str);
                    } else {
                        var $data = r.data;
                        str = "";
                        if ($data) {
                            $.each($data, function (index, item) {
                                str = str + '<li><a href="javascript:void(0)" class="ac_buildingUl_selected" address_id="' + item.address_id + '"detail_address="' + item.detail_address + '">' + item.address_name + '</a></li>';
                            });
                        } else {
                            str = '<li><a href="javascript:void(0)">没有找到你要的地址，请尝试其他关键词搜索</a></li>';
                        }
                        ;
                        $buildingUl.html(str);
                    }
                    ;
                });
            } else {
                var $buildingUl = $('.popbroadband #buildingUl').html('');
                str = '<li><a href="javascript:void(0)">该地址暂无剩余装机容量，请选择其它地址安装</a></li>';
                $buildingUl.html(str);
            }
            ;
        }

        function __buildingUl_selected() {
            var $t = $(this);
            var $t = $(this), address_id = $t.attr('address_id');
            $t.addClass('current').parent().siblings().find('a').removeClass('current');
            $.getJSON('/ajax/detail/getbroadbandaddress.json', {
                address_id: address_id,
                type: 3,
                merchant_id: merchant_id
            }, function (r) {
                if (r.code) {
                    str = "";
                    var msg = r.msg || '没有找到你要的地址，请尝试其他关键词搜索';
                    str = '<li><a href="javascript:void(0)">' + msg + '</a></li>';
                    $buildingUl.html(str);
                } else {
                    var $data = r.data;
                    var $buildingUl = $('.popbroadband #buildingUl').html('');
                    str = "";
                    if ($data) {
                        $.each($data, function (index, item) {
                            str = str + '<li><a href="javascript:void(0)" class="ac_door_selected" address_id="' + item.address_id + '"detail_address="' + item.detail_address + '">' + item.address_name + '</a></li>';
                        });
                    } else {
                        str = '<li><a href="javascript:void(0)">没有找到你要的地址，请尝试其他关键词搜索</a></li>';
                    }
                    ;
                    $buildingUl.html(str);
                    lock = 0;
                    address_id_parent = address_id;
                }
                ;
            });
        }

        function __door_selected() {
            var $t = $(this);
            $t.addClass('current').parent().siblings().find('a').removeClass('current');
            address_selected($t);
        }

        function address_selected(r) {
            var addr = {};
            addr.user_choice_address_id = $(r).attr('address_id');
            if (lock) {
                addr.address_id = $('.listtxt_left a.current').attr('address_id');
                addr.type = 2;
            } else {
                addr.type = 3;
                addr.address_id = address_id_parent;
            }
            ;
            __ui_hide();
            $('.install_addr').val($(r).attr('detail_address'));
            var detail_address = $(r).attr('detail_address');
            $(".install_addr").bind("input propertychange", function () {
                var t = this;
                setTimeout(function () {
                    t.value = detail_address;
                }, 10);
            });
            $(r).addClass('current').parent().siblings().find('a').removeClass('current');
            $.pub('addr_selected', addr);
        }

        function __selected() {
            var $t = $(this);
            var lock = true;
            $('#buildingUl a').each(function (index, el) {
                if ($(el).hasClass('current')) {
                    el.click();
                    lock = false;
                }
            });
            if (lock) {
                $('#buildingUl a').eq(0).click();
            }
            ;
        }
    });


    $(function () {
        $.sub('click_credit_qrcode', __credit_qrcode, false);

        function __credit_qrcode() {
            $('.mask').show();
            makeCode('https://touch.10086.cn' + location.pathname);
        }

        function makeCode(data) {
            $('.qrcode_warp').show();
            var qrcode;
            // if (qrcode) {
            // 	qrcode.clear();
            // }
            $('#qrcode').html('');
            if (navigator.appName.indexOf("Microsoft") != -1) {
                //IE  
                if (navigator.appVersion.match(/8./i) == "8.") {
                    //ie8  
                    qrcode = new QRCode('qrcode', {
                        text: data,
                        width: 160,
                        height: 160,
                        render: "table",
                        colorDark: '#8dc31f',
                        colorLight: '#ffffff',
                        correctLevel: QRCode.CorrectLevel.H
                    });
                }

            } else {
                qrcode = new QRCode('qrcode', {
                    text: data,
                    width: 160,
                    height: 160,
                    colorDark: '#8dc31f',
                    colorLight: '#ffffff',
                    correctLevel: QRCode.CorrectLevel.H
                });
            }
            $('#qrcode').removeAttr('title');
        };

        $('.qr_close').click(function () {
            $.hideMask();
            $(this).closest('.qrcode_warp').hide();
        });
    });

    ;$(function () {
        $.sub('click_dcstracker', __dcstracker)
            .sub('dcstracker_init', __dcstracker_init)
            .sub('login_success', __logined, true);

        function __logined() {
            $.pub('getfavbyid')
        }

        function __dcstracker() {
            $.pub('dcstracker_init', 'dcstracker_init', $(this));
        }

        function __dcstracker_init(event, action, target) {
            var $t = $(target);
            $.dcstracker({
                category: 'phone',
                action: 'phone_Product',
                event: $t.attr('dcs_id')
            });
        }

        var $tabs = $('.floatnav .detail_tab').click(function () {
            var tabindex = $tabs.index($(this));
            __detail_tabs_change(null, tabindex);
            $('body,html').animate({scrollTop: detail_tabs_height}, 1, function () {
                $('#detail_tabs .ac_detail_tab').eq(tabindex).click();
            });

        });
        $(window).bind('scroll.floatnav', function () {
            var sTop = $(window).scrollTop();
            var sheight = $('#detail_tabs').offset().top;
            var sale_type = '';
            var slave_show_info = '';

            if (sTop >= sheight) {
                $('.floatnav').show();
                $('.f_zxkf').hide();
            } else {
                $('.floatnav').hide();
                $('.f_zxkf').show();
            }
        }).trigger('scroll.floatnav');
        var animateTimer = null;
        var buy_height = $('#buy_area').offset().top;
        $('.jump_shoucang').click(function () {
            $('body,html').animate({scrollTop: buy_height}, 1, function () {
                clearTimeout(animateTimer);
                animateTimer = setTimeout(function () {
                    $('.btnFav em').click();
                }, 200);
            });
        });
        $('.jump_buy').click(function () {
            $('body,html').animate({scrollTop: buy_height}, 1, function () {
                clearTimeout(animateTimer);
                animateTimer = setTimeout(function () {
                    $('.click_float_buy').click();
                }, 200);
            });
        });
        var detail_tabs_height = $('#detail_tabs').offset().top + 1;

        function __detail_tabs_change(event, index) {
            $tabs.removeClass('current').eq(index).addClass('current');
        }

        $.sub('detail_tabs_change', __detail_tabs_change);
        $('.close').click(function () {
            $('.onestepbuymask').hide();
        });
        var $osp_top = $('#buy_area').offset().top;
        $osp_top = $osp_top - $('.onestepbuypic .pic_btn').height();
        $('.onestepbuypic .pic_btn').css('top', $osp_top + 72);

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
    },'utf-8');
</script>


<div id="webUid"></div>
<div id="serverUid"></div>
</body>
</html>