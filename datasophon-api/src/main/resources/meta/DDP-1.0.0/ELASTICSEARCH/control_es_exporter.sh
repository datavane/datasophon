#!/bin/bash
#
#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

usage="Usage: start.sh (start|stop|restart) <command> "

# if no args specified, show usage
if [ $# -le 1 ]; then
  echo $usage
  exit 1
fi
startStop=$1
shift
command=$1
SH_DIR=`dirname $0`
export LOG_DIR=$SH_DIR/logs
export PID_DIR=$SH_DIR/pid

export HOSTNAME=`hostname`

log=$LOG_DIR/$command-$HOSTNAME.out
pid=$PID_DIR/$command.pid


if [ ! -d "$LOG_DIR" ]; then
  mkdir $LOG_DIR
fi

start(){
	[ -w "$PID_DIR" ] ||  mkdir -p "$PID_DIR"
  if [ -f $pid ]; then
    if kill -0 `cat $pid` > /dev/null 2>&1; then
      echo $command running as process `cat $pid`.  Stop it first.
      exit 1
    fi
  fi
  echo starting $command, logging to $log
  exec_command="$SH_DIR/elasticsearch_exporter --es.all --es.indices --es.cluster_settings --es.indices_settings --es.shards --es.snapshots --es.timeout=10s --web.listen-address=":9114" --web.telemetry-path="/metrics" --es.uri http://$HOSTNAME:9200"
  echo "nohup $exec_command > $log 2>&1 &"
  nohup $exec_command > $log 2>&1 &
  echo $! > $pid

}
stop(){
	if [ -f $pid ]; then
        TARGET_PID=`cat $pid`
        if kill -0 $TARGET_PID > /dev/null 2>&1; then
          echo stopping $command
          kill $TARGET_PID
          sleep 3s
          if kill -0 $TARGET_PID > /dev/null 2>&1; then
            echo "$command did not stop gracefully after 3 seconds: killing with kill -9"
            kill -9 $TARGET_PID
          fi
        else
          echo no $command to stop
        fi
        rm -f $pid
      else
        echo no $command to stop
      fi
}
status(){
  if [ -f $pid ]; then
    ARGET_PID=`cat $pid`
    kill -0 $ARGET_PID
    if [ $? -eq 0 ]
    then
      echo "$command is  running "
    else
      echo "$command  is not running"
      exit 1
    fi
  else
    echo "$command  pid file is not exists"
    exit 1
	fi
}
restart(){
	stop
	sleep 10
	start
}
case $startStop in
  (start)
    start
    ;;
  (stop)
    stop
      ;;
  (status)
	  status
	;;
  (restart)
	  restart
      ;;
  (*)
    echo $usage
    exit 1
    ;;
esac


echo "End $startStop $command."