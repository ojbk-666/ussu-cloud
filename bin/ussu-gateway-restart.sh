#进入脚本所在路径
this_path=$(cd `dirname $0`;pwd)
cd $this_path

sh ussu-gateway-stop.sh

sleep 1

sh ussu-gateway-start.sh
