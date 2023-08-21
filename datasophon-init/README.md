<div align="center">
         <a href="https://github.com/datasophon/datasophon" target="_blank" rel="noopener noreferrer">
           <img src="website/static/img/logo.png" width="20%" height="20%" alt="DataSophon Logo" />
        </a>
 <h1>DataSophon</h1>
 <h3>帮助您更容易地管理和监控集群</h3>
</div>

<p align="center">
  <img src="https://img.shields.io/github/release/datasophon/datasophon.svg">
  <img src="https://img.shields.io/github/stars/datasophon/datasophon">
  <img src="https://img.shields.io/github/forks/datasophon/datasophon">
  <a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg"></a>
  <p align="center">
    <a href="https://datasophon.github.io/datasophon-website/">官网</a> |
    <a href="https://github.com/datasophon/datasophon/blob/main/README.md">English</a>
  </p>
</p>
<h3>觉得不错的话，star fork下，欢迎社区开发者共建DataSophon</h3>  

#  dataSophon-init使用说明  

前言：当前版本是根据centos8.5和openEuler-22.03进行开发适配的，其它类型和版本的操作系统目前没有进行详细的测试验证可能存在兼容性问题，需要对shell脚本和本地YUM离线安装包进行适配  
1、将datasophon-init整个目录的内容放到规划的集群主节点的/data目录下(mkdir /data)；   
2、将packages.tar.gz 离线依赖库移到主节点的 /data/datasophon-init下并解压；下载链接：https://pan.baidu.com/s/1iqudVwDgg2x_OO35VLkkSg 提取码：6zrz   
3、在未连接互联网的情况下预备安装datasophon的服务器已经配置好离线yum源，能够通过yum命令安装依赖包（此步骤为必须，因为初始化脚本中使用了"yum -y install  xxxx"的方式安装依赖，如果没有yum源会导致安装失败 ）；     
离线YUM源配置方法（Ps:能连公网的用户就不用配置离线yum源了，直接配置为公共的源，该步骤都是在主节点上操作）：   
    详情可查看配置脚本：init-private-yum-library-${initOS}.sh  
    ①首先创建目录 'mkdir -p /data/private-yum-library_temp'然后将和操作系统匹配的操作系统iso文件移动到/data/目录下；   
    ②在/data目录下创建private-yum-library目录:'mkdir -p /data/private-yum-library';  
    ③执行挂载操作系统命令：mount -o loop /data/openEuler-22.03-LTS-SP2-everything-x86_64-dvd.iso /data/private-yum-library_temp，这种挂载重启服务器之后会失效，所以我们挂载完成后将ios内的文件全部拷贝出来做离线yum源用;     
    ④在private-yum-library目录下创建两个子目录 repo 和 epel;     
    ⑤将private-yum-library_temp目录下的文件全部拷贝到/data/private-yum-library/repo目录下：cp -r /data/private-yum-library_temp/* /data/private-yum-library/repo,另外不同操作系统挂载后所产生的目录不同，比如centos8的repo有两个BaseOS和AppStream;    
    ⑥将原本的yum源的配置文件进行备份 ：mkdir -p /etc/yum.repos.d/bak && mv /etc/yum.repos.d/*.repo /etc/yum.repos.d/bak;    
    ⑦剩下的工作就交给脚本工具自己去处理了，epel目录是为了给需要自行配置离线eprl源的用户准备的，如果能配置epel建议进行配置，因为很多操作系统额外的很多工具包都在对应的epel中;    

4、集群NTP时钟默认使用Chrony,如使用其它的方式请自行安装配置，并将sbin/init.sh 脚本中的 initALL() 方法中的‘initAllNtpChronyService’  和 ‘checkNtpChronyService’方法注释掉避免重复安装；   
5、脚本的初始化需要用到python环境，主要用到pssh进行集群间的命令执行控制，目前packages目录中pssh.tar.gz中已经内置了三个安装包:   
    pssh-2.3.1-5.el7.noarch.rpm 需要python2.7的支持;  
    pssh-2.3.1-29.el8.noarch.rpm 需要python3.6的支持;     
    pssh-2.3.4-1.el9.noarch.rpm 需要python3.9的支持;  
    Centos7和8中的repo中已经存在对应的rpm格式的pssh包了,目前openEuler22.03中还没有pssh相关的安装包,
    其它版本pssh的rpm包下载工具地址：http://rpmfind.net/linux/rpm2html/search.php?query=pssh，可以根据操作系统和python版本进行适配
6、服务器操作系统的iso文件建议使用everything版本的，这样包含的依赖包更全一点；   

#  dataSophon-init目录结构如下：    
[root@localhost datasophon-init]# ls -l  
总用量 8    
drwxr-xr-x 2 root root 4096 8月  10 17:09 bin     
drwxr-xr-x 2 root root   99 7月  28 16:10 config  
drwxr-xr-x 8 root root 4096 8月   9 13:47 packages    
drwxr-xr-x 2 root root  189 7月  27 11:35 remove  
drwxr-xr-x 3 root root   50 8月  10 20:02 sbin    
drwxr-xr-x 2 root root   70 8月   7 10:54 sql     


## 上面的各个目录解释如下：  

* bin：datasophon 服务器集群环境配置初始化程序脚本(单个模块初始化脚本所在目录，无需手动管理);
* config：datasophon 服务器集群环境一键初始化所需配置文件目录，需要用户手动修改;
* packages：初始化过程需要的依赖安装包存放目录;
* remove：卸载环境脚本;
* sbin：一键初始化环境脚本所在目录;
* sql：datasophon数据库初始化sql脚本所在目录;


### 在 config 目录下面有三个配置文件：
* init.properties：主要配置私有化 yum 源安装节点信息、namp 安装节点信息、mysql 数据库安装节点信息、修复机器总数、服务器操作系统类型、以及修复模块日志存放位置。用户根据需要自行修改相关配置项；
* init-host-info.properties：节点全量修复，需要配置此文件，具体配置所有节点内网 Ip、密码、端口号以及主机名；
* init-host-info-add.properties：集群新增节点时，需要配置此文件，具体配置新增节点内网 Ip、密码、端口号以及主机名；

### init.properties 说明

* yum.repo.need：填写是否需要部署私有化yum源（true:需要/false:不需要，如服务器不能连接外网必须部署私有化yum源）；
* yum.repo.host.ip：填写即将部署私有化yum源的节点的内网IP（即执行init脚本的节点IP，推荐在主节点上进行）；
* namp.server.ip=：填写未来要部署的datasophon管理端的节点内网IP；
* namp.server.port：填写未来要部署的datasophon管理端节点的SSH端口号，默认22；
* namp.server.password：填写未来要部署的datasophon管理端节点的密码，这个密码最好不要有特殊字符比如‘,’、‘$’等；
* ntp.master.ip： 填写未来要部署ntp时钟同步的主节点服务器IP（推荐主节点）；
* mysql.ip： 填写未来要部署mysql数据库的节点服务器IP（推荐主节点）；
* mysql.host.ssh.port： 填写未来要部署mysql数据库的节点服务器SSH端口，默认为22；
* mysql.host.ssh.password： 填写未来要部署mysql数据库的节点服务器的密码；
* mysql.password： 填写未来要部署mysql数据库的root密码，这个密码不要有特殊字符比如‘,’、‘$’等，可在初始化完成之后再自行设置高复杂度密码；
* init.host.num： 填写未来要初始化的服务器数量；
* init.add.host.num： 填写未来要初始化的新增服务器节点数量，全量初始化时无需修改；
* init.log.dir  ：设置初始化服务器环境时日志存放目录；
* init.os ：填写服务器操作系统类型openEuler/centos8/centos7......；

### init-host-info.properties 说明

* dataSophon.ip.i=172.31.51.194  #预备安装dataSophon集群的节点内网IP（i表示1-n的取值，n为集群节点数量）；
* dataSophon.password.i=xxxxx   #预备安装dataSophon集群的节点的登录密码；
* dataSophon.ssh.port.i=22      #预备安装dataSophon集群的节点的SSH端口默认22；
* dataSophon.ssh.port.hostname.i=dataSophon01   #预备安装dataSophon集群的节点的主机名；


## 全量初始化流程

![img](website/static/img/Initialization-process.png)

### 执行初始化脚本

完成上述步骤后，执行如下命令即可开始一键初始化任务。

cd /data/datasophon-init/sbin    
bash init.sh initAll （等待程序执行完毕，中间需要有一次确认服务器时间的确认项需要选择）   
source /etc/profile  

当执行完 bash init.sh initAll 之后，会看到有下面输出很多的日志，因为需要配置本地离线yum源以及安装mysql8、jdk等整个过程需要一定的时间，可以查看log目录下的安装日志  
其中mysql初始化的数据库默认为datasophon，初始化过程中会自动创建用户"datasophon"密码为"datasophon"     

#### 当前初始化模块支持的操作系统版本为：CentOS-8.5.2111-x86_64、openEuler-22.03
####当前初始化模块支持的mysql为：mysql-community-8.0.28
####自动安装的JDK为：jdk-8u333



