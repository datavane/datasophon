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
  <div class="task-group">
    <div class="task-head">
      <h3 class="title"><span v-if="count">{{count}}</span>{{title}}</h3>
      <div class="actions" style="float: right">
        <a-icon class="add" type="plus" draggable="true"/>
        <a-icon class="more" style="margin-left: 8px" type="ellipsis" />
      </div>
    </div>
    <div class="task-content">
      <draggable :options="dragOptions">
        <slot></slot>
      </draggable>
    </div>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'

const dragOptions = {
  sort: true,
  scroll: true,
  scrollSpeed: 2,
  animation: 150,
  ghostClass: 'dragable-ghost',
  chosenClass: 'dragable-chose',
  dragClass: 'dragable-drag'
}

export default {
  name: 'TaskGroup',
  components: {Draggable},
  props: ['title', 'group'],
  data () {
    return {
      dragOptions: {...dragOptions, group: this.group}
    }
  },
  computed: {
    count () {
      return this.$slots.default.length
    }
  }
}
</script>

<style lang="less">
  .task-group{
    width: 33.33%;
    padding: 8px 8px;
    background-color: @background-color-light;
    border-radius: 6px;
    border: 1px solid @shadow-color;
    .task-head{
      margin-bottom: 8px;
      .title{
        display: inline-block;
        span{
          display: inline-block;
          border-radius: 10px;
          margin: 0 8px;
          font-size: 12px;
          padding: 2px 6px;
          background-color: @base-bg-color;
        }
      }
      .actions{
        display: inline-block;
        float: right;
        font-size: 18px;
        font-weight: bold;
        i{
          cursor: pointer;
        }
      }
    }
  }
</style>
