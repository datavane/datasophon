<!--
 * @Author: mjzhu
 * @Date: 2022-06-13 14:04:05
 * @LastEditTime: 2022-07-13 14:36:07
 * @FilePath: \ddh-ui\src\components\logs\index.vue
-->
<template>
  <div class="logs-container">
    <Editor :propsCode="logData" ref="child"/>
    <div v-if="!hideCancel" class="footer">
      <a-button class="mgr10" @click="getModal">刷新</a-button>
    </div>
  </div>
</template>
<script>

import Editor from "@/components/editor";

export default {
  name: "LogsContainer",
  components: {
    Editor
  },
  props: { logData: String, hideCancel: Boolean },
  data() {
    return {
    };
  },
  watch: {
    
  },
  computed: {
  },
  methods: {
    getModal() {
      this.$emit('getLog');
      
      this.$refs.child.handleCancel();
    },
  },
};
</script>
<style lang="less" scoped>
.logs-container {
  height: 100%;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  .footer {
    // width: 1300px;
    margin: 0 100px;
    height: 64px;
    background: rgba(242, 244, 247, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    button {
      width: 86px;
    }
    /deep/
      .ant-btn.ant-btn-loading:not(.ant-btn-circle):not(.ant-btn-circle-outline):not(.ant-btn-icon-only) {
      padding-left: 20px;
    }
  }
}
</style>