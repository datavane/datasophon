# DataSophon Doc
Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
1.0|2020-12-31 10:30|update|author|desc



## 集群服务角色实例配置表
### 列表
**URL:** http://localhost:8081/cluster/service/instance/config/getConfigVersion

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/config/getConfigVersion?roleGroupId=988&serviceInstanceId=30
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 81,
  "msg": "3nrqil",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/service/instance/config/info

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-
version|int32|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/config/info?serviceInstanceId=381&page=1&pageSize=10&version=324&roleGroupId=268
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 84,
  "msg": "a3uhzo",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/service/instance/config/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主机|false|-
serviceId|int32|服务角色实例id|false|-
createTime|string|创建时间|false|-
configJson|string|配置json|false|-
updateTime|string|更新时间|false|-
configJsonMd5|string|配置json md5|false|-
configVersion|int32|配置json版本|false|-
clusterId|int32|No comments found.|false|-
configFileJson|string|No comments found.|false|-
configFileJsonMd5|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/instance/config/save --data '{
  "id": 900,
  "serviceId": 172,
  "createTime": "2022-12-08 08:56:32",
  "configJson": "u9sz7n",
  "updateTime": "2022-12-08 08:56:32",
  "configJsonMd5": "f5hrnh",
  "configVersion": 100,
  "clusterId": 525,
  "configFileJson": "iny5np",
  "configFileJsonMd5": "8u24ty"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 119,
  "msg": "y1zjb4",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/service/instance/config/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主机|false|-
serviceId|int32|服务角色实例id|false|-
createTime|string|创建时间|false|-
configJson|string|配置json|false|-
updateTime|string|更新时间|false|-
configJsonMd5|string|配置json md5|false|-
configVersion|int32|配置json版本|false|-
clusterId|int32|No comments found.|false|-
configFileJson|string|No comments found.|false|-
configFileJsonMd5|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/instance/config/update --data '{
  "id": 828,
  "serviceId": 565,
  "createTime": "2022-12-08 08:56:32",
  "configJson": "o2alb3",
  "updateTime": "2022-12-08 08:56:32",
  "configJsonMd5": "nujgnl",
  "configVersion": 860,
  "clusterId": 345,
  "configFileJson": "cukfxx",
  "configFileJsonMd5": "mn1t6i"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 994,
  "msg": "fajcyn",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/service/instance/config/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/instance/config/delete --data '[
  836,
  870
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 437,
  "msg": "7sbb5t",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务角色实例配置表
### 列表
**URL:** http://localhost:8081/api/clusterserviceroleinstanceconfig/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
any object|object|any object.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/clusterserviceroleinstanceconfig/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 890,
  "msg": "xtx5r3",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/clusterserviceroleinstanceconfig/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/clusterserviceroleinstanceconfig/info/518
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 608,
  "msg": "fyx039",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/clusterserviceroleinstanceconfig/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主机|false|-
service_role_instance_id|int32|服务角色实例id|false|-
create_time|string|创建时间|false|-
config_json|string|配置json|false|-
update_time|string|更新时间|false|-
config_json_md5|string|配置json md5|false|-
config_json_version|string|配置json版本|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/clusterserviceroleinstanceconfig/save --data '{
  "id": 13,
  "service_role_instance_id": 23,
  "create_time": "2022-12-08 08:56:32",
  "config_json": "c729n9",
  "update_time": "2022-12-08 08:56:32",
  "config_json_md5": "0iaipp",
  "config_json_version": "2.6"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 235,
  "msg": "ltt8w6",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/clusterserviceroleinstanceconfig/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主机|false|-
service_role_instance_id|int32|服务角色实例id|false|-
create_time|string|创建时间|false|-
config_json|string|配置json|false|-
update_time|string|更新时间|false|-
config_json_md5|string|配置json md5|false|-
config_json_version|string|配置json版本|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/clusterserviceroleinstanceconfig/update --data '{
  "id": 701,
  "service_role_instance_id": 259,
  "create_time": "2022-12-08 08:56:32",
  "config_json": "a5u4xr",
  "update_time": "2022-12-08 08:56:32",
  "config_json_md5": "xeyz4v",
  "config_json_version": "2.6"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 826,
  "msg": "72xp1c",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/clusterserviceroleinstanceconfig/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/clusterserviceroleinstanceconfig/delete --data '[
  326,
  36
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 646,
  "msg": "9293x5",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 通知组-用户中间表
### 列表
**URL:** http://localhost:8081/api/notice/group/user/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
any object|object|any object.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/notice/group/user/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 937,
  "msg": "4w6u6w",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/notice/group/user/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/notice/group/user/info/468
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 282,
  "msg": "dbikjx",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/notice/group/user/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
noticeGroupId|int32|通知组id|false|-
userId|int32|用户id|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/notice/group/user/save --data '{
  "id": 68,
  "noticeGroupId": 563,
  "userId": 3
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 573,
  "msg": "ox82hh",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/notice/group/user/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
noticeGroupId|int32|通知组id|false|-
userId|int32|用户id|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/notice/group/user/update --data '{
  "id": 604,
  "noticeGroupId": 871,
  "userId": 108
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 629,
  "msg": "z2r9ch",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/notice/group/user/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/notice/group/user/delete --data '[
  317,
  749
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 152,
  "msg": "21j2qo",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群与用户组管理
### 列表
**URL:** http://localhost:8081/cluster/service/instance/role/group/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/role/group/list?serviceInstanceId=93
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 283,
  "msg": "ugeczk",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/service/instance/role/group/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/role/group/info/869
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 896,
  "msg": "2e417x",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/service/instance/role/group/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 保存

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-
roleGroupName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/role/group/save?serviceInstanceId=222&roleGroupId=888&roleGroupName=nakesha.turner
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 452,
  "msg": "anlaei",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 分配角色组
**URL:** http://localhost:8081/cluster/service/instance/role/group/bind

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 分配角色组

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
roleInstanceIds|string|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/role/group/bind?roleInstanceIds=bsnlcx&roleGroupId=235
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 702,
  "msg": "ijt14a",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/service/instance/role/group/rename

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 修改

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
roleGroupId|int32|No comments found.|false|-
roleGroupName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/role/group/rename?roleGroupName=nakesha.turner&roleGroupId=119
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 171,
  "msg": "umh1az",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/service/instance/role/group/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 删除

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
roleGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/role/group/delete?roleGroupId=760
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 756,
  "msg": "p7z56s",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群Yarn队列管理
### 列表
**URL:** http://localhost:8081/cluster/yarn/queue/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/yarn/queue/list?page=1&pageSize=10&clusterId=633
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 275,
  "msg": "ji1r0i",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 刷新队列
**URL:** http://localhost:8081/cluster/yarn/queue/refreshQueues

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 刷新队列

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/yarn/queue/refreshQueues?clusterId=944
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 556,
  "msg": "i4ylyp",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/yarn/queue/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/yarn/queue/info/216
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 840,
  "msg": "5sarfu",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/yarn/queue/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|false|-
queueName|string|No comments found.|false|-
minCore|int32|No comments found.|false|-
minMem|int32|No comments found.|false|-
maxCore|int32|No comments found.|false|-
maxMem|int32|No comments found.|false|-
appNum|int32|No comments found.|false|-
weight|int32|No comments found.|false|-
schedulePolicy|string|No comments found.|false|-
allowPreemption|int32|1: true 2:false|false|-
clusterId|int32|No comments found.|false|-
createTime|string|No comments found.|false|-
amShare|string|No comments found.|false|-
minResources|string|No comments found.|false|-
maxResources|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/yarn/queue/save --data '{
  "id": 216,
  "queueName": "nakesha.turner",
  "minCore": 203,
  "minMem": 73,
  "maxCore": 593,
  "maxMem": 310,
  "appNum": 559,
  "weight": 449,
  "schedulePolicy": "ovsrms",
  "allowPreemption": 908,
  "clusterId": 940,
  "createTime": "2022-12-08 08:56:32",
  "amShare": "dno2tq",
  "minResources": "toq7ue",
  "maxResources": "ehjbfv"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 818,
  "msg": "3mhh72",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/yarn/queue/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|false|-
queueName|string|No comments found.|false|-
minCore|int32|No comments found.|false|-
minMem|int32|No comments found.|false|-
maxCore|int32|No comments found.|false|-
maxMem|int32|No comments found.|false|-
appNum|int32|No comments found.|false|-
weight|int32|No comments found.|false|-
schedulePolicy|string|No comments found.|false|-
allowPreemption|int32|1: true 2:false|false|-
clusterId|int32|No comments found.|false|-
createTime|string|No comments found.|false|-
amShare|string|No comments found.|false|-
minResources|string|No comments found.|false|-
maxResources|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/yarn/queue/update --data '{
  "id": 173,
  "queueName": "nakesha.turner",
  "minCore": 990,
  "minMem": 405,
  "maxCore": 39,
  "maxMem": 473,
  "appNum": 577,
  "weight": 222,
  "schedulePolicy": "296sks",
  "allowPreemption": 282,
  "clusterId": 399,
  "createTime": "2022-12-08 08:56:32",
  "amShare": "zmx65h",
  "minResources": "qhhyss",
  "maxResources": "upnfob"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 389,
  "msg": "ypc6f0",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/yarn/queue/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/yarn/queue/delete --data '[
  622,
  14
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 767,
  "msg": "y5gupw",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群部署操作
