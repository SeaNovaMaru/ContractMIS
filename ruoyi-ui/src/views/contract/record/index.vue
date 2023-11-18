<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="合同名称" prop="contractName">
        <el-input
          v-model="queryParams.contractName"
          placeholder="请输入合同名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同状态" prop="contractStatus">
        <el-select
          v-model="queryParams.contractStatus"
          placeholder="请选择合同状态"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_contract_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同名称" align="center" prop="contractName" :show-overflow-tooltip="true" />
      <el-table-column label="合同类别" align="center" prop="contractType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_contract_type" :value="scope.row.contractType"/>
        </template>
      </el-table-column>
      <el-table-column label="合同状态" align="center" prop="contractStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_contract_status" :value="scope.row.contractStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="是否需要法审" align="center" prop="needLawSupervise">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.need_law_supervise" :value="scope.row.needLawSupervise"/>
        </template>
      </el-table-column>
      <el-table-column label="合同发起人" align="center" prop="owner" :show-overflow-tooltip="true" />
      <el-table-column label="合同发起时间" align="center" prop="createTime" :show-overflow-tooltip="true" />
      <el-table-column label="合同创建时间" align="center" prop="updateTime" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            :disabled="scope.row.userId !== userId || scope.row.contractStatus !== 1"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import {getContractList} from "../../../api/contract/contract";

// 1.

export default {
  name: "Contract",
  dicts: ['sys_contract_status', 'need_law_supervise', 'not_supervise_reason', 'sys_contract_type'],
  data() {
    return {
      userId: 0,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 字典表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractName: null,
        contractStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.userId = this.$store.state.user.id;
  },
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true;
      getContractList(this.queryParams).then((response) => {
        this.typeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        contractName: null,
        contractStatus: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.$tab.openPage("合同拟定", '/contract/info');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      let uuid = {
        uuid: row.uuid
      }
      this.$tab.openPage("合同拟定", '/contract/info', uuid);
    },
    handleView(row) {
      let uuid = {
        uuid: row.uuid
      }
      this.$tab.openPage("合同详情", '/contract/detail', uuid);
    },
  }
};
</script>
