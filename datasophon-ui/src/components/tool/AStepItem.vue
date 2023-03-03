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
  <div
    :class="['step-item', link ? 'linkable' : null]"
    @click="go"
  >
    <span :style="titleStyle">{{title}}</span>
    <a-icon v-if="icon" :style="iconStyle" :type="icon" />
    <slot></slot>
  </div>
</template>

<script>
const Group = {
  name: 'AStepItemGroup',
  props: {
    align: {
      type: String,
      default: 'center',
      validator(value) {
        return ['left', 'center', 'right'].indexOf(value) != -1
      }
    }
  },
  render (h) {
    return h(
      'div',
      {attrs: {style: `text-align: ${this.align}; margin-top: 8px`}},
      [h('div', {attrs: {style: 'text-align: left; display: inline-block;'}}, [this.$slots.default])]
    )
  }
}

export default {
  name: 'AStepItem',
  Group: Group,
  props: ['title', 'icon', 'link', 'titleStyle', 'iconStyle'],
  methods: {
    go () {
      const link = this.link
      if (link) {
        this.$router.push(link)
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .step-item{
    cursor: pointer;
  }
  :global{
    .ant-steps-item-process{
      .linkable{
      }
    }
  }
</style>
