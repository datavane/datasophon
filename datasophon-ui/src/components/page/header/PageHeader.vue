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
  <div :class="['page-header', layout, pageWidth]">
    <div class="page-header-wide">
      <!-- <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item :key="index" v-for="(item, index) in breadcrumb">
            <span>{{item}}</span>
          </a-breadcrumb-item>
        </a-breadcrumb>
      </div> -->
      <div v-if="false" class="detail">
        <div class="main">
          <div class="row">
            <h1 v-if="showPageTitle && title" class="title">{{title}}</h1>
            <div class="action"><slot name="action"></slot></div>
          </div>
          <div class="row">
            <div v-if="this.$slots.content" class="content">
              <div v-if="avatar" class="avatar"><a-avatar :src="avatar" :size="72" /></div>
              <slot name="content"></slot>
            </div>
            <div v-if="this.$slots.extra" class="extra"><slot name="extra"></slot></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'PageHeader',
  props: {
    title: {
      type: [String, Boolean],
      required: false
    },
    breadcrumb: {
      type: Array,
      required: false
    },
    logo: {
      type: String,
      required: false
    },
    avatar: {
      type: String,
      required: false
    },
  },
  computed: {
    ...mapState('setting', ['layout', 'showPageTitle', 'pageWidth'])
  }
}
</script>

<style lang="less" scoped>
  @import "index";
</style>
