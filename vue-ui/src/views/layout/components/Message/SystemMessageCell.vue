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
      <span
        v-if="canClick"
        :class="['click-content']"
        @click="enterDetail">{{ messageData.content }}</span>
      <span v-else>{{ messageData.content }}</span>
    </div>

    <div class="message-cell__ft">
      <div>{{ messageData.createTime | formatTime }} </div>
      <div class="handle">
        <span class="check" @click="messageReadClick">标记已读</span>
        <span class="delete" @click="messageDeleteClick">删除</span>
      </div>
    </div>
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
    return {}
  },
  computed: {
    typeObj() {
      if (this.messageData.type.code === 'flowable') {
        return {
          icon: 'office',
          color: '#6995FF'
        }
      }
      return {
        icon: 'log',
        color: '#6995FF'
      }
    },
    canClick() {
      return this.messageData.entityCode !== ''
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
      this.$emit('detail', this.messageData.type.code, this.messageData.entityCode, this.dataIndex, this.messageData)
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

  &:hover {
    color: $xr-color-primary;
  }
}

.is-invalid {
  color: #999;
  pointer-events: none;
  cursor: initial;
}
</style>
