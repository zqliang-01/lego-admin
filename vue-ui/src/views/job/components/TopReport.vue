<template>
  <div
    v-loading="loading"
    class="brief-box">
    <div class="brief-title">
      <span :class="'briefing' | iconPre" class="icon" />
      <span class="text">运行简报</span>
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
            <div>
              <span class="number">
                {{ item.num }}
              </span>
              <el-divider direction="vertical"/>
              <span class="desc">
                {{ item.description }}
              </span>
            </div>
          </div>
        </flexbox>
      </flexbox>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TopReport',
  props: {
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
  mounted() {
  },
  methods: {
    reportClick(item) {
      this.$emit('onClick', item)
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
      width: calc(33% - 20px);
      height: 88px;
      box-shadow: 0 0 16px 0 rgba(0,0,0,0.08);
      border-radius: $xr-border-radius-base;
      margin: 10px;
      .brief-item__body {
        flex: 1;
        overflow: hidden;
        .icon-box {
          width: 45px;
          height: 45px;
          line-height: 45px;
          text-align: center;
          border-radius: 50%;
          margin-right: 10px;
          margin-left: 15px;
          flex-shrink: 0;
          .icon {
            color: white;
            font-size: 25px;
          }
        }
        .info {
          overflow: hidden;
          .title {
            font-size: 13px;
            margin-bottom: 8px;
          }
          .number {
            font-size: 23px;
            font-weight: bold;
            line-height: 1;
            margin-top: 8px;
            overflow: hidden;
          }
          .desc {
            font-size: 13px;
            margin-top: 5px;
            white-space: nowrap;
            text-overflow: ellipsis;
            color: $xr-color-text-secondary;
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