### 获取安装步骤
**URL:** http://localhost:8081/host/install/getInstallStep

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取安装步骤

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
type|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/host/install/getInstallStep?type=160
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 60,
  "msg": "rf83qq",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 解析主机列表
**URL:** http://localhost:8081/host/install/analysisHostList

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 解析主机列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
hosts|string|No comments found.|false|-
sshUser|string|No comments found.|false|-
sshPort|int32|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/analysisHostList --data 'clusterId=905&pageSize=10&sshUser=9bvaog&page=1&sshPort=809&hosts=ra2ag7'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 325,
  "msg": "bycp0h",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 查询主机校验状态
**URL:** http://localhost:8081/host/install/getHostCheckStatus

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询主机校验状态

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
sshUser|string|No comments found.|false|-
sshPort|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/getHostCheckStatus --data 'clusterId=620&sshPort=196&sshUser=015x44'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 97,
  "msg": "8bfzt4",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 重新进行主机环境校验
**URL:** http://localhost:8081/host/install/rehostCheck

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 重新进行主机环境校验

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
hostnames|string|No comments found.|false|-
sshUser|string|No comments found.|false|-
sshPort|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/rehostCheck --data 'sshPort=985&sshUser=rilahr&clusterId=701&hostnames=mw5n5u'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 615,
  "msg": "2a4eis",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 查询主机校验是否全部完成
**URL:** http://localhost:8081/host/install/hostCheckCompleted

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询主机校验是否全部完成

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/hostCheckCompleted --data 'clusterId=986'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 395,
  "msg": "92iqyl",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 主机管理agent分发安装进度列表
**URL:** http://localhost:8081/host/install/dispatcherHostAgentList

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 主机管理agent分发安装进度列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
installStateCode|int32|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/dispatcherHostAgentList --data 'clusterId=936&pageSize=10&installStateCode=682&page=1'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 770,
  "msg": "vp4s6w",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### Agent节点分发
**URL:** http://localhost:8081/host/install/dispatcherHostAgentCompleted

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** Agent节点分发

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/dispatcherHostAgentCompleted --data 'clusterId=544'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 476,
  "msg": "329nbk",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 主机管理agent分发取消
**URL:** http://localhost:8081/host/install/cancelDispatcherHostAgent

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 主机管理agent分发取消

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
hostname|string|No comments found.|false|-
installStateCode|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/cancelDispatcherHostAgent --data 'installStateCode=588&clusterId=300&hostname=16.134.58.123'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 781,
  "msg": "g32jlu",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 主机管理agent分发安装重试
**URL:** http://localhost:8081/host/install/reStartDispatcherHostAgent

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 主机管理agent分发安装重试

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
hostnames|string|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/host/install/reStartDispatcherHostAgent --data 'clusterId=863&hostnames=ytqsll'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 809,
  "msg": "gr0mgh",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群框架表
### 列表
**URL:** http://localhost:8081/api/frame/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 920,
  "msg": "3cjc3w",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/frame/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/info/712
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 384,
  "msg": "bfhmlw",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/frame/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
frameName|string|框架名称|false|-
frameCode|string|框架编码|false|-
frameVersion|string|框架版本|false|-
frameServiceList|array|No comments found.|false|-
└─id|int32|主键|false|-
└─frameId|int32|框架id|false|-
└─serviceName|string|服务名称|false|-
└─label|string|No comments found.|false|-
└─serviceVersion|string|服务版本|false|-
└─serviceDesc|string|服务描述|false|-
└─packageName|string|No comments found.|false|-
└─dependencies|string|No comments found.|false|-
└─serviceJson|string|No comments found.|false|-
└─serviceJsonMd5|string|No comments found.|false|-
└─serviceConfig|string|No comments found.|false|-
└─frameCode|string|No comments found.|false|-
└─configFileJson|string|No comments found.|false|-
└─configFileJsonMd5|string|No comments found.|false|-
└─decompressPackageName|string|No comments found.|false|-
└─installed|boolean|No comments found.|false|-
└─sortNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/save --data '{
  "id": 618,
  "frameName": "nakesha.turner",
  "frameCode": "68009",
  "frameVersion": "2.6",
  "frameServiceList": [
    {
      "id": 505,
      "frameId": 366,
      "serviceName": "nakesha.turner",
      "label": "1d4vis",
      "serviceVersion": "2.6",
      "serviceDesc": "kseurx",
      "packageName": "nakesha.turner",
      "dependencies": "i984ow",
      "serviceJson": "x3scll",
      "serviceJsonMd5": "49hrw3",
      "serviceConfig": "forsma",
      "frameCode": "68009",
      "configFileJson": "i6m737",
      "configFileJsonMd5": "vza5et",
      "decompressPackageName": "nakesha.turner",
      "installed": true,
      "sortNum": 721
    }
  ]
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 155,
  "msg": "wervcj",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/frame/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
frameName|string|框架名称|false|-
frameCode|string|框架编码|false|-
frameVersion|string|框架版本|false|-
frameServiceList|array|No comments found.|false|-
└─id|int32|主键|false|-
└─frameId|int32|框架id|false|-
└─serviceName|string|服务名称|false|-
└─label|string|No comments found.|false|-
└─serviceVersion|string|服务版本|false|-
└─serviceDesc|string|服务描述|false|-
└─packageName|string|No comments found.|false|-
└─dependencies|string|No comments found.|false|-
└─serviceJson|string|No comments found.|false|-
└─serviceJsonMd5|string|No comments found.|false|-
└─serviceConfig|string|No comments found.|false|-
└─frameCode|string|No comments found.|false|-
└─configFileJson|string|No comments found.|false|-
└─configFileJsonMd5|string|No comments found.|false|-
└─decompressPackageName|string|No comments found.|false|-
└─installed|boolean|No comments found.|false|-
└─sortNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/update --data '{
  "id": 348,
  "frameName": "nakesha.turner",
  "frameCode": "68009",
  "frameVersion": "2.6",
  "frameServiceList": [
    {
      "id": 85,
      "frameId": 4,
      "serviceName": "nakesha.turner",
      "label": "e6q92d",
      "serviceVersion": "2.6",
      "serviceDesc": "cq4jqg",
      "packageName": "nakesha.turner",
      "dependencies": "zzpitq",
      "serviceJson": "tuwlsg",
      "serviceJsonMd5": "539dl0",
      "serviceConfig": "c1wd9p",
      "frameCode": "68009",
      "configFileJson": "cya34u",
      "configFileJsonMd5": "hrerzu",
      "decompressPackageName": "nakesha.turner",
      "installed": true,
      "sortNum": 69
    }
  ]
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 839,
  "msg": "ze1lzq",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/frame/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/delete --data '[
  485,
  351
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 289,
  "msg": "vx9pmh",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群主机表
### 查询集群所有主机
**URL:** http://localhost:8081/api/cluster/host/all

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询集群所有主机

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/host/all?clusterId=829
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 943,
  "msg": "8ntcpc",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 查询集群所有主机
**URL:** http://localhost:8081/api/cluster/host/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询集群所有主机

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
hostname|string|No comments found.|false|-
cpuArchitecture|string|No comments found.|false|-
hostState|int32|No comments found.|false|-
orderField|string|No comments found.|false|-
orderType|string|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/host/list?pageSize=10&cpuArchitecture=llnavo&clusterId=168&hostname=16.134.58.123&orderField=1063qt&orderType=znxhir&page=1&hostState=0
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 935,
  "msg": "1xjuw4",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 依据主机名查询主机信息
**URL:** http://localhost:8081/api/cluster/host/getRoleListByHostname

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 依据主机名查询主机信息

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
hostname|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/host/getRoleListByHostname?clusterId=443&hostname=16.134.58.123
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 300,
  "msg": "stx1bz",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 查询集群机架
**URL:** http://localhost:8081/api/cluster/host/getRack

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询集群机架

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/host/getRack?clusterId=383
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 61,
  "msg": "zgwmad",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/cluster/host/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/host/info/936
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 169,
  "msg": "bbsy7d",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/cluster/host/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
createTime|string|创建时间|false|-
hostname|string|主机名|false|-
ip|string|IP|false|-
rack|string|机架|false|-
coreNum|int32|核数|false|-
totalMem|int32|总内存|false|-
totalDisk|int32|总磁盘|false|-
usedMem|int32|已用内存|false|-
usedDisk|int32|已用磁盘|false|-
averageLoad|string|平均负载|false|-
checkTime|string|检测时间|false|-
clusterId|int32|集群id|false|-
hostState|int32|1:正常运行 2：存在异常 3、断线|false|-
managed|enum|1:受管 2：断线<br/>YES -(1,true)<br/>NO -(2,false)<br/>|false|-
cpuArchitecture|string|No comments found.|false|-
serviceRoleNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/host/save --data '{
  "id": 707,
  "createTime": "2022-12-08 08:56:32",
  "hostname": "16.134.58.123",
  "ip": "194.52.45.111",
  "rack": "86ecl6",
  "coreNum": 970,
  "totalMem": 305,
  "totalDisk": 169,
  "usedMem": 95,
  "usedDisk": 858,
  "averageLoad": "j4xm7w",
  "checkTime": "2022-12-08 08:56:32",
  "clusterId": 17,
  "hostState": 0,
  "managed": "YES",
  "cpuArchitecture": "ij6xpk",
  "serviceRoleNum": 657
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 636,
  "msg": "w3ad7o",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/cluster/host/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
