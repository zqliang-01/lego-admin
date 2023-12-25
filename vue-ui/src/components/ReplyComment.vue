<template>
  <div
    v-loading="commentLoading"
    class="reply-comment">
    <template v-if="!showNoFocus">
      <el-input
        v-model="commentsTextarea"
        :rows="3"
        :maxlength="2000"
        show-word-limit
        type="textarea"
        placeholder="请输入回复内容"
        @blur="blurFun" />
      <div class="btn-group">
        <el-popover
          v-model="showEmoji"
          placement="top"
          width="400"
          trigger="click">
          <!-- 表情 -->
          <emoji @select="selectEmoji" />
          <i
            slot="reference"
            :class="'expression smiling-img' | iconPre" />
        </el-popover>
        <div class="btn-box">
          <el-button
            type="primary"
            @click="commentSubmit">回复</el-button>
          <el-button @click="closeComment">取消</el-button>
        </div>
      </div>
    </template>

    <div
      v-else
      class="no-focus"
      @click="toggleFocus()">
      请输入回复内容
    </div>
  </div>
</template>

<script>
/**
 * 回复输入框
 * event:      close 关闭输入框
 *             reply 确认输入  参数： {String} 输入框值
 *             toggle 状态切换
 * public fn:  toggleFocus 切换输入框状态
 */
import xss from 'xss'
import Emoji from '@/components/Emoji'

export default {
  name: 'ReplyComment',
  components: {
    Emoji
  },
  data() {
    return {
      commentLoading: false, // 回复loading
      blurIndex: 0, // 回复表情插入位置
      commentsTextarea: '', // 回复内容
      showEmoji: false, // emoji选择标志
      showNoFocus: false
    }
  },
  methods: {
    /**
     * 输入框失去焦点
     */
    blurFun(eve) {
      this.blurIndex = eve.target.selectionEnd
    },
    /**
     * emoji 表情选择
     */
    selectEmoji(val) {
      if (this.commentsTextarea && this.commentsTextarea.length + val.length <= 2000 || !this.commentsTextarea) {
        const list = this.commentsTextarea.split('')
        list.splice(this.blurIndex, 0, val)
        this.commentsTextarea = list.join('')
        this.showEmoji = false
      }
    },
    /**
     * 提交评论回复
     */
    commentSubmit() {
      if (!this.commentsTextarea) {
        this.$message.error('回复内容不能为空')
        return
      }
      this.$emit('reply', xss(this.commentsTextarea))
    },
    /**
     * 关闭评论回复框
     */
    closeComment() {
      this.showEmoji = false
      this.$emit('close')
      this.toggleFocus()
    },

    /**
     * 切换输入框状态
     */
    toggleFocus(flag) {
      if (typeof flag === 'boolean') {
        this.showNoFocus = flag
      } else {
        this.showNoFocus = !this.showNoFocus
      }
      this.$emit('toggle', this.showNoFocus)
    }
  }
}
</script>

<style scoped lang="scss">
  .reply-comment {
    border: 1px solid #e6e6e6;
    border-radius: $xr-border-radius-base;
    overflow: hidden;
    .btn-group {
      background-color: white;
      padding: 0 10px 5px 10px;
      overflow: hidden;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      .btn-box {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }

      .smiling-img {
        cursor: pointer;
        font-size: 17px;
        color: #666;
      }
    }

    .el-textarea /deep/ .el-textarea__inner {
      resize: none;
      border: 0;
    }

    .no-focus {
      width: 100%;
      font-size: 13px;
      color: #c0c4cc;
      background-color: white;
      border-radius: $xr-border-radius-base;
      padding: 9px 8px;
      cursor: pointer;
    }
  }
</style>
