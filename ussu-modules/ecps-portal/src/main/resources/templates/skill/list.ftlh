<!DOCTYPE html>

<#--<html lang="en" xmlns:th="http://www.thymeleaf.org">-->

<!-- saved from url=(0133)https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&provinceId=771&cityId=771&WT.ac=key_search_032_%E6%89%8B%E6%9C%BA&from=10601 -->
<html xmlns="http://www.w3.org/1999/xhtml" class="">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">


    <link rel="shortcut icon" type="image/x-icon" href="https://search.10086.cn/shop//images/shop_new/favicon.ico">
    <meta name="WT.location" content="100">
    <meta name="WT.category" content="101">
    <meta name="keywords" content="中国移动网上商城,手机,值得买,可靠,质量好,移动合约机,买手机送话费,4G手机,手机最新报价,苹果,华为,魅族">
    <meta name="description" content="中国移动网上商城,提供最新最全的移动合约机、4G手机,买手机送话费,安全可靠,100%正品保证,让您足不出户,享受便捷移动服务！">
    <title>【中国移动】 手机-商品搜索_移动商城</title>
    <link href="${IMAGE_SERVER}cloud/static/skill/css/global.css__190.css" rel="stylesheet" type="text/css">
    <link href="${IMAGE_SERVER}cloud/static/skill/css/structure.css__570.css" rel="stylesheet" type="text/css">
    <link href="${IMAGE_SERVER}cloud/static/skill/css/header_footer.css__411.css" rel="stylesheet" type="text/css">
    <link href="${IMAGE_SERVER}cloud/static/skill/css/shop_searchbox.css__411.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${IMAGE_SERVER}cloud/static/skill/css/index_new.css">
    <link rel="stylesheet" href="${IMAGE_SERVER}cloud/static/skill/css/reset.css">
    <link href="${IMAGE_SERVER}cloud/static/skill/css/style.css" rel="stylesheet" type="text/css">
    <link href="${IMAGE_SERVER}cloud/static/skill/css/direct.css" rel="stylesheet" type="text/css">
    <!--[if lt IE 9]>
    <script r='m'>document.createElement("section")</script><![endif]-->
    <script src="${IMAGE_SERVER}cloud/static/skill/js/logCollecter.js"></script>
    <script async="" src="${IMAGE_SERVER}cloud/static/skill/js/webtrends.min.js"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/browser.js"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/head.js"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/shop_search.js"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/sdc_search.js"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/webtrends.min.js(1)"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/lc_search.js"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/logCollecter.js(1)"></script>
    <script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/js/jquery.cookie.js"></script>

</head>