createTime|string|创建时间|false|-
hostname|string|主机名|false|-
ip|string|IP|false|-
rack|string|机架|false|-
coreNum|int32|核数|false|-
totalMem|int32|总内存|false|-
totalDisk|int32|总磁盘|false|-
usedMem|int32|已用内存|false|-
usedDisk|int32|已用磁盘|false|-
averageLoad|string|平均负载|false|-
checkTime|string|检测时间|false|-
clusterId|int32|集群id|false|-
hostState|int32|1:正常运行 2：存在异常 3、断线|false|-
managed|enum|1:受管 2：断线<br/>YES -(1,true)<br/>NO -(2,false)<br/>|false|-
cpuArchitecture|string|No comments found.|false|-
serviceRoleNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/host/update --data '{
  "id": 15,
  "createTime": "2022-12-08 08:56:32",
  "hostname": "16.134.58.123",
  "ip": "194.52.45.111",
  "rack": "2fp319",
  "coreNum": 338,
  "totalMem": 296,
  "totalDisk": 621,
  "usedMem": 591,
  "usedDisk": 884,
  "averageLoad": "i0dw4d",
  "checkTime": "2022-12-08 08:56:32",
  "clusterId": 527,
  "hostState": 0,
  "managed": "YES",
  "cpuArchitecture": "rhtha9",
  "serviceRoleNum": 509
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 495,
  "msg": "hrh1qn",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/cluster/host/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 删除

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
hostId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/host/delete?hostId=957
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 54,
  "msg": "ls6rcj",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务角色对应web ui表
### 列表
**URL:** http://localhost:8081/cluster/webuis/getWebUis

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/webuis/getWebUis?serviceInstanceId=983
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 322,
  "msg": "sgovf9",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/webuis/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/webuis/info/492
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 577,
  "msg": "5mb6us",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/webuis/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
serviceRoleInstanceId|int32|服务角色id|false|-
webUrl|string|URL地址|false|-
serviceInstanceId|int32|No comments found.|false|-
name|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/webuis/save --data '{
  "id": 834,
  "serviceRoleInstanceId": 769,
  "webUrl": "www.sunny-kub.com",
  "serviceInstanceId": 99,
  "name": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 463,
  "msg": "q6z72y",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/webuis/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
serviceRoleInstanceId|int32|服务角色id|false|-
webUrl|string|URL地址|false|-
serviceInstanceId|int32|No comments found.|false|-
name|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/webuis/update --data '{
  "id": 161,
  "serviceRoleInstanceId": 325,
  "webUrl": "www.sunny-kub.com",
  "serviceInstanceId": 65,
  "name": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 74,
  "msg": "rlk38p",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/webuis/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/webuis/delete --data '[
  581,
  506
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 552,
  "msg": "mhjmzf",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群告警历史表
### 列表
**URL:** http://localhost:8081/cluster/alert/history/getAlertList

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/history/getAlertList?serviceInstanceId=542
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 202,
  "msg": "b3e852",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 列表
**URL:** http://localhost:8081/cluster/alert/history/getAllAlertList

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/history/getAllAlertList?page=1&pageSize=10&clusterId=342
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 275,
  "msg": "zgygm2",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/alert/history/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/history/info/463
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 815,
  "msg": "5fye1z",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/alert/history/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
alertMessage|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/history/save --data '{
  "alertMessage": "success"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 790,
  "msg": "hnb5qi",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/alert/history/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
alertGroupName|string|告警组|false|-
alertTargetName|string|告警指标|false|-
alertInfo|string|告警详情|false|-
alertAdvice|string|告警建议|false|-
hostname|string|主机|false|-
alertLevel|enum|告警级别 1：警告2：异常<br/>WARN -(1,"warning")<br/>EXCEPTION -(2,"exception")<br/>|false|-
isEnabled|int32|是否处理 1:未处理2：已处理|false|-
serviceRoleInstanceId|int32|集群服务角色实例id|false|-
serviceInstanceId|int32|集群服务实例id|false|-
createTime|string|创建时间|false|-
updateTime|string|更新时间|false|-
clusterId|int32|集群id|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/history/update --data '{
  "id": 434,
  "alertGroupName": "nakesha.turner",
  "alertTargetName": "nakesha.turner",
  "alertInfo": "6h3qcm",
  "alertAdvice": "74881v",
  "hostname": "16.134.58.123",
  "alertLevel": 1,
  "isEnabled": 67,
  "serviceRoleInstanceId": 66,
  "serviceInstanceId": 973,
  "createTime": "2022-12-08 08:56:32",
  "updateTime": "2022-12-08 08:56:32",
  "clusterId": 220
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 585,
  "msg": "p4nqg5",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/alert/history/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/history/delete --data '[
  467,
  471
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 573,
  "msg": "j76n4q",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 个性化安装服务
### 根据服务名称查询服务配置选项
**URL:** http://localhost:8081/service/install/getServiceConfigOption

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 根据服务名称查询服务配置选项

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
serviceName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/service/install/getServiceConfigOption?serviceName=nakesha.turner&clusterId=153
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 120,
  "msg": "hrbpjo",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存服务配置
**URL:** http://localhost:8081/service/install/saveServiceConfig

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 保存服务配置

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
serviceName|string|No comments found.|false|-
serviceConfig|string|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/service/install/saveServiceConfig?clusterId=491&serviceConfig=9vvm6k&roleGroupId=554&serviceName=nakesha.turner
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 163,
  "msg": "qjjjj8",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存服务角色与主机对应关系
**URL:** http://localhost:8081/service/install/saveServiceRoleHostMapping/{clusterId}

**Type:** GET


**Content-Type:** application/json; charset=utf-8

**Description:** 保存服务角色与主机对应关系

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|true|-

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceRole|string|No comments found.|false|-
hosts|array|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/service/install/saveServiceRoleHostMapping/259 --data '[
  {
    "serviceRole": "9o6cwq",
    "hosts": [
      "s5skil"
    ]
  }
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 776,
  "msg": "8vpyhg",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 查询服务角色与主机对应关系
**URL:** http://localhost:8081/service/install/getServiceRoleHostMapping

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询服务角色与主机对应关系

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/service/install/getServiceRoleHostMapping?clusterId=42
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 177,
  "msg": "0v4ly5",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存主机与服务角色对应关系
**URL:** http://localhost:8081/service/install/saveHostServiceRoleMapping/{clusterId}

**Type:** GET


**Content-Type:** application/json; charset=utf-8

**Description:** 保存主机与服务角色对应关系

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|true|-

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
host|string|No comments found.|false|-
serviceRoles|array|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/service/install/saveHostServiceRoleMapping/32 --data '[
  {
    "host": "k7y7um",
    "serviceRoles": [
      "3be1im"
    ]
  }
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 987,
  "msg": "e725bu",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 服务部署总览
**URL:** http://localhost:8081/service/install/getServiceRoleDeployOverview

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 服务部署总览

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/service/install/getServiceRoleDeployOverview?clusterId=723
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 106,
  "msg": "zo56dz",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 开始安装服务
**URL:** http://localhost:8081/service/install/startInstallService/{clusterId}

**Type:** GET


**Content-Type:** application/json; charset=utf-8

**Description:** 开始安装服务

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|true|-

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
commandIds|array|No comments found.,[array of string]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/service/install/startInstallService/467 --data '[
  "5ivbuz",
  "mczw6g"
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 469,
  "msg": "hub50u",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 下载安装包
