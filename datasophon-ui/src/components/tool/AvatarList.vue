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
  <div class="avatar-list">
    <slot>
    </slot>
  </div>
</template>

<script>
import AAvatar from 'ant-design-vue/es/avatar/Avatar'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
const Item = {
  name: 'AvatarListItem',
  props: {
    size: {
      type: String,
      required: false,
      default: 'small'
    },
    src: {
      type: String,
      required: true
    },
    tips: {
      type: String,
      required: false
    }
  },
  methods: {
    renderAvatar (h, size, src) {
      return h(AAvatar, {props: {size: size, src: src}}, [])
    }
  },
  render (h) {
    const avatar = this.renderAvatar(h, this.$props.size, this.$props.src)
    return h(
      'li',
      {class: 'avatar-item'},
      [this.$props.tips ? h(ATooltip, {props: {title: this.$props.tips}}, [avatar]) : avatar]
    )
  }
}
export default {
  name: 'AvatarList',
  Item: Item
}
</script>

<style lang="less" scoped>
  .avatar-list {
    display: inline-block;
      display: inline-block;
      margin-left: 8px;
      font-size: 0;
      .avatar-item {
        display: inline-block;
        font-size: 14px;
        margin-left: -8px;
        width: 20px;
        height: 20px;
        :global {
          .ant-avatar {
            border: 1px solid #fff;
            width: 20px;
            height: 20px;
          }
        }
      }
  }
</style>
