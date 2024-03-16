<template>
  <div>
    <el-dialog
      :visible="visible"
      title="员工选择"
      width="70%"
      append-to-body
      @close="handleCancel">
      <sash-form-layout>
        <template v-slot:left>
          <el-tree
            ref="deptTree"
            :props="deptTreeProps"
            :expand-on-click-node="false"
            :data="deptList"
            node-key="code"
            default-expand-all
            @node-click="changeDeptClick">
            <flexbox slot-scope="{ node }" :class="{ 'is-current': node.isCurrent}" class="node-data">
              <i v-if="node.level == 1" :class="'department' | iconPre" />
              <span v-else class="node-data__mark" />
              <div class="node-data__label text-one-line ">{{ node.label }}</div>
              <i
                v-if="node.childNodes && node.childNodes.length"
                :class="[{ 'is-close': !node.expanded }, {'wk wk-up-unfold': true}]"
                @click="node.expanded = !node.expanded"/>
            </flexbox>
          </el-tree>
        </template>
        <template v-slot:right>
          <user-list
            :value="value"
            :dept-code="currentDeptCode"
            @selected="handleSelect"/>
        </template>
      </sash-form-layout>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 认</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { depListAPI } from '@/api/admin/dept'
import SashFormLayout from '@/components/layout/SashFormLayout'
import UserList from './UserList'

export default {
  name: 'UserSelect',
  components: {
    SashFormLayout,
    UserList
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    value: String
  },
  data() {
    return {
      loading: false,
      dataValue: '',
      deptTreeProps: {
        label: 'name',
        children: 'childrens'
      },
      deptList: [],
      selectedEmployee: '',
      currentDeptCode: ''
    }
  },
  mounted() {
    this.selectedEmployee = this.value
    depListAPI().then(res => {
      this.deptList = res.data
    })
  },
  methods: {
    handleCancel() {
      this.$emit('update:visible', false)
    },
    changeDeptClick(data) {
      if (data) {
        this.currentDeptCode = data.code
      }
    },
    handleSelect(val) {
      this.selectedEmployee = val
    },
    submit() {
      if (this.selectedEmployee) {
        this.$emit('selected', this.selectedEmployee)
        this.$emit('update:visible', false)
        return
      }
      this.$message.error('请选择员工信息！')
    }
  }
}
</script>
<style lang="scss" scoped>
.el-tree ::v-deep .el-tree-node__content {
  height: 40px;

  .node-data {
    height: 40px;
    padding: 0 15px;
    position: relative;
    border-radius: $xr-border-radius-base;

    .wk {
      font-size: 14px;
      color: #8a94a6;
      flex-shrink: 0;
    }

    .wk-department {
      margin-right: 8px;
    }

    &__mark {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: #e6e6e6;
      margin-right: 8px;
      flex-shrink: 0;
    }

    &__label {
      flex: 1;
      color: #333;
      font-size: 14px;
      margin-right: 8px;
    }

    .wk-up-unfold {
      margin-left: 8px;
      transition: transform 0.3s;
    }

    .wk-up-unfold.is-close {
      transform: rotateZ(180deg);
    }
  }

  .node-data.is-current,
  .node-data:hover {
    background-color: $xr--background-color-base;
  }
}
.el-tree ::v-deep .el-tree-node__expand-icon {
  display: none;
}
</style>