<body id="navs">
<script type="text/javascript">


    function searchFun(search, formId) {
        //事件追踪码
        if (typeof (_tag) != 'undefined') {
            _tag.dcsMultiTrack('WT.event', 'key_search_033_' + $("#search").val());
        }
        var content = $("#" + search).val();
        if (content == null || content == "" || content == "undefined") {
            return false;
        }
        var form = document.getElementById(formId);
        form.submit();
    }

    var recItemList = '';
    var recLength = '';

    if ($.browser.msie && $.browser.version == "7.0") {
        window.location.href = "${IMAGE_SERVER}cloud/static/skill/jsp/shop/popup.html";
    }

    function webapp() {
        var sUserAgent = navigator.userAgent.toLowerCase();
        var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
        var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
        var bIsMidp = sUserAgent.match(/midp/i) == "midp";
        var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
        var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
        var bIsAndroid = sUserAgent.match(/android/i) == "android";
        var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
        var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
        //document.writeln("您的浏览设备为：");
        if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
            window.location.href = "http://search.10086.cn/staff/shop_index_mobile/index/771/771/";
            //window.location.href="http://search.10086.cn/shop_index_mobile/index/771/771/";
        }
    }

    var scheme = getProjectPath();

    function getProjectPath() {
        var path = window.location.href;
        var projectPath = "http";

        var index = path.indexOf("https");

        if (index == 0) {
            projectPath = "https";
        }
        return projectPath;
    }

    var rec_url = scheme + "://search.10086.cn/openapi/re/doRec";
    var base_url = scheme + "://search.10086.cn/openapi"
    //webapp();
    /*
     * 加载猜你喜欢推荐
     */
    function loadCNXHRecommend(areaCode) {
        // $('#cnxh').css('display', 'none');
        var userId = "";
        if (userId == null || userId == '') {
            userId = '';
        }
        $.ajax({
            data: {
                areacode: areaCode,
                userId: userId
            },
            url: rec_url + "?appkey=5d7365e3a01c4261a978e0e66f3e333a&page_size=5&page_no=1&scene_id=scene_004&user_id=" + userId + "&user_account=&province_id=" + areaCode + "&_=1578968543243",
            dataType: "json",
            // dataType: "jsonp",
            // jsonp: "jsonpcallback",
            success: function (res) {

                if (res.length > 0) {
                    $('#cnxh').css('display', 'block');
                }

                var str = "";
                var json = res.data.rec;

                json.forEach((val, i) => {
                    str += '<a  id="G10_rec_' + (Number(i) + 1) + '_' + val.item_id + '_4de83c81e9224efcb0692f3083ccefa5" type="1" href="http://shop.10086.cn/goods/771_771_' + val.item_id + '_' + val.item_id + '.html?WT.ac=rec_search_003_771_4de83c81e9224efcb0692f3083ccefa5_' + val.item_id + '" target="_blank" title="' + val.title + '" style="display:block;height:245px"><dl class="fixf">' +
                        '<dd><img class="" width=" 170" height="170" src="http://img1.shop.10086.cn/goods/' + val.picture + '_170x170.png" />' +
                        '</dd><dt><h3>￥' + val.min_price / 100 + '</h3><p>' + val.title + '</p>' +
                        '</dt></dl></a>';
                    recItemList += '{"item_id":"' + val.goodsId + '","data_type":"' + val.goodsId + '"}';
                })

                recLength = json.length;

                $('.related-recom-list').html(str);
                var recResult = '{"rec":[' + recItemList + ']}';
                if (recItemList != '') {
                    // requestShowListener("003", recResult, recLength, recLength, 1);
                }
            }
        });
    }


    /**删除筛选条件**/
    function clearAttr(valueID, attrs) {

        var av = '';
        var ns = attrs.split("|");
        for (var n in ns) {
            //var index =ns[n].indexOf(","+valueID);
            var vid = ns[n].substring(ns[n].indexOf(',') + 1);
            if (valueID != vid) {
                if (av.length <= 0)
                    av = ns[n];
                else
                    av = av + "|" + ns[n];
            }
        }
        var url = "list?key=手机&cityId=771&provinceId=771&code=&sort=&stock=&no_re=1&from=10601&attrsFlag=1&attrs=" + av;

        window.location.href = url;
    }

    /**增加筛选条件**/
    var ppids = "";

    function addAttr(keyID, valueID, attrIds, divID) {
        var a = $("#pinpai_genduo").attr("style");
        if (a != null && "attrID1" == divID && a.indexOf("display: none") >= 0) {
            if (ppids.indexOf(valueID) >= 0) {
                ppids = ppids.replace("_" + valueID, "");
                ppids = ppids.replace(valueID + "_", "");
                ppids = ppids.replace(valueID, "");
                return;
            }
            if (ppids == "")
                ppids = valueID;
            else
                ppids = ppids + "_" + valueID;
            return;
        } else {
            ppids = "";
        }

        var css = $("#" + divID).attr("class");
        if (css != null && css.indexOf("dxactive") >= 0)
            return;
        if (attrIds == null || attrIds.length <= 0)
            attrIds = keyID + "," + valueID;
        else

            attrIds = attrIds + "|" + keyID + "," + valueID;


        var url = "list?key=手机&cityId=771&provinceId=771&code=&sort=&stock=&no_re=1&from=10601&attrsFlag=1&attrs=" + attrIds;

        window.location.href = url;
    }

    function price_attr(keyID, attrIds, divID) {

        var minp = $("#min_price").val();
        var maxp = $("#max_price").val();
        if (minp.length <= 0 || isNaN(minp))
            minp = "0";

        if (maxp.length <= 0 || isNaN(maxp))
            return;
        addAttr(keyID, minp + "-" + maxp, attrIds, divID);
    }

    /**增加多选**/
    function addAttrs(divID, attrIds, keyID) {
        var ids = "";
        var a = $("#pinpai_genduo").attr("style");
        if (a != null && "attrID1" == divID) {
            ids = ppids;
        } else {
            var as = $("#" + divID + " .active");
            for (var i = 0; i < as.size(); i++) {
                var c = $(as.get(i)).attr('name');
                if (ids == null || ids.length <= 0)
                    ids = c;
                else
                    ids += "_" + c;
            }
        }
        if ("" == ids)
            return;
        if (attrIds == null || attrIds.length <= 0)
            attrIds = keyID + "," + ids;
        else
            attrIds = attrIds + "|" + keyID + "," + ids;
        var url = "list?key=手机&cityId=771&provinceId=771&code=&sort=&stock=&no_re=1&attrsFlag=1&attrs=" + attrIds;

        window.location.href = url;
    }

    function goods(id, img, url, price, title) {
        this.id = id;
        this.img = img;
        this.url = url;
        this.price = price;
        this.title = title;
        return this;
    }

    function removep(p) {
        for (var i = 0; i < ps.length; i++) {
            if (ps[i] != null && ps[i].id == p.id) {
                ps.splice(i, 1);
            }
        }
    }

    function addp(p) {
        if (ps.length >= 4)
            return;
        ps[ps.length] = p;
    }

    var ps = new Array();

    //增加对比商品到对比列表
    function compare(id, img, url, price, title) {
        var html = $("#diff-items");
        var remove = 0;
        var p = new goods(id, img, url, price, title);

        if ($("#compare_" + id).hasClass("btn-compare-s-active")) {
            $("#compare_" + id).removeClass("btn-compare-s-active");
            $("#compare_" + id).children().removeClass("duibi");
            $("#dl_" + id).remove();
            removep(p);
            remove = 1;
        } else if ($(".hasItem").size() < 4) {
            $("#compare_" + id).addClass("btn-compare-s-active duibi");
            $("#compare_" + id).children().addClass("duibi");
        } else {
            //alert("对比栏已满，您可以删除不需要的商品再继续添加哦！");
            //$("#tishi").html("<p>对比栏已满，您可以删除不需要的商品再继续添加哦！</p>");
            $("#pop-compare").show();
            return;
        }

        var items = $(".hasItem");
        $(html).html("");

        for (var i = 0; i < 4; i++) {
            if (i <= ps.length - 1) {
                $(html).append("<dl class='hasItem' onmouseover='mover(this);' onmouseout='mout(this);' id='dl_" + ps[i].id + "' fore='0'><dt> <a target='_blank' href='" + ps[i].url + "'><img src='" + ps[i].img + "' width='50' height='50'></a> </dt><dd> <a target='_blank' class='diff-item-name' href='" + ps[i].url + "'>" + ps[i].title + "</a> <span class='p-price'><strong class='J-p-1057746'>￥" + ps[i].price + "</strong><a class='del-comp-item' onclick=\"compare('" + ps[i].id + "','','','','');\" style='visibility: hidden;'>删除</a></span> </dd></dl>");
            } else if (i == $(items).size() && $("#compare_" + id).hasClass("btn-compare-s-active")) {
                $(html).append("<dl class='hasItem' onmouseover='mover(this);' onmouseout='mout(this);' id='dl_" + id + "' fore='0'><dt> <a target='_blank' href='" + url + "'><img src='" + img + "' width='50' height='50'></a> </dt><dd> <a target='_blank' class='diff-item-name' href='" + url + "'>" + title + "</a> <span class='p-price'><strong class='J-p-1057746'>￥" + price + "</strong><a class='del-comp-item' onclick=\"compare('" + id + "','','','','');\" style='visibility: hidden;'>删除</a></span> </dd></dl>");
                addp(p);
            } else
                $(html).append("<dl class='item-empty'><dt>" + (i + 1) + "</dt><dd>您还可以继续添加</dd></dl>");
        }

        if ($(".hasItem").size() > 0) {
            $("#pop-compare").show();
            if ($(".hasItem").size() > 1)
                $(".btn-compare-b").addClass("compare-active");
            else
                $(".btn-compare-b").removeClass("compare-active");
        } else {
            $("#pop-compare").hide();
        }
    }

    function clearItems() {
        var html = $("#diff-items");
        $(html).html("");
        $(".btn-compare-s-active").removeClass("btn-compare-s-active");
        $(".btn-compare-b").removeClass("compare-active");
        ps = new Array();
        for (var i = 0; i < 4; i++) {
            $(html).append("<dl class='item-empty'><dt>" + (i + 1) + "</dt><dd>您还可以继续添加</dd></dl>");
        }
    }

    function goContrast() {
        var items = $(".hasItem");
        var ids = "";
        if ($(items).size() < 2)
            return;
        for (var i = 0; i < $(items).size(); i++) {
            var id = $(items[i]).attr("id");

            if (id == null || id == "")
                continue;
            id = id.split("_")[1];
            if (ids == "")
                ids = id;
            else
                ids += "," + id;
        }
        var url = "shopcompare?provinceId=771&cityId=771&ids=" + ids;
        //window.location.href=url;
        window.open(url);
    }

    function mover(obj) {
        $(obj).find(".del-comp-item").css("visibility", "visible")
    }

    function mout(obj) {
        $(obj).find(".del-comp-item").css("visibility", "hidden");
    }


    window.allcity = {};
    $(function () {
        if ("" != 1) {
            $('.ch_c_c').attr("checked", false);
        } else {
            $('.ch_c_c').attr("checked", true);
        }


        $.sub('click_show_areabar', __show_areabar).sub('click_show_city', __show_city).sub('click_city_choose', __gotocity);
        var $province = $('#header .province'),
            $city = $('#header .city'),
            current_province_id = $province.find('span').attr('province_id'),
            city_id = '771',
            reg = new RegExp('<!--|-->', 'g'),
            template = new Formater($('.header_area_template').html().replace(reg, '') || '<li><a href="#" target="">{cityName}</a></li>'),
            city_name = '南宁',
            province_id = "771";

        //清除浮层
        function __clear_areabar() {
            $('#header').find('.area ul').hide();
        }

        //展开浮层
        function __show_areabar() {
            __clear_areabar();
            var $this = $(this);
            if ($this.next()[0].innerHTML == "") {
                var data = getCityByProvince(province_id), tmp = '';
                $.each(data, function (inx, item) {
                    tmp += "<li style='height:24px;overflow:hidden;'><a id='G3' type='1' href='javascript:void(0)' class='ac_city_choose' location='"
                        + province_id + "_" + item.cityId + "' title='" + item.cityName + "'>" + item.cityName + "</a></li>";
                    /* tmp+=template.exec(item); */
                });
                __clear_areabar();
                $this.closest('ul').prev().attr('province_id', $this.attr('province_id')).text($this.text());
                $city.find('ul').html(tmp).show();
            } else {
                $this.next().show();
            }
            return false;
        }


        function __gotocity(loc) {
            if (typeof loc != "string") {
                loc = $(this).attr('location');
            }
            //切换地区纠错词需传参nh=1标识是纠错词
            var rectify = '';
            var locarray = loc.split('_');
            var pid = locarray[0], cid = locarray[1];
            var word = $("#search").val();
            var url = '';
            if (rectify != '' && rectify != 'undefined' && rectify != null) {
                url = "./list?key=" + word + "&provinceId=" + pid + "&cityId=" + cid + "&code=&nh=1";
            } else {
                url = "./list?key=" + word + "&provinceId=" + pid + "&cityId=" + cid + "&code=";
            }
            //有搜索词切换地区时也要加入from标识
            if (word != '') {
                url = url + "&from=10201";
            } else {
                url = url + "&from=10207";
            }

            window.location.href = url;
        }

        function getCityByProvince(province_id) {
            if (!window.allcity[province_id]) {
                $.ajax({
                    async: false,
                    url: "./getCityByProvince.json",
                    data: {province_id: province_id},
                    success: function (r) {
                        if (r.code) {
                            $.alert(r.code)
                        } else {
                            window.allcity[province_id] = r.data || [];
                        }
                    },
                    dataType: 'json'
                });
            }
            return window.allcity[province_id];
        }

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
                tmp += "<li style='height:24px;overflow:hidden;'><a id='G3' type='1' href='javascript:void(0)' class='ac_city_choose' location='"
                    + province_id + "_" + item.cityId + "' title='" + item.cityName + "'>" + item.cityName + "</a></li>";
                /* tmp+=template.exec(item); */
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

        });

        $(".city").click(function () {
            getCityByProvince("771");
        });

    });

    function searchHistoryRecord(word) {
        if (word != null && word != "" && word != undefined) {
            $.ajax({
                type: "post",
                data: {
                    "word": word
                },
                dataType: "json",
                url: "/searchHistoryAdd.json",
                success: function (json) {

                }
            });
        }
    }

    function screenResize() {
        var ScreenWidth = window.screen.width;
        var client_Width = document.body.clientWidth;
        var html_ele = document.getElementsByTagName("html");
        if (ScreenWidth < 1200 || client_Width < 1200) {
            html_ele[0].className = "w1k";
        } else {
            html_ele[0].className = "";
        }

    }

    screenResize();


    if (window.addEventListener) {                    //所有主流浏览器，除了 IE 8 及更早 IE版本
        var tid = null;
        window.addEventListener('onorientationchange' in window ? 'orientationchange' : 'resize', function () {
            clearTimeout(tid);
            tid = setTimeout(screenResize, 300);
        }, false);
    } else if (window.attachEvent) {                  // IE 8 及更早 IE 版本
        var tid = null;
        window.attachEvent('onorientationchange' in window ? 'orientationchange' : 'resize', function () {
            clearTimeout(tid);
            tid = setTimeout(screenResize, 300);
        });
    }

    $(function () {
        $("#search").search({
            type: 1,
            maxResult: 10,
            cityId: "771",
            provinceId: "771"
        });

        $.ajax({
            type: "get",
            url: "https://search.10086.cn/openapi/se/word/selectHotWord",
            data: {"provinceId": '771', "sceneId": 'scene_077', "bussinessId": '044'},
            dataType: "json",
            success: function (json) {
                var json = json.data;
                if (json.length == 0 || json == null) {
                    $("#hotsearch").remove();
                    return false;
                }
                var hotModule = "";
                for (var i in json) {
                    var word = json[i].word;
                    var isLight = json[i].isHigh;
                    var tempClass = "search_blue";
                    if (isLight == 1) {
                        tempClass = "search_red";
                    }
                    var index = parseInt(i) + 1;

                    //解决ie浏览器不支持jq trim方法
                    String.prototype.trim = function () {
                        return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
                    }

                    var hurl = "list?key=" + word + "&cityId=" + "771" + "&provinceId=" + "771" + "&from=10202&no_re=1&nh=1" +
                        "&WT.ac=hot_search_011_771_" + index + "_" + word;
                    if (json[i].url.trim() != "") {
                        hurl = json[i].url.trim();
                        if (hurl.indexOf('?') >= 0) {
                            hurl = hurl + "&WT.ac=hot_search_011_771_" + index + "_" + word;
                        } else {
                            hurl = hurl + "?WT.ac=hot_search_011_771_" + index + "_" + word;
                        }
                    }


                    hotModule = hotModule + "<li><a class='" + tempClass + "' id='G5_hot_" + index + "_" + word + "' type='" + index + "' href='" + hurl + "'>" +
                        word +
                        "</a></li>";

                }
                $("#hotsearch").append(hotModule);
            }
        });

    });

    function login() {
        var url = "http://" + window.location.host + window.location.pathname + encodeURIComponent(window.location.search);
        url = "https://login.10086.cn/html/login/login.html?channelID=12009&backUrl=" + url;

        window.location.href = url;
    }

    function logout() {
        $.ajax({
            type: "post",
            dataType: "json",
            url: "./quit",
            success: function (data) {
                if (true == data) {
                    window.location.reload();
                    //$("#quit_num").hide();
                    //$("#quit_text").hide();
                    //$("#login").show();
                }
            }
        });
    }

    //搜索日志采集
    window.onload = function () {
        console.log("window.onload");
        //后端传到前台
        window.Collecter.pageEnviroment['trace_id'] = '4de83c81e9224efcb0692f3083ccefa5';  //后端生成 UID
        window.Collecter.pageEnviroment['login_name'] = "";
        var wt_co_f = $.getwt_fpc(document.cookie, "WT_FPC=id=", ":");
        window.Collecter.pageEnviroment['wt_co_f'] = wt_co_f;
        //搜索请求日志
        requestListener("scene_077", "05", "biz_018");

        //搜索结果
        if ('{"res":[{"data_type":"z10001","item_score":"","item_position":1},{"data_type":"z10001","item_score":"","item_position":2},{"data_type":"z10001","item_score":"","item_position":3},{"data_type":"z10001","item_score":"","item_position":4},{"data_type":"z10001","item_score":"","item_position":5},{"data_type":"z10001","item_score":"","item_position":6},{"data_type":"z10001","item_score":"","item_position":7},{"data_type":"z10001","item_score":"","item_position":8},{"data_type":"z10001","item_score":"","item_position":9},{"data_type":"z10001","item_score":"","item_position":10},{"data_type":"z10001","item_score":"","item_position":11},{"data_type":"z10001","item_score":"","item_position":12},{"data_type":"z10001","item_score":"","item_position":13},{"data_type":"z10001","item_score":"","item_position":14},{"data_type":"z10001","item_score":"","item_position":15},{"data_type":"z10001","item_score":"","item_position":16},{"data_type":"z10001","item_score":"","item_position":17},{"data_type":"z10001","item_score":"","item_position":18},{"data_type":"z10001","item_score":"","item_position":19},{"data_type":"z10001","item_score":"","item_position":20},{"data_type":"z10001","item_score":"","item_position":21},{"data_type":"z10001","item_score":"","item_position":22},{"data_type":"z10001","item_score":"","item_position":23},{"data_type":"z10001","item_score":"","item_position":24},{"data_type":"z10001","item_score":"","item_position":25},{"data_type":"z10001","item_score":"","item_position":26},{"data_type":"z10001","item_score":"","item_position":27},{"data_type":"z10001","item_score":"","item_position":28}]}' != '') {
            requestShowListener("scene_077", '{"res":[{"data_type":"z10001","item_score":"","item_position":1},{"data_type":"z10001","item_score":"","item_position":2},{"data_type":"z10001","item_score":"","item_position":3},{"data_type":"z10001","item_score":"","item_position":4},{"data_type":"z10001","item_score":"","item_position":5},{"data_type":"z10001","item_score":"","item_position":6},{"data_type":"z10001","item_score":"","item_position":7},{"data_type":"z10001","item_score":"","item_position":8},{"data_type":"z10001","item_score":"","item_position":9},{"data_type":"z10001","item_score":"","item_position":10},{"data_type":"z10001","item_score":"","item_position":11},{"data_type":"z10001","item_score":"","item_position":12},{"data_type":"z10001","item_score":"","item_position":13},{"data_type":"z10001","item_score":"","item_position":14},{"data_type":"z10001","item_score":"","item_position":15},{"data_type":"z10001","item_score":"","item_position":16},{"data_type":"z10001","item_score":"","item_position":17},{"data_type":"z10001","item_score":"","item_position":18},{"data_type":"z10001","item_score":"","item_position":19},{"data_type":"z10001","item_score":"","item_position":20},{"data_type":"z10001","item_score":"","item_position":21},{"data_type":"z10001","item_score":"","item_position":22},{"data_type":"z10001","item_score":"","item_position":23},{"data_type":"z10001","item_score":"","item_position":24},{"data_type":"z10001","item_score":"","item_position":25},{"data_type":"z10001","item_score":"","item_position":26},{"data_type":"z10001","item_score":"","item_position":27},{"data_type":"z10001","item_score":"","item_position":28}]}', '122', '28', '1');
        }
        //猜你喜欢推荐
        /* var recResult = '{"rec":['+recItemList+']}';
        if(recItemList != ''){
        	requestShowListener("003", recResult, recLength, recLength,1);
        } */
        //精品热卖
        if ('{"lev":[{"item_id":"1075701","data_type":"","item_position":1,"item_score":"3.26778"},{"item_id":"1072473","data_type":"","item_position":2,"item_score":"3.26778"},{"item_id":"1077618","data_type":"","item_position":3,"item_score":"3.26778"},{"item_id":"1076467","data_type":"","item_position":4,"item_score":"3.26778"}]}' != '') {
            requestShowListener("scene_077", '{"lev":[{"item_id":"1075701","data_type":"","item_position":1,"item_score":"3.26778"},{"item_id":"1072473","data_type":"","item_position":2,"item_score":"3.26778"},{"item_id":"1077618","data_type":"","item_position":3,"item_score":"3.26778"},{"item_id":"1076467","data_type":"","item_position":4,"item_score":"3.26778"}]}', '4', '4', 1);
        }
        //将上一次搜索生成的trace_id存储在cookie中
        $.cookie("trace_referer", '4de83c81e9224efcb0692f3083ccefa5', {path: "/"});


    };
    window.onbeforeunload = function () {
        //window.Collecter.push("SearchResult", "{搜索结果}");
        window.Collecter.closeListener();
    };

    function requestListener(scene_id, channel_id, bus_id) {
        window.Collecter.push("log_type", "1");        //日志类型
        window.Collecter.push("scene_id", scene_id);   //场景标识
        window.Collecter.push("channel_id", channel_id); //渠道标识
        window.Collecter.push("bus_id", bus_id);         //业务标识
        window.Collecter.push("page_id", "shop_result");   //页面标识
        window.Collecter.push("trace_referer", $.cookie("trace_referer"));
        window.Collecter.push("search_from", '10601');
        window.Collecter.push("content", '手机');
        window.Collecter.push("province_id", '771');
        window.Collecter.push("city_id", '771');
        window.Collecter.push("uid", '');
        window.Collecter.loadListener();
    }

    function requestShowListener(scene_id, strs, resultNum, showNum, pageNo) {
        window.Collecter.push("log_type", "2");
        window.Collecter.push("scene_id", scene_id);
        window.Collecter.push("item_list", strs);
        window.Collecter.push("result_num", resultNum);
        window.Collecter.push("show_num", showNum);
        window.Collecter.push("page_no", pageNo);
        window.Collecter.loadListener();
    }
