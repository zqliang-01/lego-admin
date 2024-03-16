<template>
  <el-select
    ref="treeSelect"
    v-model="valueTitle"
    :clearable="clearable"
    :disabled="disabled"
    :filterable="filterable"
    :filter-method="filterSelect"
    :placeholder="placeholder"
    class="select-tree"
    @clear="clearHandle"
    @change="valueChange"
  >
    <el-option :value="valueId" :label="valueTitle">
      <el-tree
        id="tree-option"
        ref="selectTree"
        :accordion="accordion"
        :data="options"
        :props="props"
        :node-key="props.value"
        :default-expanded-keys="defaultExpandedKey"
        :filter-node-method="filterTree"
        @node-click="handleNodeClick"/>
    </el-option>
  </el-select>
</template>

<script>
import Emitter from 'element-ui/lib/mixins/emitter'

export default {
  name: 'LegoSelectTree',
  mixins: [Emitter],
  props: {
    // 配置项
    props: {
      type: Object,
      default: () => {
        return {
          value: 'code', // ID字段名
          label: 'name', // 显示名称
          children: 'childrens' // 子级字段名
        }
      }
    },
    value: [String, Number],
    // 选项列表数据(树形结构的对象数组)
    options: { type: Array, default: () => { return [] } },
    placeholder: { type: String, default: '请选择' },

    // 可清空选项
    clearable: { type: Boolean, default: () => { return true } },

    // 自动收起
    accordion: { type: Boolean, default: () => { return true } },
    disabled: { type: Boolean, default: () => { return false } },
    filterable: { type: Boolean, default: () => { return false } }
  },
  data() {
    return {
      valueId: '',
      valueTitle: '',
      defaultExpandedKey: []
    }
  },
  watch: {
    value: {
      handler(newVal, oldVal) {
        this.valueId = this.value
        this.$nextTick(function() {
          this.initHandle()
        })
      },
      immediate: true
    },
    options: {
      handler(newVal, oldVal) {
        this.valueId = this.value
        this.$nextTick(function() {
          this.initHandle()
        })
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.valueId = this.value,
    this.$nextTick(function() {
      this.initHandle()
    })
  },
  methods: {
    // 初始化值
    initHandle() {
      this.$nextTick(function() {
        if (this.valueId) {
          const node = this.$refs.selectTree.getNode(this.valueId)
          if (node) {
            this.valueTitle = node.data[this.props.label] // 初始化显示
          }
          this.$refs.selectTree.setCurrentKey(this.valueId) // 设置默认选中
          this.defaultExpandedKey = [this.valueId] // 设置默认展开
        } else {
          this.valueTitle = ''
        }
        this.initScroll()
      })
    },

    // 初始化滚动条
    initScroll() {
      this.$nextTick(() => {
        const scrollWrap = document.querySelectorAll('.el-scrollbar .el-select-dropdown__wrap')[0]
        const scrollBar = document.querySelectorAll('.el-scrollbar .el-scrollbar__bar')
        scrollWrap.style.cssText = 'margin: 0px; max-height: none; overflow: hidden;'
        scrollBar.forEach(ele => {
          ele.style.width = 0
        })
      })
    },

    // 切换选项
    handleNodeClick(node, obj) {
      this.valueTitle = node[this.props.label]
      this.valueId = node[this.props.value]
      this.defaultExpandedKey = []
      this.$emit('input', this.valueId)
      this.$emit('change', this.valueId)
      this.dispatch('ElFormItem', 'el.form.change', this.valueId)
      this.$refs.treeSelect.blur()
    },

    valueChange() {
      this.$emit('input', this.valueId)
      this.$emit('change', this.valueId)
      this.dispatch('ElFormItem', 'el.form.change', this.valueId)
    },

    // 清除选中
    clearHandle() {
      this.valueTitle = ''
      this.valueId = null
      this.defaultExpandedKey = []
      this.clearSelected()
      this.filterSelect()
      this.$emit('input', this.valueId)
      this.$emit('change', this.valueId)
      this.dispatch('ElFormItem', 'el.form.change', this.valueId)
    },

    // 清空选中样式
    clearSelected() {
      const allNode = document.querySelectorAll('#tree-option .el-tree-node')
      allNode.forEach((element) => element.classList.remove('is-current'))
    },
    filterSelect(value) {
      this.$refs.selectTree.filter(value)
    },
    filterTree(value, data) {
      if (!value) {
        return true
      }
      return data.name.indexOf(value) !== -1
    }
  }
}
</script>

<style scoped>
  .select-tree {
    width: 100%;
  }
  .el-scrollbar .el-scrollbar__view .el-select-dropdown__item{
    height: auto;
    max-height: 274px;
    padding: 0;
    overflow: hidden;
    overflow-y: auto;
  }
  .el-select-dropdown__item.selected{
    font-weight: normal;
  }
  ul li >>>.el-tree .el-tree-node__content{
    height:auto;
    padding: 0 20px;
  }
  .el-tree-node__label{
    font-weight: normal;
  }
  .el-tree >>>.is-current .el-tree-node__label{
    color: #409EFF;
    font-weight: 700;
  }
  .el-tree >>>.is-current .el-tree-node__children .el-tree-node__label{
    color:#606266;
    font-weight: normal;
  }
</style>
