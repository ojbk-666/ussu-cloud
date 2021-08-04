function request(url, param, method) {
    return new Promise((resolve, reject) => {
        let token = $.cookie('token');
        let ct = method == 'get' ? 'application/x-www-form-urlencoded' : 'application/json';
        let p = method == 'get' ? param : JSON.stringify(param);
        $.ajax({
            type: method,
            url: "http://localhost:9090/" + url,
            data: p,
            contentType: ct,
            dataType: "json",
            beforeSend: function(xhr) {
                xhr.setRequestHeader("token", token);
            },
            success: function (res) {
                if (res.code == 20000) {
                    resolve(res);
                } else {
                    if (res.code == 50014) {
                        // 需要重新登录
                        let backUrl = window.location.href;
                        window.location = window.location.protocol + "//" + window.location.host + '/portal/login?backUrl=' + backUrl;
                    }
                    reject(res);
                }
            }
        });
    })
}