</script>

<div class="topNav" id="G1">
    <div class="clearfix globalMain">
        <p class="leftMenu clearfix">
            <a id="G1" type="1" href="http://www.chinamobileltd.com/en/global/home.php">ENGLISH</a> <a id="G1"
                                                                                                               type="1"
                                                                                                               href="http://www.10086.cn/power/">政企客户</a>
        </p>
        <div class="rightMenu clearfix"> <span id="userinfo" class="floatleft">
        <a id="G1" type="1" onclick="login();" href="javascript:void(0)" class=" red login">登录</a>

    <em>|</em> </span>
            <a id="G1" type="1" class="cmccclient" href="http://www.10086.cn/cmccclient/">手机营业厅</a>
            <em>|</em>
            <a id="G1" type="1" href="http://jf.10086.cn/">积分商城</a>
            <em>|</em>
            <a id="G1" type="1" href="http://www.10086.cn/aboutus/gx/">关于中国移动</a>
            <div class="ewm" style="display:none"><img src="${IMAGE_SERVER}cloud/static/skill/images/ewm.jpg__0.jpg">
                <p>扫一扫，随时查话费！</p>
            </div>
        </div>
    </div>
</div>

<iframe src="${IMAGE_SERVER}cloud/static/skill/【中国移动】 手机-商品搜索_移动商城_files/saved_resource.html" id="ssocheckframe"
        style="display:none"></iframe>
