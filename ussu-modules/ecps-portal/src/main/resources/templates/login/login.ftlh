<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<!-- saved from url=(0134)https://login.10086.cn/html/login/login.html?channelID=12002&backUrl=https%3A%2F%2Fshop.10086.cn%2Fmall_431_431.html%3Fforcelogin%3D1# -->
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Cache-Control" content="max-age=7200">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="shortcut icon" href="https://login.10086.cn/platform/images/favicon.ico" type="images/x-icon">
    <link href="https://img.seasmall.top/cloud/static/login/css/ui.css" rel="stylesheet" type="text/css">
    <!-- <link href="/platform/css/ngca.css?resVer=20141124" rel="stylesheet" type="text/css" /> -->
    <!-- <script src="/platform/js/??jquery.min.js,jquery.cookie.js,jquery.mailAutoComplete.l.js,login_qr_reg.js,login_qr_fun.js?resVer=20180112,detectmobilebrowser.js?resVer=20170925" type="text/javascript"></script> -->
    <script src="https://img.seasmall.top/cloud/static/login/js/jquery.min.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/jquery.cookie.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/jquery.mailAutoComplete.l.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/login_qr_reg.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/remember_decrypts.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/login_qr_fun.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/detectmobilebrowser.js"></script>

    <!-- add by wangp at 2018-01-23 密码加密方法 start -->
    <script src="https://img.seasmall.top/cloud/static/login/js/jsencrypt.min.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/aes.js"></script>
    <script src="https://img.seasmall.top/cloud/static/login/js/encrypt.js"></script>
    <!-- add by wangp at 2018-01-23 密码加密方法 end -->
    <script type="text/javascript" src="https://img.seasmall.top/cloud/static/md5/md5.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/axios/0.21.1/axios.min.js"></script>

    <script type="text/javascript">
        if (jQuery.browser.mobile || /iPad/i.test(navigator.userAgent)) {
            window.location.href = '/html/login/touch.html' + window.location.search;
        }
    </script>
    <title>用户登录</title>
</head>

<body>
<!-- 逻辑控制全局变量 -->
<input id="send_sms_flag" type="hidden" value="1">
<!-- add by wangp at 2016-07-13 增加二次认证标识 start -->
<input id="send_smspwd_flag" type="hidden" value="1">
<!-- add by wangp at 2016-07-13 增加二次认证标识 end -->
<input type="hidden" id="codeResult" name="codeResult" value="false">
<input id="channelID" type="hidden" value="12002">
<input id="backUrl" type="hidden" value="${backUrl!}">
<input id="tab_name" type="hidden" value="phoneLogin">
<input id="accountType" type="hidden" value="01">
<input id="pwdType" type="hidden" value="01">
<input id="captchaValidity" type="hidden" value="1">
<!-- 验证码调整策略 yangyc 2015-11-17 start -->
<input id="needVerifyCodeFlag" type="hidden" value="0">
<!-- 验证码调整策略 yangyc 2015-11-17 end -->
<input id="loginMode" type="hidden" value="01">

<img id="sendImg" style="display:none;" src="https://img.seasmall.top/cloud/static/login/images/loadSendflag.htm">

<input type="hidden" id="smsflagCode" value="">

