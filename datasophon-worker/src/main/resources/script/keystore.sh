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

set -e
host=$1
echo ${host}
keytool -keystore /etc/security/keytab/keystore -alias localhost -validity 9999 -importpass -keypass admin123  -importpass -storepass admin123 -genkey -keyalg RSA -keysize 2048 -dname "CN=${host}, OU=${host}, O=${host}, L=hefei, ST=hefei, C=CN"
keytool -keystore /etc/security/keytab/truststore -alias CARoot -importpass -storepass admin123 -noprompt -import -file bd_ca_cert
keytool -importpass -storepass admin123 -certreq -alias localhost -keystore /etc/security/keytab/keystore -file cert
openssl x509 -req -CA bd_ca_cert -CAkey bd_ca_key -in cert -out cert_signed -days 9999 -CAcreateserial -passin pass:admin123
keytool -importpass -storepass admin123 -noprompt -keystore /etc/security/keytab/keystore -alias CARoot -import -file bd_ca_cert
keytool -importpass -storepass admin123 -keystore /etc/security/keytab/keystore -alias localhost -import -file cert_signed
chmod 755 /etc/security/keytab/keystore
chown hdfs:hadoop /etc/security/keytab/keystore
chmod 755 /etc/security/keytab/truststore
chown hdfs:hadoop /etc/security/keytab/truststore
