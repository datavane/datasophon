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

import {loadRoutes, loadGuards, setAppOptions} from '@/utils/routerUtil'
import {loadInterceptors} from '@/utils/request'
import guards from '@/router/guards'
import interceptors from '@/utils/axios-interceptors'

/**
 * 启动引导方法
 * 应用启动时需要执行的操作放在这里
 * @param router 应用的路由实例
 * @param store 应用的 vuex.store 实例
 * @param i18n 应用的 vue-i18n 实例
 * @param i18n 应用的 message 实例
 */
function bootstrap({router, store, i18n, message}) {
  // 设置应用配置
  setAppOptions({router, store, i18n})
  // 加载 axios 拦截器
  loadInterceptors(interceptors, {router, store, i18n, message})
  // 加载路由
  loadRoutes()
  // 加载路由守卫
  loadGuards(guards, {router, store, i18n, message})
}

export default bootstrap