<div class="globalMain clearfix" id="header">
    <div class="search_mlogo clearfix">
        <#include "../common/logoWhere.ftlh">
        <div class="search" id="G4">
            <form class="clearfix" id="form" onsubmit="return searchFun('search','form')"
                  action="https://search.10086.cn/shop/list" method="get">
                <input maxlength="35" id="search" type="text" name="key" autocomplete="off" disableautocomplete=""
                       class="txt i-prompt i-prompt-gray " placeholder="手机">
                <ul id="searchKeyWord1598326894949" class="search_toplist" style="display: none;"></ul>
                <ul id="searchHistory1598326894949" class="search_toplist" style="display: none;"></ul>
                <input type="submit" class="b" value="">
                <input type="hidden" class="txt" name="provinceId" value="771">
                <input type="hidden" class="txt" name="cityId" value="771">
                <input type="hidden" class="txt" name="nh" value="1">
                <input type="hidden" class="txt" name="from" value="10201">
                <!-- <input type="hidden" class="txt" id="WT.ac" name="WT.ac" value='' /> -->
            </form>
            <div id="G5">
                <ul class="floatleft search_hotlist clearfix" id="hotsearch">
                    <li><a class="search_blue" id="G5_hot_1_5G正式商用" type="1"
                           href="http://www.10086.cn/5Gplus?WT.ac=hot_search_011_771_1_5G%E6%AD%A3%E5%BC%8F%E5%95%86%E7%94%A8">5G正式商用</a>
                    </li>
                    <li><a class="search_blue" id="G5_hot_2_我的卡券" type="2"
                           href="https://shop.10086.cn/i/?f=mycoupons&WT.ac=hot_search_011_771_2_%E6%88%91%E7%9A%84%E5%8D%A1%E5%88%B8">我的卡券</a>
                    </li>
                    <li><a class="search_red" id="G5_hot_3_彩铃" type="3"
                           href="https://search.10086.cn/shop/list?key=%E5%BD%A9%E9%93%83&cityId=771&provinceId=771&from=10202&no_re=1&nh=1&WT.ac=hot_search_011_771_3_%E5%BD%A9%E9%93%83">彩铃</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="clearfix navigation" id="G7">
        <ul class="clearfix withhover" id="nav">
            <li class="title">商品服务分类</li>
            <li class=""><a id="G7" type="1" target="_blank" class="ac_dcs_base index_nohover" dcs_id="nav_0"
                            href="http://www.10086.cn/">首页</a></li>
            <li class="shop"><a id="G7" type="1" class="ac_dcs_base shop_ini" dcs_id="nav_1"
                                href="http://shop.10086.cn/mall_771_771.html">移动商城</a>
            </li>

            <li class=""><a id="G7" type="1" target="_blank" class="ac_dcs_base wdyd" dcs_id="nav_2"
                            href="http://shop.10086.cn/i/">个人中心</a></li>
            <li class=""><a id="G7" type="1" target="_blank" class="ac_dcs_base wsyyt" dcs_id="nav_3"
                            href="http://shop.10086.cn/list/101_771_771_1_0_0_0_0_0_2_0.html">信用购机</a>
            </li>
            <li class=""><a id="G7" type="1" target="_blank" class="ac_dcs_base" dcs_id="nav_4"
                            href="http://www.10086.cn/support/gx/">服务与支持</a></li>
        </ul>
        <div id="G9" class="spnav">
            <style type="text/css">
                .sname a.jine {
                    padding: 0 3px 0 0 !important;
                }
            </style>
            <ul class="goodsNav clearfix withhover">
                <li class="101_c" onclick="h6();">
                    <div class="sname iconNavPhone" id="G9_dir_1">
                        <a id="G9_dir_1" type="1" dcs_id="101"
                           href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=101&from=10207&sort=sale%20desc"
                           class="title  ac_dcstracker_homefirstscreen">买手机</a></div>
                    <div class="smore"></div>
                </li>
                <li class="jhf_c" onclick="h7();">
                    <div class="sname iconNavHuafei" id="G9_dir_2"><a id="G9_dir_2" type="1" dcs_id="jiaohuafei"
                                                                      href="http://shop.10086.cn/i/service/recharge/item/itemcollect_771_771_.html"
                                                                      class="title  ac_dcstracker_homefirstscreen">交话费</a>
                    </div>
                    <div class="smore"></div>
                </li>
                <li class="140_c" onclick="h8();">
                    <div class="sname iconNavTaocan" id="G9_dir_3"><a id="G9_dir_3" type="1" dcs_id="140"
                                                                      href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=140&from=10207&sort=sale%20desc"
                                                                      class="title  ac_dcstracker_homefirstscreen">办套餐</a>
                    </div>
                    <div class="smore"></div>
                </li>
                <li class="146_c" onclick="h9();">
                    <div class="sname iconNavYewu" id="G9_dir_4"><a id="G9_dir_4" type="1" dcs_id="146"
                                                                    href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=146&from=10207&sort=sale%20desc"
                                                                    class="title  ac_dcstracker_homefirstscreen">办业务</a>
                    </div>
                    <div class="smore"></div>
                </li>
                <li class="goodsNavGray 134_c" onclick="h10();">
                    <div class="sname iconNavUser" id="G9_dir_5"><a id="G9_dir_5" type="1" dcs_id="134"
                                                                    href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=128&from=10207&sort=sale%20desc"
                                                                    class=" title ac_dcstracker_homefirstscreen">挑配件</a>
                    </div>
                    <div class="smore"></div>
                </li>
                <li class="128_c" onclick="h11();">
                    <div class="sname iconNavPeijian" id="G9_dir_6"><a id="G9_dir_6" type="1" dcs_id="128"
                                                                       href="http://shop.10086.cn/i/?f=home"
                                                                       class="title  ac_dcstracker_homefirstscreen">个人中心</a>
                    </div>
                    <div class="smore"></div>
                </li>
            </ul>
        </div>
    </div>
    <!--    top   end     -->


    <div class="main container">


        <div class="related-recom">
            <div id="cnxh">
                <div class="title">
                    <h2>猜你喜欢</h2>
                </div>
                <div class="related-recom-list" id="G10_rec">
                    <a id="G10_rec_1_1059271_4de83c81e9224efcb0692f3083ccefa5" type="1"
                       href="http://shop.10086.cn/goods/771_771_1059271_1059271.html?WT.ac=rec_search_003_771_4de83c81e9224efcb0692f3083ccefa5_1059271"
                       target="_blank" title="白领会员" style="display:block;height:245px">
                        <dl class="fixf">
                            <dd>
                                <img class="" width=" 170" height="170"
                                     src="${IMAGE_SERVER}cloud/static/skill/images/t7dzuekg4uhnfv8h_170x170.png"></dd>
                            <dt><h3>￥0</h3>
                                <p>白领会员</p></dt>
                        </dl>
                    </a><a id="G10_rec_2_1059257_4de83c81e9224efcb0692f3083ccefa5" type="1"
                           href="http://shop.10086.cn/goods/771_771_1059257_1059257.html?WT.ac=rec_search_003_771_4de83c81e9224efcb0692f3083ccefa5_1059257"
                           target="_blank" title="咪咕超清音乐会员" style="display:block;height:245px">
                        <dl class="fixf">
                            <dd><img class="" width=" 170" height="170"
                                     src="${IMAGE_SERVER}cloud/static/skill/images/t7dzt255mgageqhg_170x170.png"></dd>
                            <dt><h3>￥10</h3>
                                <p>咪咕超清音乐会员</p></dt>
                        </dl>
                    </a><a id="G10_rec_3_1025687_4de83c81e9224efcb0692f3083ccefa5" type="1"
                           href="http://shop.10086.cn/goods/771_771_1025687_1025687.html?WT.ac=rec_search_003_771_4de83c81e9224efcb0692f3083ccefa5_1025687"
                           target="_blank" title="彩信套餐" style="display:block;height:245px">
                        <dl class="fixf">
                            <dd><img class="" width=" 170" height="170"
                                     src="${IMAGE_SERVER}cloud/static/skill/images/tmkjdy6afy9df332_170x170.png"></dd>
                            <dt><h3>￥1</h3>
                                <p>彩信套餐</p></dt>
                        </dl>
                    </a><a id="G10_rec_4_1068507_4de83c81e9224efcb0692f3083ccefa5" type="1"
                           href="http://shop.10086.cn/goods/771_771_1068507_1068507.html?WT.ac=rec_search_003_771_4de83c81e9224efcb0692f3083ccefa5_1068507"
                           target="_blank" title="校园WLAN流量套餐" style="display:block;height:245px">
                        <dl class="fixf">
                            <dd><img class="" width=" 170" height="170"
                                     src="${IMAGE_SERVER}cloud/static/skill/images/t7h5hgw2sckttq7ye_170x170.png"></dd>
                            <dt><h3>￥10</h3>
                                <p>校园WLAN流量套餐</p></dt>
                        </dl>
                    </a><a id="G10_rec_5_1068492_4de83c81e9224efcb0692f3083ccefa5" type="1"
                           href="http://shop.10086.cn/goods/771_771_1068492_1068492.html?WT.ac=rec_search_003_771_4de83c81e9224efcb0692f3083ccefa5_1068492"
                           target="_blank" title="WLAN流量套餐" style="display:block;height:245px">
                        <dl class="fixf">
                            <dd><img class="" width=" 170" height="170"
                                     src="${IMAGE_SERVER}cloud/static/skill/images/t2ue4mxqw3b9uyxp_170x170.png"></dd>
                            <dt><h3>￥5</h3>
                                <p>WLAN流量套餐</p></dt>
                        </dl>
                    </a>
                </div>
            </div>
        </div>


        <div class="main-list">

            <div class="filter fix">
                <div id="G15_sort">
                    <ul class="sort fix">
                        <li onclick="h1();" class="active"><a id="G15_sort_1_score" type="1" href="javascript:void(0);">综合排序</a>
                        </li>
                        <li onclick="h2();" class=""><a id="G15_sort_2_sale" type="1" href="javascript:void(0);">销量</a>
                        </li>
                        <li onclick="h3();" class=""><a id="G15_sort_3_priceasc" type="1" href="javascript:void(0);">价格升序</a>
                        </li>
                        <li onclick="h4();" class=""><a id="G15_sort_4_pricedesc" type="1" href="javascript:void(0);">价格降序</a>
                        </li>
                    </ul>
                </div>
                <div class="is-available">
                    <a id="G15_stock" type="1"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&no_re=1&sort=&attrsFlag=1&attrs=&stock=1">
                        <input type="checkbox" id="available-chx" class="ch_c_c" onclick="h5();">
                        仅显示有货</a>
                </div>
                <div class="filter-page"><span class="cur-page">1</span>/<span class="p_page">5</span>
                    <a id="G16_page" type="1"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&sort=&from=10601&attrsFlag=1&attrs=&start=2"><b
                                class="next next_g">&gt;</b></a>
                    <b class="pre over ">&lt;</b>
                </div>
            </div>

            <div class="tab" style="height:900px;" id="G13_res">
                <div data-item="0" class="main-list-container1 on fix">

                    <#list relationList! as relation>
                        <div class="hoverGoods">
                            <a type="1" href="./skilldetails?skuId=${relation.skuId}" target="_blank">
                                <dl class="pds">
                                    <dd><img src="${IMAGE_SERVER}${relation.ebSku.skuImg}"></dd>
                                    <dt>
                                        <h3> ¥ ${relation.seckillPrice}<span>现货</span></h3>
                                        <p class="p1">
                                            ${relation.ebSku.skuName}<span></span>
                                        </p>
                                    </dt>
                                </dl>
                            </a>
                        </div>
                    </#list>

                </div>
            </div>
        </div>

        <div class="page" id="G16_page"> 第 <span class="cur-page">1 </span><span>/ 5</span>
            &nbsp;&nbsp;&nbsp;共<span>122</span>条
            <ul>


                <li class="active"><a class="active" id="G16_page" type="1" href="javascript:void(0);">1</a></li>


                <li onclick="window.location.href='list?key=手机&cityId=771&provinceId=771&code=&sort=&from=10601&attrsFlag=1&attrs=&start=2&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1'">
                    <a id="G16_page" type="1"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&attrs=&start=2&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1">2</a>
                </li>


                <li onclick="window.location.href='list?key=手机&cityId=771&provinceId=771&code=&sort=&from=10601&attrsFlag=1&attrs=&start=3&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1'">
                    <a id="G16_page" type="1"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&attrs=&start=3&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1">3</a>
                </li>


                <li onclick="window.location.href='list?key=手机&cityId=771&provinceId=771&code=&sort=&from=10601&attrsFlag=1&attrs=&start=4&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1'">
                    <a id="G16_page" type="1"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&attrs=&start=4&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1">4</a>
                </li>


                <li onclick="window.location.href='list?key=手机&cityId=771&provinceId=771&code=&sort=&from=10601&attrsFlag=1&attrs=&start=5&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1'">
                    <a id="G16_page" type="1"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&attrs=&start=5&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1">5</a>
                </li>


                <!-- <li class="ellipsis">…</li> -->

                <li class="page-li-l"
                    onclick="window.location.href='list?key=手机&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&from=10601&attrs=&start=5&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1'">
                    <a id="G16_page" type="1" class="span2 color2"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&attrs=&start=5&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1">末页</a>
                </li>
                <li onclick="window.location.href='list?key=手机&cityId=771&provinceId=771&code=&sort=&from=10601&attrsFlag=1&attrs=&start=2&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1'">
                    <a id="G16_page" type="1" class="span2 color2"
                       href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&attrs=&start=2&stock=&logId=98a527f20475426f91de606af10b9628&no_re=1">&gt;</a>
                </li>


            </ul>
        </div>

    </div>
