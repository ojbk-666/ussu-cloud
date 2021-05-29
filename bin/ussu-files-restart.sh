#进入脚本所在路径
this_path=$(cd `dirname $0`;pwd)
cd $this_path

sh ussu-files-stop.sh

sleep 1

sh ussu-files-start.sh
