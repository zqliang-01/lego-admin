<template>
  <div class="report-table-head">
    <table>
      <thead>
        <tr>
          <th class="required title">
            字段
            <el-tooltip effect="dark" placement="top">
              <div slot="content">
                需与数据脚本中定义的的返回列一致
              </div>
              <i :class="'help lego-help-tips' | iconPre"/>
            </el-tooltip>
          </th>
          <th v-for="(item, index) in fieldForm.titles" :key="index">
            <el-input
              v-model="fieldForm.titles[index].sqlKey"
              class="input-column"
            />
          </th>
          <th>...</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td class="required title">名称</td>
          <td v-for="(item, index) in fieldForm.titles" :key="index">
            <el-input
              v-model="fieldForm.titles[index].name"
              class="input-column"
            />
          </td>
          <td>...</td>
        </tr>
        <tr>
          <td class="required title">序号</td>
          <td v-for="(item, index) in fieldForm.titles" :key="index">
            <el-input
              v-model="fieldForm.titles[index].sn"
              class="input-column"
            />
          </td>
          <td>...</td>
        </tr>
        <tr>
          <td class="title">对齐</td>
          <td v-for="(item, index) in fieldForm.titles" :key="index">
            <el-select
              v-model="fieldForm.titles[index].align"
              class="input-column"
              transfer
              placement="top"
              placeholder=""
            >
              <el-option
                v-for="align in alignTypeList"
                :key="align.code"
                :value="align.code"
                :label="align.name"
              />
            </el-select>
          </td>
          <td>...</td>
        </tr>
        <tr>
          <td class="title">操作</td>
          <td v-for="(item, index) in fieldForm.titles" :key="index">
            <el-button type="danger" size="small" @click="handleDelete(index)">删除</el-button>
          </td>
          <td>
            <el-button type="success" size="small" @click="handleAdd()">添加</el-button>
          </td>
        </tr>
      </tbody>
    </table>
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
        align: 'left',
        sqlKey: '',
        sn: ''
      },
      alignTypeList: [
        {
          name: '左对齐',
          code: 'left'
        },
        {
          name: '右对齐',
          code: 'right'
        },
        {
          name: '居中对齐',
          code: 'center'
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
.title {
  width: 50px;
  white-space: nowrap;
}

.input-column {
  width: 100px;
}

.required:before {
  content: "*";
  font-size: 16px;
  margin-right: 1px;
  color: #f00;
}

.report-table-head {
  overflow: auto;
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
