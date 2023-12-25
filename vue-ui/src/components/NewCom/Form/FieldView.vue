<template>
  <div
    :class="`is-${formType}`"
    class="field-view">
    <slot name="leftContent" />
    <el-switch
      v-if="formType == 'boolean_value'"
      :value="value"
      disabled />
    <signature-image
      v-else-if="formType == 'handwriting_sign'"
      :src="value"
      :height="config.signatureHeight" />
    <desc-text
      v-else-if="formType == 'desc_text'"
      :key="Date.now().toString()"
      :value="value" />
    <span
      v-else-if="formType == 'website'"
      :class="{'can-check': !isEmpty}"
      @click.stop="openUrl(value)" >{{ value }}</span>
    <span
      v-else-if="formType == 'entity'"
      :class="{'can-check': !isEmpty}"
      @click="handleEntityClick(value)" >{{ getCommonShowValue() }}</span>
    <span v-else>{{ getCommonShowValue() }}</span>
    <map-view
      v-if="mapViewShow"
      :title="value.address"
      :lat="value.lat"
      :lng="value.lng"
      @hidden="mapViewShow=false"
    />
  </div>
</template>

<script>
import SignatureImage from '@/components/NewCom/SignaturePad/Image'
import DescText from '@/components/NewCom/DescText'
import MapView from '@/components/MapView' // 地图详情

import merge from '@/utils/merge'
import { isObject, isEmpty } from '@/utils/types'
import { getFormFieldShowValue } from './utils'

const DefaultFieldView = {
  signatureHeight: '26px'
}

export default {
  // 特殊字段展示
  name: 'FieldView',

  components: {
    SignatureImage,
    DescText,
    MapView
  },

  props: {
    props: Object, // 自定义字段参数信息
    leftContent: Object,
    formType: String,
    value: [String, Object, Array, Number, Boolean]
  },

  data() {
    return {
      // 控制展示地图详情
      mapViewShow: false
    }
  },

  computed: {
    config() {
      return merge({ ...DefaultFieldView }, this.props || {})
    },
    isEmpty() {
      return isEmpty(this.value)
    },
    infoName() {
      if (isEmpty(this.value)) {
        return this.value
      }
      if (isObject(this.value) && this.value.hasOwnProperty('name')) {
        return this.value.name
      }
      const info = this.props.setting.find(item => item.code == this.value)
      return isEmpty(info) ? this.value : info.name
    },
    treeName() {
      if (isEmpty(this.value)) {
        return this.value
      }
      if (isObject(this.value) && this.value.hasOwnProperty('name')) {
        return this.value.name
      }
      return this.getTreeName(this.props.setting, this.value)
    }
  },
  methods: {
    /**
		 * 判断对象是否值
		 */
    objectHasValue(obj, key) {
      if (isObject(obj)) {
        return !isEmpty(obj[key])
      }
      return false
    },

    openUrl(url) {
      if (!url.match(/^https?:\/\//i)) {
        url = 'http://' + url
      }
      window.open(url)
    },

    /**
     * 获取类型的展示值
     */
    getCommonShowValue() {
      return getFormFieldShowValue(this.formType, this.value)
    },

    getTreeName(trees, value) {
      let name = ''
      trees.forEach(tree => {
        if (tree.childrens.length > 0) {
          const tmp = this.getTreeName(tree.childrens, value)
          if (tmp) {
            name = tmp
          }
        } else if (tree.code = value) {
          name = tree.name
        }
      })
      return name
    },
    handleEntityClick(value) {
      this.$emit('clickEntity', { field: this.props, value: value })
    }
  }
}
</script>

<style lang="scss" scoped>
.field-view {
  overflow: hidden;
  text-overflow: ellipsis;
	.can-check {
		color: $xr-color-primary;
		cursor: pointer;
	}

	&.is-website {
		display: inline;
	}
}
</style>
