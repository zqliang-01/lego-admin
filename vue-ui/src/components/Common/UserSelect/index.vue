<template>
  <div>
    <el-dialog
      :visible="visible"
      title="员工选择"
      width="70%"
      append-to-body
      @close="handleCancel">
      <div slot="title">
        <span class="el-dialog__title">
          员工选择
        </span>
        <span v-if="multiple">
          (已选择<span class="select-info__num">{{ selectedCodes.length }}</span>项)
        </span>
      </div>
      <sash-form-layout>
        <el-tree
          slot="left"
          ref="deptTree"
          :props="deptTreeProps"
          :expand-on-click-node="false"
          :data="deptList"
          node-key="code"
          default-expand-all
          @node-click="changeDeptClick">
          <flexbox slot-scope="{ node }" :class="{ 'is-current': node.isCurrent}" class="node-data">
            <i v-if="node.level == 1" :class="'box-double' | iconPre" />
            <span v-else class="node-data__mark" />
            <div class="node-data__label text-one-line ">{{ node.label }}</div>
            <i
              v-if="node.childNodes && node.childNodes.length"
              :class="[{ 'is-close': !node.expanded }, {'lego lego-up-unfold': true}]"
              @click="node.expanded = !node.expanded"/>
          </flexbox>
        </el-tree>
        <user-list
          slot="right"
          :value="value"
          :multiple="multiple"
          :dept-code="currentDeptCode"
          @selected="handleSelect"/>
      </sash-form-layout>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 认</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { deptListAPI } from '@/api/admin/dept'
import SashFormLayout from '@/components/Layout/SashFormLayout'
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
    required: {
      type: Boolean,
      default: false
    },
    multiple: {
      type: Boolean,
      default: false
    },
    value: [String, Array]
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
      selectedCodes: [],
      currentDeptCode: ''
    }
  },
  mounted() {
    if (this.value) {
      this.selectedCodes = this.multiple ? this.value : [this.value]
    }
    deptListAPI().then(res => {
      this.deptList = res.data
    })
  },
  methods: {
    changeDeptClick(data) {
      if (data) {
        this.currentDeptCode = data.code
      }
    },
    handleSelect(val) {
      this.selectedCodes = this.multiple ? val : [val]
    },
    submit() {
      if (this.required && this.selectedCodes.length === 0) {
        this.$message.error('请选择员工信息！')
        return
      }
      let val = this.selectedCodes
      if (!this.multiple) {
        val = this.selectedCodes.length > 0 ? this.selectedCodes[0] : ''
      }
      this.$emit('selected', val)
      this.$emit('update:visible', false)
    },
    handleCancel() {
      this.$emit('update:visible', false)
    }
  }
}
</script>
<style lang="scss" scoped>
.select-info {
  &__num {
    color: $xr-color-primary;
  }
}
.el-tree ::v-deep .el-tree-node__content {
  height: 40px;

  .node-data {
    height: 40px;
    padding: 0 15px;
    position: relative;
    border-radius: $xr-border-radius-base;

    .lego {
      font-size: 14px;
      color: #8a94a6;
      flex-shrink: 0;
    }

    .lego-box-double {
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

    .lego-up-unfold {
      margin-left: 8px;
      transition: transform 0.3s;
    }

    .lego-up-unfold.is-close {
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

