<script type="text/javascript">
    (function () {
        function screenResize() {
            var ScreenWidth = window.screen.width;
            var client_Width = document.body.clientWidth;
            var html_ele = document.getElementsByTagName("html");
            var sw = '';
            if (ScreenWidth < 1200 || client_Width < 1200) {
                sw = 'w1k';
            }
            html_ele[0].className = sw;
            if (window.$ && $.pub) {
                $.pub('wkresize', sw);
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
            window.attachEvent('onorientationchange' in window ? 'onorientationchange' : 'onresize', function () {
                clearTimeout(tid);
                tid = setTimeout(screenResize, 300);
            });
        }
    })();
</script>