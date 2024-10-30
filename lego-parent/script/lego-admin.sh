#!/bin/bash

COMMAND="$1"

if [[ "$COMMAND" != "start" ]] && [[ "$COMMAND" != "stop" ]] && [[ "$COMMAND" != "restart" ]]; then
	echo "Usage: $0 start | stop | restart"
	exit 0
fi

APP_BASE_PATH=$(cd `dirname $0`; pwd)

function start()
{
    nohup java -jar -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs\oom_dump.log -Xms512m -Xmx512m -Djava.io.tmpdir=tmpdir ${project.build.finalName}.jar > output.log 2>&1 &
    echo "--------项目启动成功--------"
    echo "--------欢迎使用LegoAdmin--------"
}

function stop()
{
    P_ID=`ps -ef | grep -w ${project.build.finalName}.jar | grep -v "grep" | awk '{print $2}'`
    kill $P_ID
    echo "--------项目已关--------"
    echo ""
}

if [[ "$COMMAND" == "start" ]]; then
	start
elif [[ "$COMMAND" == "stop" ]]; then
    stop
else
    stop
    start
fi
