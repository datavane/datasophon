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

const TabsPagePlugin = {
  install(Vue) {
    Vue.mixin({
      methods: {
        $closePage(closeRoute, nextRoute) {
          const event = new CustomEvent('page:close', {detail:{closeRoute, nextRoute}})
          window.dispatchEvent(event)
        },
        $refreshPage(route) {
          const path = typeof route === 'object' ? route.path : route
          const event = new CustomEvent('page:refresh', {detail:{pageKey: path}})
          window.dispatchEvent(event)
        },
        $openPage(route, title) {
          this.$setPageTitle(route, title)
          this.$router.push(route)
        },
        $setPageTitle(route, title) {
          if (title) {
            let path = typeof route === 'object' ? route.path : route
            path = path && path.split('?')[0]
            this.$store.commit('setting/setCustomTitle', {path, title})
          }
        }
      },
      computed: {
        customTitle() {
          const customTitles = this.$store.state.setting.customTitles
          const path = this.$route.path.split('?')[0]
          const custom = customTitles.find(item => item.path === path)
          return custom && custom.title
        }
      }
    })
  }
}

export default TabsPagePlugin
