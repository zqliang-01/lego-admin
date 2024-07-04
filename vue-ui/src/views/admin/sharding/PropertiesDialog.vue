<template>
  <div>
    <el-dialog
      v-loading="loading"
      :visible="visible"
      :close-on-click-modal="false"
      title="属性更新"
      class="new-dialog-form"
      @close="handleCancel">
      <el-form ref="propertyForm" :model="properties" :label-position="labelPosition" label-width="80px">
        <el-form-item v-for="value in templateProperties" :key="value.code">
          <template slot="label">
            {{ value.name }}({{ value.code }})
            <el-tooltip
              effect="dark"
              placement="top">
              <div slot="content" v-html="value.description"/>
              <i :class="'help lego-help-tips' | iconPre"/>
            </el-tooltip>
          </template>
          <el-input v-model="properties[value.code]" :placeholder="value.description" />
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary"
          @click="saveProperties">保 存</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  propertiesListAPI,
  propertiesAddAPI
} from '@/api/admin/sharding/properties'
import { templateJsonGetAPI } from '@/api/admin/sharding/template'

export default {
  name: 'ShardingPropertiesDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    entityId: String,
    templateCode: String,
    configCode: String,
    labelPosition: {
      type: String,
      default: 'top'
    }
  },
  data() {
    return {
      loading: false,
      templateProperties: [],
      properties: {}
    }
  },
  watch: {
    visible() {
      this.init()
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      if (this.templateCode) {
        templateJsonGetAPI(this.templateCode).then(res => {
          this.templateProperties = JSON.parse(res.data)
        })
        propertiesListAPI({
          entityId: this.entityId
        }).then(res => {
          this.properties = {}
          res.data.forEach(value => {
            this.$set(this.properties, value.code, value.name)
          })
        })
      }
    },
    handleCancel() {
      this.$emit('update:visible', false)
    },
    saveProperties() {
      this.loading = true
      propertiesAddAPI({
        entityId: this.entityId,
        configCode: this.configCode,
        templateCode: this.templateCode,
        properties: this.properties
      }).then(() => {
        this.loading = false
        this.$message.success('更新成功！')
        this.$emit('onSuccess')
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.new-dialog-form ::v-deep .el-form-item {
  margin: 0;
}
.new-dialog-form ::v-deep .el-form-item .el-form-item__label {
  padding: 0;
}
.new-dialog-form ::v-deep .el-dialog__body {
  max-height: 60vh;
  overflow-y: auto;
}
</style>
