#!/bin/bash
Action="$1"

if [ $UID -ne 0 ]; then
  echo Non root user. Please run as root.
  exit 1
fi
BASE_DIR=$(dirname $0)

BASE_PATH=$(
  cd ${BASE_DIR}
  pwd
)
echo "Bash Path: ${BASE_PATH}"
INIT_PATH=$(dirname "${BASE_PATH}")
echo "INIT_PATH: ${INIT_PATH}"
DATASOPHON_PATH=$(dirname "${INIT_PATH}")
echo "DATASOPHON_PATH: ${DATASOPHON_PATH}"
INIT_BIN_PATH=${INIT_PATH}/bin
echo "INIT_BIN_PATH: ${INIT_BIN_PATH}"
INIT_CONFIG_PATH=${INIT_PATH}/config
echo "INIT_CONFIG_PATH: ${INIT_CONFIG_PATH}"
INIT_SBIN_PATH=${INIT_PATH}/sbin
echo "INIT_SBIN_PATH: ${INIT_SBIN_PATH}"
PACKAGES_PATH=${INIT_PATH}/packages
echo "PACKAGES_PATH: ${PACKAGES_PATH}"
hostAllInfoPath=${INIT_CONFIG_PATH}/init-host-info.properties
echo "hostAllInfoPath: ${hostAllInfoPath}"
hostSingleInfoPath=${INIT_CONFIG_PATH}/init-host-info-add.properties
echo "hostSingleInfoPath: ${hostSingleInfoPath}"
FilePath=${INIT_CONFIG_PATH}/init.properties
echo "FilePath: ${FilePath}"
JDK_PATH=/opt/module
smallTimeOut=300
middleTimeOut=600
longTimeOut=1200

function prop {
  [ -f "$FilePath" ] && grep -P "^\s*[^#]?${1}=.*$" $FilePath | cut -d'=' -f2
}

ntpMasterIP=$(prop "ntp.master.ip")
mysqlIP=$(prop "mysql.ip")
mysqlPort=$(prop "mysql.host.ssh.port")
mysqlHostSshPassword=$(prop "mysql.host.ssh.password")
mysqlPassword=$(prop "mysql.password")
nmapServerIp=$(prop "nmap.server.ip")
nmapServerPort=$(prop "nmap.server.port")
nmapServerPassword=$(prop "nmap.server.password")
initLogDir=$(prop "init.log.dir")
yumRepoIp=$(prop "yum.repo.host.ip")
initAllHostNums=$(prop "init.host.num")
initSingleHostNums=$(prop "init.add.host.num")

echo "yumRepoIp: ${yumRepoIp}"

