<template>
  <div
    v-clickoutside="clickOutSide"
    v-if="typeObj"
    class="field-setting">
    <div class="setting-title">
      {{ typeObj.name }}
    </div>

    <div class="setting-body">
      <div class="item-section">
        <div class="name input-tips"><span>*</span> 标识编码</div>
        <el-select
          v-model="field.fieldCode"
          style="width: 100%;"
          clearable
          filterable
          placeholder="请选择/创建编码"
          @change="changeCode">
          <el-option
            v-for="item in columnList"
            :key="item.code"
            :label="item.name + '(' + item.code + ')'"
            :value="item.code" />
        </el-select>
        <div class="name input-tips"><span>*</span> 标识名称</div>
        <el-input v-model="field.name" />
      </div>
      <template v-if="!isDescText">
        <div class="item-section">
          <div class="name">提示文字</div>
          <el-input
            v-model="field.inputTips"
            :rows="3"
            type="textarea"
            resize="none"/>
          <div class="input-tips">显示在标识名右侧的说明文字</div>
        </div>
        <div v-if="canPrecisions" class="item-section">
          <div class="name">
            {{ precisionsTitle }}
          </div>
          <setting-precisions :field="field" />
        </div>

        <template v-if="canOptions">
          <div class="item-section">
            <div class="name">选项内容</div>
            <div style="margin-top: 10px; margin-bottom: 10px">
              <el-radio-group
                v-model="dataResourceRadio"
                size="small">
                <el-radio-button label="dict" border>
                  字典表
                </el-radio-button>
                <el-radio-button label="custom" border disabled>
                  手动添加
                </el-radio-button>
              </el-radio-group>
            </div>
            <setting-options
              v-if="dataResourceRadio == 'custom'"
              :field="field"/>
            <setting-dict-type
              v-if="dataResourceRadio == 'dict'"
              :app-code="appCode"
              :field="field" />
          </div>
        </template>

        <div v-if="canDefault" class="item-section">
          <div
            v-if="field.formType === 'entity'"
            class="name input-tips">
            <span>*</span>关联表单
            <el-tooltip
              content="所关联的表单需与代码生成功能中的字段关联表一致，如修改需重新生成代码"
              effect="dark"
              placement="top">
              <i :class="'help lego-help-tips' | iconPre" style="margin-left: 3px;"/>
            </el-tooltip>
          </div>
          <div v-else class="name">
            默认值
            <el-tooltip
              v-if="field.unique"
              content="当前字段为唯一字段，默认值支持自动编码生成"
              effect="dark"
              placement="top">
              <i :class="'help lego-help-tips' | iconPre" style="margin-left: 3px;"/>
            </el-tooltip>
          </div>
          <setting-default
            :field="field"
            :app-code="appCode" />
        </div>

        <div v-if="canNumber || canFloat" class="item-section">
          <setting-float v-if="canFloat" :field="field" />
          <setting-number v-if="canNumber" :field="field" />
        </div>
      </template>

      <div v-if="isDescText" class="item-section">
        <div class="name">内容</div>
        <setting-desc-text :field="field" />
      </div>

      <div class="item-section">
        <div class="name">
          字段占比 %
          <el-tooltip
            content="配置表单布局，可以单行多字段排布"
            effect="dark"
            placement="top">
            <i :class="'help lego-help-tips' | iconPre" style="margin-left: 3px;"/>
          </el-tooltip>
        </div>
        <el-radio-group
          v-model="field.stylePercent"
          size="medium"
          @change="emitUpdateWidth">
          <el-radio-button
            v-for="item in widthOptions"
            :label="item.value"
            :key="item.value">{{ item.value }}</el-radio-button>
        </el-radio-group>
      </div>
      <template v-if="!isDescText">
        <div
          class="item-check-section">
          <el-checkbox
            v-model="field.required">设为必填</el-checkbox>
        </div>

        <div
          class="item-check-section">
          <el-checkbox
            v-model="field.unique">设为唯一</el-checkbox>
        </div>

        <div
          class="item-check-section">
          <el-checkbox
            v-model="field.hidden">隐藏字段</el-checkbox>
        </div>
      </template>

    </div>
  </div>
