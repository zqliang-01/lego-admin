<template>
  <div>
    <div class="report-table-head">
      <table>
        <thead>
          <tr>
            <th class="required">序号</th>
            <th class="required">名称</th>
            <th class="required">
              编码
              <el-tooltip effect="dark" placement="top">
                <div slot="content">
                  需与数据脚本中定义的条件编码一致
                </div>
                <i :class="'help lego-help-tips' | iconPre"/>
              </el-tooltip>
            </th>
            <th>类型</th>
            <th>必填</th>
            <th>
              定义编码
              <el-tooltip effect="dark" placement="top">
                <div slot="content">
                  类型为单选时可选值的来源，内容为定义的报表，报表查询结果需为code和name
                </div>
                <i :class="'help lego-help-tips' | iconPre"/>
              </el-tooltip>
            </th>
            <th>默认值</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(item, index) in fieldForm.params"
            :key="'query-tr' + index" >
            <td>
              <el-input
                v-model="fieldForm.params[index].sn" />
            </td>
            <td>
              <el-input v-model="fieldForm.params[index].name" />
            </td>
            <td>
              <el-input v-model="fieldForm.params[index].sqlKey" />
            </td>
            <td>
              <el-select
                v-model="fieldForm.params[index].type"
                style="width: 95px"
                transfer>
                <el-option
                  v-for="dataType in dataTypeList"
                  :key="dataType.code"
                  :value="dataType.code"
                  :label="dataType.name" />
              </el-select>
            </td>
            <td><el-switch v-model="fieldForm.params[index].required" /></td>
            <td>
              <el-select
                v-model="fieldForm.params[index].dataDefinitionCode"
                class="input-width"
                filterable
                transfer>
                <el-option
                  v-for="condition in definitionList"
                  :key="condition.code"
                  :value="condition.code"
                  :label="condition.name" />
              </el-select>
            </td>
            <td>
              <el-input v-model="fieldForm.params[index].defaultValue" />
            </td>
            <td>
              <el-button type="danger" size="small" @click="handleDelete(index)">删除</el-button>
            </td>
          </tr>
          <tr>
            <td>...</td>
            <td>...</td>
            <td>...</td>
            <td>...</td>
            <td>...</td>
            <td>...</td>
            <td>...</td>
            <td>
              <el-button type="success" size="small" @click="handleAdd()">添加</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>

export default {
  name: 'DefinitionParamInput',
  props: {
    fieldForm: Object,
    definitionList: Array
  },
  data() {
    return {
      item: {
        sn: '',
        name: '',
        sqlKey: '',
        type: 'string',
        dataDefinitionCode: '',
        required: false,
        dependentCode: '',
        defaultValue: ''
      },
      dataTypeList: [
        {
          name: '日期',
          code: 'date'
        },
        {
          name: '字符串',
          code: 'string'
        },
        {
          name: '布尔',
          code: 'boolean'
        },
        {
          name: '单选',
          code: 'select'
        }
      ]
    }
  },
  created() {
  },
  methods: {
    handleDelete(index) {
      this.$emit('delete', index)
    },
    handleAdd() {
      this.$emit('add', this.item)
    }
  }
}
</script>
<style lang="scss" scoped>
.required:before {
  content: "*";
  font-size: 16px;
  margin-right: 1px;
  color: #f00;
}
.report-table-head table {
  border-spacing: 0px;
  border-collapse: collapse;
  width: auto;
  height: 100%;
  max-width: 100%;
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
  background-color: #fff;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;

  td,
  th {
    position: relative;
    border: 1px solid #dcdee2;
    padding: 5px 10px;
    text-align: left;
    min-width: 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    text-overflow: ellipsis;
    vertical-align: middle;

    .table-label {
      display: block;
      min-width: 2em;
      text-decoration: underline;
      cursor: pointer;

      &:hover {
        color: rgba(82, 183, 245, 1);
      }
    }
  }

  th {
    background-color: #f8f8f9;
    height: 100%;
    padding: 8px 10px;
  }
}
</style>
