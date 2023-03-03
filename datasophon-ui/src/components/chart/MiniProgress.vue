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
  <div class="mini-progress">
    <a-tooltip :title="'目标值：' + target + '%'">
      <div class="target" :style="{left: target + '%'}">
        <span :style="{backgroundColor: color}" />
        <span :style="{backgroundColor: color}" />
      </div>
    </a-tooltip>
    <div class="wrap">
      <div class="progress" :style="{backgroundColor: color, width: percent + '%', height: height}" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'MiniProgress',
  props: ['target', 'color', 'percent', 'height']
}
</script>

<style lang="less"  scoped>
  .mini-progress {
    padding: 5px 0;
    position: relative;
    width: 100%;
    .wrap {
      background-color: @layout-bg-color;
      position: relative;
    }
    .progress {
      transition: all 0.4s cubic-bezier(0.08, 0.82, 0.17, 1) 0s;
      border-radius: 1px 0 0 1px;
      background-color: #13C2C2;
      width: 0;
      height: 100%;
    }
    .target {
      position: absolute;
      top: 0;
      bottom: 0;
      span {
        border-radius: 100px;
        position: absolute;
        top: 0;
        left: 0;
        height: 4px;
        width: 2px;
      }
      span:last-child {
        top: auto;
        bottom: 0;
      }
    }
  }
</style>
