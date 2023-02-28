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

/**
 * 给对象注入属性
 * @param keys 属性key数组， 如 keys = ['config', 'path'] , 则会给对象注入 object.config.path 的属性
 * @param value 属性值
 * @returns {Object}
 */
Object.defineProperty(Object.prototype, 'assignProps', {
  writable: false,
  enumerable: false,
  configurable: true,
  value: function (keys, value) {
    let props = this
    for (let i = 0; i < keys.length; i++) {
      let key = keys[i]
      if (i == keys.length - 1) {
        props[key] = value
      } else {
        props[key] = props[key] == undefined ? {} : props[key]
        props = props[key]
      }
    }
    return this
  }
})
