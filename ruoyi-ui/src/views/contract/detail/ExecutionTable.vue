<template>
  <el-col :span="24" class="card-box">
    <el-card>
      <div slot="header" style="display: flex; align-items: center;">
        <span>流转记录</span>
      </div>
      <div class="el-table el-table--enable-row-hover el-table--medium">
        <el-row :gutter="15">
          <el-table v-loading="loading" :data="dataList">
            <el-table-column label="流程节点" align="center" prop="contractStatus">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.sys_contract_status" :value="scope.row.contractStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="流程操作人" align="center" prop="executionUserName" :show-overflow-tooltip="true" />
            <el-table-column label="流程处理操作" align="center" prop="executionOperation">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.sys_execute_status" :value="scope.row.executionOperation"/>
              </template>
            </el-table-column>
            <el-table-column label="流程意见文件" align="center" prop="executionFileName">
              <template slot-scope="scope">
                <a :href="scope.row.executionFile" target="_blank">{{ scope.row.executionFileName }}</a>
              </template>
            </el-table-column>
            <el-table-column label="执行时间" align="center" prop="executeTime" :show-overflow-tooltip="true" />
          </el-table>
        </el-row>
      </div>
    </el-card>
  </el-col>
</template>

<script>
import {getExecutionList} from "../../../api/contract/contract";

export default {
  name: "ExecutionTable",
  dicts: ['sys_contract_status', 'sys_execute_status'],
  data() {
    return {
      loading: false,
      dataList: [],
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      let uuid = this.$route.query && this.$route.query.uuid;
      this.loading = true;
      let query = {
        uuid: uuid,
      }
      if (uuid) {
        getExecutionList(query).then((response) => {
          this.dataList = response.data;
          this.loading = false;
        });
      }
    }
  }
}
</script>

<style scoped>

</style>
