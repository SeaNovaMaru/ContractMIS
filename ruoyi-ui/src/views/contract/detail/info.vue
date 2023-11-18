<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header" style="display: flex; align-items: center;">
            <span>合同基本信息</span>
            <div class="right-components">
              <el-button type="primary" v-if="permissionData.submit" size="small" @click="submitForm">提交</el-button>
              <el-button type="primary" v-if="permissionData.edit" size="small" @click="editForm">修改</el-button>
              <el-button type="primary" v-if="permissionData.agree" size="small" @click="submitForm">同意</el-button>
              <el-button type="danger" v-if="permissionData.disagree" size="small" @click="submitForm">驳回</el-button>
              <el-button size="small" @click="closeTab">关闭</el-button>
            </div>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <el-row :gutter="15">
              <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
                <el-col :span="12">
                  <el-form-item label-width="150px" label="合同名称: " prop="contractName">
                    <span>{{this.formData.contractName}}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label-width="150px" label="文本形式: " prop="contractType">
                    <template slot-scope="scope">
                      <dict-tag :options="dict.type.sys_contract_type" :value="formData.contractType"/>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item v-if="formData.contractType === 2" label-width="150px" label="导入范本: " prop="contractTemplate" required>
                    <a :href="formData.contractTemplate" target="_blank" style="text-decoration: underline;">{{ formData.contractTemplateName }}</a>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label-width="150px" label="导入合同: " prop="contractFile" required>
                    <a :href="formData.contractFile" target="_blank" style="text-decoration: underline;">{{ formData.contractFileName }}</a>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label-width="150px" label="是否需要法审: " prop="needLawSupervise">
                    <template slot-scope="scope">
                      <dict-tag :options="dict.type.need_law_supervise" :value="formData.needLawSupervise"/>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item v-if="formData.needLawSupervise === 0" label-width="150px" label="不需要法审的原因: " prop="notSuperviseReason">
                    <template slot-scope="scope">
                      <dict-tag :options="dict.type.not_supervise_reason" :value="formData.notSuperviseReason"/>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label-width="150px" label="合同审批状态: " prop="contractStatus">
                    <template slot-scope="scope">
                      <dict-tag :options="dict.type.sys_contract_status" :value="formData.contractStatus"/>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label-width="150px" label="合同发起人: " prop="userName">
                    <span>{{this.formData.userName}}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label-width="150px" v-if="formData.contractStatus === 4" label="比对意见: " prop="verifyResult">
                    <span>{{this.formData.verifyResult}}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item size="medium">
                  </el-form-item>
                </el-col>
              </el-form>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <execution-table></execution-table>
    </el-row>
  </div>
</template>

<script>

import {getToken} from "../../../utils/auth";
import {getContractDetail, saveContract, submitContract} from "../../../api/contract/contract";
import ExecutionTable from "./ExecutionTable.vue";

export default {
  name: "ContractDetail",
  dicts: ['sys_contract_status', 'need_law_supervise', 'not_supervise_reason', 'sys_contract_type'],
  components: {
    ExecutionTable
  },
  props: {
  },
  data() {
    return {
      userId: 0,
      formData: {
        uuid: null,
        contractType: null,
        contractFile: null,
        contractFileName: null,
        contractTemplate: null,
        contractTemplateName: null,
        contractName: undefined,
        needLawSupervise: null,
        userName: null,
        verifyResult: null,
        notSuperviseReason: null,
        contractStatus: null,
      },
      permissionData: {},
      fileAction: process.env.VUE_APP_BASE_API + '/common/upload',
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      contractFileList: [],
      contractTemplateList: [],
    }
  },
  computed: {},
  watch: {},
  created() {
    this.userId = this.$store.state.user.id.toString();
    this.roles = this.$store.state.user.roles;
    console.log(this.roles);
    let uuid = this.$route.query && this.$route.query.uuid;
    let query = {
      uuid: uuid
    }
    if (uuid) {
      getContractDetail(query).then((response) => {
        this.formData = response.contractInfo;
        this.permissionData = response.permissionInfo;
        console.log(this.fileAction);
        if (this.formData.contractFile !== null && this.formData.contractFile !== undefined) {
          this.contractFileList.push({
            url: this.formData.contractFile,
            name: this.formData.contractFileName,
            file: new File([], this.formData.contractFileName),
            response: {
              url: this.formData.contractFile
            }
          })
        }
        if (this.formData.contractTemplate !== null && this.formData.contractTemplateName !== undefined) {
          this.contractTemplateList.push({
            url: this.formData.contractTemplate,
            name: this.formData.contractTemplateName,
            file: new File([], this.formData.contractTemplateName),
            response: {
              url: this.formData.contractTemplate
            }
          })
        }
      })
    }
  },
  mounted() {},
  methods: {
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (!valid) {
          this.$message.error("合同名称不得为空!");
        } else {
          submitContract(this.formData).then(response => {
            if (response.code === 200) {
              this.$tab.closeCurrentPage();
              this.$modal.msgSuccess("提交成功");
            }
          });
        }
      });
    },
    resetForm() {
      this.$refs['elForm'].resetFields();
    },
    closeTab() {
      this.$tab.closeCurrentPage();
    },
    editForm() {

    }
  }
}
</script>

<style scoped lang="scss">
.right-components {
  margin-left: auto; /* 将右边的组件推向右边 */
  display: flex;
  /* 根据需要调整子组件之间的间距 */
}
</style>
