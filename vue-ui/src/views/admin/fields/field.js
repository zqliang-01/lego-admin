export default class Field {
  constructor(obj) {
    this.name = obj.name || '' //  标识名
    this.fieldCode = obj.fieldCode || '' //  编码
    this.formType = obj.formType || '' // 字段类型
    this.componentName = obj.componentName || '' // 控件类型
    this.unique = obj.unique || 0 // 是否唯一
    this.required = obj.required || 0 // 是否必填
    this.hidden = obj.hidden || 0 // 是否隐藏字段
    this.inputTips = obj.inputTips || '' // 输入提示
    if (this.formType === 'textarea') {
      this.maxLength = obj.maxLength || 800 // textarea 多行文本有最大数量
    }
    this.defaultValue = obj.defaultValue || ''
    if (this.formType === 'checkbox') {
      this.defaultValue = obj.defaultValue || []
    }
    this.setting = obj.setting || [] // 选项
  }

  // 校验数据
  check() {
    if (this.name === '') {
      return '字段名称不能为空'
    }
    return ''
  }
}
