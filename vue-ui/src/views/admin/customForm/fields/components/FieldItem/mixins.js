import { isEmpty } from '@/utils/types'
import FieldTypeLib from '../../fieldTypeLib'

export default {
  props: {
    field: { // 当前字段信息
      type: Object,
      required: true
    },
    fieldList: { // 全部字段数组，为空时则禁止点击改变位置
      type: Array,
      default: () => []
    },
    point: { // 当前字段坐标
      type: Array
    },
    activePoint: { // 选中的字段坐标
      type: Array,
      default: () => []
    },
    showBorder: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
    }
  },
  computed: {
    // 当前字段是否激活
    activate() {
      return this.point[0] === this.activePoint[0] &&
        this.point[1] === this.activePoint[1]
    },
    // 向上操作按钮
    topFlag() {
      if (isEmpty(this.fieldList)) return false
      // 第一行、上一行有4个、上一行为特殊字段类型不显示
      const row = this.point[0]
      if (row === 0) return false
      const prevRow = this.fieldList[row - 1]
      if (prevRow.length === 4) return false
      return true
    },
    // 向下操作按钮
    bottomFlag() {
      if (isEmpty(this.fieldList)) return false
      // 最后一行、当前行只有一个不显示
      const row = this.point[0]
      if (row === this.fieldList.length - 1) return false
      // if (this.fieldList[row].length <= 1) return false
      return true
    },
    // 左侧操作按钮
    leftFlag() {
      if (isEmpty(this.fieldList)) return false
      // 第一列不显示
      const column = this.point[1]
      if (column === 0) return false
      return true
    },
    // 右侧操作按钮
    rightFlag() {
      if (isEmpty(this.fieldList)) return false
      // 最后一列不显示
      const column = this.point[1]
      const row = this.point[0]
      if (column === this.fieldList[row].length - 1) return false
      return true
    },
    // 复制按钮
    copyFlag() {
      if (isEmpty(this.fieldList)) return false
      return !['single_user'].includes(this.field.formType)
    },
    controlFlag() {
      return {
        top: this.topFlag,
        bottom: this.bottomFlag,
        left: this.leftFlag,
        right: this.rightFlag,
        delete: true,
        copy: this.copyFlag
      }
    },
    fieldIcon() {
      const fieldType = FieldTypeLib.find(fieldType => fieldType.formType === this.field.formType)
      return fieldType ? fieldType.icon : ''
    }
  },
  methods: {
    /**
     * click
     * @param evt
     */
    emitClick(evt) {
      this.$emit('click', evt)
    },

    /**
     * 点击删除
     * @param action
     * @param evt
     */
    handleAction(action, evt) {
      this.$emit('action', action, this.point, evt)
    }
  }
}
