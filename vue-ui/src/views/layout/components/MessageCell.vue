<template>
  <flexbox
    :class="{ 'is-unread' :data.isRead == 0 }"
    class="message-cell"
    align="stretch">
    <div class="message-cell__hd">
      <i
        :class="typeObj.icon | iconPre"
      />
    </div>

    <div class="message-cell__bd">
      <span>{{ leftContent }}</span>
      <span :class="[{ 'is-invalid': isInvalid }, 'click-content']" @click="enterDetail">{{ centerCotent }}</span>
      <span>{{ rightContent }}</span>
    </div>

    <div class="message-cell__ft">
      <div>{{ data.createTime | formatTime }} </div>
      <div class="handle">
        <span class="check" @click="messageReadClick">标记已读</span>
        <span class="delete" @click="messageDeleteClick">删除</span>
      </div>
    </div>
  </flexbox>
</template>

<script>
// import moment from 'moment'
export default {
  // 消息cell
  name: 'MessageCell',
  components: {

  },
  props: {
    data: Object,
    dataIndex: Number
  },
  data() {
    return {}
  },
  computed: {
    typeObj() {
      const typesObj = {
        leads: {
          icon: 'leads',
          color: '#6995FF',
          type: 'leads'
        },
        customer: {
          icon: 'customer',
          color: '#6995FF',
          type: 'customer'
        },
        contacts: {
          icon: 'contacts',
          color: '#6995FF',
          type: 'contacts'
        },
        business: {
          icon: 'business',
          color: '#6995FF',
          type: 'business'
        },
        contract: {
          icon: 'contract',
          color: '#6995FF',
          type: 'contract'
        },
        receivables: {
          icon: 'receivables',
          color: '#6995FF',
          type: 'receivables'
        },
        product: {
          icon: 'product',
          color: '#6995FF',
          type: 'product'
        },
        log: {
          icon: 'log',
          color: '#6995FF',
          type: 'log'
        },
        examine: {
          icon: 'approve',
          color: '#6995FF',
          type: 'examine'
        },
        task: {
          icon: 'o-task',
          color: '#6995FF',
          type: 'task'
        },
        announcement: {
          icon: 'announcement',
          color: '#6995FF',
          type: 'announcement'
        },
        schedule: {
          icon: 'schedule',
          color: '#6995FF',
          type: 'schedule'
        },
        invoice: {
          icon: 'invoice',
          color: '#6995FF',
          type: 'invoice'
        }
      }

      // 1 任务 2 日志 3 oa审批 4公告 5 日程 6 客户管理
      let key = ''
      if (this.data.label && this.data.label <= 5) {
        key = ['task', 'log', 'examine', 'announcement', 'schedule'][this.data.label - 1]
      } else {
        if ([1, 2, 3].includes(this.data.type)) {
          key = 'task'
        } else if ([4, 5, 34].includes(this.data.type)) {
          key = 'log'
        } else if ([6, 7, 25].includes(this.data.type)) {
          key = 'examine'
        } else if ([8].includes(this.data.type)) {
          key = 'announcement'
        } else if ([10, 11, 24, 26, 30, 33].includes(this.data.type)) {
          key = 'contract'
        } else if ([12, 13, 27].includes(this.data.type)) {
          key = 'receivables'
        } else if ([14, 15, 23, 29, 32, 42, 43].includes(this.data.type)) {
          key = 'customer'
        } else if ([16, 17].includes(this.data.type)) {
          key = 'contacts'
        } else if ([18, 19].includes(this.data.type)) {
          key = 'leads'
        } else if ([20, 21].includes(this.data.type)) {
          key = 'product'
        } else if ([22, 28, 31].includes(this.data.type)) {
          key = 'business'
        } else if ([35, 36, 37].includes(this.data.type)) {
          key = 'invoice'
        }
      }

      return typesObj[key]
    },

    leftContent() {
      return {
        1: `${this.data.realname}将`,
        2: `${this.data.realname}邀请您参与`,
        3: `${this.data.realname}将`,
        4: `${this.data.realname}回复了您的`,
        5: `${this.data.realname}将日志`,
        6: `${this.data.realname}拒绝您的`,
        7: `${this.data.realname}已经审核通过您的`,
        8: `您有一个新公告`,
        9: `${this.data.realname}邀请您参与`,
        10: `${this.data.realname}拒绝您的`,
        11: `${this.data.realname}已经审核通过您的`,
        12: `${this.data.realname}拒绝您的`,
        13: `${this.data.realname}已经审核通过您的`,
        14: `${this.data.realname}导入客户数据${this.data.title}条，${this.getImportContent(this.data)}`,
        15: `${this.data.realname}取消导入客户数据，已导入${this.data.title}条，${this.getImportContent(this.data)}`,
        16: `${this.data.realname}导入联系人数据${this.data.title}条，${this.getImportContent(this.data)}`,
        17: `${this.data.realname}取消导入联系人数据，已导入${this.data.title}条，${this.getImportContent(this.data)}`,
        18: `${this.data.realname}导入线索数据${this.data.title}条，${this.getImportContent(this.data)}`,
        19: `${this.data.realname}取消导入线索数据，已导入${this.data.title}条，${this.getImportContent(this.data)}`,
        20: `${this.data.realname}导入产品数据${this.data.title}条，${this.getImportContent(this.data)}`,
        21: `${this.data.realname}取消导入产品数据，已导入${this.data.title}条，${this.getImportContent(this.data)}`,
        22: `${this.data.realname}将您添加为商机`,
        23: `${this.data.realname}将您添加为客户`,
        24: `${this.data.realname}将您添加为合同`,
        25: `${this.data.realname}提交了`,
        26: `${this.data.realname}提交了`,
        27: `${this.data.realname}提交了`,
        28: `${this.data.realname}退出了您商机`,
        29: `${this.data.realname}退出了您客户`,
        30: `${this.data.realname}退出了您合同`,
        31: `${this.data.realname}将您移出了商机`,
        32: `${this.data.realname}将您移出了客户`,
        33: `${this.data.realname}将您移出了合同`,
        34: `${this.data.realname}回复了您评论的日志`,
        35: `${this.data.realname}拒绝您的`,
        36: `${this.data.realname}已经审核通过您的`,
        37: `${this.data.realname}提交了`,
        42: `${this.data.realname}同步客户数据${this.data.title}条，${this.getSyncContent(this.data)}`,
        43: `${this.data.realname}同步项目数据${this.data.title}条，${this.getSyncContent(this.data)}`,
        77: `${this.data.realname}点赞了您的`,
        120: `${this.data.realname}将您添加为联系人`,
        121: `${this.data.realname}将您添加为回款`,
        122: `${this.data.realname}退出了您联系人`,
        123: `${this.data.realname}退出了您回款`,
        124: `${this.data.realname}将您移出了联系人`,
        125: `${this.data.realname}将您移出了回款`
      }[this.data.type]
    },

    centerCotent() {
      // 导入提示与其他不一样
      if (this.isImportType) {
        // title 是总数 content 是错误数据 valid 错误文件是否有效 1 有效 0 失效
        const list = this.data.content.split(',') || []
        const errSize = Number(list[0] || 0)
        if (errSize > 0) {
          return this.data.valid === 0 ? '已失效' : '点击下载错误数据'
        }
        return ''
      }
      if (this.isSyncType) {
        if (this.data == null || this.data.content == null) {
          return ''
        }
        // title 是总数 content 是错误数据 valid 错误文件是否有效 1 有效 0 失效
        const list = this.data.content.split(',') || []
        const errSize = Number(list[0] || 0)
        if (errSize > 0) {
          return this.data.valid === 0 ? '已失效' : '点击下载错误数据'
        }
        return ''
      } else {
        return `《${this.data.title}》`
      }
    },

    isInvalid() {
      if (this.isImportType) {
        // title 是总数 content 是错误数据 valid 错误文件是否有效 1 有效 0 失效
        return this.data.valid == 0
      } else {
        return false
      }
    },

    /**
     * 是导入type
     */
    isImportType() {
      return this.data.type >= 14 && this.data.type <= 21
    },

    isSyncType() {
      return this.data.type == 42 || this.data.type == 43
    },

    rightContent() {
      return {
        1: `任务分配给您，请及时查看`,
        2: `任务，请及时查看`,
        3: `任务标记结束`,
        4: `日志：“${this.data.content}”，请及时查看`,
        5: `发送给您，请及时查看`,
        6: `，拒绝理由：“${this.data.content}”，请及时处理`,
        7: `，请及时查看`,
        8: `，请及时查看`,
        9: `的日程，${this.getStartTime(this.data.content)}请及时查看`,
        10: `合同审批，拒绝理由：“${this.data.content}”，请及时处理`,
        11: `合同，请及时查看`,
        12: `回款审批，拒绝理由：“${this.data.content}”，请及时处理`,
        13: `回款，请及时查看`,
        14: ``,
        15: ``,
        16: ``,
        17: ``,
        18: ``,
        19: ``,
        20: ``,
        21: ``,
        22: `的成员`,
        23: `的成员`,
        24: `的成员`,
        25: `，请及时处理`,
        26: `合同审批，请及时处理`,
        27: `回款审批，请及时处理`,
        28: `的团队`,
        29: `的团队`,
        30: `的团队`,
        31: `的团队`,
        32: `的团队`,
        33: `的团队`,
        34: `：“${this.data.content}”，请及时查看`,
        35: `发票审批，拒绝理由：“${this.data.content}”，请及时处理`,
        36: `发票审批，请及时查看`,
        37: `发票审批，请及时处理`,
        77: `日志，请及时查看`,
        120: `的成员`,
        121: `的成员`,
        122: `的团队`,
        123: `的团队`,
        124: `的团队`,
        125: `的团队`
      }[this.data.type]
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

      // 是导入提醒
      if (this.isImportType) {
        this.$emit('download', this.data.messageId, this.dataIndex)
      } else {
        // 未读触发读
        if (this.data.isRead == 0) {
          this.$emit('read', this.data.messageId, this.dataIndex)
        }
        this.$emit('detail', this.typeObj.type, this.data.typeId, this.dataIndex, this.data)
      }
    },

    /**
     * 标记已读
     */
    messageReadClick() {
      this.$emit('read', this.data.messageId, this.dataIndex)
    },

    /**
     * 消息删除
     */
    messageDeleteClick() {
      this.$emit('delete', this.data.messageId, this.dataIndex)
    },

    /**
     * 日程提醒，多长时间后开始
     */
    getStartTime(content) {
      if (this.data.type != 9) {
        return
      }
      const timeObj = content ? JSON.parse(content) : { type: 0, value: '' }

      let dataValue = ''
      if (!timeObj.type) {
        dataValue = ''
      } else {
        dataValue = '将于' + timeObj.value + ['', '分钟', '小时', '天'][timeObj.type] + '后开始, '
      }
      return dataValue
    },

    getImportContent({ title, content }) {
      const countList = [14, 15, 16, 17, 18, 19, 20, 21, 50]
      if (!countList.includes(this.data.type)) {
        return
      }
      const list = content.split(',') || []
      const totalSize = Number(title || '0')
      const updateSize = Number(list[1] || '0')
      const errSize = Number(list[0] || '0')
      return `覆盖${updateSize}条，导入成功${totalSize - errSize}条，导入失败${errSize}条。`
    },

    getSyncContent({ title, content }) {
      const countList = [42, 43]
      if (!countList.includes(this.data.type)) {
        return
      }
      if (content == null) {
        return '同步处理中'
      }
      const list = content.split(',') || []
      const totalSize = Number(title || '0')
      const updateSize = Number(list[1] || '0')
      const errSize = Number(list[0] || '0')
      return `覆盖${updateSize}条，同步成功${totalSize - errSize}条，同步失败${errSize}条。`
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
  color: $xr-color-primary;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
}

.is-invalid {
  color: #999;
  pointer-events: none;
  cursor: initial;
}
</style>
