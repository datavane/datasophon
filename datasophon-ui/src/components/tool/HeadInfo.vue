<!--
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
 -->

<template>
  <div class="head-info">
    <span>{{title}}</span>
    <p>{{content}}</p>
  </div>
</template>

<script>
export default {
  name: 'HeadInfo',
  props: ['title', 'content', 'bordered']
}
</script>

<style lang="less" scoped>
  .head-info{
    text-align: center;
    padding: 0 24px;
    flex-grow: 1;
    flex-shrink: 0;
    align-self: center;
    span{
      color: @text-color-second;
      display: inline-block;
      font-size: 14px;
      margin-bottom: 4px;
    }
    p{
      color: @text-color;
      font-size: 24px;
      margin: 0;
    }
  }
</style>
