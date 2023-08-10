#!/bin/bash

# example: sh init-ssh-gen-key.sh

filePath=$1
initAllHostNums="$2"
if [ -L $0 ]
then
    BASE_DIR=`dirname $(readlink $0)`
else
    BASE_DIR=`dirname $0`
fi
BASE_PATH=$(cd ${BASE_DIR}; pwd)
INIT_PATH=$(dirname "${BASE_PATH}")
echo "INIT_PATH: ${INIT_PATH}"
DATASOPHON_PATH=$(dirname "${INIT_PATH}")
echo "DATASOPHON_PATH: ${DATASOPHON_PATH}"
INIT_BIN_PATH=${INIT_PATH}/bin
echo "INIT_BIN_PATH: ${INIT_BIN_PATH}"
INIT_SBIN_PATH=${INIT_PATH}/sbin
echo "INIT_SBIN_PATH: ${INIT_SBIN_PATH}"
PACKAGES_PATH=${INIT_PATH}/packages
echo "PACKAGES_PATH: ${PACKAGES_PATH}"

 sshHadoopDir=/home/hadoop/.ssh/
 rm -rf ${INIT_BIN_PATH}/tmp_scp_host_info.txt
 bash ${INIT_BIN_PATH}/init-hostIp-txt.sh ${filePath} ${initAllHostNums}
 bash ${INIT_BIN_PATH}/init-pssh.sh
 pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i "mkdir -p '${DATASOPHON_PATH}'"
 pscp.pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -r ${INIT_PATH} ${DATASOPHON_PATH}
 pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-add-hadoop-user.sh
 pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i "mkdir -p /home/hadoop/"
 pscp.pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -r /root/.ssh /home/hadoop/
 pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i  "chown hadoop:hadoop -R '${sshHadoopDir}'"
echo "repair  init-ssh-hadoop.sh finished."
echo "Done."




