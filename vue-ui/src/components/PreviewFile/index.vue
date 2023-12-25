<template>
  <div
    v-if="visible"
    id="vue-picture-viewer"
    ref="vuePictureViewer"
    :style="maskContainer"
    @mouseup="removeMove()">
    <!-- 头部 -->
    <div class="perview-header">
      <span>{{ title }}</span>
    </div>
    <!-- 图片容器 -->
    <div
      ref="imgContainer"
      class="imgContainer">
      <img
        v-src="bigImageUrl"
        v-if="currentFile.type.code == 'image'"
        ref="bigImg"
        :style="
          'transform: scale(' +
            imgScale +
            ') rotate(' +
            imgRotate +
            'deg);margin-top:' +
            imgTop +
            'px;margin-left:' +
            imgLeft +
            'px;' +
            'max-width:80%;max-height:80%;'
        "
        alt=""
        @click.stop=""
        @mousedown="addMove">
      <div
        v-if="currentFile.type.code == 'file'"
        class="file-show">
        <div v-if="!showPreviewBtn" class="title">该附件格式不支持预览，请下载后查看</div>
        <i
          class="el-icon-close"
          @click="closeViewer" />
        <div class="content">
          <img
            :src="fileIcon"
            class="file-icon">
          <div class="file-handle">
            <el-button
              v-if="showPreviewBtn"
              type="primary"
              plain
              @click.native="previewFile">预览</el-button>
            <el-button
              type="primary"
              @click.native="downloadFile">下载</el-button>
          </div>
        </div>
      </div>
      <!-- tips -->
      <transition name="fade">
        <div
          v-show="showTips"
          class="tips">{{ tipsText }}</div>
      </transition>
    </div>
    <div class="fixedHandle">
      <!-- 操作按钮 -->
      <flexbox
        v-if="currentFile.type.code == 'image'"
        class="handleContainer">
        <div class="handle-box">
          <i
            :class="'zoom-in' | iconPre"
            @click="enlarge" />
          <i
            :class="'zoom-on' | iconPre"
            @click="reduce" />
          <i
            :class="'rotate' | iconPre"
            @click="rotate" />
          <i
            :class="'download' | iconPre"
            @click="downloadFile" />
        </div>

        <div
          class="icon-btn"
          @click="closeViewer">
          <i class="el-icon-close" />
        </div>
      </flexbox>

      <!-- 缩略图容器 -->
      <div
        v-if="imgLength > 1"
        class="thumbnailContainer">
        <ul>
          <li
            v-for="(item, index) in imgData"
            ref="thumbnailItem"
            :key="index"
            @click="switchImgUrl(index, $event)">
            <img
              v-src="getItemImageUrl(item)"
              v-if="item.type.code == 'image'"
              alt="">
            <img
              v-else
              :src="getFileIcon(item.name)"
              alt="">
          </li>
        </ul>
      </div>
    </div>
    <!-- 左边箭头 -->
    <div
      class="leftArrowCon"
      @click="handlePrev"
      @mouseenter="enterLeft"
      @mouseout="outLeft">
      <div
        v-show="leftArrowShow"
        class="icon-btn leftArrow">
        <i class="el-icon-arrow-left" />
      </div>
    </div>
    <!-- 右边箭头 -->
    <div
      class="rightArrowCon"
      @click="handleNext"
      @mouseenter="enterRight"
      @mouseout="outRight">
      <div
        v-show="rightArrowShow"
        class="icon-btn rightArrow">
        <i class="el-icon-arrow-right" />
      </div>
    </div>
  </div>
</template>

<script>
import {
  getMaxIndex,
  getFileIconWithSuffix,
  downloadFileWithBuffer,
  canPreviewFile,
  previewFile
} from '@/utils'
import { fileDownloadAPI, filePreviewUrl } from '@/api/common'

