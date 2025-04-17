<script>
  export default {

    /**
     * 全局变量
     */
    globalData: {
    },

    /**
     * 初始化完成时触发
     */
    onLaunch(options) {
      // 小程序主动更新
      this.updateManager()
      if (options.query.spm) {
          uni.setStorageSync('shareId', options.query.spm);
      }
    },

    methods: {

      /**
       * 小程序主动更新
       */
      updateManager() {
        const updateManager = uni.getUpdateManager();
        updateManager.onCheckForUpdate(res => {
          // 请求完新版本信息的回调
          // console.log(res.hasUpdate)
        })
        updateManager.onUpdateReady(() => {
          uni.showModal({
            title: '更新提示',
            content: '新版本已经准备好，即将重启应用',
            showCancel: false,
            success(res) {
              if (res.confirm) {
                // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                updateManager.applyUpdate()
              }
            }
          })
        })
        updateManager.onUpdateFailed(() => {
          // 新的版本下载失败
          uni.showModal({
            title: '更新提示',
            content: '新版本下载失败',
            showCancel: false
          })
        })
      }
    }

  }
</script>

<style lang="scss">
  /* 引入uView库样式 */
  @import "@/uni_modules/uview-ui/index.scss";
	@import '@/static/fonts/iconfont.css';
	.com-loadmore {
		padding: 15rpx;
		.u-more-text {
			font-weight: 500;
			font-size: 26rpx !important;
			color: rgb(185, 185, 185) !important;
		}
	}
	.lego-card {
		padding: 20rpx 40rpx;
		position: relative;
		margin: 20rpx;
		background-color: $uni-bg-color;
		border-radius: $uni-border-radius-infos;
		box-shadow: 0rpx 2rpx 10rpx 2rpx rgba(0,122,255,0.08);
	}
</style>

<style>
  /* 项目基础样式 */
  @import "./app.scss";
</style>