</div>
<div id="G6">
    <ul class="rightfloatnav" id="returntop" style="display: none;">

        <li><a id="G6_nav_1_101" type="1"
               href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=101&from=10207&sort=sale%20desc"
               class="rfn_1">购买手机</a></li>
        <li><a id="G6_nav_2_140" type="1"
               href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=140&from=10207&sort=sale%20desc"
               class="rfn_8">办理套餐</a></li>
        <li><a id="G6_nav_3_146" type="1"
               href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=146&from=10207&sort=sale%20desc"
               class="rfn_2">办理业务</a></li>
        <!-- <li><a id="G6" type="1" href="" class="rfn_3">选购号码</a></li> -->
        <li><a id="G6_nav_4_128" type="1"
               href="https://search.10086.cn/shop/list?cityId=771&provinceId=771&code=128&from=10207&sort=sale%20desc"
               class="rfn_4">挑选配件</a></li>
        <li><a id="G6_nav_5_personal" type="1" href="http://shop.10086.cn/i/?f=home" class="rfn_5">个人中心</a></li>
        <li><a id="G6_nav_6_backHome" type="1" href="http://www.10086.cn/gx/" class="rfn_6">返回首页</a>
        </li>
        <li><a id="G6_nav_7_backTop" type="1"
               href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA&provinceId=771&cityId=771&WT.ac=key_search_032_%E6%89%8B%E6%9C%BA&from=10601#"
               class="rfn_7">返回顶部</a></li>

    </ul>
