<template>
  <div ref="signaturePad" class="signature-pad">
    <signature-image
      v-if="disabled"
      :src="value"
      :height="height"
    />
    <template v-else>
      <vue-signature-pad
        ref="signaturePad"
        :key="height"
        :options="options"
        :height="height"
        width="100%"
      />
      <div class="signature-pad__handle">
        <el-button :icon="'icon-reply' | iconPre" type="text" @click="handleClick('undo')">撤回</el-button>
        <el-button :icon="'icon-bin' | iconPre" type="text" @click="handleClick('clear')">清空</el-button>
        <el-button v-if="edited" :icon="'icon-bin' | iconPre" type="text" @click="handleClick('save')">确认</el-button>
      </div>
    </template>
  </div>
</template>

<script>
import { fileGetAPI } from '@/api/common'
import { fileUploadAPI } from '@/api/common'

import VueSignaturePad from './VueSignaturePad'
import SignatureImage from './Image'

import { valueEquals } from 'element-ui/lib/utils/util'
import { getImageData } from '@/utils'

export default {
  // 签名
  name: 'SignaturePad',
  components: {
    VueSignaturePad,
    SignatureImage
  },
  props: {
    value: String,
    data: String, // 同步数据源
    disabled: Boolean
  },
  data() {
    return {
      edited: false,
      options: {
        backgroundColor: '#fff',
        onEnd: this.onEnd,
        minWidth: 1,
        maxWidth: 3
      },
      height: '150px'
    }
  },
  watch: {
    data(newVal, oldVal) {
      if (!valueEquals(newVal, oldVal)) {
        this.$refs.signaturePad.fromDataURL(newVal)
      }
    }
  },
  mounted() {
    if (this.value) {
      this.getData()
    }
    this.height = `${parseInt(this.$refs.signaturePad.clientWidth / 2.6)}px`
  },
  methods: {
    getData() {
      if (!this.value) {
        return
      }
      fileGetAPI(this.value).then(res => {
        const resData = res.data
        if (resData) {
          getImageData(resData.code).then(data => {
            var img = new Image()
            img.onload = () => {
              const canvasWidth = this.$refs.signaturePad.canvas.width
              const width = img.width
              const ratio = canvasWidth / width
              console.log(width, canvasWidth, ratio)
              this.options.minWidth = this.options.minWidth * ratio
              this.options.maxWidth = this.options.maxWidth * ratio

              this.$nextTick(() => {
                this.$refs.signaturePad.fromDataURL(data.src)
              })
            }
            img.src = data.src
          })
        }
      })
    },
    onEnd() {
      this.edited = true
    },
    uploadFile(blob) {
      fileUploadAPI({
        file: new File([blob], '手写签字.png'),
        entityCode: 'manage',
        permissionCode: 'manage'
      }).then(res => {
        this.$message.success('签字成功！')
        this.$emit('input', res.data)
        this.edited = false
      })
    },

    handleClick(type) {
      if (type === 'clear') {
        this.$refs.signaturePad.clearSignature()
      } else if (type === 'undo') {
        this.$refs.signaturePad.undoSignature()
      } else if (type === 'save') {
        const { isEmpty } = this.$refs.signaturePad.saveSignature()
        if (isEmpty) {
          this.$message.error('签字内容为空！')
        }
        this.$refs.signaturePad.toBlob((blob) => {
          this.uploadFile(blob)
        })
      }
    }
  }
}
</script>
<style lang="scss">
.el-form-item.is-error {
  .signature-pad {
    border-color: #f56c6c;
  }
}
</style>

<style lang="scss" scoped>
.signature-pad {
  position: relative;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  overflow: hidden;

  &__handle {
    position: absolute;
    right: 15px;
    bottom: 0;
    .el-button--text {
      color: #999;
      &:hover {
        color: $xr-color-primary;
      }
    }
  }
}
</style>