initALL() {
  function network() {
    local timeout=1

    local target=www.baidu.com

    local ret_code=$(curl -I -s --connect-timeout ${timeout} ${target} -w %{http_code} | tail -n1)

    if [ "x$ret_code" = "x200" ]; then
      return 1
    else
      return 0
    fi

    return 0
  }

  network

  if [ $? -eq 0 ]; then
    echo -e "\033[31m 由于机器没有联网，请自行检查设置正确的机器时间！ \033[0m"
    read -p "Please confirm whether the time displayed by the machine is correct(yes/no):" isCorrect
    if [ "$isCorrect" = "yes" ]; then
      echo "机器时间设置正常，请进行如下环境初始化操作"
    else
      echo "机器时间不正确，请设置正确时间后，再进行 DataSophon 初始化环境操作"
      exit
    fi
  else
    echo "机器可以联网，无需自行检查系统时间是否正常"
  fi
  rm -rf ${INIT_BIN_PATH}/tmp_scp_host_info.txt
  rm -rf ${INIT_BIN_PATH}/1.txt
  rm -rf ${initLogDir}/installAllSuccess_$(date +%Y%m%d).log

  #如果文件不存在
  if [ ! -f "/data/private-yum-library.tar.gz" ]; then
    echo "the initial work needn't use the provided YUM"
  else
    mv /etc/yum.repos.d/backup/* /etc/yum.repos.d
    mkdir -p /etc/yum.repos.d/backup
    mv $(find /etc/yum.repos.d/ -name "*.repo") /etc/yum.repos.d/backup
    bash ${INIT_BIN_PATH}/init-stop-firewall.sh
    bash ${INIT_BIN_PATH}/init-stop-selinux.sh
    bash ${INIT_BIN_PATH}/init-private-yum-library.sh ${yumRepoIp}
  fi
  secretFreeAllLogin
  checkSecretFreeAllLogin

  #close all Firewall
  echo "closeAllFirewall start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllFirewall_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-stop-firewall.sh >>${initLogDir}/closeAllFirewall_$(date +%Y%m%d).log
  echo "closeAllFirewall end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllFirewall_$(date +%Y%m%d).log
  checkCloseAllFirewall

  #close all selinux
  echo "closeAllSelinux start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllSelinux_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-stop-selinux.sh >>${initLogDir}/closeAllSelinux_$(date +%Y%m%d).log
  echo "closeAllSelinux end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllSelinux_$(date +%Y%m%d).log

  #close all Swap
  echo "closeAllSwap start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllSwap_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-close-swap.sh >>${initLogDir}/closeAllSwap_$(date +%Y%m%d).log
  echo "closeAllSwap end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllSwap_$(date +%Y%m%d).log
  checkcloseAllSwap

  #configure all  slave node yum source
  if [ ! -f "/data/private-yum-library.tar.gz" ]; then
    echo "the initial work needn't use the provided YUM"
  else
    pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-yum-hosts-mapping.sh ${yumRepoIp} >>${initLogDir}/modifyYumRepo_$(date +%Y%m%d).log
  fi

  #optimiz system conf
  echo "modifyAllSystemConf start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/modifyAllSystemConf_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-system-conf.sh >>${initLogDir}/modifyAllSystemConf_$(date +%Y%m%d).log
  echo "modifyAllSystemConf end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/modifyAllSystemConf_$(date +%Y%m%d).log

  #Configure all hostname
  setAllHostname
  checkHostName

  #Configure all hosts
  echo "modifyAllhostRelation start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/modifyAllhostRelation_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-allhosts.sh ${hostAllInfoPath} ${initAllHostNums} >>${initLogDir}/modifyAllhostRelation_$(date +%Y%m%d).log
  echo "modifyAllhostRelation end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/modifyAllhostRelation_$(date +%Y%m%d).log

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-sourceSSHHostname.sh
  installServerNmap

  #Configure NTP
  initAllNtpChronyService
  checkNtpChronyService

  #Configurelibxslt_devel
  echo "installAlllibxsltdevel start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAlllibxsltdevel_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-libxslt-devel.sh >>${initLogDir}/installAlllibxsltdevel_$(date +%Y%m%d).log
  echo "installAlllibxsltdevel end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAlllibxsltdevel_$(date +%Y%m%d).log
  checkLibxsltDevel
  #Configure Psmisc
  echo "installAllpsmisc start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAllpsmisc_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-psmisc.sh >>${initLogDir}/installAllpsmisc_$(date +%Y%m%d).log
  echo "installAllpsmisc end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAllpsmisc_$(date +%Y%m%d).log
  checkPsmisc

  echo "installAllPerlJSON start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAllPerlJSON_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-perl-JSON.sh >>${initLogDir}/installAllPerlJSON_$(date +%Y%m%d).log
  echo "installAllPerlJSON end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAllPerlJSON_$(date +%Y%m%d).log

  #Configure Mysql and DataSophon data
  initMysqlDataSophon
  initMysqlDevel

  #Configure Disable transparent-hugepage
  echo "closeAllTransparentHugepage start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllTransparentHugepage_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-close-transparent-hugepage.sh >>${initLogDir}/closeAllTransparentHugepage_$(date +%Y%m%d).log
  echo "closeAllTransparentHugepage start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/closeAllTransparentHugepage_$(date +%Y%m%d).log
  checkTransparentHugepage

  #Configure JDK
  echo "installAllJDK start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAllJDK_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-jdk.sh >>${initLogDir}/installAllJDK_$(date +%Y%m%d).log
  echo "installAllJDK end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installAllJDK_$(date +%Y%m%d).log
  checkJDK

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-java-policy.sh >>${initLogDir}/modifyJavaPolicy_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-tmp_pid.sh

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -t ${smallTimeOut} -i bash ${INIT_BIN_PATH}/init-xdg-utils.sh >>${initLogDir}/installXdg_$(date +%Y%m%d).log
  checkXdg
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -t ${middleTimeOut} -i bash ${INIT_BIN_PATH}/init-redhat-lsb.sh >>${initLogDir}/installRedhatLsb_$(date +%Y%m%d).log
  checkRedhatLsb
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-python2-rpm-macros.sh >>${initLogDir}/installPython2Macros_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-python-srpm-macros.sh >>${initLogDir}/installPythonSrpmMacros_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-python-rpm-macros.sh >>${initLogDir}/installPythonMacros_$(date +%Y%m%d).log

  #pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-init-python-devel.sh >> ${initLogDir}/installPythonDevel_`date +%Y%m%d`.log
  #checkPythonDevel

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-cyrus-sasl.sh >>${initLogDir}/installCyrusSasl_$(date +%Y%m%d).log
  checkCyrusSasl

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-python36-devel.sh >>${initLogDir}/installPython36Devel_$(date +%Y%m%d).log
  checkPython36Devel

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -t ${smallTimeOut} -i bash ${INIT_BIN_PATH}/init-gcc-c++.sh >>${initLogDir}/installGccC++_$(date +%Y%m%d).log
  checkGccC

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -t ${smallTimeOut} -i bash ${INIT_BIN_PATH}/init-Cython.sh >>${initLogDir}/installCython_$(date +%Y%m%d).log
  checkCython

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-six.sh >>${initLogDir}/installSix_$(date +%Y%m%d).log
  checkSix

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-websocket_client.sh >>${initLogDir}/installWebsocketClient_$(date +%Y%m%d).log
  checkWebsocketClient

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-ecdsa.sh >>${initLogDir}/installEcdsa_$(date +%Y%m%d).log
  checkecdsa

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-pytest_runner.sh >>${initLogDir}/installPytestRunner_$(date +%Y%m%d).log
  checkPytestRunner

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-krb5-devel.sh >>${initLogDir}/installKrb5Devel_$(date +%Y%m%d).log
  checkKrb5Devel

  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-ntp_enable.sh >>${initLogDir}/installSingleNtpEnable_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-chmod-dev-null.sh >>${initLogDir}/chmodDevNull_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-cleanBuff-async.sh >>${initLogDir}/cleanBuff_$(date +%Y%m%d).log
  rm -rf ${INIT_BIN_PATH}/tmp_scp_host_info.txt
  rm -rf ${INIT_BIN_PATH}/1.txt
  initsource
  source /etc/profile
  source /root/.bash_profile
  echo "The DataSophon deployment environment of added nodes has been inited successfully . Please proceed to the next step" >>${initLogDir}/installSingleSuccess_$(date +%Y%m%d).log
  cat ${initLogDir}/installSingleSuccess_$(date +%Y%m%d).log
  rm -rf ${initLogDir}/installSingleSuccess_$(date +%Y%m%d).log

}

initsource() {
  source /etc/profile
  source /root/.bash_profile
  echo $(java -version)
}

testFun() {
  pssh -h ${INIT_BIN_PATH}/tmp_scp_host_info.txt -i bash ${INIT_BIN_PATH}/init-java-policy.sh >>${initLogDir}/modifyJavaPolicy_$(date +%Y%m%d).log
}

#免密登录
secretFreeAllLogin() {
  echo "secretFreeAllLogin........................"
  mkdir -p ${initLogDir}
  echo "secretFreeAllLogin start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
  rpm -qa | grep openssh
  if [[ "$?" == "0" ]]; then
    echo "ssh exists" >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
    echo "${INIT_BIN_PATH}"
  else
    bash ${INIT_BIN_PATH}/init-sshpackage.sh >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
  fi
  rpm -qa | grep sshpass
  if [[ "$?" == "0" ]]; then
    echo "sshpass exists" >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
  else
    bash ${INIT_BIN_PATH}/init-sshpass.sh >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
  fi
  rpm -qa | grep expect-5.45
  if [[ "$?" == "0" ]]; then
    echo "expect exists"
  else
    bash ${INIT_BIN_PATH}/init-expect.sh
  fi
  bash ${INIT_BIN_PATH}/init-ssh-gen-key.sh >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
  bash ${INIT_BIN_PATH}/init-ssh-copy-key.sh ${hostAllInfoPath} ${initAllHostNums} ${nmapServerPort} >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
  bash ${INIT_BIN_PATH}/init-ssh-hadoop.sh ${hostAllInfoPath} ${initAllHostNums} >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
  echo "secretFreeAllLogin end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/secretFreeAllLogin_$(date +%Y%m%d).log
}

#检查免密登录
checkSecretFreeAllLogin() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    echo "${pwd}"
    echo "${hostAllInfoPath}"
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'ls' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} free login successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' free login failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' free login failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: All free login links have been init successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查所有的swap是否关闭
checkcloseAllSwap() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'cat /etc/sysctl.conf | grep vm.swappiness=0' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} close swap successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' close swap failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' close swap failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: All closing swap links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查所有的Firewall是否关闭
checkCloseAllFirewall() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'firewall-cmd --state' >${initLogDir}/checkCloseAllFirewall.log
    cat ${initLogDir}/checkCloseAllFirewall.log | grep running
    if [ $? -eq 0 ]; then
      echo "ERROR: '${ip}' close firewall failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' close firewall failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    else
      echo "${ip} close firewall successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    fi
  done
  echo "SUCCESS: All closing firewall links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#设置所有的的hostname
setAllHostname() {
  echo "setAllHostname start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/setAllHostname_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    hostname=$(prop "dataSophon.ssh.port.hostname.${i}")

    echo "root@${ip}:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/setAllHostname_$(date +%Y%m%d).log
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} bash ${INIT_BIN_PATH}/init-hostname.sh ${hostname} </dev/null >>${initLogDir}/setAllHostname_$(date +%Y%m%d).log
  done
  echo "setAllHostname end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/setAllHostname_$(date +%Y%m%d).log
}

#检查设置所有的的hostname
checkHostName() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")                      #ip
    pwd=$(prop "dataSophon.password.${i}")               # password
    port=$(prop "dataSophon.ssh.port.${i}")              # port
    hostname=$(prop "dataSophon.ssh.port.hostname.${i}") # hostname
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'hostname' >${initLogDir}/checkHostName.log
    cat ${initLogDir}/checkHostName.log | grep "${hostname}"
    if [ $? -eq 0 ]; then
      echo "${ip} set hostname successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set hostname failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set hostname failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: set hostname links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#安装nmap
installServerNmap() {
  echo "installServerNmap start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installServerNmap_$(date +%Y%m%d).log
  echo "root@${ip}:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installServerNmap_$(date +%Y%m%d).log
  sshpass -p'${nmapServerPassword}' ssh -P${nmapServerPort} -o StrictHostKeyChecking=no root@${nmapServerIp} bash ${INIT_BIN_PATH}/init-nmap.sh </dev/null >>${initLogDir}/installServerNmap_$(date +%Y%m%d).log
  echo "installServerNmap end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/installServerNmap_$(date +%Y%m%d).log
}

#初始化配置安装Ntp服务
initAllNtpChronyService() {
  echo "initAllNtpChronyService start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initAllNtpChronyService_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    if [ "${ntpMasterIP}" = "${ip}" ]; then
      echo "masterntp"
      echo "root@${ip}:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initAllNtpChronyService_$(date +%Y%m%d).log
      sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} bash ${INIT_BIN_PATH}/init-ntp-chrony-server.sh </dev/null >>${initLogDir}/initAllNtpChronyService_$(date +%Y%m%d).log
    else
      echo "slaventp"
      echo "root@${ip}:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initAllNtpChronyService_$(date +%Y%m%d).log
      sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} bash ${INIT_BIN_PATH}/init-ntp-chrony-slave.sh ${ntpMasterIP} </dev/null >>${initLogDir}/initAllNtpChronyService_$(date +%Y%m%d).log
    fi
  done
  echo "initAllNtpChronyService end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initAllNtpChronyService_$(date +%Y%m%d).log
}

#检查配置安装的Ntp服务
checkNtpChronyService() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'rpm -qa | grep chrony-' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} set ntp successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set ntp failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set ntp failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  ntp Chrony have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查配置安装的libxslt-devel
checkLibxsltDevel() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'rpm -qa | grep libxslt-devel' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} set libxslt devel successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set libxslt devel failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set libxslt devel failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  libxslt devel  have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查配置安装的Psmisc
checkPsmisc() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'rpm -qa | grep psmisc' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} set psmisc successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set psmisc failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set psmisc failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  psmisc  have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#配置安装mysql8以及初始化DataSophon数据库
initMysqlDataSophon() {
  echo "initMysqlDataSophon start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initMysqlDataSophon_$(date +%Y%m%d).log
  echo "${mysqlIP}" >>${initLogDir}/initMysqlDataSophon_$(date +%Y%m%d).log
  sshpass -p'${mysqlHostSshPassword}' ssh -P${mysqlPort} -o StrictHostKeyChecking=no root@${mysqlIP} bash ${INIT_BIN_PATH}/init-mysql-datasophon.sh $mysqlPassword </dev/null >>${initLogDir}/initMysqlDataSophon_$(date +%Y%m%d).log
  echo "initMysqlDataSophon end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initMysqlDataSophon_$(date +%Y%m%d).log
}

#配置安装mysql-devel
initMysqlDevel() {
  rm -rf ${INIT_BIN_PATH}/m.txt
  echo "initMysqlDevel start_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initMysqlDevel_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port

    if [ "${mysqlIP}" = "${ip}" ]; then
      echo "already install mysql"
    else
      echo -e "root@${ip}:${port}" >>${INIT_BIN_PATH}/m.txt
    fi
  done
  echo "root@${ip}:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initMysqlDevel_$(date +%Y%m%d).log
  pssh -h ${INIT_BIN_PATH}/m.txt -t ${smallTimeOut} -i bash ${INIT_BIN_PATH}/init-mysql-devel.sh >>${initLogDir}/initMysqlDevel_$(date +%Y%m%d).log
  #pssh -h ${INIT_BIN_PATH}/m.txt -t ${smallTimeOut} -i bash ${INIT_BIN_PATH}/init-mysql-client.sh >> ${initLogDir}/initMysqlDevel_`date +%Y%m%d`.log
  rm -rf ${INIT_BIN_PATH}/m.txt
  echo "initMysqlDevel end_time:$(date '+%Y%m%d %H:%M:%S')" >>${initLogDir}/initMysqlDevel_$(date +%Y%m%d).log
}

#检查配置配置安装Cython
checkCython() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'pip3 list | grep Cython' >${initLogDir}/checkCython.log
    cat ${initLogDir}/checkCython.log | grep Cython
    if [ $? -eq 0 ]; then
      echo "${ip} set  Cython successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set  Cython failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set  Cython failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  Cython links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}
#检查配置配置安装Six
checkSix() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'pip3 list | grep six' >${initLogDir}/checkSix.log
    cat ${initLogDir}/checkSix.log | grep six
    if [ $? -eq 0 ]; then
      echo "${ip} set  Six successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set  Six failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set  Six failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  Six links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查配置安装WebsocketClient
checkWebsocketClient() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'pip3 list | grep websocket-client' >${initLogDir}/checkWebsocketClient.log
    cat ${initLogDir}/checkWebsocketClient.log | grep websocket-client
    if [ $? -eq 0 ]; then
      echo "${ip} set  websocket-client successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set  websocket-client failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set  websocket-client failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  websocket-client links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#配置禁止透明大页
checkTransparentHugepage() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'cat /sys/kernel/mm/transparent_hugepage/enabled | grep [never]' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} close transparent_hugepage successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' close transparent_hugepage failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' close transparent_hugepage failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: All transparent_hugepage links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查配置安装的jdk环境
checkJDK() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'source /etc/profile ; java -version' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} set jdk successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set jdk failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set jdk failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set JDK links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查配置安装的xdg-utils
checkXdg() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'rpm -qa | grep xdg-utils' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} set xdg-utils successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set xdg-utils failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set xdg-utils failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set xdg-utils links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

#检查配置安装的redhat-lsb
checkRedhatLsb() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'rpm -qa | grep redhat-lsb' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} set redhat-lsb successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set redhat-lsb failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set redhat-lsb failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set redhat-lsb links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

checkecdsa() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'pip3 list | grep ecdsa' >${initLogDir}/checkecdsa.log
    cat ${initLogDir}/checkecdsa.log | grep ecdsa
    if [ $? -eq 0 ]; then
      echo "${ip} set  ecdsa successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set  ecdsa failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set  ecdsa failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  ecdsa links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

checkPytestRunner() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'pip3 list | grep pytest-runner' >${initLogDir}/checkPytestRunner.log
    cat ${initLogDir}/checkPytestRunner.log | grep pytest-runner
    if [ $? -eq 0 ]; then
      echo "${ip} set  pytest-runner successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set  pytest-runner failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set  pytest-runner failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set  pytest-runner links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

checkKrb5Devel() {
  rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
  function prop {
    [ -f "${hostAllInfoPath}" ] && grep -P "^\s*[^#]?${1}=.*$" ${hostAllInfoPath} | cut -d'=' -f2
  }
  for ((i = 1; i <= ${initAllHostNums}; i++)); do
    ip=$(prop "dataSophon.ip.${i}")         #ip
    pwd=$(prop "dataSophon.password.${i}")  # password
    port=$(prop "dataSophon.ssh.port.${i}") # port
    sshpass -p'${pwd}' ssh -P${port} -o StrictHostKeyChecking=no root@${ip} 'rpm -qa | grep krb5-devel' </dev/null
    if [ $? -eq 0 ]; then
      echo "${ip} set krb5-devel successfully" >>${initLogDir}/installAll_$(date +%Y%m%d).log
    else
      echo "ERROR: '${ip}' set krb5-devel failed" >>${initLogDir}/installAll_$(date +%Y%m%d).log
      echo "ERROR: '${ip}' set krb5-devel failed" >>${initLogDir}/installAllError_$(date +%Y%m%d).log
      cat ${initLogDir}/installAllError_$(date +%Y%m%d).log
      rm -rf ${initLogDir}/installAllError_$(date +%Y%m%d).log
      exit
    fi
  done
  echo "SUCCESS: Set krb5-devel links have been inited successfully" >>${initLogDir}/installAllSuccess_$(date +%Y%m%d).log
}

if [ "$Action" = "initAll" ]; then
  initALL
  echo "initALL....................."
fi
if [ "$Action" = "secretFreeAllLogin" ]; then
  secretFreeAllLogin
fi
if [ "$Action" = "checkcloseAllSwap" ]; then
  checkcloseAllSwap
fi

if [ "$Action" = "testFun" ]; then
  testFun
fi
