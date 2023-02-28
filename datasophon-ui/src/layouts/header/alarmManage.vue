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
  <div class="cluster-setting mgr10 mgl10" style="margin-top: -1px;"   @click="showManage">
      <svg-icon class="icon-gj" icon-class="gaojing" ></svg-icon>
  </div>
</template>

<script>
import {mapMutations ,mapState} from 'vuex'
import alarmModal from '@/components/alarmModal'
export default {
  name: "AlarmManage",

  data() {
    return {

    };
  },

  mounted() {},
  computed:{
    ...mapState('setting', ['alarmManageVisible'])
  
  },
  methods: {
    ...mapMutations('setting',["showAlarmManageVisible"]),
    showManage(){
      //this.alarmManageVisible?this.showAlarmManageVisible(false):this.showAlarmManageVisible(true)
      let width = 1000;
      let title = "告警详情";
      let content = (
        <alarmModal alarmAll={true} />
      );
      this.$confirm({
        width: width,
        title: title,
        content: content,
        closable: true,
        icon: () => {
          return <div />;
        },
      });
    }
  },
};
</script>

<style lang="less" scoped>
.cluster-setting {
  .icon-gj{
     color: #fff;
     width: 16px;
     height: 16px;
    cursor: pointer;
  }
}
</style>
