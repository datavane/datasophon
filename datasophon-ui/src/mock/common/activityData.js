/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import {users, groups} from './index'

const events = [
  {
    type: 0,
    event: '八月迭代'
  },
  {
    type: 1,
    event: '留言'
  },
  {
    type: 2,
    event: '项目进展'
  }
]

const activities = users.map((user, index) => {
  return {
    user: Object.assign({}, user, {group: groups[user.groupId]}),
    activity: events[index % events.length],
    template: ''
  }
})

const templates = [
  (user, activity) => { return `${user.name} 在 <a >${user.group}</a> 新建项目 <a>${activity.event}</a>` },
  (user, activity) => { return `${user.name} 在 <a >${user.group}</a> 发布了 <a>${activity.event}</a>` },
  (user, activity) => { return `${user.name} 将 <a >${activity.event}</a> 更新至已发布状态` }
]

export {activities, templates}
