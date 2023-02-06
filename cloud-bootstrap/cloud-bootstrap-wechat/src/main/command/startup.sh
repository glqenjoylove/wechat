#!/usr/bin/env bash

workdir=`pwd`
dirname $0|grep "^/" >/dev/null
if [ $? -eq 0 ];then
   workdir=`dirname $0`
else
    dirname $0|grep "^\." >/dev/null
    retval=$?
    if [ $retval -eq 0 ];then
        workdir=`dirname $0|sed "s#^.#$workdir#"`
    else
        workdir=`dirname $0|sed "s#^#$workdir/#"`
    fi
fi

cd $workdir


# config log dir
logdir=$workdir/logs
if [ ! -d "$logdir" ]; then
	mkdir "$logdir"
fi

if [ -f "$workdir/config-env.sh" ]; then
    source $workdir/config-env.sh
fi

SERVICE_NAME="`echo $workdir | grep -o -E "[^/]+$"`"

JVM_OPTS="-server -Xms128m -Xmx2048m -Xloggc:$logdir/$SERVICE_NAME-gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGC -XX:+HeapDumpOnOutOfMemoryError -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
DEBUG_OPTS="-Dlog.dir=$logdir -Duser.timezone=Asia/Shanghai -Dnacos.logging.path=$workdir/nacos -DJM.SNAPSHOT.PATH=$workdir -DJM.LOG.PATH=$workdir/nacos -Dcom.alibaba.nacos.naming.cache.dir=$workdir/nacos"
USER_OPTS="$USER_OPTS "

DEP_LIB="$workdir/libx"
START_LIB="$workdir/lib"
DEP_LIB_JARS=`ls $DEP_LIB|grep .jar|awk '{print "'$DEP_LIB'/"$0}'|tr "\n" ":"`
START_LIB_JARS=`ls $START_LIB|grep ".jar$"|awk '{print "'$START_LIB'/"$0}'|tr "\n" ":"`
SERVICE_NAME="`echo $workdir | grep -o -E "[^/]+$"`"

CONFIG_DIR="$workdir/config"
echo $DEP_LIB_JARS$START_LIB_JARS$CONFIG_DIR

java $JVM_OPTS $DEBUG_OPTS $USER_OPTS -cp $DEP_LIB_JARS$START_LIB_JARS$CONFIG_DIR com.mywg.cloud.wechat.WeChatApplication
echo $! > $logdir/$SERVICE_NAME-pid.txt
