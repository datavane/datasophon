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

// 语句模式
const MODE = {
  STATEMENTS: 's', //语句模式
  PHRASAL: 'p', //词组模式
}

const VueI18nPlugin = {
  install: function (Vue) {
    Vue.mixin({
      methods: {
        $ta(syntaxKey, mode) {
          let _mode = mode || MODE.STATEMENTS
          let keys = syntaxKey.split('|')
          let _this = this
          let locale = this.$i18n.locale
          let message = ''
          let splitter = locale == 'US' ? ' ' : ''
          // 拼接 message
          keys.forEach(key => {
            message += _this.$t(key) + splitter
          })
          // 英文环境语句模式下，转换单词大小写
          if (keys.length > 0 && _mode == MODE.STATEMENTS && locale == 'US') {
            message = message.charAt(0).toUpperCase() + message.toLowerCase().substring(1)
          }
          return message
        }
      }
    })
  }
}
export default VueI18nPlugin
