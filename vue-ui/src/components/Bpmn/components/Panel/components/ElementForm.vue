<template>
  <el-collapse-item name="element-form">
    <template #title>
      <collapse-title title="设置表单">
        <lucide-icon name="user-round" />
        <i :class="'icon-des' | iconPre" />
      </collapse-title>
    </template>
    <div class="element-form">
      <el-row>
        <el-select
          v-model="formKey"
          placeholder="请选择表单"
          clearable
          style="width: 100%;"
          @change="updateElementFormKey">
          <el-option v-for="item in formList" :key="item.code" :label="item.name" :value="`${item.code}`" />
        </el-select>
      </el-row>
      <el-row class="data-value">
        <el-tooltip content="若为节点表单，则表单信息仅在此节点可用，默认为全局表单，表单信息在整个流程实例中可用" placement="top-start" @click.stop.prevent>
          <i :class="'help lego-help-tips' | iconPre"/>
        </el-tooltip>
        <span class="custom-label">是否节点：</span>
        <el-switch v-model="localScope" @change="updateElementFormScope" />
      </el-row>
    </div>
  </el-collapse-item>
</template>

<script>
import LucideIcon from '../../common/LucideIcon'
import CollapseTitle from '../../common/CollapseTitle'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import { getActive } from '../../../bpmn-utils/BpmnDesignerUtils'
import { getBusinessObject } from 'bpmn-js/lib/util/ModelUtil'
import { setProperty } from '../../../bo-utils/formUtil'
import { customFormSimpleListAPI } from '@/api/admin/formField'
import { debounce } from 'min-dash'

export default {
  name: 'ElementForm',
  components: {
    CollapseTitle,
    LucideIcon
  },
  data() {
    return {
      formKey: '',
      localScope: true,
      formList: []
    }
  },
  mounted() {
    this.init()
    EventEmitter.on('element-update', () => {
      this.init()
    })
  },
  methods: {
    init: debounce(function() {
      customFormSimpleListAPI().then(res => {
        this.formList = res.data
        const elementObj = getBusinessObject(getActive())
        this.formKey = elementObj.get('formKey')
        this.localScope = elementObj.get('localScope')
      })
    }, 100),
    updateElementFormKey() {
      setProperty(getActive(), { formKey: this.formKey })
    },
    updateElementFormScope() {
      setProperty(getActive(), { localScope: this.localScope })
    }
  }
}
</script>

<style lang="scss" scoped>
.element-form {
  padding: 5px 15px;
  font-size: 15px;
}
.data-value {
  margin-top: 10px;
}
</style>
