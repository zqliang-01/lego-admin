<template>
  <div
    v-loading="loading"
    class="brief-box">
    <div class="brief-title">
      <span :class="'briefing' | iconPre" class="icon" />
      <span class="text">数据简报</span>
    </div>
    <div v-if="briefList.length > 0" class="brief">
      <flexbox
        v-for="(item, index) in briefList"
        :key="index"
        :span="2"
        class="brief-item"
        @click.native="reportClick(item)">
        <flexbox class="brief-item__body">
          <div
            :style="{backgroundColor: item.color}"
            class="icon-box">
            <span :class="item.icon | iconPre" class="icon lego" />
          </div>
          <div class="info">
            <div class="title">
              {{ item.label }}
            </div>
            <div class="number">
              {{ item.num }}
            </div>
          </div>
        </flexbox>
        <div v-if="rateText !== ''" class="brief-item__others">
          <div class="text">
            {{ rateText }}
          </div>
          <div :class="item.status" class="rate text-one-line">
            <span class="rate__num">{{ item.rate }}%</span>
            <span
              :class="`el-icon-${item.status}`"
              class="rate__icon" />
          </div>
        </div>
      </flexbox>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TopReport',
  props: {
    rateText: {
      type: String,
      default: ''
    },
    briefList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      loading: false
    }
  },
  watch: {
    rateText() {
      console.log(this.rateText)
    }
  },
  mounted() {
  },
  methods: {
    reportClick(item) {
    }
  }
}
</script>
<style scoped lang="scss">
.brief-box {
  width: 100%;
  margin-top: 15px;
  background-color: white;
  border: 1px solid #e6e6e6;
  border-radius: $xr-border-radius-base;
  .brief-title {
    padding: 15px 20px 0;
    .icon {
      color: #50CF9E;
      font-size: 18px;
    }
    .text {
      font-size: 16px;
      font-weight: bold;
      margin-left: 5px;
    }
  }
  .brief {
    width: 100%;
    padding: 10px 10px 14px 10px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: wrap;
    .brief-item {
      cursor: pointer;
      width: calc(25% - 20px);
      height: 88px;
      box-shadow: 0 0 16px 0 rgba(0,0,0,0.08);
      border-radius: $xr-border-radius-base;
      margin: 10px;
      .brief-item__body {
        flex: 1;
        overflow: hidden;
        .icon-box {
          width: 36px;
          height: 36px;
          line-height: 36px;
          text-align: center;
          border-radius: 50%;
          margin-right: 10px;
          margin-left: 15px;
          flex-shrink: 0;

          .icon {
            color: white;
            font-size: 19px;
          }
        }
        .info {
          overflow: hidden;
          .title {
            font-size: 13px;
          }
          .number {
            font-size: 23px;
            font-weight: bold;
            line-height: 1;
            margin-top: 8px;
            margin-right: 5px;
            // white-space: nowrap;
            // text-overflow: ellipsis;
            overflow: hidden;
          }
        }
      }
      .brief-item__others {
        position: relative;
        width: 100px;
        text-align: center;
        padding: 0 3px;
        overflow: hidden;

        &:before {
          position: absolute;
          top: 7.5%;
          left: 0;
          content: '';
          width: 1px;
          height: 85%;
          background-color: #e6e6e6;
          display: block;
        }
        .text {
          font-size: 12px;
          margin-left: -5px;
        }
        .rate {
          font-size: 14px;
          color: #2BBF24;
          margin-top: 8px;

          .rate__icon {
            font-size: 12px;
          }
          &.bottom {
            color: #2BBF24;
          }
          &.top {
            color: #F24B4B;
          }
        }
      }
      &:hover {
        box-shadow: 0 0 16px 0 rgba(0,0,0,0.2);
      }
    }
  }
}

</style>
