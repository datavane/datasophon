#!/bin/bash
usage="Usage: start.sh (start|stop|restart) <command> "

# if no args specified, show usage
if [ $# -le 1 ]; then
  echo $usage
  exit 1
fi
startStop=$1
shift
command=$1



status(){
  echo "start check $command status"
	if [[ "$command" = "jobmanager" ]];then
		pid=`jps | grep -iw StandaloneSessionClusterEntrypoint | grep -v grep | awk '{print $1}'`
	elif [[ "$command" = "taskmanager" ]]; then
		pid=`jps | grep -iw TaskManagerRunner | grep -v grep | awk '{print $1}'`
	else
		pid=`jps | grep -iw $command | grep -v grep | awk '{print $1}'`
	fi
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

case $startStop in
  (status)
	  status
	;;
  (*)
    echo $usage
    exit 1
    ;;
esac


echo "End $startStop $command."