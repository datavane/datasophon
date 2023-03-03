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
  <page-layout :desc="desc" :linkList="linkList">
    <div v-if="this.extraImage && !isMobile" slot="extra" class="extraImg">
      <img :src="extraImage"/>
    </div>
    <page-toggle-transition :disabled="animate.disabled" :animate="animate.name" :direction="animate.direction">
        <router-view ref="page" />
    </page-toggle-transition>
  </page-layout>
</template>

<script>
import PageLayout from './PageLayout'
import PageToggleTransition from '../components/transition/PageToggleTransition';
import {mapState} from 'vuex'

export default {
  name: 'PageView',
  components: {PageToggleTransition, PageLayout},
  data () {
    return {
      page: {}
    }
  },
  computed: {
    ...mapState('setting', ['isMobile', 'multiPage', 'animate']),
    desc() {
      return this.page.desc
    },
    linkList() {
      return this.page.linkList
    },
    extraImage() {
      return this.page.extraImage
    }
  },
  mounted () {
    this.page = this.$refs.page
  },
  updated () {
    this.page = this.$refs.page
  }
}
</script>

<style lang="less" scoped>
  .extraImg{
    margin-top: -60px;
    text-align: center;
    width: 195px;
    img{
      width: 100%;
    }
  }
</style>
