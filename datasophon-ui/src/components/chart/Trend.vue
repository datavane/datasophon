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
  <div class="chart-trend">
    {{term}}
    <span>{{rate}}%</span>
    <span :class="['chart-trend-icon', trend]" style=""><a-icon :type="'caret-' + trend" /></span>
  </div>
</template>

<script>
export default {
  name: 'Trend',
  props: {
    term: {
      type: String,
      required: true
    },
    target: {
      type: Number,
      required: false,
      default: 0
    },
    value: {
      type: Number,
      required: false,
      default: 0
    },
    isIncrease: {
      type: Boolean,
      required: false,
      default: null
    },
    percent: {
      type: Number,
      required: false,
      default: null
    },
    scale: {
      type: Number,
      required: false,
      default: 2
    }
  },
  data () {
    return {
      trend: this.isIncrease ? 'up' : 'down',
      rate: this.percent
    }
  },
  created () {
    this.trend = this.caulateTrend()
    this.rate = this.caulateRate()
  },
  methods: {
    caulateRate () {
      return (this.percent === null ? Math.abs(this.value - this.target) * 100 / this.target : this.percent).toFixed(this.scale)
    },
    caulateTrend () {
      let isIncrease = this.isIncrease === null ? this.value >= this.target : this.isIncrease
      return isIncrease ? 'up' : 'down'
    }
  }
}
</script>

<style lang="less" scoped>
  .chart-trend{
    display: inline-block;
    font-size: 14px;
    .chart-trend-icon{
      font-size: 12px;
      &.up{
        color: @red-6;
      }
      &.down{
        color: @green-6;
      }
    }
  }
</style>
