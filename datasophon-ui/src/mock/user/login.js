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

import Mock from 'mockjs'
import '@/mock/extend'

const user = Mock.mock({
  name: '@ADMIN',
  avatar: '@AVATAR',
  address: '@CITY',
  position: '@POSITION'
})
Mock.mock(`${process.env.VUE_APP_API_BASE_URL}/login`, 'post', ({body}) => {
  let result = {data: {}}
  const {name, password} = JSON.parse(body)

  let success = false

  if (name === 'admin' && password === '888888') {
    success = true
    result.data.permissions = [{id: 'queryForm', operation: ['add', 'edit']}]
    result.data.roles = [{id: 'admin', operation: ['add', 'edit', 'delete']}]
  } else if (name === 'test' || password === '888888') {
    success = true
    result.data.permissions = [{id: 'queryForm', operation: ['add', 'edit']}]
    result.data.roles = [{id: 'test', operation: ['add', 'edit', 'delete']}]
  } else {
    success = false
  }

  if (success) {
    result.code = 0
    result.message = Mock.mock('@TIMEFIX').CN + '，欢迎回来'
    result.data.user = user
    result.data.token = 'Authorization:' + Math.random()
    result.data.expireAt = new Date(new Date().getTime() + 30 * 60 * 1000)
  } else {
    result.code = -1
    result.message = '账户名或密码错误（admin/888888 or test/888888）'
  }
  return result
})
