<template>
  <div class="wrapper">
    <div class="welcome-container">
      <img class="pic" src="@/assets/img/empty.png" alt="">
      <div class="title">欢迎使用-{{ appName }}系统</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Welcome',
  props: {
    appName: {
      type: String,
      default: ''
    }
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
.wrapper {
  position: relative;
}

.welcome-container {
  position: relative;
  top: 50%;

  .pic {
    width: 600px;
    display: block;
    margin: 0 auto;
  }

  .title {
    width: 100%;
    text-align: center;
    font-size: 24px;
    color: #909399;
    line-height: 38px;
    margin-top: 77px;
  }

  .desc {
    width: 100%;
    font-size: 18px;
    color: #777;
    text-align: center;
    line-height: 38px;
    margin-top: 8px;
  }

  .btn {
    width: 125px;
    height: 44px;
    text-align: center;
    font-size: 14px;
    color: #fefefe;
    line-height: 44px;
    background: #2362FB;
    border-radius: 6px;
    margin: 50px auto 0;
    cursor: pointer;
  }
}

@media screen and (max-width: 1500px) {
  .welcome-container {
    .pic {
      width: 500px;
    }

    .title {
      margin-top: 50px;
    }

    .btn {
      margin-top: 40px;
    }
  }
}</style>
