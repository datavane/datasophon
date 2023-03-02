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

/* 
工具类
*/

// 生成十位字母加数字随机数
const getRandom = () => {
  // 生成十位字母加数字随机数
  let arr = [];
  for (let i = 0; i < 1000; i++) {
    let n = Math.random()
      .toString(36)
      .substr(2, 5);
    arr.push(n);
  }
  // 去重
  let ukeys = [];
  for (let i = 0; i < arr.length; i++) {
    if (ukeys.indexOf(arr[i]) === -1) {
      ukeys.push(arr[i]);
    }
  }
  let keys = "";
  for (let i = 0; i < ukeys.length; i++) {
    keys += ukeys[i];
  }
  return keys.substr(0, 5);
};

export default {
  getRandom // 生成十位字母加数字随机数
}
