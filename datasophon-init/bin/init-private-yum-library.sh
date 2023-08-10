#!/bin/bash
ip="$1"
# example: sh init-xxxxxxxx.sh
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

echo "Depending on the performance of your machine, it may take between 15-20 minutes to initialize the private YUM source. Please do not log out."
#如果文件不存在
 if [ ! -f "/data/private-yum-library" ];then
   echo "start tar -zxvf /data/private-yum-library.tar.gz......."
   tar -zxvf /data/private-yum-library.tar.gz -C /data/
fi
cat > /etc/yum.repos.d/dataSophon.repo << EOF
[dataSophon-base]
name=dataSophon-base
baseurl=file:///data/private-yum-library/centos/8/x86_64/BaseOS/
gpgcheck=0
enable=1

[dataSophon-AppStream]
name=dataSophon-AppStream
baseurl=file:///data/private-yum-library/centos/8/x86_64/AppStream/
gpgcheck=0
enabled=1

[dataSophon-epel]
name=dataSophon-epel
baseurl=file:///data/private-yum-library/epel/8/x86_64/
enabled=1
gpgcheck=0
EOF
yum clean all
yum makecache
echo "init-private-yum-library.sh finished."
echo "Done."


cat /etc/httpd/conf/httpd.conf | grep 'Listen 8000'
if [ $? -eq 0 ]; then
    echo "httpd port modified successfully" >> ${initLogDir}/installSingle_`date +%Y%m%d`.log
    systemctl stop httpd
    systemctl start httpd

else
echo "init httpd begin."
yum install httpd
cat /etc/httpd/conf/httpd.conf | grep 'Listen 8000'
sed -i 's/Listen 80/Listen 8000/g' /etc/httpd/conf/httpd.conf
systemctl start httpd
systemctl enable httpd
echo "init httpd finished."
fi
ln -s /data/private-yum-library/centos/ /var/www/html/
ln -s /data/private-yum-library/epel/ /var/www/html/
sed -i '/#modify yum mapping hosts start/,/#modify yum mapping hosts end/d' /etc/hosts
modifyYumHosts(){
echo "#modify yum mapping hosts start" >> /etc/hosts
echo "${ip} yum.dataSophon.cn" >> /etc/hosts
echo "#modify yum mapping hosts end" >> /etc/hosts
source /etc/profile
source /root/.bash_profile
}
modifyYumHosts

rm /etc/yum.repos.d/dataSophon.repo

cat > /etc/yum.repos.d/dataSophon.repo << EOF
[dataSophon-base]
name=dataSophon-base
baseurl=http://yum.dataSophon.cn:8000/centos/8/x86_64/BaseOS/
gpgcheck=0
enable=1

[dataSophon-AppStream]
name=dataSophon-AppStream
baseurl=http://yum.dataSophon.cn:8000/centos/8/x86_64/AppStream/
gpgcheck=0
enabled=1

[dataSophon-epel]
name=dataSophon-epel
baseurl=http://yum.dataSophon.cn:8000/epel/8/x86_64/
enabled=1
gpgcheck=0
EOF
yum clean all
yum makecache
echo "init-private-yum-library.sh finished."
echo "Done."

