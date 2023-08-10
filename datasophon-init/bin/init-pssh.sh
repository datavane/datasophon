#!/bin/bash


if [ $UID -ne 0 ]; then
  echo Non root user. Please run as root.
  exit 1
fi
if [ -L $0 ]
then
    BASE_DIR=`dirname $(readlink $0)`
else
    BASE_DIR=`dirname $0`
fi
BASE_PATH=$(cd ${BASE_DIR}; pwd)
INIT_PATH=$(dirname "${BASE_PATH}")
echo "INIT_PATH: ${INIT_PATH}"
INIT_BIN_PATH=${INIT_PATH}/bin
echo "INIT_BIN_PATH: ${INIT_BIN_PATH}"
INIT_SBIN_PATH=${INIT_PATH}/sbin
echo "INIT_SBIN_PATH: ${INIT_SBIN_PATH}"
PACKAGES_PATH=${INIT_PATH}/packages
echo "PACKAGES_PATH: ${PACKAGES_PATH}"
PSSH_FOLDER_NAME=pssh

PSSH_TAR_NAME=pssh.tar.gz
rpm -qa | grep pssh
if [ "$?" == "0" ]; then
echo "pssh exists"
else
   yum -y install pssh
   rpm -qa | grep pssh
   if [ "$?" == "0" ]; then
   echo "pssh install successfully"
   fi
fi

