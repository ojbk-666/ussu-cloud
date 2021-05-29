#!/bin/sh
#进入脚本所在路径
this_path=$(cd `dirname $0`;pwd)
cd $this_path

#注意：
#1.必须有&让其后台执行，否则没有pid生成
#2.一定要明确指定输出文件，否则当服务器开机启动或者定时重启的时候不生成nohup日志
nohup java -Xms128m -Xmx128m -jar ../ussu-modules/ussu-files/target/ussu-files-1.0.jar > $this_path/nohup-files.out &

echo $! > $this_path/run-files.pid

exec -c echo ""
