#!/bin/sh
#脚本所在路径
this_path=$(cd `dirname $0`;pwd)
PID=$(cat $this_path/run-gateway.pid)
kill -9 $PID