export default {
  name: 'PreviewFile',
  props: {},
  data() {
    return {
      visible: false,
      imgData: [],
      background: 'rgba(0,0,0,0.4)',
      // 选择的索引
      selectIndex: -1,
      // 默认不显示左右切换箭头
      leftArrowShow: false,
      rightArrowShow: false,
      // 图片容器数据
      fileIcon: '',
      bigImgName: '',
      imgLength: 0,
      imgIndex: 0,
      showTips: false,
      tipsText: '',
      maskContainer: {
        width: '100%',
        height: '100%',
        background: this.background,
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0
      },
      // 预览图片样式
      imgTop: 0,
      imgLeft: 0,
      imgScale: 1,
      imgRotate: 0,
      clientX: 0,
      clientY: 0
    }
  },
  computed: {
    title() {
      const fileName = this.bigImgName.slice(0, this.bigImgName.indexOf('.'))
      return `${fileName} （${this.imgIndex + 1} / ${this.imgLength}）`
    },

    currentFile() {
      return this.imgData[this.imgIndex]
    },

    bigImageUrl() {
      if (this.currentFile.type.code == 'image') {
        return filePreviewUrl + this.currentFile.code
      }
      return ''
    },

    showPreviewBtn() {
      return canPreviewFile(this.currentFile.name)
    }
  },
  methods: {
    /**
     * 预览
     */
    preview(data) {
      this.selectIndex = data.index
      this.imgData = data.data
      this.imgLength = this.imgData.length
      this.imgIndex = this.selectIndex
      this.visible = true

      this.$nextTick(() => {
        const _dom = document.getElementById('vue-picture-viewer')
        _dom.onmousewheel = this.scrollFunc
        // 火狐浏览器没有onmousewheel事件，用DOMMouseScroll代替(滚轮事件)
        document.body.addEventListener('DOMMouseScroll', this.scrollFunc)
        // 禁止火狐浏览器下拖拽图片的默认事件
        document.ondragstart = function() {
          return false
        }
        document.getElementById('vue-picture-viewer')
          .addEventListener('click', e => {
            e.stopPropagation()
          })
        this.$nextTick(() => {
          this.bigImgName = this.imgData[this.imgIndex].name
          this.fileIcon = this.getFileIcon(this.bigImgName)
          if (this.imgLength > 1) {
            // 大于1的时候才会展示缩略图
            var item = this.$refs.thumbnailItem
            item[this.imgIndex].className = 'borderActive'
          }
          this.init()
        })
        this.maskContainer['z-index'] = getMaxIndex()
      })
    },
    getItemImageUrl(item) {
      return filePreviewUrl + item.code
    },
    /**
     * init
     */
    init() {
      this.imgTop = 0
      this.imgLeft = 0
      this.imgScale = 1
      this.imgRotate = 0
      this.clientX = 0
      this.clientY = 0
    },

    /**
     * 放大
     */
    enlarge() {
      if (this.imgScale >= 5) return
      this.imgScale += 0.15
    },

    /**
     * 缩小
     */
    reduce() {
      if (this.imgScale <= 0.2) return

      this.imgScale -= 0.15
    },

    /**
     * 旋转
     */
    rotate() {
      this.imgRotate -= 90
    },

    /**
     * 点击缩略图切换图片
     */
    switchImgUrl(num, e) {
      var item = this.$refs.thumbnailItem
      item.forEach(function(i) {
        i.className = ''
      })
      this.imgIndex = num
      this.bigImgName = this.imgData[num].name
      this.fileIcon = this.getFileIcon(this.bigImgName)
      e.currentTarget.className = 'borderActive'
      if (this.imgData[num].type.code == 'image') {
        this.init()
      }
    },

    /**
     * 切换到上一张
     */
    handlePrev() {
      if (this.imgIndex <= 0) {
        this.tips('已经是第一张了!')
        this.imgIndex = 0
      } else {
        this.imgIndex--
        this.bigImgName = this.imgData[this.imgIndex].name
        this.fileIcon = this.getFileIcon(this.bigImgName)

        var item = this.$refs.thumbnailItem
        item.forEach(function(i) {
          i.className = ''
        })
        item[this.imgIndex].className = 'borderActive'
        if (item[this.imgIndex].type.code == 'image') {
          this.init()
        }
      }
    },

    /**
     * 切换到下一张
     */
    handleNext() {
      if (this.imgIndex + 1 >= this.imgData.length) {
        this.tips('已经是最后一张了!')
      } else {
        this.imgIndex++
        this.bigImgName = this.imgData[this.imgIndex].name
        this.fileIcon = this.getFileIcon(this.bigImgName)

        var item = this.$refs.thumbnailItem
        item.forEach(function(i) {
          i.className = ''
        })
        item[this.imgIndex].className = 'borderActive'
        if (item[this.imgIndex].type.code == 'image') {
          this.init()
        }
      }
    },

    /**
     * 提示框
     */
    tips(msg) {
      this.showTips = true
      this.tipsText = msg
      const _this = this
      setTimeout(function() {
        _this.showTips = false
      }, 10000)
    },

    /**
     * 鼠标左移
     */
    enterLeft() {
      this.leftArrowShow = true
    },
    outLeft() {
      this.leftArrowShow = false
    },

    /**
     * 鼠标右移
     */
    enterRight() {
      this.rightArrowShow = true
    },
    outRight() {
      this.rightArrowShow = false
    },

    /**
     * 关闭查看器
     */
    closeViewer() {
      if (document.getElementById('vue-picture-viewer')) {
        document
          .getElementById('vue-picture-viewer')
          .removeEventListener('click', e => {
            e.stopPropagation()
          })
        // 移除火狐浏览器下的鼠标滚动事件
        document.body.removeEventListener('DOMMouseScroll', this.scrollFunc)
        // 恢复火狐及Safari浏览器下的图片拖拽
        document.ondragstart = null
      }

      this.visible = false
      this.imgData = []
      this.selectIndex = -1
      this.showTips = false
    },

    /**
     * 附件逻辑
     */
    downloadFile() {
      fileDownloadAPI(this.currentFile.code).then(res => {
        const blob = new Blob([res.data], { type: '' })
        downloadFileWithBuffer(blob, this.currentFile.name)
      }).catch(() => {})
    },
    previewFile() {
      this.$message.error('暂未支持非图片类型文件预览！')
      return
      if (this.currentFile.url) {
        previewFile(this.currentFile.url, this.currentFile.name)
      }
    },
    getFileIcon(name) {
      var ext = ''
      const temps = name ? name.split('.') : []
      if (temps.length > 0) {
        ext = temps[temps.length - 1]
      }
      return getFileIconWithSuffix(ext)
    },
    removeMove() {
      this.$refs.vuePictureViewer.onmousemove = null
    },

    /**
     * 鼠标按下
     */
    addMove(e) {
      e = e || window.event
      this.clientX = e.clientX
      this.clientY = e.clientY
      this.$refs.vuePictureViewer.onmousemove = this.moveFunc
    },

    /**
     * 鼠标拖动
     */
    moveFunc(e) {
      e = e || window.event
      e.preventDefault()
      const movementX = e.clientX - this.clientX
      const movementY = e.clientY - this.clientY
      // event.clientY;
      this.imgLeft += movementX * 2
      this.imgTop += movementY * 2
      this.clientX = e.clientX
      this.clientY = e.clientY
    },

    /**
     * 鼠标滚轮缩放
     */
    scrollFunc(e) {
      e = e || window.event
      // e.returnValue = false // ie
      // 火狐下没有wheelDelta，用detail代替，由于detail值的正负和wheelDelta相反，所以取反
      e.delta = e.wheelDelta || -e.detail

      e.preventDefault()
      if (e.delta > 0) {
        // 当滑轮向上滚动时
        this.enlarge()
      }
      if (e.delta < 0) {
        // 当滑轮向下滚动时
        this.reduce()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.perview-header {
  width: 100%;
  height: 40px;
  background: rgba(0, 0, 0, 0.6);
  color: rgba(255, 255, 255, 0.8);
  line-height: 40px;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 101;
  padding: 10px;
  text-align: center;
  padding: 0 20px;
  cursor: pointer;
}

.leftArrowCon {
  width: 30%;
  height: calc(100% - 40px);
  background: transparent;
  position: absolute;
  top: 40px;
  left: 0;
  z-index: 98;
  cursor: pointer;
}
.rightArrowCon {
  width: 30%;
  height: calc(100% - 40px);
  background: transparent;
  position: absolute;
  top: 40px;
  right: 0;
  z-index: 99;
  cursor: pointer;
}
.leftArrow {
  position: absolute;
  top: 50%;
  left: 25%;
  margin-top: -60px;
  transition: all 0.5s;
  pointer-events: none;
}
.rightArrow {
  position: absolute;
  top: 50%;
  right: 25%;
  margin-top: -60px;
  transition: all 0.5s;
  pointer-events: none;
}
.imgContainer {
  width: 100%;
  height: 100%;
  text-align: center;
  vertical-align: middle;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(0,0,0,.3);
}
.imgContainer .tips {
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  text-align: center;
  line-height: 40px;
  position: absolute;
  left: 50%;
  top: 50%;
  min-width: 150px;
  margin-left: -60px;
  margin-top: -20px;
  border-radius: 6px;
  padding: 4px 4px;
  font-size: 14px;
}
.fixedHandle {
  width: 800px;
  height: 140px;
  position: fixed;
  left: 50%;
  bottom: 0;
  margin-left: -400px;
  overflow: hidden;
  z-index: 100;
}
.handleContainer {
  width: auto;
  position: absolute;
  left: 50%;
  bottom: 100px;
  margin-left: -150px;

  .handle-box {
    background: rgba(0, 0, 0, 0.6);
    height: 40px;
    line-height: 40px;
    border-radius: 20px;
    padding: 0 20px;
    margin-right: 30px;
    user-select: none;

    i {
      font-size: 20px;
      color: #fff;
      cursor: pointer;
    }

    i + i {
      margin-left: 15px;
    }
  }
}

// 图标按钮
.icon-btn {
  cursor: pointer;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  width: 40px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  border-radius: 20px;

  i {
    font-weight: 600;
  }
}

.handleItem {
  width: 28px;
  height: 28px;
  color: white;
}
ul {
  padding: 0;
  margin: 0;
}
ul li {
  list-style: none;
  display: inline-block;
  width: 30px;
  height: 30px;
  margin-left: 20px;
  cursor: pointer;
}

.thumbnailContainer {
  max-width: 800px;
  background: rgba(255, 255, 255, 0.7);
  position: absolute;
  left: 50%;
  bottom: 0;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  transform: translate(-50%, 0%);
  overflow-x: auto;
  overflow-y: hidden;
}

.thumbnailContainer ul {
  padding-top: 10px;
  padding-bottom: 10px;
  text-align: center;
  white-space: nowrap;
}
.thumbnailContainer ul li {
  display: inline-block;
  width: 38px;
  height: 38px;
  box-sizing: content-box;
  margin-left: 10px;
  user-select: none;
}
.thumbnailContainer ul li:last-child {
  margin-right: 10px;
}
.thumbnailContainer ul li img {
  object-fit: contain;
  vertical-align: top;
  width: 100%;
  height: 100%;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

/* 添加border */
.borderActive {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
}
/* 修改原生的滚动条 */
::-webkit-scrollbar {
  /* 血槽宽度 */
  width: 8px;
  height: 8px;
}
::-webkit-scrollbar-thumb {
  /* 拖动条 */
  background: rgba(0, 0, 0, 0.3);
  border-radius: 6px;
}
::-webkit-scrollbar-track {
  /* 背景槽 */
  background: #ddd;
  border-radius: 6px;
}
/** 文件展示*/
.file-show {
  position: absolute;
  top: 60%;
  left: 50%;
  width: 450px;
  height: 260px;
  margin-top: -150px;
  margin-left: -225px;
  background-color: white;
  border-radius: 3px;
  padding: 15px;

  .el-icon-close {
    position: absolute;
    top: 20px;
    right: 20px;
    font-size: 18px;
    font-weight: bold;
    color: #909399;
    cursor: pointer;
  }

  .el-icon-close:hover {
    color: $xr-color-primary;
  }

  .title {
    position: relative;
    font-size: 13px;
    color: #666;
    padding-left: 6px;
  }

  .title::before {
    content: '*';
    position: absolute;
    left: 0;
    top: 0;
    color: red;
  }

  .content {
    text-align: center;
    margin-top: 40px;

    .file-icon {
      width: 100px;
      width: 85px;
      vertical-align: middle;
    }

    .file-handle {
      width: 100px;
      margin-left: 50px;;
      margin-right: 0;
      vertical-align: middle;
      display: inline-block;

      .el-button {
        height: 34px;
        margin-left: 0;
      }

      .el-button + .el-button {
        margin-top: 8px;
      }
    }
  }
}
</style>
