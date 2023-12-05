<template>
  <div class="app-container">
    <el-row>
      <el-col v-loading="loading" :span="24" class="card-box">
        <el-card>
          <div slot="header"><span>合同基本信息</span></div>

          <div class="el-table el-table--enable-row-hover el-table--medium">
            <el-row :gutter="15">
              <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
                <el-col :span="12">
                  <el-form-item label-width="150px" label="文本形式" prop="contractType">
                    <el-select v-model="formData.contractType" placeholder="请选择文本形式" clearable
                               :style="{width: '60%'}">
                      <el-option v-for="(item, index) in contractTypeOptions" :key="item.value" :label="item.label"
                                 :value="item.value" :disabled="item.disabled"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item v-if="formData.contractType === 2" label-width="150px" label="导入范本" prop="contractTemplate" required>
                    <el-upload ref="contractTemplate" :file-list="contractTemplateList" :action="fileAction"
                               :before-upload="contractTemplateBeforeUpload" :headers="headers"
                               :on-success="uploadTemplateSuccess" :on-preview="previewFile" :limit="1" :on-exceed="handleExceed">
                      <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
                    </el-upload>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label-width="150px" label="导入合同" prop="contractFile" required>
                    <el-upload ref="contractFile" :file-list="contractFileList" :action="fileAction"
                               :before-upload="contractFileBeforeUpload" :headers="headers"
                               :on-success="uploadContractSuccess" :on-preview="previewFile" :limit="1" :on-exceed="handleExceed">
                      <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
                    </el-upload>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label-width="150px" label="合同名称" prop="contractName">
                    <el-input v-model="formData.contractName" placeholder="请输入合同名称" clearable
                              :style="{width: '30%'}"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label-width="150px" label="是否需要法审" prop="needLawSupervise">
                    <el-radio-group v-model="formData.needLawSupervise" size="medium">
                      <el-radio v-for="(item, index) in needLawSuperviseOptions" :key="item.value" :label="item.value"
                                :disabled="item.disabled">{{item.label}}</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item v-if="formData.needLawSupervise === 0" label-width="150px" label="不需要法审的原因" prop="notSuperviseReason">
                    <el-select v-model="formData.notSuperviseReason" placeholder="请选择不需要法审的原因" clearable
                               :style="{width: '81%'}">
                      <el-option v-for="(item, index) in notSuperviseReasonOptions" :key="item.value" :label="item.label"
                                 :value="item.value" :disabled="item.disabled"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item size="medium">
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item size="medium" style="display: flex; justify-content: center;">
                    <el-button type="primary" @click="submitForm">提交</el-button>
                    <el-button @click="saveForm">保存</el-button>
                    <el-button @click="resetForm">重置</el-button>
                  </el-form-item>
                </el-col>
              </el-form>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import {getToken} from "../../../utils/auth";
import {getContractDetail, saveContract, submitContract} from "../../../api/contract/contract";

export default {
  components: {
  },
  props: {
  },
  data() {
    return {
      formData: {
        uuid: null,
        contractType: null,
        contractFile: null,
        contractFileName: null,
        contractTemplate: null,
        contractTemplateName: null,
        contractName: undefined,
        needLawSupervise: null,
        notSuperviseReason: null,
      },
      loading: false,
      rules: {
        contractType: [{
          required: true,
          message: '请选择文本形式',
          trigger: 'change'
        }],
        contractName: [{
          required: true,
          message: '请输入合同名称',
          trigger: 'blur'
        }],
        needLawSupervise: [{
          required: true,
          message: '是否需要法审不能为空',
          trigger: 'change'
        }],
        notSuperviseReason: [{
          required: true,
          message: '无需法审原因不得为空f',
          trigger: 'change'
        }],
      },
      fileAction: process.env.VUE_APP_BASE_API + '/common/upload',
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      contractFileList: [],
      contractTemplateList: [],
      contractTypeOptions: [{
        "label": "自拟文本",
        "value": 1
      }, {
        "label": "范本合同",
        "value": 2
      }],
      needLawSuperviseOptions: [{
        "label": "是",
        "value": 1
      }, {
        "label": "否",
        "value": 0
      }],
      notSuperviseReasonOptions: [{
        "label": "拟签订的合同为格式合同、未作修改或仅作商务或技术条款修改的示范合同的",
        "value": 1
      }, {
        "label": "拟签订的合同为未作修改或仅作商务或技术条款修改的参考合同，且制定部门在下发该参考合同文本时，已确定在特定使用期间、适用对象或使用条件内无需再履行法律审查程序的；",
        "value": 2
      }, {
        "label": "具有管理、垄断地位的合同对方制定或指定使用的合同文本且不允许修改，法律合规部门在法律审查意见中已明确今后在特定使用期间、适用对象或使用条件内可参照已出具的法律审查意见，不需再履行法律审查程序的；",
        "value": 3
      }, {
        "label": "在同类业务中，合同对方为同一或同类主体，且使用的为同一合同文本，法律合规部门在法律审查意见中已明确今后在特定使用期间、适用对象或使用条件内可参照已出具的法律审查意见，不需再履行法律审查程序的；",
        "value": 4
      }, {
        "label": "已外聘律师审查的合同",
        "value": 5
      }],
    }
  },
  computed: {},
  watch: {},
  created() {
    let uuid = this.$route.query && this.$route.query.uuid;
    let query = {
      uuid: uuid
    }
    if (uuid) {
      getContractDetail(query).then((response) => {
        this.formData = response.contractInfo;
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
          this.loading = true;
          submitContract(this.formData).then(response => {
            this.loading = false;
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
    saveForm() {
      if (this.formData.contractName === undefined || this.formData.contractName === '') {
        this.$message.error("合同名称不得为空!");
        return;
      }
      saveContract(this.formData).then(response => {
        if (response.code === 200) {
          this.formData.uuid = response.msg;
          this.$modal.msgSuccess("保存成功");
        }
      });
    },
    contractFileBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 10;
      if (!isRightSize) {
        this.$message.error('文件大小超过 10MB');
      }
      return isRightSize
    },
    contractTemplateBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 10;
      if (!isRightSize) {
        this.$message.error('文件大小超过 10MB');
      }
      return isRightSize;
    },
    uploadContractSuccess(response, file, fileList) {
      this.formData.contractFile = response.url;
      this.formData.contractFileName = response.originalFilename;
    },
    uploadTemplateSuccess(response, file, fileList) {
      this.formData.contractTemplate = response.url;
      this.formData.contractTemplateName = response.originalFilename;
    },
    previewFile(file) {
      window.open(file.response.url);
    },
    handleExceed() {
      this.$message.error("最多只能上传一个文件！");
    }
  }
}
</script>
