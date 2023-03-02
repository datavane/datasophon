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
    <a-card class="result-error" :bordered="false">
      <result style="margin-bottom: 16px; margin-top: 48px" :is-success="false" :title="title" :description="description">
        <template slot="action">
          <a-button type="primary" >返回修改</a-button>
        </template>
        <div>
          <div style="fontSize: 16px; fontWeight: 500; marginBottom: 16px">
            您提交的内容有如下错误：
          </div>
          <div style="margin-bottom: 16px">
            <a-icon class="error-icon" type="close-circle-o"/>
            您的账户已被冻结
            <a style="margin-left: 16px">立即解冻 <a-icon type="right" /></a>
          </div>
          <div>
            <a-icon class="error-icon" type="close-circle-o"/>
            您的账户还不具备申请资格
            <a style="margin-left: 16px">立即升级 <a-icon type="right" /></a>
          </div>
        </div>
      </result>
    </a-card>
</template>

<script>
import Result from '@/components/result/Result'
export default {
  name: 'Error',
  components: {Result},
  data () {
    return {
      title: '提交失败',
      description: '请核对并修改以下信息后，再重新提交。'
    }
  }
}
</script>

<style scoped lang="less">
.result-error{
  .error-icon{
    color: @red-6;
    margin-right: 8px
  }
}
</style>
