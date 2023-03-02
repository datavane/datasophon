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
  <div class="form-row">
    <div class="label">
      <span>{{label}}</span>
    </div>
    <div class="content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FormRow',
  props: ['label']
}
</script>

<style lang="less" scoped>
  .form-row{
    display: flex;
    border-bottom: 1px dashed @border-color-base;
    margin-bottom: 16px;
    .label {
      color: @title-color;
      font-size: 14px;
      margin-right: 24px;
      flex: 0 0 auto;
      text-align: right;
      & > span {
        display: inline-block;
        height: 39px;
        line-height: 39px;
        &:after {
          content: '：';
        }
      }
    }
    .content {
      flex: 1 1 0;
      :global {
        .ant-form-item:last-child {
          margin-right: 0;
        }
        .ant-form-item {
          margin-bottom: 0px;
        }
      }
    }
  }
</style>
