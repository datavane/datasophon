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
startStop=$1

start(){
	echo "ranger admin start"
	ranger-admin start
	if [ $? -eq 0 ]
    then
		echo "ranger admin start success"
	else
		echo "ranger admin start failed"
		exit 1
	fi
}
stop(){
	echo "ranger admin stop"
	ranger-admin stop
	if [ $? -eq 0 ]
    then
		echo "ranger admin stop success"
	else
		echo "ranger admin stop failed"
		exit 1
	fi
}
status(){
  echo "ranger admin status"
  pid=`jps | grep -iw EmbeddedServer | grep -v grep | awk '{print $1}'`
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
	echo "ranger admin restart"
	ranger-admin restart
	if [ $? -eq 0 ]
    then
		echo "ranger admin restart success"
	else
		echo "ranger admin restart failed"
		exit 1
	fi
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


echo "End $startStop ranger"