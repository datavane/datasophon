export HADOOP_OS_TYPE=${r"${HADOOP_OS_TYPE:-$(uname -s)}"}
case ${r"${HADOOP_OS_TYPE}"} in
  Darwin*)
    export HADOOP_OPTS="${r"${HADOOP_OPTS}"} -Djava.security.krb5.realm= "
    export HADOOP_OPTS="${r"${HADOOP_OPTS}"} -Djava.security.krb5.kdc= "
    export HADOOP_OPTS="${r"${HADOOP_OPTS}"} -Djava.security.krb5.conf= "
  ;;
esac
export HADOOP_PID_DIR=/opt/datasophon/hadoop-3.3.3/pid
export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root

<#list itemList as item>
export ${item.name}=${item.value}
</#list>
export HADOOP_LOG_DIR=${hadoopLogDir}

SH_PATH=${r"$(cd `dirname $0`; pwd)"}

export HDFS_NAMENODE_OPTS="$HDFS_NAMENODE_OPTS -javaagent:/opt/datasophon/hadoop-3.3.3/jmx/jmx_prometheus_javaagent-0.16.1.jar=27001:/opt/datasophon/hadoop-3.3.3/jmx/prometheus_config.yml"

export HDFS_DATANODE_OPTS="$HDFS_DATANODE_OPTS -javaagent:/opt/datasophon/hadoop-3.3.3/jmx/jmx_prometheus_javaagent-0.16.1.jar=27002:/opt/datasophon/hadoop-3.3.3/jmx/prometheus_config.yml"

export HDFS_JOURNALNODE_OPTS="$HDFS_JOURNALNODE_OPTS -javaagent:/opt/datasophon/hadoop-3.3.3/jmx/jmx_prometheus_javaagent-0.16.1.jar=27003:/opt/datasophon/hadoop-3.3.3/jmx/prometheus_config.yml"

export HDFS_ZKFC_OPTS="$HDFS_ZKFC_OPTS -javaagent:/opt/datasophon/hadoop-3.3.3/jmx/jmx_prometheus_javaagent-0.16.1.jar=27004:/opt/datasophon/hadoop-3.3.3/jmx/prometheus_config.yml"


