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
if [[ "$command" = "namenode"  ||  "$command" = "datanode" ]]; then
   cmd=/opt/datasophon/hadoop-3.1.1/bin/hdfs
elif [[ "$command" = "resourcemanager" || "$command" = "nodemanager" ]]; then
   cmd=/opt/datasophon/hadoop-3.1.1/bin/yarn
else
  echo "Error: No command named \'$command' was found."
  exit 1
fi

start(){
	$cmd --daemon start $command
	if [ $? -eq 0 ]
    then
		echo "$command start success"
	else
		echo "$command start failed"
		exit 1
	fi
}
stop(){
	$cmd --daemon stop $command
	if [ $? -eq 0 ]
    then
		echo "$command stop success"
	else
		echo "$command stop failed"
		exit 1
	fi
}
status(){
  echo "start check $command status"
	pid=`jps | grep -iw $command | grep -v grep | awk '{print $1}'`
	echo "pid is : $pid"
	kill -0 $pid
	if [ $? -eq 0 ]
	then
		echo "$command is  running "
	else
		echo "$command  is not running"
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