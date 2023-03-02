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
  <a-checkable-tag @change="$emit('click')" class="tag-default" v-model="checked">
    <slot></slot>
  </a-checkable-tag>
</template>

<script>
export default {
  name: 'TagSelectOption',
  props: {
    size: {
      type: String,
      required: false,
      default: 'default'
    }
  },
  data () {
    return {
      checked: false,
      isTagSelectOption: true
    }
  }
}
</script>

<style lang="less" scoped>
  .tag-default{
    font-size: 14px;
    padding: 0 8px;
    height: auto;
    margin-right: 24px;
  }
</style>