<div class="content">
    <!--头部内容  开始-->
    <div class="top clearfix">
        <a class="fll" href="http://shop.10086.cn/"><img src="https://img.seasmall.top/cloud/static/login/images/yd_logo.png" alt=""></a>
        <a class="flr margin-top-30" href="javascript:window.history.go(-1)">[返回]
        </a>
    </div>
    <!--头部内容  结束-->
    <!--主体内容  开始-->
    <div class="main login_main">
        <div class="tabs">
            <!-- 标签头，切换 -->
            <ul class="tabsNav">
                <a id="J_qrcode" class="s-qrcode" href="javascript:void(0);"></a>
                <a id="J_pc" class="pc hide" href="javascript:void(0);"></a>
                <li class="cubService_login on" id="service_login_1" style="display: list-item;">服务密码登录</li>
                <li class="cubSms_login" id="sms_login_1" style="display: list-item;">短信随机码登录</li>

                <!--
                <li class="mail_login hide" id="mail_login_1" style="display: none;">互联网用户登录</li>
                <li class="qrcode_login hide" id="qrcode_login_1" style="display: none;">扫码登录</li>
                -->

            </ul>
            <!-- 登录框主体 -->
            <div class="tabsContent">

                <!-- 服务密码登录  -->

                <div id="service_login_main" class="clearfix" style="display: block;">

                    <div class="reg_item clearfix">
                        <input type="text" id="p_name" name="p_name" autocomplete="off" class="reg_input"
                               style="color:#5f6063"
                               onfocus="if(this.value=='手机号码'){this.value='';this.style.color='#5f6063'};"
                               onblur="if(this.value==''){this.value='手机号码';this.style.color='#5f6063'}"
                               value="手机号码" maxlength="11">
                        <div id="phone_error" class="reg_error hide" style="display: none;">对应的错误信息提示</div>
                    </div>

                    <div class="reg_item passwordbox clearfix">
                        <label style="display: block;">服务密码</label>
                        <input id="p_pwd" autocomplete="off" class="reg_input" type="password" value="">
                        <div id="phonepwd_error" class="reg_error hide" style="display: none;">
                            对应的错误信息提示
                        </div>
                    </div>

                </div>
                <!-- 服务密码登录  end-->

                <!-- 短信随机码登录  -->
                <div id="sms_login_main" class="clearfix hide" style="margin-top: 15px; display: none;">
                    <div class="reg_item clearfix">
                        <input id="sms_name" name="sms_name" type="text" class="reg_input" style="color:#5f6063"
                               onfocus="if(this.value=='手机号码'){this.value='';this.style.color='#5f6063'};"
                               onblur="if(this.value==''){this.value='手机号码';this.style.color='#5f6063'}"
                               value="手机号码" maxlength="11">
                        <div class="reg_error hide" id="smsphone_err" style="display: none;">对应的错误信息提示</div>
                    </div>
                    <div class="reg_item clearfix">

                        <input id="sms_pwd_l" type="text" name="rpwd" class="reg_input small" style="color:#5f6063"
                               onfocus="if(this.value=='短信验证码'){this.value='';this.style.color='#5f6063'};"
                               onblur="if(this.value==''){this.value='短信验证码';this.style.color='#5f6063'}"
                               value="短信验证码" maxlength="11">
                        <button class="btn btn-B" type="submit" href="javascript:void(0);" id="getSMSPwd1"
                                onclick="sendsms()">点击获取
                        </button>
                        <span class="login_a hide" id="resendSMS" style="color:#CCC">重新获取(60秒)</span>
                        <!--                <a class="login_a hide" style="color:#666" href="#">重新获取</a> -->
                        <div id="smspwd_err" class="reg_error hide" style="display: none;">请输入短信随机码</div>
                    </div>
                </div>
                <!-- 短信随机码登录  end-->

                <!-- 互联网用户登录  -->
                <div id="mail_login_main" class="clearfix hide" style="display: none;">
                    <div class="reg_item clearfix">
                        <ul class="emailist" style="visibility: hidden; z-index: 999;"></ul>
                        <input id="e_name" name="e_name" type="text" autocomplete="off" class="reg_input"
                               style="color:#5f6063"
                               onfocus="if(this.value=='请输入注册时所填写的邮箱'){this.value='';this.style.color='#5f6063'};"
                               onblur="if(this.value==''){this.value='请输入注册时所填写的邮箱';this.style.color='#5f6063'}"
                               value="请输入注册时所填写的邮箱">
                        <div class="reg_error hide" id="email_error" style="display: none;">请输入邮箱</div>
                    </div>
                    <div class="reg_item passwordbox clearfix">
                        <label>请输入注册时所填写的密码</label><input class="reg_input" id="e_pwd" autocomplete="off"
                                                          type="password" value="">
                        <div class="reg_error" id="emailpwd_error" style="display: none;">请输入服务密码</div>
                    </div>
                    <div class="reg_item clearfix hide" id="code_item" style="display: none; opacity: 0;">
                        <input id="inputCode" type="text" name="cubService_name" class="reg_input small"
                               style="color:#5f6063"
                               onfocus="if(this.value=='验证码'){this.value='';this.style.color='#5f6063'};"
                               onblur="if(this.value==''){this.value='验证码';this.style.color='#5f6063'}"
                               value="验证码" maxlength="11">
                        <div class="yzm_main">
                            <img id="captchaImg" class="num-code" width="80" height="40" alt="">
                        </div>
                        <div class="reg_error hide" id="captcha_error" style="display: none;">对应的错误信息提示</div>
                    </div>
                </div>
                <!-- 互联网用户登录  end-->

            </div>


            <!-- 登录框底部 -->
            <div class="btn-bar clearfix" id="login_btn" style="top: 194px;">
                <button id="submit_bt" class="btn" type="submit" onclick="mylogin()">登录</button>
                <!--           <input id="submit_bt_loading" class="login_btn loading hide" type="button" value="登录中..." disabled="disabled"> -->
                <div class="reg_xy random clearfix">
                    <label><input type="checkbox" id="rememberMe" value="0" onclick="rememberMe()">记住用户名</label>
                    <a id="freeReg" class="fr" href="javascript:void(0);" onclick="subReg()">免费注册</a></div>
            </div>
            <div class="tab-login clearfix">

                <!--
                <a href="https://login.10086.cn/html/login/login.html?channelID=12002&backUrl=https%3A%2F%2Fshop.10086.cn%2Fmall_431_431.html%3Fforcelogin%3D1#" class="" id="mail_login_2" style="display: inline;">互联网用户登录</a><a href="https://login.10086.cn/html/login/login.html?channelID=12002&backUrl=https%3A%2F%2Fshop.10086.cn%2Fmall_431_431.html%3Fforcelogin%3D1#" class="hide" id="service_login_2" style="display: none;">服务密码登录</a><a href="https://login.10086.cn/html/login/login.html?channelID=12002&backUrl=https%3A%2F%2Fshop.10086.cn%2Fmall_431_431.html%3Fforcelogin%3D1#" class="hide" id="sms_login_2" style="display: none;">短信随机码登录</a>
                -->
            </div>
        </div>
    </div>


    <!--登录保护-->
    <div id="password-protect" class="ss-add-app-modal-mask"></div>
    <div class="login_protect" id="login_protect_id">
        <a class="protect_close"></a>
        <div class="login_pro_title">登录保护确认</div>
        <div id="sms_tips" class="login_tip" style="width:160px; margin-left:70px"></div>
        <div class="reg_item1 clearfix" style="margin-top:0">
            <div class="reg_item_title">手机号码</div>
            <span id="protect_phone" class="login_a margin-0"></span></div>
        <div class="reg_item1 clearfix">
            <div class="reg_item_title">短信随机码</div>
            <input id="secondSmd" style="width:197px" class="reg_input1" type="text" maxlength="6">
        </div>
        <div class="random1"><a class="login_a" href="javascript:void(0);" onclick="sendsms_protect()"
                                id="getSMSpwd_protect">点击获取</a><span id="resendSMS_protect" class="login_a hide"
                                                                     style="color:#CCC">重新获取(60秒)</span></div>
        <div class="protect_btn">
            <input id="login_protect_auth" class="qr_btn" type="button" value="确  认">
        </div>
    </div>
    <!--登录保护-->

    <!--是否使用新帐号登录-->
    <div class="ss-add-app-modal-mask"></div>
    <div id="login_choose_account" class="login_protect"><a class="protect_close"></a>
        <div class="login_pro_title">登陆账号提醒</div>
        <div class="reg_success_text margin-top-15">已有账号登录,是否使用新账号</div>
        <div class="protect_btn">
            <input class="qr_btn" type="button" value="是" onclick="LoginSubmit('Y')">
            <input style="margin-left:10px; background:#ccc" class="qr_btn" type="button" value="否"
                   onclick="LoginSubmit('N')">
        </div>
    </div>

    <!--登录错误提示-->
    <div id="userlock_Tips" class="login_protect"><a class="protect_close"></a>
        <div class="login_pro_title"></div>
        <div id="userlock" class="margin-top-15 clearfix" style="padding:10px 20px">对不起，您的账户被锁定，24小时候后可重新登录，如有问题请咨询客服。
        </div>
        <div id="login_error" class="reg_success_text"><img src="https://img.seasmall.top/cloud/static/login/images/error_pic.png" alt=""><span id="errorP">7次后您的账户将被锁定。</span>
        </div>
        <div class="protect_btn">
            <input id="userlock_Tips_btn" class="qr_btn" type="button" value="确  认">
        </div>
    </div>
    <!--登录错误提示-->

