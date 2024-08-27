<template>
	<el-row class="contnet" :style="{height: height}">
		<el-col id="left-content" class="left-content" :span="spanLeft">
      <slot name="left"></slot>
		</el-col>
		<el-col id="right-content" class="right-content" :style="{background: rightBackground}" :span="spanRight">
      <div id="resize" class="resize" title="收缩侧边栏">⋮</div>
      <slot name="right"></slot>
		</el-col>
	</el-row>
</template>

<script>
export default {
  name: 'SashFormLayout',
  props: {
    spanLeft: {
      type: Number,
      default: 6
    },
    spanRight: {
      type: Number,
      default: 18
    },
    height: {
      type: String,
      default: '420px'
    },
    rightBackground: {
      type: String,
      default: '#FFF'
    }
  },
  mounted() {
    this.dragControllerDiv()
  },
  methods: {
    dragControllerDiv() {
      const left = document.getElementById('left-content')
      const line = document.getElementById('resize')
      const right = document.getElementById('right-content')
      // 鼠标按下事件
      line.onmousedown = function(e) {
        const startX = e.clientX
        line.left = line.offsetLeft
        // 鼠标拖动事件
        document.onmousemove = function(e) {
          const moveLen = line.left + (e.clientX - startX)
          if (
            moveLen >= document.body.clientWidth * 0.1 &&
            moveLen <= document.body.clientWidth * 0.4
          ) {
            left.style.width = moveLen + 'px'
            right.style.width = document.body.clientWidth - moveLen + 'px'
          }
        }
        document.onmouseup = function() {
          document.onmousemove = null
          document.onmouseup = null
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.contnet {
  display: flex;
  margin: 0px !important;
}
.left-content {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  display: inline-block;
  -ms-flex-negative: 0;
  flex-shrink: 0;
  padding: 10px 0 0 10px;
  margin-right: 8px;
  overflow: auto;
}
.resize {
  cursor: col-resize;
  position: absolute;
  z-index: 1;
  top: 45%;
  background-color: #d6d6d6;
  border-radius: 5px;
  margin-top: -10px;
  margin-left: -10px;
  width: 10px;
  padding: 5px 0px;
  background-size: cover;
  background-position: 50%;
  font-size: 32px;
  color: #fff;
}
.right-content {
  display: inline-block;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  vertical-align: top;
  overflow: auto;
}
</style>
