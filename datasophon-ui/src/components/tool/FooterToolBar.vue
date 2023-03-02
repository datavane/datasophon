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
  <div class="toolbar">
    <div style="float: left">
      <slot name="extra"></slot>
    </div>
    <div style="float: right">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FooterToolBar'
}
</script>

<style lang="less" scoped>
  .toolbar{
    position: fixed;
    width: 100%;
    bottom: 0;
    right: 0;
    box-shadow: 0 -1px 2px @shadow-color;
    background: @base-bg-color;
    border-top: 1px solid @border-color-split;
    padding: 12px 24px;
    z-index: 9;
  }
</style>
