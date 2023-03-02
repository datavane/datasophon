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

import store from '@/store'

// 过滤文件类型
const fileTypeFilter = function(data) {
  let val = ''
  store.state.fileTypes.forEach(item => {
    if(item.value === data) val = item.name
  })
  return val
}

// 处理时间格式
const timeFormat = (value, format) => {
  let date = new Date(value);
  let y = date.getFullYear();
  let m = date.getMonth() + 1;
  let d = date.getDate();
  let h = date.getHours();
  let min = date.getMinutes();
  let s = date.getSeconds();
  let result = "";
  if (format == undefined) {
    result = `${y}-${m < 10 ? "0" + m : m}-${d < 10 ? "0" + d : d} ${
      h < 10 ? "0" + h : h
    }:${min < 10 ? "0" + min : min}:${s < 10 ? "0" + s : s}`;
  }
  if (format == "yyyy-MM-dd") {
    result = `${y}-${m < 10 ? "0" + m : m}-${d < 10 ? "0" + d : d}`;
  }
  if (format == "yyyy-MM") {
    result = `${y}-${m < 10 ? "0" + m : m}`;
  }
  if (format == "MM-dd") {
    result = ` ${m < 10 ? "0" + m : m}-${d < 10 ? "0" + d : d}`;
  }
  if (format == "hh:mm") {
    result = ` ${h < 10 ? "0" + h : h}:${min < 10 ? "0" + min : min}`;
  }
  if (format == "hh:mm:ss") {
    result = ` ${h < 10 ? "0" + h : h}:${min < 10 ? "0" + min : min}:${s < 10 ? "0" + s : s}`;
  }
  if (format == "yyyy") {
    result = `${y}`;
  }
  if (format == "yyyy-MM-dd hh:mm:ss") {
    result = `${y}-${m < 10 ? "0" + m : m}-${d < 10 ? "0" + d : d}  ${h < 10 ? "0" + h : h}:${min < 10 ? "0" + min : min}:${s < 10 ? "0" + s : s}`;
  }
  if (format == "年月日") {
    result = `${y}年${m}月${d}日`;
  }
  return result;
};

// 过滤小数点位数
const tofixed = function(val, num = 2) {
  let result = ''
  if(isNaN(val)) {
    result = val.toFixed(num)
  }else {
    result = Number(val).toFixed(num)
  }
  return result
}

export { timeFormat, fileTypeFilter, tofixed }
