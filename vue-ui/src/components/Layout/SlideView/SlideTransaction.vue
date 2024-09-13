<template>
  <transition
    name="slide-fade"
    @after-enter="afterEnter">
    <div
      id="slide"
      ref="slide"
      :style="{ 'z-index': zIndex }"
      :body-style="bodyStyle"
      class="slide-detail-card-container">
      <el-button
        v-if="showClose"
        class="close-btn xr-btn--orange"
        type="primary"
        icon="el-icon-close"
        @click="close"/>
      <slot/>
    </div>
  </transition>
</template>
<script type="text/javascript">
import { getMaxIndex } from '@/utils/index'

export default {
  name: 'SlideTransaction',
  componentName: 'SlideView',
  components: {},
  props: {
    showClose: {
      type: Boolean,
      default: true
    },
    bodyStyle: {
      type: Object,
      default: () => {
        return { padding: 0 }
      }
    },
    /** 监听点击事件 隐藏视图 */
    listenerIDs: {
      type: Array,
      default: () => {
        return []
      }
    },
    /** 阻挡点击事件 隐藏视图 */
    noListenerIDs: {
      type: Array,
      default: () => {
        return []
      }
    },
    noListenerClass: {
      type: Array,
      default: () => {
        return []
      }
    },
    appendToBody: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      zIndex: getMaxIndex(),
      targetData: {
        isMoveClick: false,
        pageX: 0,
        pageY: 0
      }
    }
  },
  computed: {},
  watch: {},
  created() {
  },
  mounted() {
    if (this.appendToBody) {
      document.body.appendChild(this.$el)
    }

    document
      .getElementById('app')
      .addEventListener('click', this.handleDocumentClick, false)
    document
      .getElementById('app')
      .addEventListener('mousedown', this.handleDocumentMousedown, false)
    document
      .getElementById('app')
      .addEventListener('mouseup', this.handleDocumentMouseup, false)
  },

  beforeDestroy() {
    if (this.appendToBody && this.$el && this.$el.parentNode) {
      this.$el.parentNode.removeChild(this.$el)
    }
    document
      .getElementById('app')
      .removeEventListener('click', this.handleDocumentClick, false)
    document
      .getElementById('app')
      .removeEventListener('mousedown', this.handleDocumentMousedown, false)
    document
      .getElementById('app')
      .removeEventListener('mouseup', this.handleDocumentMouseup, false)
  },
  methods: {
    handleDocumentClick(e) {
      if (this.targetData.isMoveClick) {
        this.targetData.isMoveClick = false
      } else {
        var hidden = true
        this.noListenerIDs.forEach(element => {
          if (document.getElementById(element) && document.getElementById(element).contains(e.target)) {
            hidden = false
          }
        })

        this.noListenerClass.forEach(element => {
          var items = document.getElementsByClassName(element)
          if (items && hidden) {
            for (let index = 0; index < items.length; index++) {
              const element = items[index]
              if (element.contains(e.target)) {
                hidden = false
                break
              }
            }
          }
        })
        if (this.$el && this.$el.contains(e.target)) {
          hidden = false
        }
        if (hidden) {
          this.close()
        }
      }
    },
    handleDocumentMousedown(e) {
      this.targetData.pageX = e.pageX
      this.targetData.pageY = e.pageY
    },
    handleDocumentMouseup(e) {
      if (Math.abs(this.targetData.pageX - e.pageX) > 30 || Math.abs(this.targetData.pageY - e.pageY) > 30) {
        this.targetData.isMoveClick = true
      } else {
        this.targetData.isMoveClick = false
      }
    },
    afterEnter() {
      this.$emit('afterEnter')
    },
    close() {
      this.$emit('close')
    }
  }
}
</script>
<style lang="scss" scoped>
.slide-fade-enter-active,
.slide-fade-leave-active {
  will-change: transform;
  transition: all 0.35s ease;
}
.slide-fade-enter,
.slide-fade-leave-to {
  transform: translateX(100%);
}

.el-card {
  overflow: visible;
}

.slide-detail-card-container {
  // position: relative;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: $xr-backgroud;
}

.close-btn {
  position: absolute;
  top: 160px;
  left: -40px;
  z-index: 0;
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
  padding: 6px;

  ::v-deep i {
    font-size: 26px;
    margin-right: 0;
  }
}
</style>
