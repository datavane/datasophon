#!/usr/bin/env bash
current_path=$(cd `dirname $0`;pwd)
command=$1
shift
option=$1

function start() {
   process_name=$1
   status $process_name
   if [ $? -eq 0 ]
   then
    echo "ERROR: $process_name is running"
    exit 1
   fi
   $current_path/bin/dolphinscheduler-daemon.sh start $process_name
}

function stop() {
   process_name=$1
   status $process_name
   if [ $? -eq 1 ]
   then
    echo "ERROR: $process_name is not running"
    exit 1
   fi
   $current_path/bin/dolphinscheduler-daemon.sh stop $process_name
}

function status() {
   process_name=$1
   status=` $current_path/bin/dolphinscheduler-daemon.sh status $process_name | grep -v Begin | grep -v End | cut -d " " -f5`
   echo "INFO: current status is $status"
   if [[ $status="STOP"  ]]
   then
      return 1
   fi
   if [[ $status="RUNNING"  ]]
   then
      return 0
   fi

}

function restart() {
    stop $1
    start $1
}



case $command in
start)
  start $option
  ;;
stop)
  stop $option
  ;;
status)
  status $option
  ;;
restart)
  restart $option
  ;;
esac