<template>
  <div class="comment">
    <template v-if="commentList.length > 0">
      <div v-for="comment in commentList">
        <div class="comment-section__title">
          <span class="section-title"><span class="section-title__time">{{ comment.createTime }}</span></span>
        </div>
        <div class="comment-cell activity-cell">
          <i
            :class="handleIcon(comment.type)"
            class="comment-cell__mark"
            :style="{ backgroundColor: handleIconColor(comment.type) }"/>
          <div class="activity-cell">
            <span class="activity-cell__content">{{ comment.type.name }}</span>:
            <span class="activity-cell__label">{{ comment.content }}</span>
          </div>
        </div>
      </div>
    </template>
    <template v-else-if="isView">
      <div
        v-empty="true"
        xs-empty-text="暂无内容">
      </div>
    </template>
  </div>
</template>
<script>
export default {
  props: {
    commentList: {
      type: Array,
      default: () => {
        return []
      }
    },
    isView: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false
    }
  },
  methods: {
    handleIconColor(type) {
      if (!type) {
        return '#487dff'
      }
      if (type.code === 'transfer') {
        return '#67C23A'
      }
      if (type.code === 'delegate') {
        return '#E6A23C'
      }
      if (type.code === 'reject') {
        return '#F56C6C'
      }
      return '#487dff'
    },
    handleIcon(type) {
      if (!type) {
        return 'lego lego-icon-checkbox'
      }
      if (type.code === 'transfer') {
        return 'el-icon-thumb'
      }
      if (type.code === 'delegate') {
        return 'el-icon-chat-line-square'
      }
      if (type.code === 'reject') {
        return 'el-icon-circle-close'
      }
      return 'lego lego-icon-checkbox'
    }
  }
}
</script>
<style lang="scss" scoped>
.comment {
  padding-left: 20px;
  margin-top: 20px;
  position: relative;

  &-cell {
    margin-left: 30px;
    padding: 8px;
    position: relative;

    &__mark {
      position: absolute;
      left: -10px;
      top: 24px;
      color: white;
      border-radius: $xr-border-radius-base;
      padding: 4px;
      font-size: 12px;
      z-index: 1;
    }
  }

  &-cell.activity-cell {
    .comment-cell__mark {
      top: 6px;
    }
  }

  &-section {
    &__title {
      padding: 8px 0;
      .section-title {
        background-color: #f1f5fd;
        color: white;
        font-size: 12px;
        padding: 4px 10px;
        height: 20px;
        border-radius: 15px;
        &__time {
          color: #666;
          font-weight: 600;
          &::before {
            content: ' ';
            position: relative;
            width: 6px;
            height: 6px;
            border-radius: 3px;
            background-color: #666;
            margin-right: 5px;
            bottom: 2px;
            display: inline-block;
          }
        }
      }
    }
  }
}
.activity-cell {
  font-size: 12px;
  padding: 5px 13px;
  &__label {
    color: #666666;
  }
  &__content {
    color: $xr-color-primary;
  }
}
</style>
