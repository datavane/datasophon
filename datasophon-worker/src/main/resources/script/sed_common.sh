#!/bin/bash
#
#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#

masterHost=$1
masterWebPort=$2
frameCode=$3
clusterId=$4
sed -i -e "s:masterHost=.*:masterHost=${masterHost}:g" /opt/datasophon/datasophon-worker/conf/common.properties
sed -i -e "s:masterWebPort=.*:masterWebPort=${masterWebPort}:g" /opt/datasophon/datasophon-worker/conf/common.properties
sed -i -e "s:frameCode=.*:frameCode=${frameCode}:g" /opt/datasophon/datasophon-worker/conf/common.properties
sed -i -e "s:clusterId=.*:clusterId=${clusterId}:g" /opt/datasophon/datasophon-worker/conf/common.properties
echo "success"