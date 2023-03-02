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
  <div style="padding-top: 20px">
    <a-form
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
      :form="form"
      class="p0-32-10-32 form-content"
    >
      <a-form-item label="角色组名称">
        <a-input
          id="error"
          v-decorator="[
            'roleGroupName',
            { rules: [{ required: true, message: '角色组名称不能为空!' }] },
          ]"
          placeholder="请输入角色组名称"
        />
      </a-form-item>
    </a-form>
    <div class="ant-modal-confirm-btns-new">
      <a-button
        style="margin-right: 10px"
        type="primary"
        @click.stop="handleSubmit"
        :loading="loading"
        >确认</a-button
      >
      <a-button @click.stop="formCancel">取消</a-button>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    grouopObj:{
      type:Object,
      default: function () {
        return {};
      },
    },
    callBack:Function
  },
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 19 },
      },
      form: this.$form.createForm(this),
      value1: "",
      loading: false,
      cateList: [], //类型
      GroupList:[]  //列表
    };
  },
  watch: {},
  methods: {
    formCancel() {
      this.$destroyAll();
    },
    handleSubmit(e) {
      const _this = this
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          const params = {
            "roleGroupName": values.roleGroupName, 
            "roleGroupId": this.grouopObj.id,
          }
          this.loading = true;
          this.$axiosPost(global.API.reNameGroup, params).then((res) => {  
            this.loading = false;
            if (res.code !== 200) return
            this.$message.success('修改成功')
            this.$destroyAll();
            _this.callBack(params);
          }).catch((err) => {});
        }
      });
    },
    initData () {
      if (JSON.stringify(this.grouopObj) !== "{}") {
        this.form.getFieldsValue([
          "roleGroupName"
        ]);
        this.form.setFieldsValue({
          roleGroupName: this.grouopObj.roleGroupName
        });
      } 
    }
  },
  mounted() {
    this.initData()
  },
};
</script>
<style lang="less" scoped>
</style>
