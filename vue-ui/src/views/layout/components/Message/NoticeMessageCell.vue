<template>
  <flexbox
    :class="{ 'is-unread' : !messageData.readed }"
    class="message-cell"
    align="stretch">
    <div class="message-cell__hd">
      <i
        :class="typeObj.icon | iconPre"
      />
    </div>

    <div class="message-cell__bd">
      <span>您有一个新公告</span>
      <span
        :class="['click-content']"
        @click="enterDetail">《{{ messageData.name }}》</span>
      <span>，请及时查看</span>
    </div>

    <div class="message-cell__ft">
      <div>{{ messageData.createTime | formatTime }} </div>
      <div class="handle">
        <span class="check" @click="messageReadClick">标记已读</span>
        <span class="delete" @click="messageDeleteClick">删除</span>
      </div>
    </div>

    <el-dialog :title="noticeTitle" :visible.sync="contentVisible" width="70%" append-to-body>
      <div class="notice-content">
        <div v-html="noticeContent"/>
      </div>
    </el-dialog>
  </flexbox>
</template>

<script>
export default {
  name: 'MessageCell',
  props: {
    messageData: Object,
    dataIndex: Number
  },
  data() {
    return {
      contentVisible: false,
      noticeTitle: '',
      noticeContent: ''
    }
  },
  computed: {
    typeObj() {
      return {
        icon: 'announcement',
        color: '#6995FF'
      }
    }
  },
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    enterDetail() {
      if (this.isInvalid) {
        return
      }
      // 未读触发读
      if (!this.messageData.readed) {
        this.$emit('read', this.messageData.code, this.dataIndex)
      }
      this.$emit('detail', this.messageData.entityCode, this.dataIndex, this.messageData)
      this.noticeTitle = this.messageData.name
      this.noticeContent = this.messageData.content
      this.contentVisible = true
    },

    /**
     * 标记已读
     */
    messageReadClick() {
      this.$emit('read', this.messageData.code, this.dataIndex)
    },

    /**
     * 消息删除
     */
    messageDeleteClick() {
      this.$emit('delete', this.messageData.code, this.dataIndex)
    }
  }
}
</script>

<style lang="scss" scoped>
.message-cell {
  position: relative;
  font-size: 14px;
  color: #333;
  padding: 15px 20px 15px 15px;
  line-height: 1.5;

  &__hd {
    flex-shrink: 0;
    margin-right: 15px;
    background-color: $xr-color-primary;
    text-align: center;
    width: 28px;
    height: 28px;
    line-height: 28px;
    border-radius: 14px;

    .lego {
      color: white;
      font-size: 13px;
    }
  }

  &__bd {
    flex: 1;
    white-space: pre-wrap;
    word-wrap: break-word;
    word-break: break-all;
  }

  &__ft {
    flex-shrink: 0;
    font-size: 12px;
    color: #999;
    width: 85px;
    margin-left: 35px;
    text-align: right;
    position: relative;

    .handle {
      position: absolute;
      top: 20px;
      right: 0;
      font-size: 12px;
      color: #999;
      .check,
      .delete {
        visibility: hidden;
        cursor: pointer;
      }

      .check + .delete {
        margin-left: 5px;
      }

      .check:hover {
        color: $xr-color-primary;
      }
      .delete:hover {
        color: #F56C6C;
      }
    }
  }

  &.is-unread::before {
    content: '';
    position: absolute;
    top: 20px;
    right: 8px;
    width: 6px;
    height: 6px;
    border-radius: 3px;
    background-color: #f94e4e;
  }

  &:hover {
    background-color: #f7f8fa;
    .delete {
      visibility: visible;
    }
  }

  &.is-unread:hover {
    .check {
      visibility: visible;
    }
  }
}


.click-content {
  cursor: pointer;
  color: $xr-color-primary;

  &:hover {
    text-decoration: underline;
  }
}

.is-invalid {
  color: #999;
  pointer-events: none;
  cursor: initial;
}

.notice-content {
  min-height: 400px;
  max-height: 500px;
  overflow-y: auto;
}
</style>
