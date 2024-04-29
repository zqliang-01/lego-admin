<template>
  <div class="report-design">
    <div class="report-list template-box">
      <div class="report-head">
        <p class="report-head-p">
          <el-input v-model="queryForm.name" placeholder="报表名称" class="input" />
        </p>
        <p class="report-head-p">
          <el-input
            v-model="queryForm.code"
            placeholder="报表编码"
            class="el-input"
          />
        </p>
      </div>
      <div class="report-head-btn">
        <el-button type="primary" class="btn" @click="handleQuery()">查询报表</el-button>
        <el-button type="success" class="btn" @click="handleAdd()">添加报表</el-button>
      </div>
      <div class="report-list-body" v-loading="queryLoading">
        <p
          v-for="(item, index) in reportList"
          :key="index"
          :class="{ action: currentCode === item.code }"
          class="report-list-item"
          @click="handleSelect(item.code)"
        >
          {{ item.name }}
          <i v-if="currentCode === item.code" class="el-icon-right" />
        </p>
      </div>
    </div>
    <div class="report-infos">
      <div v-loading="formLoading" class="page-box-body">
        <el-form
          ref="createForm"
          :model="fieldForm"
          :rules="fieldRules"
          :validate-on-rule-change="false"
          class="form report-box"
          label-position="top">
          <div class="report-items">
            <h3>1. 基本信息</h3>
            <base-info
              :field-form="fieldForm"
              :field-rules="fieldRules"
            />
          </div>
          <div class="report-items">
            <h3>2. 查询条件</h3>
            <param-input
              :field-form="fieldForm"
              :definition-list="reportList"
              @delete="handleDelectInput"
              @add="handleAddInput"
            />
          </div>
          <div class="report-items">
            <h3>3. 数据脚本 <reminder class="setting-reminder" content="支持Mybatis语法，F11可全屏编辑！" /></h3>
            <param-script
              :field-form="fieldForm"
              :field-rules="fieldRules"
              @resetOutput="handleResetOutput"
            />
          </div>
          <div class="report-items">
            <h3>4. 输出结果</h3>
            <param-output
              :field-form="fieldForm"
              @delete="handleDelectOutput"
              @add="handleAddOutput"
            />
          </div>
          <div class="report-save-btn">
            <el-button class="submit" type="primary" @click="handleSave()">保存</el-button>
            <reminder class="setting-reminder" content="报表保存后需到菜单管理中关联才能显示到菜单内！" />
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import {
  definitionGetAPI,
  definitionAddAPI,
  definitionModifyAPI,
  definitionSimpleListAPI
} from '@/api/report/definition'
import BaseInfo from './BaseInfo'
import ParamScript from './ParamScript'
import ParamInput from './ParamInput'
import ParamOutput from './ParamOutput'
import Reminder from '@/components/Reminder'
import { showFormErrorMessage } from '@/components/Common/Form/utils'

export default {
  name: 'ReportDefinition',
  components: {
    BaseInfo,
    ParamScript,
    ParamInput,
    ParamOutput,
    Reminder
  },
  computed: {
    request() {
      if (this.fieldForm.code) {
        return definitionModifyAPI
      }
      return definitionAddAPI
    }
  },
  data() {
    return {
      queryLoading: false,
      formLoading: false,
      fieldForm: {
        sqlText: '',
        params: [],
        titles: []
      },
      fieldRules: {},
      queryForm: {
        name: '',
        code: ''
      },
      reportList: [],
      currentCode: ''
    }
  },
  created() {
    this.handleQuery()
    this.handleAdd()
  },
  methods: {
    handleSave() {
      const createForm = this.$refs.createForm
      createForm.validate(valid => {
        if (!valid) {
          showFormErrorMessage(createForm)
          return false
        }
        this.formLoading = true
        this.request(this.fieldForm).then(res => {
          this.currentCode = res.data.code
          this.handleQuery()
          this.$message.success('保存成功！')
          this.formLoading = false
        }).catch(() => {
          this.formLoading = false
        })
      })
    },
    handleResetOutput(record) {
      const titles = []
      Object.keys(record).map((keyName) => {
        let title = this.fieldForm.titles.find(title => title.sqlKey === keyName)
        if (!title) {
          title = {
            align: 'left',
            sqlKey: keyName,
            name: title ? title.name : ''
          }
        }
        title.sn = titles.length + 1
        titles.push(title)
      })
      this.$set(this.fieldForm, 'titles', titles)
    },
    handleAdd() {
      this.jsonData = ''
      this.currentCode = ''
      this.fieldForm = {
        sqlText: '',
        params: [],
        titles: []
      }
    },
    handleSelect(code) {
      this.jsonData = ''
      this.currentCode = code
      definitionGetAPI(code).then(res => {
        this.fieldForm = res.data
      })
    },
    handleQuery() {
      this.queryLoading = true
      definitionSimpleListAPI(this.queryForm).then((res) => {
        this.queryLoading = false
        this.reportList = res.data
        if (this.currentCode) {
          this.handleSelect(this.currentCode)
          return
        }
        if (this.reportList.length > 0) {
          this.handleSelect(this.reportList[0].code)
        }
      }).catch(() => {
        this.queryLoading = false
      })
    },
    handleAddOutput(item) {
      var obj = Object.assign({}, item)
      obj.sn = this.fieldForm.titles.length + 1
      this.fieldForm.titles.push(obj)
    },
    handleDelectOutput(index) {
      this.fieldForm.titles.splice(index, 1)
    },
    handleAddInput(item) {
      var obj = Object.assign({}, item)
      obj.sn = this.fieldForm.params.length + 1
      this.fieldForm.params.push(obj)
    },
    handleDelectInput(index) {
      this.fieldForm.params.splice(index, 1)
    }
  }
}
</script>
<style lang="scss" scoped>
.report-design {
  overflow: hidden;
  display: flex;

  .setting-reminder {
    width: auto;
    display: inline-block;
  }

  .report-list {
    width: 245px;
    height: 100%;
    background: #fff;
    margin: 15px;
    padding: 15px;
    position: fixed;

    .report-head {
      &-p {
        margin-bottom: 8px;

        .input {
          width: 100%;
        }
      }
    }

    .report-head-btn {
      margin-top: 8px;
      padding-bottom: 15px;
      border-bottom: 1px dashed #dcdee2;

      .btn {
        width: 95px;

        &:first-of-type {
          margin-right: 8px;
        }
      }
    }

    .report-list-body {
      padding-top: 15px;

      .report-list-item {
        line-height: 18px;
        font-size: 14px;
        padding: 8px 8px;
        padding-right: 30px;
        position: relative;
        border-radius: 5px;
        cursor: pointer;

        &:hover {
          background: #f6f6fc;
        }

        .icon {
          position: absolute;
          right: 5px;
          top: 50%;
          margin-top: -10px;
          line-height: 20px;
          color: rgba(82, 183, 245, 1);
        }
      }
    }
  }

  .report-infos {
    flex: 1;
    width: calc(100% - 260px);

    .page-box-body {
      min-height: 500px;

      .report-box {
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 4px 5px 0px rgba(152, 152, 152, 0.1);
        margin: 15px 15px 15px 280px;
        padding: 15px;
      }
    }
  }

  .report-items {
    background: #f6f6fc;
    margin-bottom: 15px;
    border-radius: 5px;
    padding: 8px 15px 15px;
    border: 1px solid #dcdee2;
    width: 100%;

    h3 {
      margin: 8px 0;
    }

  }

  .report-save-btn {
    padding: 0 15px;

    .submit {
      margin-right: 10px;
    }
  }
}
</style>