</div>
<!--    main   end     -->
<!--    main2   start     -->
<script>

</script>
<div class="bottom_a">

    <div class="tab_a" id="G19_rel">
        <ul class="tab_li">
            <li class="li_a"><span style="font-size:12px;">您是不是要找</span>&nbsp;&nbsp;:</li>

            <li class="li_b">
                <a id="G19_rel_1_手机号码" type="1"
                   href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA%E5%8F%B7%E7%A0%81&cityId=771&provinceId=771&no_re=1&nh=1&from=10206&WT.ac=rel_search_056_771_1_%E6%89%8B%E6%9C%BA%E5%8F%B7%E7%A0%81">手机号码</a>
            </li>

            <li class="li_b">
                <a id="G19_rel_2_手机号" type="2"
                   href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA%E5%8F%B7&cityId=771&provinceId=771&no_re=1&nh=1&from=10206&WT.ac=rel_search_056_771_2_%E6%89%8B%E6%9C%BA%E5%8F%B7">手机号</a>
            </li>

            <li class="li_b">
                <a id="G19_rel_3_手机卡" type="3"
                   href="https://search.10086.cn/shop/list?key=%E6%89%8B%E6%9C%BA%E5%8D%A1&cityId=771&provinceId=771&no_re=1&nh=1&from=10206&WT.ac=rel_search_056_771_3_%E6%89%8B%E6%9C%BA%E5%8D%A1">手机卡</a>
            </li>

            <li class="li_b">
                <a id="G19_rel_4_购机赠话费" type="4"
                   href="https://search.10086.cn/shop/list?key=%E8%B4%AD%E6%9C%BA%E8%B5%A0%E8%AF%9D%E8%B4%B9&cityId=771&provinceId=771&no_re=1&nh=1&from=10206&WT.ac=rel_search_056_771_4_%E8%B4%AD%E6%9C%BA%E8%B5%A0%E8%AF%9D%E8%B4%B9">购机赠话费</a>
            </li>

            <li class="li_b">
                <a id="G19_rel_5_广东年中特惠" type="5"
                   href="https://search.10086.cn/shop/list?key=%E5%B9%BF%E4%B8%9C%E5%B9%B4%E4%B8%AD%E7%89%B9%E6%83%A0&cityId=771&provinceId=771&no_re=1&nh=1&from=10206&WT.ac=rel_search_056_771_5_%E5%B9%BF%E4%B8%9C%E5%B9%B4%E4%B8%AD%E7%89%B9%E6%83%A0">广东年中特惠</a>
            </li>

            <li class="li_c">
                <a class="li_ac" id="G19_rel_6_华为p9" type="6"
                   href="https://search.10086.cn/shop/list?key=%E5%8D%8E%E4%B8%BAp9&cityId=771&provinceId=771&no_re=1&nh=1&from=10206&WT.ac=rel_search_056_771_6_%E5%8D%8E%E4%B8%BAp9">华为p9</a>
            </li>
        </ul>
    </div>

    <#include "../common/footerSearch.ftlh">
