<template>
  <div class="comment-list">
    <div
      v-for="(item, index) in list"
      :key="index"
      class="comment-list-item">
      <div class="parent-reply reply">
        <div class="user-info">
          <xr-avatar
            :name="item.user.realname"
            :id="item.user.userId"
            :size="34"
            :src="item.user.img"
            :disabled="false"
            class="user-img" />
          <div class="user-info__bd">
            <div class="user">{{ item.user.realname }}</div>
            <div class="time">
              {{ item.createTime }}
            </div>
          </div>
          <div class="control">
            <span
              class="reply-btn"
              @click="handleToReply(index)">
              <i :class="'icon-reply' | iconPre" />
              <span>回复</span>
            </span>
            <span
              class="delete-btn"
              @click="handleToDelete(item, index)">
              <i :class="'s-delete' | iconPre"/>
              <span>删除</span>
            </span>
          </div>
        </div>
        <div class="content" v-html="emoji(item.content)" />
        <reply-comment
          v-if="replyIndex === String(index)"
          @reply="handleReply"
          @close="replyIndex = null" />
      </div>
      <template v-if="item.childCommentList && item.childCommentList.length > 0">
        <div
          v-for="(child, childIndex) in item.childCommentList"
          :key="childIndex"
          class="child-reply-list">
          <div class="child-reply reply">
            <div class="user-info">
              <xr-avatar
                :name="child.user.realname"
                :id="child.user.userId"
                :size="34"
                :src="child.user.img"
                :disabled="false"
                class="user-img" />
              <div class="user-info__bd">
                <div class="user">
                  {{ child.user.realname }}
                </div>
                <div class="time">
                  {{ child.createTime }}
                </div>
              </div>
              <div class="control">
                <span class="reply-btn" @click="handleToReply(index, childIndex)">
                  <i :class="'icon-reply' | iconPre" />
                  <span>回复</span>
                </span>
                <span class="delete-btn" @click="handleToDelete(child, index, childIndex)">
                  <i :class="'s-delete' | iconPre" />
                  <span>删除</span>
                </span>
              </div>
            </div>
            <div class="child-content">
              <span>
                回复 @{{ child.replyUser.realname }}：
              </span>
              <span class="content" v-html="emoji(child.content)" />
            </div>
            <reply-comment
              v-loading="commentLoading"
              v-if="replyIndex === `${index}-${childIndex}`"
              @reply="handleReply"
              @close="replyIndex = null" />
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
/**
 * 评论列表
 */
import ReplyComment from '@/components/ReplyComment'
import {
  setCommentAPI,
  deleteCommentAPI
} from '@/api/oa/common'
import { mapGetters } from 'vuex'
import xss from 'xss'

export default {
  name: 'CommentList',
  components: {
    ReplyComment
  },
  props: {
    list: {
      type: Array,
      required: true
    },
    id: {
      type: [String, Number],
      required: true
    },
    // 1是任务 2是日志
    type: [String, Number]
  },
  data() {
    return {
      replyIndex: null,
      commentLoading: false
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  methods: {
    /**
     * 显示回复框
     * @param index
     * @param childIndex
     */
    handleToReply(index, childIndex = null) {
      const str = `${index}${childIndex !== null ? ('-' + childIndex) : ''}`
      this.$emit('close-other-reply')
      if (str === this.replyIndex) return
      this.replyIndex = str
    },
    closeReply() {
      this.replyIndex = null
    },
    /**
     * 子评论排序
     */
    // sortChildComment(childList) {
    //   let arr = [].concat(childList || [])
    //   arr = arr.sort((a, b) => {
    //     return new Date(b.createTime) - new Date(a.createTime)
    //   }) || []
    //   return arr
    // },
    /**
     * 回复
     * @param data
     */
    handleReply(data) {
      const params = {
        type: this.type,
        content: xss(data),
        typeId: this.id
      }
      let c_comment = null
      const arr = this.replyIndex.split('-')
      const f_comment = this.list[arr[0]]
      if (arr[1]) {
        c_comment = f_comment.childCommentList[arr[1]]
      }
      if (f_comment) {
        params.mainId = f_comment.commentId
        params.pid = f_comment.userId
      }
      if (c_comment) {
        params.pid = c_comment.userId
      }
      this.commentLoading = true
      setCommentAPI(params).then(res => {
        res.data.user = {
          userId: this.userInfo.id,
          realname: this.userInfo.realname,
          img: this.userInfo.img
        }
        if (c_comment) {
          res.data.replyUser = c_comment.user
        } else {
          res.data.replyUser = f_comment.user
        }
        this.list[arr[0]].childCommentList.push(res.data)
        this.replyIndex = null
        this.commentLoading = false
      }).catch(() => {
        this.commentLoading = false
      })
    },
    /**
     * 删除回复
     */
    handleToDelete(data, index, childIndex = -1) {
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteCommentAPI({
            commentId: data.commentId
          }).then(res => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })

            if (childIndex >= 0) {
              this.list[index].childCommentList.splice(childIndex, 1)
            } else {
              this.$emit('delete', index)
            }
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    }
  }
}
</script>

<style scoped lang="scss">
  .comment-list {
    .comment-list-item {
      .user-img {
        margin-right: 15px;
        border-radius: 50% !important;
      }
      .reply {
        font-size: 13px;
        border-bottom: 1px solid #e6e6e6;
        padding: 10px;
        margin-bottom: 5px;

        .user-info {
          line-height: 1;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          &__bd {
            flex: 1;
            .user {
              font-size: 14px;
            }
            .time {
              font-size: 12px;
              color: #666;
              margin-top: 3px;
            }
          }
          .control {
            // margin-left: 10px;
            visibility: hidden;
            i {
              font-size: 14px;
            }

            .delete-btn,
            .reply-btn {
              cursor: pointer;
              color: #666;
            }

            .delete-btn {
              margin-left: 10px;
            }

            .reply-btn:hover {
              color: $xr-color-primary;
            }

            .delete-btn:hover {
              color: #F56C6C;
            }
          }
        }

        .content {
          line-height: 1.5;
          white-space: pre-wrap;
          word-wrap: break-word;
          margin-left: 50px;
          margin-top: 5px;
        }

        .child-content {
          margin-left: 50px;
          .content {
            margin-left: 0;
          }
        }

        &:hover {
          background-color: #f9f9f9;
          .user-info .control {
            visibility: unset;
          }
        }
      }
      .reply-comment {
        margin-top: 10px;
      }
    }

    .child-reply-list {
      padding-left: 50px;
    }
  }
</style>
