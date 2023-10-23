#!/usr/bin/env bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#
# - JAVA_HOME               Java runtime to use. By default use "java" from PATH.
#
#
# - KYUUBI_CONF_DIR         Directory containing the Kyuubi configurations to use.
#                           (Default: $KYUUBI_HOME/conf)
# - KYUUBI_LOG_DIR          Directory for Kyuubi server-side logs.
#                           (Default: $KYUUBI_HOME/logs)
# - KYUUBI_PID_DIR          Directory stores the Kyuubi instance pid file.
#                           (Default: $KYUUBI_HOME/pid)
# - KYUUBI_MAX_LOG_FILES    Maximum number of Kyuubi server logs can rotate to.
#                           (Default: 5)
# - KYUUBI_JAVA_OPTS        JVM options for the Kyuubi server itself in the form "-Dx=y".
#                           (Default: none).
# - KYUUBI_CTL_JAVA_OPTS    JVM options for the Kyuubi ctl itself in the form "-Dx=y".
#                           (Default: none).
# - KYUUBI_BEELINE_OPTS     JVM options for the Kyuubi BeeLine in the form "-Dx=Y".
#                           (Default: none)
# - KYUUBI_NICENESS         The scheduling priority for Kyuubi server.
#                           (Default: 0)
# - KYUUBI_WORK_DIR_ROOT    Root directory for launching sql engine applications.
#                           (Default: $KYUUBI_HOME/work)
# - HADOOP_CONF_DIR         Directory containing the Hadoop / YARN configuration to use.
# - YARN_CONF_DIR           Directory containing the YARN configuration to use.
#
# - SPARK_HOME              Spark distribution which you would like to use in Kyuubi.
# - SPARK_CONF_DIR          Optional directory where the Spark configuration lives.
#                           (Default: $SPARK_HOME/conf)
# - FLINK_HOME              Flink distribution which you would like to use in Kyuubi.
# - FLINK_CONF_DIR          Optional directory where the Flink configuration lives.
#                           (Default: $FLINK_HOME/conf)
# - FLINK_HADOOP_CLASSPATH  Required Hadoop jars when you use the Kyuubi Flink engine.
# - HIVE_HOME               Hive distribution which you would like to use in Kyuubi.
# - HIVE_CONF_DIR           Optional directory where the Hive configuration lives.
#                           (Default: $HIVE_HOME/conf)
# - HIVE_HADOOP_CLASSPATH   Required Hadoop jars when you use the Kyuubi Hive engine.
#

# set server jvm
export KYUUBI_JAVA_OPTS="-Xmx${kyuubiServerHeapSize}g -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=4096 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSConcurrentMTEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+CMSClassUnloadingEnabled -XX:+CMSParallelRemarkEnabled -XX:+UseCondCardMark -XX:MaxDirectMemorySize=1024m  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -Xloggc:./logs/kyuubi-server-gc-%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=5M -XX:NewRatio=3 -XX:MetaspaceSize=512m"

# set client jvm
export KYUUBI_BEELINE_OPTS="-Xmx${kyuubiClientHeapSize}g -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=4096 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSConcurrentMTEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+CMSClassUnloadingEnabled -XX:+CMSParallelRemarkEnabled -XX:+UseCondCardMark"
#jdk
export JAVA_HOME=${javaHome}
#spark engine
export SPARK_HOME=${sparkHome}

#hadoop config
export HADOOP_CONF_DIR=${hadoopConfDir}
export YARN_CONF_DIR=${hadoopConfDir}

# customer env
<#list itemList as item>
  export ${item.name}=${item.value}
</#list>