</div>
<!--    main2   end     -->
<!--    bottom   start     -->
<#include "../common/footerNav.ftlh">
<#include "../common/footer.ftlh">

<input id="key_j" type="hidden" value="手机">
<input id="cityId_j" type="hidden" value="771">
<input id="pId_j" type="hidden" value="771">
<input id="code_j" type="hidden" value="">
<input id="sort_j" type="hidden" value="">
<input id="stock_j" type="hidden" value="">
<input id="from_j" type="hidden" value="10601">
<input id="attrs_j" type="hidden" value="">
<input id="flag_j" type="hidden" value="0">

<!--    bottom   end     -->
<script type="text/javascript" src="${IMAGE_SERVER}cloud/static/skill/images/loader.js__177.js"></script>
<script type="text/javascript">
    loadCNXHRecommend("771");
    //导航样式
    $('#nav .title').hover(function () {
        $(".spnav .smore").css('display', 'none');
        $(".spnav .goodsNav").show();
    }, function () {
        $(".spnav .smore").css('display', 'none');
        $(".spnav .goodsNav").hide();
    });
    $(".spnav .goodsNav").hover(function () {
        $(this).show();
    }, function () {
        $(this).hide();
    });

    $('.101_c,.jhf_c,.140_c,.146_c,.134_c,.128_c').hover(function () {
            $(this).addClass('hover');
        }, function () {
            $(this).removeClass('hover');
        }
    );

    function h1() {
        url = "list?key=手机&cityId=771&provinceId=771&code=&sort=score desc&attrsFlag=1&attrs=&stock=&no_re=1";
        window.location.href = url;
    }

    function h2() {
        url = "list?key=手机&cityId=771&provinceId=771&code=&sort=sale desc&attrsFlag=1&attrs=&stock=&no_re=1"
        window.location.href = url;
    }

    function h3() {
        url = "list?key=手机&cityId=771&provinceId=771&code=&sort=min_price asc&attrsFlag=1&attrs=&stock=&no_re=1"
        window.location.href = url;
    }

    function h4() {
        url = "list?key=手机&cityId=771&provinceId=771&code=&sort=min_price desc&attrsFlag=1&attrs=&stock=&no_re=1"
        window.location.href = url;
    }

    function h5() {
        url = "list?key=手机&cityId=771&provinceId=771&code=&sort=&attrsFlag=1&attrs=&stock=1&no_re=1";
        window.location.href = url;
    }

    function h6() {
        url = "list?cityId=771&provinceId=771&code=101&sort=sale desc";
        window.location.href = url;
    }

    function h7() {
        url = "http://shop.10086.cn/i/service/recharge/item/itemcollect_771_771_.html";
        window.location.href = url;
    }

    function h8() {
        url = "list?cityId=771&provinceId=771&code=140&sort=sale desc";
        window.location.href = url;
    }

    function h9() {
        url = "list?cityId=771&provinceId=771&code=146&sort=sale desc";
        window.location.href = url;
    }

    function h10() {
        url = "list?cityId=771&provinceId=771&code=128&sort=sale desc";
        window.location.href = url;
    }

    function h11() {
        url = "http://shop.10086.cn/i/?f=home";
        window.location.href = url;
    }

    $('.li_hover').hover(function () {
        $(this).addClass('active');
    }, function () {
        $(this).removeClass('hhh');
    });


    $(".cmccclient").hover(function () {
        $('.ewm').show();
    }, function () {
        $('.ewm').hide();
    });
    $('.ewm').hover(function () {
        $(this).show();
    }, function () {
        $(this).hide();
    });

    setInterval("checkWindow()", 100);

    function checkWindow() {
        var h = document.body.scrollTop || document.documentElement.scrollTop;
        if (h > 200) {
            document.getElementById("returntop").style.display = "block";
        } else {
            document.getElementById("returntop").style.display = "none";
        }
    }


    $('.eachI .eachI_i').each(function () {

        if ($(this).attr('eimg') == '' || $(this).attr('eimg') == null) {
            $(this).attr("alt", "");
            $(this).attr("src", "");
            $(this).css('display', 'none');
            $(this).next('.text').addClass('imp');
        }
    });
    /* $(function(){
        var pid = "771";
	var wdyd = "";
	var wsyyt = "";
	if(pid == "551"){//安微
		wdyd = "http://ah.10086.cn/wdzh";
		wsyyt = "http://service.ah.10086.cn/";
	}else if(pid == "771"){//广西
		wdyd = "http://www.gx.10086.cn/wodeyidong";
		wsyyt = "http://service.gx.10086.cn";
	}
	else if(pid == "371"){//河南
		wdyd = "http://service.ha.10086.cn/service/mobile/my-consume.action";
		wsyyt = "http://service.ha.10086.cn/service/index.action";
	}
	else if(pid == "250"){//江苏
		wdyd = "http://service.js.10086.cn/login.html?url=my/index.html";
		wsyyt = "http://service.js.10086.cn/index.jsp#home";
	}
	else if(pid == "971"){//青海线
		wsyyt = "http://www.qh.10086.cn/ics/app";
	}
	else if(pid == "210"){//上海
		wdyd = "http://www.sh.10086.cn/sh/my/";
		wsyyt = "http://www.sh.10086.cn/sh/service/";
	}
	else if(pid == "290"){//陕西
		wdyd = "http://www.sn.10086.cn/my/account/";
	}
	else if(pid == "871"){//云南
		wdyd = "http://www.yn.10086.cn/service/login.html?url=my/index.html";
		wsyyt = "http://www.yn.10086.cn/service/index.html";
	}
	else if(pid == "571"){//浙江
		wsyyt = "http://service.zj.10086.cn/index.jsp#home";
	}
	else if(pid == "351"){//浙江
		wdyd = "http://service.sx.10086.cn/my/hfcx.html";
	}
	if(wdyd != "")
	{
		$(".wdyd").attr("href",wdyd);
	}
	if(wsyyt != ""){
		$(".wsyyt").attr("href",wsyyt);
	}
}); */
</script>

<script src="${IMAGE_SERVER}cloud/static/skill/images/index1.js"></script>
<!-- 页面采集 -->


</div>
<div class="xl-chrome-ext-bar" id="xl_chrome_ext_{4DB361DE-01F7-4376-B494-639E489D19ED}" style="display: none;">
    <div class="xl-chrome-ext-bar__logo"></div>

    <a id="xl_chrome_ext_download" href="javascript:;" class="xl-chrome-ext-bar__option">下载视频</a>
    <a id="xl_chrome_ext_close" href="javascript:;" class="xl-chrome-ext-bar__close"></a>
</div>
</body>
</html>