</div>
<!--发送短信提示-->

<!--主体内容  结束-->
<!--footer  开始-->
<#include "../common/footer.ftlh">
<!--footer  end-->

</body>
</html>

<script type="text/javascript">

    // 用户登录功能
    function mylogin() {
        // 获取返回页面的地址
        var backUrl = $("#backUrl").val();

        // 获取用户号，密码
        var name = $("#p_name").val();
        var pwd = $("#p_pwd").val();
        let field = {};
        field.username = name;
        field.password = md5(pwd);
        $.ajax({
            type: "POST",
            url: "http://localhost:9090/auth/login",
            data: JSON.stringify(field),
            contentType: 'application/json',
            dataType: "json",
            success: function (resp) {
                if (resp.code == 20000) {
                    // 创建cookie
                    // 创建cookie，存入token，设置7天过期
                    $.cookie('token', resp.data.token, {path: '/', domain: window.location.domain});
                    // 做个编码
                    $.cookie('isLogin', true, {path: '/', domain: window.location.domain});
                    $.cookie('nickname', resp.data.nickName, {path: '/', domain: window.location.domain});

                    // 跳转回前端
                    window.location.href = backUrl;
                } else {
                    // $('#phone_error').html(res.msg);
                    // $('#phone_error').show();
                }
            }
        });
    }

</script>