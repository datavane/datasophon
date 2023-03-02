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
  <div class="bar">
    <h4>{{title}}</h4>
    <div class="chart">
      <v-chart :force-fit="true" height="312" :data="data" :padding="[24, 0, 0, 0]">
        <v-tooltip />
        <v-axis />
        <v-bar position="x*y"/>
      </v-chart>
    </div>
  </div>
</template>

<script>

const data = []
for (let i = 0; i < 12; i += 1) {
  data.push({
    x: `${i + 1}月`,
    y: Math.floor(Math.random() * 1000) + 200
  })
}
const tooltip = [
  'x*y',
  (x, y) => ({
    name: x,
    value: y
  })
]

const scale = [{
  dataKey: 'x',
  min: 2
}, {
  dataKey: 'y',
  title: '时间',
  min: 1,
  max: 22
}]
export default {
  name: 'Bar',
  props: ['title'],
  data () {
    return {
      data,
      scale,
      tooltip
    }
  }
}
</script>

<style scoped lang="less">
  .bar{
    position: relative;
    .chart{
    }
  }
</style>
