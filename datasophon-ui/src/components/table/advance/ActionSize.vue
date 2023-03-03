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
  <div class="action-size" ref="root">
    <a-tooltip title="密度">
      <a-dropdown placement="bottomCenter" :trigger="['click']" :get-popup-container="() => $refs.root">
        <a-icon class="action" type="column-height" />
        <a-menu :selected-keys="[value]" slot="overlay" @click="onClick">
          <a-menu-item key="default">
            默认
          </a-menu-item>
          <a-menu-item key="middle">
            中等
          </a-menu-item>
          <a-menu-item key="small">
            紧密
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </a-tooltip>
  </div>
</template>

<script>
export default {
  name: 'ActionSize',
  props: ['value'],
  inject: ['table'],
  data() {
    return {
      selectedKeys: ['middle']
    }
  },
  methods: {
    onClick({key}) {
      this.$emit('input', key)
    }
  }
}
</script>

<style scoped lang="less">
.action-size{
  display: inline-block;
}
</style>
