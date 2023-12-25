<template>
  <el-dialog
    v-loading="loading"
    :visible="visible"
    :append-to-body="true"
    :close-on-click-modal="false"
    :title="title"
    width="400px"
    @close="handleCancel">
    <el-form ref="form" label-width="80px">
      <el-form-item label="模板名称">
        <el-input v-model="dataForm.templateName"/>
      </el-form-item>
      <el-form-item v-if="!isEdit" label="关联对象">
        <el-select v-model="dataForm.type" style="width: 100%;">
          <el-option
            v-for="(item, index) in options"
            :key="index"
            :label="item.label"
            :value="item.value"/>
        </el-select>
      </el-form-item>
    </el-form>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="handleCancel">取消</el-button>
      <el-button
        v-debounce="handleConfirm"
        type="primary">{{ confirmText }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { printAddTemplateAPI, printUpdateTemplateAPI } from '@/api/admin/printTemplate'

export default {
  // 自定义模板新建
  name: 'TemplateTypeAdd',
  components: {},
  mixins: [],
  props: {
    detail: Object, // 编辑用
    visible: {
      type: Boolean,
      required: true,
      default: false
    }
  },
  data() {
    return {
      loading: true,
      dataForm: {},
      options: [
        {
          label: '商机',
          value: 5
        },
        {
          label: '合同',
          value: 6
        },
        {
          label: '回款',
          value: 7
        },
        {
          label: '供应商',
          value: 19
        }
      ]
    }
  },
  computed: {
    isEdit() {
      return !!this.detail
    },

    title() {
      return this.isEdit ? '编辑打印模板' : '新建打印模板'
    },

    confirmText() {
      return this.isEdit ? '保存' : '下一步'
    }
  },
  watch: {
    visible: {
      handler(val) {
        if (val) {
          this.dataForm = {
            templateName: '',
            type: 5
          }

          if (this.isEdit) {
            this.dataForm.templateName = this.detail.templateName
          }
        }
      },
      immediate: true
    }
  },
  mounted() {},
  methods: {
    /**
     * 取消选择
     */
    handleCancel() {
      this.$emit('update:visible', false)
    },

    /**
     * 点击确定
     */
    handleConfirm() {
      if (!this.dataForm.templateName) {
        return
      }

      if (this.isEdit) {
        this.loading = true
        const request = this.isEdit ? printUpdateTemplateAPI : printAddTemplateAPI
        const params = this.isEdit ? {
          templateName: this.dataForm.templateName,
          templateId: this.detail.templateId
        } : this.dataForm
        request(params)
          .then(res => {
            this.$message({
              type: 'success',
              message: '编辑成功'
            })
            this.loading = false
            this.handleCancel()
            this.$emit('save')
          })
          .catch(() => {
            this.loading = false
          })
      } else {
        this.$emit('next', this.dataForm)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