**URL:** http://localhost:8081/service/install/downloadPackage

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 下载安装包

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
packageName|string|No comments found.|false|-
cpuArchitecture|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/service/install/downloadPackage?cpuArchitecture=mgdpo0&packageName=nakesha.turner
```

**Response-example:**
```
Return void.
```

### 服务处理
**URL:** http://localhost:8081/service/install/serviceHandle

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 服务处理

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-
commandType|enum|INSTALL_SERVICE -(1,INSTALL,安装)<br/>START_SERVICE -(2,START,启动)<br/>STOP_SERVICE -(3,STOP,停止)<br/>RESTART_SERVICE -(4,RESTART,重启)<br/>START_WITH_CONFIG -(5,START_WITH_CONFIG,)<br/>RESTART_WITH_CONFIG -(6,RESTART_WITH_CONFIG,znDesc)<br/>|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/service/install/serviceHandle?serviceInstanceId=581&commandType=INSTALL_SERVICE
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 864,
  "msg": "ck8gxv",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 服务角色处理
**URL:** http://localhost:8081/service/install/serviceRoleHandle

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 服务角色处理

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceRoleInstanceId|int32|No comments found.|false|-
commandType|enum|INSTALL_SERVICE -(1,INSTALL,安装)<br/>START_SERVICE -(2,START,启动)<br/>STOP_SERVICE -(3,STOP,停止)<br/>RESTART_SERVICE -(4,RESTART,重启)<br/>START_WITH_CONFIG -(5,START_WITH_CONFIG,)<br/>RESTART_WITH_CONFIG -(6,RESTART_WITH_CONFIG,znDesc)<br/>|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/service/install/serviceRoleHandle?commandType=INSTALL_SERVICE&serviceRoleInstanceId=962
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 945,
  "msg": "tjlj26",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务总览仪表盘
### 列表
**URL:** http://localhost:8081/cluster/service/dashboard/getDashboardUrl

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/dashboard/getDashboardUrl?clusterId=294
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 923,
  "msg": "zh7vlz",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群用户组配置
### 列表
**URL:** http://localhost:8081/cluster/service/role/group/config/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/role/group/config/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 880,
  "msg": "yry17w",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/service/role/group/config/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/role/group/config/info/716
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 917,
  "msg": "drb5p7",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/service/role/group/config/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-
configJson|string|No comments found.|false|-
configJsonMd5|string|No comments found.|false|-
configVersion|int32|No comments found.|false|-
configFileJson|string|No comments found.|false|-
configFileJsonMd5|string|No comments found.|false|-
clusterId|int32|No comments found.|false|-
createTime|string|No comments found.|false|-
updateTime|string|No comments found.|false|-
serviceName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/role/group/config/save --data '{
  "id": 183,
  "roleGroupId": 625,
  "configJson": "ihr8qx",
  "configJsonMd5": "7jp7ho",
  "configVersion": 928,
  "configFileJson": "ocyhmh",
  "configFileJsonMd5": "2qh5mo",
  "clusterId": 614,
  "createTime": "2022-12-08 08:56:32",
  "updateTime": "2022-12-08 08:56:32",
  "serviceName": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 278,
  "msg": "aan3va",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/service/role/group/config/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-
configJson|string|No comments found.|false|-
configJsonMd5|string|No comments found.|false|-
configVersion|int32|No comments found.|false|-
configFileJson|string|No comments found.|false|-
configFileJsonMd5|string|No comments found.|false|-
clusterId|int32|No comments found.|false|-
createTime|string|No comments found.|false|-
updateTime|string|No comments found.|false|-
serviceName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/role/group/config/update --data '{
  "id": 803,
  "roleGroupId": 613,
  "configJson": "nnb88f",
  "configJsonMd5": "yf2urm",
  "configVersion": 413,
  "configFileJson": "gx9bd5",
  "configFileJsonMd5": "ng4nko",
  "clusterId": 641,
  "createTime": "2022-12-08 08:56:32",
  "updateTime": "2022-12-08 08:56:32",
  "serviceName": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 266,
  "msg": "rpyb5l",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/service/role/group/config/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/role/group/config/delete --data '[
  998,
  327
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 370,
  "msg": "de839h",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务操作指令主机指令表
### 列表
**URL:** http://localhost:8081/api/cluster/service/command/host/command/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
hostname|string|No comments found.|false|-
commandHostId|string|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/host/command/list?commandHostId=11&pageSize=10&hostname=16.134.58.123&page=1
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 440,
  "msg": "x8r17c",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 查询主机命令执行日志
**URL:** http://localhost:8081/api/cluster/service/command/host/command/getHostCommandLog

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询主机命令执行日志

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
hostCommandId|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/host/command/getHostCommandLog?hostCommandId=11&clusterId=655
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 276,
  "msg": "2goo0k",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/cluster/service/command/host/command/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/host/command/info/544
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 325,
  "msg": "3scabs",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/cluster/service/command/host/command/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
hostCommandId|string|主键|false|-
commandName|string|指令名称|false|-
commandState|enum|指令状态 1、正在运行2：成功3：失败<br/>WAIT -(0,"待运行")<br/>RUNNING -(1,"正在运行")<br/>SUCCESS -(2,"成功")<br/>FAILED -(3,"失败")<br/>CANCEL -(4,"取消")<br/>|false|-
commandStateCode|int32|No comments found.|false|-
commandProgress|int32|指令进度|false|-
commandHostId|string|主机id|false|-
commandId|string|No comments found.|false|-
hostname|string|No comments found.|false|-
serviceRoleName|string|服务角色名称|false|-
serviceRoleType|enum|null<br/>MASTER -(1,"master")<br/>WORKER -(2,"worker")<br/>CLIENT -(3,"client")<br/>SLAVE -(4,"slave")<br/>|false|-
resultMsg|string|No comments found.|false|-
createTime|string|No comments found.|false|-
commandType|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/host/command/save --data '{
  "hostCommandId": "11",
  "commandName": "nakesha.turner",
  "commandState": 0,
  "commandStateCode": 381,
  "commandProgress": 183,
  "commandHostId": "11",
  "commandId": "11",
  "hostname": "16.134.58.123",
  "serviceRoleName": "nakesha.turner",
  "serviceRoleType": 1,
  "resultMsg": "zdad61",
  "createTime": "2022-12-08 08:56:32",
  "commandType": 991
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 866,
  "msg": "hp7pag",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/cluster/service/command/host/command/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
hostCommandId|string|主键|false|-
commandName|string|指令名称|false|-
commandState|enum|指令状态 1、正在运行2：成功3：失败<br/>WAIT -(0,"待运行")<br/>RUNNING -(1,"正在运行")<br/>SUCCESS -(2,"成功")<br/>FAILED -(3,"失败")<br/>CANCEL -(4,"取消")<br/>|false|-
commandStateCode|int32|No comments found.|false|-
commandProgress|int32|指令进度|false|-
commandHostId|string|主机id|false|-
commandId|string|No comments found.|false|-
hostname|string|No comments found.|false|-
serviceRoleName|string|服务角色名称|false|-
serviceRoleType|enum|null<br/>MASTER -(1,"master")<br/>WORKER -(2,"worker")<br/>CLIENT -(3,"client")<br/>SLAVE -(4,"slave")<br/>|false|-
resultMsg|string|No comments found.|false|-
createTime|string|No comments found.|false|-
commandType|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/host/command/update --data '{
  "hostCommandId": "11",
  "commandName": "nakesha.turner",
  "commandState": 0,
  "commandStateCode": 528,
  "commandProgress": 623,
  "commandHostId": "11",
  "commandId": "11",
  "hostname": "16.134.58.123",
  "serviceRoleName": "nakesha.turner",
  "serviceRoleType": 1,
  "resultMsg": "bq0evd",
  "createTime": "2022-12-08 08:56:32",
  "commandType": 635
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 781,
  "msg": "8ipxbm",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/cluster/service/command/host/command/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/host/command/delete --data '[
  687,
  366
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 842,
  "msg": "11mscs",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群告警分组管理
### 列表
**URL:** http://localhost:8081/cluster/alert/group/map/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/group/map/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 790,
  "msg": "mb7trc",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/alert/group/map/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/group/map/info/32
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 646,
  "msg": "pfiae0",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/alert/group/map/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|false|-
clusterId|int32|No comments found.|false|-
alertGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/group/map/save --data '{
  "id": 713,
  "clusterId": 92,
  "alertGroupId": 851
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 407,
  "msg": "ebvk49",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/alert/group/map/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|false|-
clusterId|int32|No comments found.|false|-
alertGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/group/map/update --data '{
  "id": 636,
  "clusterId": 42,
  "alertGroupId": 296
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 877,
  "msg": "86fuxx",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/alert/group/map/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/group/map/delete --data '[
  669,
  808
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 587,
  "msg": "0drhup",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务角色实例表
### 列表
**URL:** http://localhost:8081/cluster/service/role/instance/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-
hostname|string|No comments found.|false|-
serviceRoleState|int32|No comments found.|false|-
serviceRoleName|string|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/role/instance/list?hostname=16.134.58.123&pageSize=10&serviceRoleName=nakesha.turner&serviceInstanceId=278&roleGroupId=597&page=1&serviceRoleState=0
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 687,
  "msg": "yly5bn",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/service/role/instance/getLog

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceRoleInstanceId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/role/instance/getLog?serviceRoleInstanceId=895
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 14,
  "msg": "0plwm4",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 退役
**URL:** http://localhost:8081/cluster/service/role/instance/decommissionNode

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 退役

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceRoleInstanceIds|string|No comments found.|false|-
serviceName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/role/instance/decommissionNode?serviceRoleInstanceIds=0j2zfi&serviceName=nakesha.turner
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 35,
  "msg": "r0t670",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 重启过时服务
**URL:** http://localhost:8081/cluster/service/role/instance/restartObsoleteService

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 重启过时服务

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
roleGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/role/instance/restartObsoleteService?roleGroupId=268
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 895,
  "msg": "6f08wi",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/service/role/instance/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
serviceRoleName|string|服务角色名称|false|-
hostname|string|主机|false|-
serviceRoleState|enum|服务角色状态 1:正在运行2：存在告警3：存在异常4：需要重启<br/>RUNNING -(1,"正在运行")<br/>STOP -(2,"停止")<br/>EXISTS_ALARM -(3,"存在告警")<br/>DECOMMISSIONING -(4,"退役中")<br/>DECOMMISSIONED -(5,"已退役")<br/>|false|-
serviceRoleStateCode|int32|No comments found.|false|-
updateTime|string|更新时间|false|-
createTime|string|创建时间|false|-
serviceId|int32|服务id|false|-
roleType|enum|角色类型 1:master2:worker3:client<br/>MASTER -(1,"master")<br/>WORKER -(2,"worker")<br/>CLIENT -(3,"client")<br/>SLAVE -(4,"slave")<br/>|false|-
clusterId|int32|集群id|false|-
serviceName|string|服务名称|false|-
roleGroupId|int32|No comments found.|false|-
needRestart|enum|null<br/>NO -(1,false)<br/>YES -(2,true)<br/>|false|-
roleGroupName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/role/instance/save --data '{
  "id": 605,
  "serviceRoleName": "nakesha.turner",
  "hostname": "16.134.58.123",
  "serviceRoleState": 1,
  "serviceRoleStateCode": 44,
  "updateTime": "2022-12-08 08:56:32",
  "createTime": "2022-12-08 08:56:32",
  "serviceId": 940,
  "roleType": 1,
  "clusterId": 127,
  "serviceName": "nakesha.turner",
  "roleGroupId": 205,
  "needRestart": 1,
  "roleGroupName": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 733,
  "msg": "u5fm0x",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/service/role/instance/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
serviceRoleName|string|服务角色名称|false|-
hostname|string|主机|false|-
serviceRoleState|enum|服务角色状态 1:正在运行2：存在告警3：存在异常4：需要重启<br/>RUNNING -(1,"正在运行")<br/>STOP -(2,"停止")<br/>EXISTS_ALARM -(3,"存在告警")<br/>DECOMMISSIONING -(4,"退役中")<br/>DECOMMISSIONED -(5,"已退役")<br/>|false|-
serviceRoleStateCode|int32|No comments found.|false|-
updateTime|string|更新时间|false|-
createTime|string|创建时间|false|-
serviceId|int32|服务id|false|-
roleType|enum|角色类型 1:master2:worker3:client<br/>MASTER -(1,"master")<br/>WORKER -(2,"worker")<br/>CLIENT -(3,"client")<br/>SLAVE -(4,"slave")<br/>|false|-
clusterId|int32|集群id|false|-
serviceName|string|服务名称|false|-
roleGroupId|int32|No comments found.|false|-
needRestart|enum|null<br/>NO -(1,false)<br/>YES -(2,true)<br/>|false|-
roleGroupName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/role/instance/update --data '{
  "id": 270,
  "serviceRoleName": "nakesha.turner",
  "hostname": "16.134.58.123",
  "serviceRoleState": 1,
  "serviceRoleStateCode": 963,
  "updateTime": "2022-12-08 08:56:32",
  "createTime": "2022-12-08 08:56:32",
  "serviceId": 435,
  "roleType": 1,
  "clusterId": 691,
  "serviceName": "nakesha.turner",
  "roleGroupId": 467,
  "needRestart": 1,
  "roleGroupName": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 407,
  "msg": "r9lzuc",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/service/role/instance/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 删除

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceRoleInstancesIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/role/instance/delete?serviceRoleInstancesIds=g21vsz
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 977,
  "msg": "8jzkwt",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务操作指令表
### 查询集群服务指令列表
**URL:** http://localhost:8081/api/cluster/service/command/getServiceCommandlist

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询集群服务指令列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/getServiceCommandlist?clusterId=830&pageSize=10&page=1
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 789,
  "msg": "dh5cip",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 生成服务安装操作指令
**URL:** http://localhost:8081/api/cluster/service/command/generateCommand

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 生成服务安装操作指令

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
commandType|string|No comments found.|false|-
serviceNames|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/generateCommand?clusterId=38&commandType=hknym2&serviceNames=33jdpo
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 954,
  "msg": "90601t",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 生成服务实例操作指令
**URL:** http://localhost:8081/api/cluster/service/command/generateServiceCommand

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 生成服务实例操作指令

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
commandType|string|No comments found.|false|-
serviceInstanceIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/generateServiceCommand?clusterId=237&commandType=11s7pv&serviceInstanceIds=9z1dgr
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 530,
  "msg": "vun0m7",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 生成服务角色实例操作指令
**URL:** http://localhost:8081/api/cluster/service/command/generateServiceRoleCommand

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 生成服务角色实例操作指令

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
commandType|string|No comments found.|false|-
serviceInstanceId|int32|No comments found.|false|-
serviceRoleInstancesIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/generateServiceRoleCommand?serviceInstanceId=576&commandType=6on9cg&serviceRoleInstancesIds=kgm07f&clusterId=883
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 128,
  "msg": "nhay5q",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 启动执行指令
**URL:** http://localhost:8081/api/cluster/service/command/startExecuteCommand

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 启动执行指令

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
commandType|string|No comments found.|false|-
commandIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/startExecuteCommand?commandIds=bkgv8s&commandType=msul7s&clusterId=422
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 366,
  "msg": "bjapvp",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 取消命令
**URL:** http://localhost:8081/api/cluster/service/command/cancelCommand

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 取消命令

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
commandId|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/cancelCommand?commandId=11
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 124,
  "msg": "jlv6jb",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/cluster/service/command/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/info/388
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 942,
  "msg": "3t9uc2",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/cluster/service/command/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
commandId|string|主键|false|-
createBy|string|创建人|false|-
createTime|string|创建时间|false|-
commandName|string|命令名称|false|-
commandState|enum|命令状态 1：正在运行2：成功3：失败<br/>WAIT -(0,"待运行")<br/>RUNNING -(1,"正在运行")<br/>SUCCESS -(2,"成功")<br/>FAILED -(3,"失败")<br/>CANCEL -(4,"取消")<br/>|false|-
commandStateCode|int32|No comments found.|false|-
commandProgress|int32|命令进度|false|-
clusterId|int32|集群id|false|-
serviceName|string|服务名称|false|-
commandType|int32|命令类型|false|-
durationTime|string|No comments found.|false|-
endTime|string|No comments found.|false|-
serviceInstanceId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/save --data '{
  "commandId": "11",
  "createBy": "3peslr",
  "createTime": "2022-12-08 08:56:32",
  "commandName": "nakesha.turner",
  "commandState": 0,
  "commandStateCode": 894,
  "commandProgress": 896,
  "clusterId": 51,
  "serviceName": "nakesha.turner",
  "commandType": 923,
  "durationTime": "2022-12-08 08:56:32",
  "endTime": "2022-12-08",
  "serviceInstanceId": 955
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 496,
  "msg": "rsqg9i",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/cluster/service/command/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
commandId|string|主键|false|-
createBy|string|创建人|false|-
createTime|string|创建时间|false|-
commandName|string|命令名称|false|-
commandState|enum|命令状态 1：正在运行2：成功3：失败<br/>WAIT -(0,"待运行")<br/>RUNNING -(1,"正在运行")<br/>SUCCESS -(2,"成功")<br/>FAILED -(3,"失败")<br/>CANCEL -(4,"取消")<br/>|false|-
commandStateCode|int32|No comments found.|false|-
commandProgress|int32|命令进度|false|-
clusterId|int32|集群id|false|-
serviceName|string|服务名称|false|-
commandType|int32|命令类型|false|-
durationTime|string|No comments found.|false|-
endTime|string|No comments found.|false|-
serviceInstanceId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/update --data '{
  "commandId": "11",
  "createBy": "999eo4",
  "createTime": "2022-12-08 08:56:32",
  "commandName": "nakesha.turner",
  "commandState": 0,
  "commandStateCode": 222,
  "commandProgress": 512,
  "clusterId": 476,
  "serviceName": "nakesha.turner",
  "commandType": 555,
  "durationTime": "2022-12-08 08:56:32",
  "endTime": "2022-12-08",
  "serviceInstanceId": 121
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 639,
  "msg": "qyxypv",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/cluster/service/command/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/delete --data '[
  656,
  98
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 96,
  "msg": "be51hf",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 角色信息表
### 列表
**URL:** http://localhost:8081/api/role/info/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
any object|object|any object.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/role/info/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 111,
  "msg": "eizylw",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/role/info/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/role/info/info/403
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 690,
  "msg": "gd7a2p",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/role/info/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
roleName|string|角色名称|false|-
roleCode|string|角色编码|false|-
createTime|string|创建时间|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/role/info/save --data '{
  "id": 843,
  "roleName": "nakesha.turner",
  "roleCode": "68009",
  "createTime": "2022-12-08 08:56:33"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 802,
  "msg": "d2z68p",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/role/info/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
roleName|string|角色名称|false|-
roleCode|string|角色编码|false|-
createTime|string|创建时间|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/role/info/update --data '{
  "id": 448,
  "roleName": "nakesha.turner",
  "roleCode": "68009",
  "createTime": "2022-12-08 08:56:33"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 3,
  "msg": "4aocgk",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/role/info/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/role/info/delete --data '[
  670,
  302
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 169,
  "msg": "4ria41",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群角色用户中间表
### 列表
**URL:** http://localhost:8081/api/cluster/user/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
any object|object|any object.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/user/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 59,
  "msg": "cl7kb0",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/cluster/user/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/user/info/568
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 905,
  "msg": "gwuk1k",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/cluster/user/saveClusterManager

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 保存

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
userIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/user/saveClusterManager?clusterId=765&userIds=opn1ft
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 138,
  "msg": "2ct6m4",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/cluster/user/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
clusterId|int32|集群id|false|-
userType|enum|角色id<br/>CLUSTER_MANAGER -(1,"集群管理员")<br/>|false|-
userId|int32|用户id|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/user/update --data '{
  "id": 653,
  "clusterId": 485,
  "userType": 1,
  "userId": 198
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 321,
  "msg": "016ixe",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/cluster/user/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/user/delete --data '[
  62,
  710
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 894,
  "msg": "u3teh5",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群框架版本服务表
### 列表
**URL:** http://localhost:8081/api/frame/service/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/service/list?clusterId=420
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 923,
  "msg": "4fp8xx",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 根据servce id列表查询服务
**URL:** http://localhost:8081/api/frame/service/getServiceListByServiceIds

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 根据servce id列表查询服务

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceIds|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/service/getServiceListByServiceIds?serviceIds=esniur&serviceIds=esniur
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 482,
  "msg": "zelhu7",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/frame/service/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/service/info/567
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 668,
  "msg": "jtprw5",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/frame/service/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
frameId|int32|框架id|false|-
serviceName|string|服务名称|false|-
label|string|No comments found.|false|-
serviceVersion|string|服务版本|false|-
serviceDesc|string|服务描述|false|-
packageName|string|No comments found.|false|-
dependencies|string|No comments found.|false|-
serviceJson|string|No comments found.|false|-
serviceJsonMd5|string|No comments found.|false|-
serviceConfig|string|No comments found.|false|-
frameCode|string|No comments found.|false|-
configFileJson|string|No comments found.|false|-
configFileJsonMd5|string|No comments found.|false|-
decompressPackageName|string|No comments found.|false|-
installed|boolean|No comments found.|false|-
sortNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/service/save --data '{
  "id": 351,
  "frameId": 142,
  "serviceName": "nakesha.turner",
  "label": "zd0ocr",
  "serviceVersion": "2.6",
  "serviceDesc": "zojw63",
  "packageName": "nakesha.turner",
  "dependencies": "3e0gt4",
  "serviceJson": "wbzs6r",
  "serviceJsonMd5": "vlbxrj",
  "serviceConfig": "xmgwr8",
  "frameCode": "68009",
  "configFileJson": "9j6fqk",
  "configFileJsonMd5": "iwyaso",
  "decompressPackageName": "nakesha.turner",
  "installed": true,
  "sortNum": 621
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 883,
  "msg": "gmt56q",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/frame/service/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
frameId|int32|框架id|false|-
serviceName|string|服务名称|false|-
label|string|No comments found.|false|-
serviceVersion|string|服务版本|false|-
serviceDesc|string|服务描述|false|-
packageName|string|No comments found.|false|-
dependencies|string|No comments found.|false|-
serviceJson|string|No comments found.|false|-
serviceJsonMd5|string|No comments found.|false|-
serviceConfig|string|No comments found.|false|-
frameCode|string|No comments found.|false|-
configFileJson|string|No comments found.|false|-
configFileJsonMd5|string|No comments found.|false|-
decompressPackageName|string|No comments found.|false|-
installed|boolean|No comments found.|false|-
sortNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/service/update --data '{
  "id": 514,
  "frameId": 306,
  "serviceName": "nakesha.turner",
  "label": "y7v859",
  "serviceVersion": "2.6",
  "serviceDesc": "zwi71t",
  "packageName": "nakesha.turner",
  "dependencies": "ykxjvt",
  "serviceJson": "t4k2kq",
  "serviceJsonMd5": "sbh851",
  "serviceConfig": "0ap5bs",
  "frameCode": "68009",
  "configFileJson": "g39qgx",
  "configFileJsonMd5": "45q354",
  "decompressPackageName": "nakesha.turner",
  "installed": true,
  "sortNum": 728
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 813,
  "msg": "mmr9gz",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/frame/service/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/service/delete --data '[
  179,
  525
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 560,
  "msg": "9zhs6g",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 告警组表
### 列表
**URL:** http://localhost:8081/alert/group/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
alertGroupName|string|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/alert/group/list?alertGroupName=nakesha.turner&clusterId=576&pageSize=10&page=1
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 642,
  "msg": "3jrg4o",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/alert/group/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/alert/group/info/463
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 134,
  "msg": "g0wddp",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/alert/group/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
alertGroupName|string|告警组名称|false|-
alertGroupCategory|string|告警组类别|false|-
createTime|string|No comments found.|false|-
alertQuotaNum|int32|No comments found.|false|-
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/alert/group/save --data '{
  "id": 980,
  "alertGroupName": "nakesha.turner",
  "alertGroupCategory": "dcxft0",
  "createTime": "2022-12-08 08:56:33",
  "alertQuotaNum": 743,
  "clusterId": 146
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 746,
  "msg": "ox07x8",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/alert/group/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
alertGroupName|string|告警组名称|false|-
alertGroupCategory|string|告警组类别|false|-
createTime|string|No comments found.|false|-
alertQuotaNum|int32|No comments found.|false|-
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/alert/group/update --data '{
  "id": 903,
  "alertGroupName": "nakesha.turner",
  "alertGroupCategory": "hotvi6",
  "createTime": "2022-12-08 08:56:33",
  "alertQuotaNum": 344,
  "clusterId": 705
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 369,
  "msg": "a4x240",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/alert/group/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/alert/group/delete --data '[
  825,
  179
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 446,
  "msg": "ufhnt4",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 用户信息表
### 列表带分页
**URL:** http://localhost:8081/api/user/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表带分页

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
username|string|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/user/list?page=1&pageSize=10&username=nakesha.turner
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 280,
  "msg": "49ya24",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 查询所有用户
**URL:** http://localhost:8081/api/user/all

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询所有用户

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/user/all
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 722,
  "msg": "tsmkyg",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/user/info/{id}

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/user/info/258
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 92,
  "msg": "pt9u75",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/user/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
username|string|用户名|false|-
password|string|密码|false|-
email|string|邮箱|false|-
phone|string|手机号|false|-
createTime|string|创建时间|false|-
userType|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/user/save --data '{
  "id": 426,
  "username": "nakesha.turner",
  "password": "pp8ztf",
  "email": "brendan.harris@yahoo.com",
  "phone": "208.224.7832",
  "createTime": "2022-12-08 08:56:33",
  "userType": 249
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 636,
  "msg": "hc3d38",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/user/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
username|string|用户名|false|-
password|string|密码|false|-
email|string|邮箱|false|-
phone|string|手机号|false|-
createTime|string|创建时间|false|-
userType|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/user/update --data '{
  "id": 263,
  "username": "nakesha.turner",
  "password": "e7f6gk",
  "email": "brendan.harris@yahoo.com",
  "phone": "208.224.7832",
  "createTime": "2022-12-08 08:56:33",
  "userType": 729
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 251,
  "msg": "ydujld",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/user/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/user/delete --data '[
  734,
  515
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 33,
  "msg": "2olc0v",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群告警指标表
### 信息
**URL:** http://localhost:8081/cluster/alert/quota/list

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
alertGroupId|int32|No comments found.|false|-
quotaName|string|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/quota/list?page=1&alertGroupId=686&pageSize=10&clusterId=406&quotaName=nakesha.turner
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 254,
  "msg": "mkl150",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 启用
**URL:** http://localhost:8081/cluster/alert/quota/start

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 启用

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
alertQuotaIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/quota/start?clusterId=219&alertQuotaIds=rle83n
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 934,
  "msg": "zrzafv",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 停用
**URL:** http://localhost:8081/cluster/alert/quota/stop

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 停用

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
alertQuotaIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/alert/quota/stop?clusterId=265&alertQuotaIds=f2ua75
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 939,
  "msg": "tylf7c",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/alert/quota/save

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
alertQuotaName|string|告警指标名称|false|-
serviceCategory|string|服务分类|false|-
alertExpr|string|告警指标表达式|false|-
alertLevel|enum|告警级别 1:警告2：异常<br/>WARN -(1,"warning")<br/>EXCEPTION -(2,"exception")<br/>|false|-
alertGroupId|int32|告警组|false|-
noticeGroupId|int32|通知组|false|-
alertAdvice|string|告警建议|false|-
compareMethod|string|比较方式 !=;>;<|false|-
alertThreshold|int64|告警阀值|false|-
alertTactic|int32|告警策略 1:单次2：连续|false|-
intervalDuration|int32|间隔时长 单位分钟|false|-
triggerDuration|int32|触发时长 单位秒|false|-
serviceRoleName|string|No comments found.|false|-
quotaState|enum|null<br/>RUNNING -(1,"启用")<br/>STOPPED -(2,"未启用")<br/>|false|-
createTime|string|No comments found.|false|-
quotaStateCode|int32|No comments found.|false|-
alertGroupName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/quota/save --data '{
  "id": 549,
  "alertQuotaName": "nakesha.turner",
  "serviceCategory": "z6syma",
  "alertExpr": "atuu6c",
  "alertLevel": 1,
  "alertGroupId": 311,
  "noticeGroupId": 33,
  "alertAdvice": "bhozzt",
  "compareMethod": "k6gnmk",
  "alertThreshold": 893,
  "alertTactic": 621,
  "intervalDuration": 67,
  "triggerDuration": 11,
  "serviceRoleName": "nakesha.turner",
  "quotaState": 1,
  "createTime": "2022-12-08 08:56:33",
  "quotaStateCode": 800,
  "alertGroupName": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 814,
  "msg": "eoxb4d",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/alert/quota/update

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
alertQuotaName|string|告警指标名称|false|-
serviceCategory|string|服务分类|false|-
alertExpr|string|告警指标表达式|false|-
alertLevel|enum|告警级别 1:警告2：异常<br/>WARN -(1,"warning")<br/>EXCEPTION -(2,"exception")<br/>|false|-
alertGroupId|int32|告警组|false|-
noticeGroupId|int32|通知组|false|-
alertAdvice|string|告警建议|false|-
compareMethod|string|比较方式 !=;>;<|false|-
alertThreshold|int64|告警阀值|false|-
alertTactic|int32|告警策略 1:单次2：连续|false|-
intervalDuration|int32|间隔时长 单位分钟|false|-
triggerDuration|int32|触发时长 单位秒|false|-
serviceRoleName|string|No comments found.|false|-
quotaState|enum|null<br/>RUNNING -(1,"启用")<br/>STOPPED -(2,"未启用")<br/>|false|-
createTime|string|No comments found.|false|-
quotaStateCode|int32|No comments found.|false|-
alertGroupName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/quota/update --data '{
  "id": 825,
  "alertQuotaName": "nakesha.turner",
  "serviceCategory": "auyhqy",
  "alertExpr": "3z332o",
  "alertLevel": 1,
  "alertGroupId": 964,
  "noticeGroupId": 348,
  "alertAdvice": "l25gka",
  "compareMethod": "ghohne",
  "alertThreshold": 790,
  "alertTactic": 5,
  "intervalDuration": 624,
  "triggerDuration": 972,
  "serviceRoleName": "nakesha.turner",
  "quotaState": 1,
  "createTime": "2022-12-08 08:56:33",
  "quotaStateCode": 483,
  "alertGroupName": "nakesha.turner"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 728,
  "msg": "9rz8j6",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/alert/quota/delete

**Type:** GET

**Author:** gaodayu

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/alert/quota/delete --data '[
  313,
  99
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 240,
  "msg": "qmnbam",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群信息表
### 列表
**URL:** http://localhost:8081/api/cluster/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/list
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 185,
  "msg": "ajcrj9",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 配置好的集群列表
**URL:** http://localhost:8081/api/cluster/runningClusterList

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 配置好的集群列表

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/runningClusterList
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 839,
  "msg": "16ajzl",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/cluster/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/info/928
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 913,
  "msg": "n3m1ud",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/cluster/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
createBy|string|创建人|false|-
createTime|string|创建时间|false|-
clusterName|string|集群名称|false|-
clusterCode|string|集群编码|false|-
clusterFrame|string|集群框架|false|-
frameVersion|string|集群版本|false|-
clusterState|enum|集群状态 1:待配置2：正在运行<br/>RUNNING -(2,"正在运行")<br/>NEED_CONFIG -(1,"待配置")<br/>|false|-
frameId|int32|集群框架id|false|-
clusterManagerList|array|No comments found.|false|-
└─id|int32|主键|false|-
└─username|string|用户名|false|-
└─password|string|密码|false|-
└─email|string|邮箱|false|-
└─phone|string|手机号|false|-
└─createTime|string|创建时间|false|-
└─userType|int32|No comments found.|false|-
clusterStateCode|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/save --data '{
  "id": 387,
  "createBy": "bmzqaa",
  "createTime": "2022-12-08 08:56:33",
  "clusterName": "nakesha.turner",
  "clusterCode": "68009",
  "clusterFrame": "nca80b",
  "frameVersion": "2.6",
  "clusterState": 2,
  "frameId": 330,
  "clusterManagerList": [
    {
      "id": 303,
      "username": "nakesha.turner",
      "password": "ru1ooh",
      "email": "brendan.harris@yahoo.com",
      "phone": "208.224.7832",
      "createTime": "2022-12-08 08:56:33",
      "userType": 410
    }
  ],
  "clusterStateCode": 539
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 388,
  "msg": "aa5weu",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 更新集群状态
**URL:** http://localhost:8081/api/cluster/updateClusterState

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 更新集群状态

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
clusterState|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/updateClusterState?clusterState=0&clusterId=674
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 214,
  "msg": "2brni7",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/cluster/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
createBy|string|创建人|false|-
createTime|string|创建时间|false|-
clusterName|string|集群名称|false|-
clusterCode|string|集群编码|false|-
clusterFrame|string|集群框架|false|-
frameVersion|string|集群版本|false|-
clusterState|enum|集群状态 1:待配置2：正在运行<br/>RUNNING -(2,"正在运行")<br/>NEED_CONFIG -(1,"待配置")<br/>|false|-
frameId|int32|集群框架id|false|-
clusterManagerList|array|No comments found.|false|-
└─id|int32|主键|false|-
└─username|string|用户名|false|-
└─password|string|密码|false|-
└─email|string|邮箱|false|-
└─phone|string|手机号|false|-
└─createTime|string|创建时间|false|-
└─userType|int32|No comments found.|false|-
clusterStateCode|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/update --data '{
  "id": 208,
  "createBy": "3vx4wg",
  "createTime": "2022-12-08 08:56:33",
  "clusterName": "nakesha.turner",
  "clusterCode": "68009",
  "clusterFrame": "0vnxhj",
  "frameVersion": "2.6",
  "clusterState": 2,
  "frameId": 790,
  "clusterManagerList": [
    {
      "id": 815,
      "username": "nakesha.turner",
      "password": "d6rqk3",
      "email": "brendan.harris@yahoo.com",
      "phone": "208.224.7832",
      "createTime": "2022-12-08 08:56:33",
      "userType": 930
    }
  ],
  "clusterStateCode": 815
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 322,
  "msg": "okd6kf",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/cluster/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/delete --data '[
  262,
  441
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 895,
  "msg": "1d0xe4",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务操作指令主机表
### 列表
**URL:** http://localhost:8081/api/cluster/service/command/host/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
commandId|string|No comments found.|false|-
page|int32|No comments found.|false|-
pageSize|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/host/list?pageSize=10&commandId=11&page=1&clusterId=863
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 413,
  "msg": "es843a",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/cluster/service/command/host/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/cluster/service/command/host/info/74
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 453,
  "msg": "4w20lp",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/cluster/service/command/host/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
commandHostId|string|主键|false|-
hostname|string|主机|false|-
commandState|enum|命令状态 1：正在运行2：成功3：失败<br/>WAIT -(0,"待运行")<br/>RUNNING -(1,"正在运行")<br/>SUCCESS -(2,"成功")<br/>FAILED -(3,"失败")<br/>CANCEL -(4,"取消")<br/>|false|-
commandStateCode|int32|No comments found.|false|-
commandProgress|int32|命令进度|false|-
commandId|string|操作指令id|false|-
createTime|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/host/save --data '{
  "commandHostId": "11",
  "hostname": "16.134.58.123",
  "commandState": 0,
  "commandStateCode": 912,
  "commandProgress": 309,
  "commandId": "11",
  "createTime": "2022-12-08 08:56:33"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 578,
  "msg": "zy3ma3",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/cluster/service/command/host/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
commandHostId|string|主键|false|-
hostname|string|主机|false|-
commandState|enum|命令状态 1：正在运行2：成功3：失败<br/>WAIT -(0,"待运行")<br/>RUNNING -(1,"正在运行")<br/>SUCCESS -(2,"成功")<br/>FAILED -(3,"失败")<br/>CANCEL -(4,"取消")<br/>|false|-
commandStateCode|int32|No comments found.|false|-
commandProgress|int32|命令进度|false|-
commandId|string|操作指令id|false|-
createTime|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/host/update --data '{
  "commandHostId": "11",
  "hostname": "16.134.58.123",
  "commandState": 0,
  "commandStateCode": 125,
  "commandProgress": 33,
  "commandId": "11",
  "createTime": "2022-12-08 08:56:33"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 792,
  "msg": "yd4dsy",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/cluster/service/command/host/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/cluster/service/command/host/delete --data '[
  916,
  260
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 777,
  "msg": "ay586z",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 框架服务角色表
### 查询服务对应的角色列表
**URL:** http://localhost:8081/api/frame/service/role/getServiceRoleList

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 查询服务对应的角色列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
serviceIds|string|No comments found.|false|-
serviceRoleType|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/service/role/getServiceRoleList?serviceRoleType=540&serviceIds=if7enr&clusterId=349
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 244,
  "msg": "j2ijad",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 
**URL:** http://localhost:8081/api/frame/service/role/getNonMasterRoleList

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
serviceIds|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/service/role/getNonMasterRoleList?serviceIds=3ilsvu&clusterId=824
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 293,
  "msg": "w68un9",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 依据服务查询对应的服务角色信息
**URL:** http://localhost:8081/api/frame/service/role/getServiceRoleByServiceName

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 依据服务查询对应的服务角色信息

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
alertGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/service/role/getServiceRoleByServiceName?clusterId=618&alertGroupId=422
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 446,
  "msg": "dbs00l",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/api/frame/service/role/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/api/frame/service/role/info/398
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 282,
  "msg": "r64sfd",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/api/frame/service/role/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
serviceId|int32|服务id|false|-
serviceRoleName|string|角色名称|false|-
serviceRoleType|enum|角色类型 1:master2:worker3:client<br/>MASTER -(1,"master")<br/>WORKER -(2,"worker")<br/>CLIENT -(3,"client")<br/>SLAVE -(4,"slave")<br/>|false|-
cardinality|string|1  1+|false|-
serviceRoleJson|string|No comments found.|false|-
serviceRoleJsonMd5|string|No comments found.|false|-
frameCode|string|No comments found.|false|-
jmxPort|string|No comments found.|false|-
hosts|array|No comments found.|false|-
logFile|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/service/role/save --data '{
  "id": 793,
  "serviceId": 142,
  "serviceRoleName": "nakesha.turner",
  "serviceRoleType": 1,
  "cardinality": "vsl0iq",
  "serviceRoleJson": "59tomd",
  "serviceRoleJsonMd5": "4fv9dv",
  "frameCode": "68009",
  "jmxPort": "cecle5",
  "hosts": [
    "yrv7xd"
  ],
  "logFile": "jq23aa"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 252,
  "msg": "ikcfhp",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/api/frame/service/role/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
serviceId|int32|服务id|false|-
serviceRoleName|string|角色名称|false|-
serviceRoleType|enum|角色类型 1:master2:worker3:client<br/>MASTER -(1,"master")<br/>WORKER -(2,"worker")<br/>CLIENT -(3,"client")<br/>SLAVE -(4,"slave")<br/>|false|-
cardinality|string|1  1+|false|-
serviceRoleJson|string|No comments found.|false|-
serviceRoleJsonMd5|string|No comments found.|false|-
frameCode|string|No comments found.|false|-
jmxPort|string|No comments found.|false|-
hosts|array|No comments found.|false|-
logFile|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/service/role/update --data '{
  "id": 975,
  "serviceId": 538,
  "serviceRoleName": "nakesha.turner",
  "serviceRoleType": 1,
  "cardinality": "gqmygr",
  "serviceRoleJson": "y8n7cu",
  "serviceRoleJsonMd5": "47tjtd",
  "frameCode": "68009",
  "jmxPort": "2cndoq",
  "hosts": [
    "857ngc"
  ],
  "logFile": "0tvzkf"
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 500,
  "msg": "0exohy",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/api/frame/service/role/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 删除

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
ids|array|No comments found.,[array of int32]|false|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/api/frame/service/role/delete --data '[
  580,
  986
]'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 726,
  "msg": "h3vyu0",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 集群服务表
### 列表
**URL:** http://localhost:8081/cluster/service/instance/list

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/list?clusterId=77
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 569,
  "msg": "ujya08",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 获取服务角色类型列表
**URL:** http://localhost:8081/cluster/service/instance/getServiceRoleType

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取服务角色类型列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/getServiceRoleType?serviceInstanceId=380
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 429,
  "msg": "p8gdhf",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 获取服务角色类型列表
**URL:** http://localhost:8081/cluster/service/instance/configVersionCompare

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取服务角色类型列表

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceId|int32|No comments found.|false|-
roleGroupId|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/configVersionCompare?roleGroupId=829&serviceInstanceId=730
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 346,
  "msg": "34gan3",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/service/instance/info/{id}

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|No comments found.|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/info/610
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 571,
  "msg": "be6s8k",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 信息
**URL:** http://localhost:8081/cluster/service/instance/downloadClientConfig

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 信息

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
clusterId|int32|No comments found.|false|-
serviceName|string|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/downloadClientConfig?clusterId=555&serviceName=nakesha.turner
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 445,
  "msg": "oetcbh",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 保存
**URL:** http://localhost:8081/cluster/service/instance/save

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 保存

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
clusterId|int32|集群id|false|-
serviceName|string|服务名称|false|-
label|string|No comments found.|false|-
serviceState|enum|服务状态 1、待安装 2：正在运行  3：存在告警 4:存在异常<br/>WAIT_INSTALL -(1,"待安装")<br/>RUNNING -(2,"正常")<br/>EXISTS_ALARM -(3,"存在告警")<br/>EXISTS_EXCEPTION -(4,"存在异常")<br/>|false|-
serviceStateCode|int32|No comments found.|false|-
updateTime|string|更新时间|false|-
createTime|string|创建时间|false|-
needRestart|enum|null<br/>NO -(1,false)<br/>YES -(2,true)<br/>|false|-
frameServiceId|int32|No comments found.|false|-
dashboardUrl|string|No comments found.|false|-
alertNum|int32|No comments found.|false|-
sortNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/instance/save --data '{
  "id": 71,
  "clusterId": 53,
  "serviceName": "nakesha.turner",
  "label": "9mprfs",
  "serviceState": 1,
  "serviceStateCode": 582,
  "updateTime": "2022-12-08 08:56:33",
  "createTime": "2022-12-08 08:56:33",
  "needRestart": 1,
  "frameServiceId": 512,
  "dashboardUrl": "www.sunny-kub.com",
  "alertNum": 205,
  "sortNum": 414
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 489,
  "msg": "r8tpr5",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 修改
**URL:** http://localhost:8081/cluster/service/instance/update

**Type:** GET

**Author:** dygao2

**Content-Type:** application/json; charset=utf-8

**Description:** 修改

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
clusterId|int32|集群id|false|-
serviceName|string|服务名称|false|-
label|string|No comments found.|false|-
serviceState|enum|服务状态 1、待安装 2：正在运行  3：存在告警 4:存在异常<br/>WAIT_INSTALL -(1,"待安装")<br/>RUNNING -(2,"正常")<br/>EXISTS_ALARM -(3,"存在告警")<br/>EXISTS_EXCEPTION -(4,"存在异常")<br/>|false|-
serviceStateCode|int32|No comments found.|false|-
updateTime|string|更新时间|false|-
createTime|string|创建时间|false|-
needRestart|enum|null<br/>NO -(1,false)<br/>YES -(2,true)<br/>|false|-
frameServiceId|int32|No comments found.|false|-
dashboardUrl|string|No comments found.|false|-
alertNum|int32|No comments found.|false|-
sortNum|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json; charset=utf-8' -i http://localhost:8081/cluster/service/instance/update --data '{
  "id": 819,
  "clusterId": 831,
  "serviceName": "nakesha.turner",
  "label": "6pesby",
  "serviceState": 1,
  "serviceStateCode": 143,
  "updateTime": "2022-12-08 08:56:33",
  "createTime": "2022-12-08 08:56:33",
  "needRestart": 1,
  "frameServiceId": 87,
  "dashboardUrl": "www.sunny-kub.com",
  "alertNum": 650,
  "sortNum": 846
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 392,
  "msg": "c8vo5n",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### 删除
**URL:** http://localhost:8081/cluster/service/instance/delete

**Type:** GET

**Author:** dygao2

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 删除

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
serviceInstanceIds|int32|No comments found.|false|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/cluster/service/instance/delete?serviceInstanceIds=346
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 383,
  "msg": "22smt5",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

## 登录/下线操作
### login登录操作
**URL:** http://localhost:8081/login

**Type:** GET


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** login登录操作

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
username|string|    user name|true|-
password|string|user password|true|-

**Request-example:**
```
curl -X GET -i http://localhost:8081/login?username=nakesha.turner&password=hq1jm9
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 728,
  "msg": "rlgreu",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```

### sign out 下线操作
**URL:** http://localhost:8081/signOut

**Type:** POST


**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** sign out 下线操作

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int32|主键|false|-
username|string|用户名|false|-
password|string|密码|false|-
email|string|邮箱|false|-
phone|string|手机号|false|-
createTime|string|创建时间|false|-
userType|int32|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://localhost:8081/signOut --data 'createTime=2022-12-08 08:56:33&password=z70ly5&phone=208.224.7832&username=nakesha.turner&id=511&userType=238&email=brendan.harris@yahoo.com'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
code|int32|No comments found.|-
msg|string|No comments found.|-
data|object|No comments found.|-

**Response-example:**
```
{
  "code": 922,
  "msg": "cyu9ll",
  "data": {
    "waring": "You may have used non-display generics."
  }
}
```