</template>

<script>
import SettingDefault from './SettingDefault'
import SettingOptions from './SettingOptions'
import SettingNumber from './SettingNumber'
import SettingFloat from './SettingFloat'
import SettingPrecisions from './SettingPrecisions'
import SettingDescText from './SettingDescText'
import SettingLogicForm from './SettingLogicForm'
import SettingDictType from './SettingDictType'

import FieldTypeLib from '../../fieldTypeLib'

export default {
  name: 'FieldSetting',
  components: {
    SettingDefault,
    SettingOptions,
    SettingNumber,
    SettingFloat,
    SettingPrecisions,
    SettingDescText,
    SettingLogicForm,
    SettingDictType
  },
  props: {
    field: { // 要编辑的字段信息
      type: Object,
      required: true
    },
    appCode: {
      type: String,
      required: true
    },
    fieldList: { // 所有字段
      type: Array,
      required: true
    },
    columnList: {
      type: Array,
      required: true
    },
    point: { // 被选中的字段坐标
      type: Array,
      required: true
    }
  },
  data() {
    return {
      widthOptions: [
        { value: 25 },
        { value: 50 },
        { value: 75 },
        { value: 100 }
      ],
      stylePercentValue: [],
      dataResourceRadio: 'dict'
    }
  },
  computed: {
    typeObj() {
      const field = FieldTypeLib.find(o => o.formType === this.field.formType)
      return field || this.field
    },
    // 是否允许设置字段默认值
    canDefault() {
      return ![
        'user',
        'structure',
        'handwriting_sign',
        'picture'
      ].includes(this.field.formType)
    },
    // 是否允许设置选项内容
    canOptions() {
      return [
        'select',
        'checkbox'
      ].includes(this.field.formType)
    },
    // 是否允许设置范围
    canNumber() {
      return [
        'number',
        'floatnumber',
        'percent'
      ].includes(this.field.formType)
    },
    // 是否允许设置小数
    canFloat() {
      return [
        'floatnumber',
        'percent'
      ].includes(this.field.formType)
    },
    // 精度
    canPrecisions() {
      return [
        'position',
        'select',
        'checkbox'
      ].includes(this.field.formType)
    },
    // 精度标题
    precisionsTitle() {
      if (!this.canPrecisions) return ''
      switch (this.field.formType) {
        case 'select':
          return '展示方式'
        case 'checkbox':
          return '展示方式'
        default:
          return '精度'
      }
    },
    // 是否为描述文字类型
    isDescText() {
      return this.field.formType === 'desc_text'
    }
  },
  watch: {
    field: {
      handler() {
        this.stylePercentValue = [Number(this.field.stylePercent) || 100]
      },
      deep: true,
      immediate: true
    },
    dataResourceRadio: {
      handler(value) {
        this.$set(this.field, 'optionDataType', value)
      }
    }
  },
  methods: {
    changeCode(code) {
      const column = this.columnList.find(column => column.code == code)
      if (column) {
        this.field.name = column.name
      }
    },
    emitUpdateWidth() {
      this.$emit('update-width')
    },
    emitChildEdit(field = null) {
      this.$emit('child-edit', field)
    },
    clickOutSide() {
      this.emitChildEdit()
    }
  }
}
</script>

<style scoped lang="scss">
.el-checkbox ::v-deep .el-checkbox__label {
  font-size: 13px;
  color: #333333;
}

.field-setting {
  .setting-title {
    padding: 20px 15px 0;
    font-weight: bold;
  }

  .setting-body {
    padding: 0 15px 10px;
    .input-tips {
      font-size: 12px;
      margin-top: 10px;
      color: #999;
      span {
        color: red;
      }
    }

    .item-section {
      padding: 10px 0;
      border-bottom: 1px solid #e6e6e6;
      .name {
        font-size: 13px;
        font-weight: 500;
        color: #333;
        margin: 10px 0;
      }
    }

    .item-check-section {
      margin-top: 10px;
    }
  }
}
</style>
