#!/bin/bash

if [ $UID -ne 0 ]; then
  echo Non root user. Please run as root.
  exit 1
fi
if [ -L $0 ]; then
  BASE_DIR=$(dirname $(readlink $0))
else
  BASE_DIR=$(dirname $0)
fi
BASE_PATH=$(
  cd ${BASE_DIR}
  pwd
)
REPAIR_PATH=$(dirname "${BASE_PATH}")
echo "REPAIR_PATH: ${REPAIR_PATH}"

mariadb_rpm=$(rpm -qa | grep mariadb)
if [[ "$?" == "0" ]]; then
  echo "exist mariadb"
#rpm -qa | grep mariadb | xargs rpm -e --nodeps
else
  echo "not exist mariadb"
fi
mysql_rpm=$(rpm -qa | grep mysql)
if [[ "$?" == "0" ]]; then
  echo "exist mysql"
else
  echo "not exist mysql"
fi

echo "repair_init db_udp finished."
echo "Done."
