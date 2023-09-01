#!/bin/bash

num1="$1"
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
INIT_PATH=$(dirname "${BASE_PATH}")
echo "INIT_PATH: ${INIT_PATH}"
INIT_BIN_PATH=${INIT_PATH}/bin
echo "INIT_BIN_PATH: ${INIT_BIN_PATH}"
INIT_SBIN_PATH=${INIT_PATH}/sbin
echo "INIT_SBIN_PATH: ${INIT_SBIN_PATH}"
PACKAGES_PATH=${INIT_PATH}/packages
echo "PACKAGES_PATH: ${PACKAGES_PATH}"
INIT_SQL_PATH=${INIT_PATH}/sql
echo "INIT_SQL_PATH: ${INIT_SQL_PATH}"

mariadb_rpm=$(rpm -qa | grep mariadb)
if [[ "$?" == "0" ]]; then
  echo "exist mariadb"
  rpm -qa | grep mariadb | xargs rpm -e --nodeps
fi
mysql_rpm=$(rpm -qa | grep mysql)
if [[ "$?" == "0" ]]; then
  echo "exist mysql"
  echo "开始卸载已存在的 mysql..............."
  systemctl stop mysqld
  rpm -qa | grep mysql | xargs rpm -e
  rm -rf /var/lib/mysql
  rm -rf /usr/sbin/mysqld
  rm -rf /usr/local/mysql
  rm -rf /etc/my.cnf
  rm -rf /var/log/mysqld.log
  rm -rf /var/log/mysql.log
fi

bash ${INIT_BIN_PATH}/init-mysql-8.sh ${num1}

if [ $(systemctl status mysqld | grep running | wc -l) -eq 1 ]; then

  mysql -uroot -p''$num1'' <<EOF
CREATE USER 'datasophon'@'%' IDENTIFIED BY 'datasophon';
ALTER USER 'datasophon'@'%' IDENTIFIED BY 'datasophon' PASSWORD EXPIRE NEVER;
ALTER USER 'datasophon'@'%' IDENTIFIED WITH mysql_native_password BY 'datasophon';
GRANT ALL PRIVILEGES ON *.* TO 'datasophon'@'%';
FLUSH PRIVILEGES;
CREATE DATABASE IF NOT EXISTS datasophon DEFAULT CHARACTER SET utf8;
EOF
  echo " init user datasophon finished."
  echo " init database datasophon finished."

  mysql -udatasophon -p'datasophon' <<EOF
source ${INIT_SQL_PATH}/V1.1.0__DDL.sql;
source ${INIT_SQL_PATH}/V1.1.0__DML.sql;
EOF
  echo " init  datasophon data finished."

else
  systemctl start mysqld
  systemctl enable mysqld

fi
echo " init db_datasophon finished."
echo "Done."
