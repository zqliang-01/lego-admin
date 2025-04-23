<template>
  <div class="login-wrapper">
    <div class="top-nav">
      <img
        :src="logoWhite"
        alt="">
    </div>
    <div class="container">
      <div class="left">
        <h1 class="title">
          新一代低代码框架LegoAdmin
        </h1>
        <img
          :src="mainPic"
          alt=""
          class="main-pic">
      </div>
      <div class="right">
        <div class="login-main-content">
          <div class="logo-box">
            欢迎登录
          </div>

          <LoginByPwd/>

          <div class="use-tip">
            <div>
              <span class="text">建议使用</span>
              <img :src="chromeIcon" alt="" class="icon">
              <span class="text">Chrome、</span>
              <img :src="firefoxIcon" alt="" class="icon">
              <span class="text">火狐、</span>
              <img :src="safariIcon" alt="" class="icon">
              <span class="text">Safari浏览器</span>
            </div>
            <div>
              <span class="text">或</span>
              <img :src="browser360Icon" alt="" class="icon">
              <span class="text">360、</span>
              <img :src="sougouIcon" alt="" class="icon">
              <span class="text">搜狗、</span>
              <img :src="qqIcon" alt="" class="icon">
              <span class="text">QQ等浏览器</span>
              <img :src="fastIcon" alt="" class="icon">
              <span class="text">极速模式</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import browser360Icon from '@/assets/login/360.png'
import chromeIcon from '@/assets/login/chrome.png'
import fastIcon from '@/assets/login/fast.png'
import firefoxIcon from '@/assets/login/firefox.png'
import mainPic from '@/assets/login/main_pic.png'
import qqIcon from '@/assets/login/qq.png'
import safariIcon from '@/assets/login/safari.png'
import sougouIcon from '@/assets/login/sougou.png'
import logoWhite from '@/assets/logo_white.png'
import LoginByPwd from './component/LoginByPwd'

</script>

<style scoped lang="scss">
@import 'index';

.login-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('~@/assets/login/bg.png') center no-repeat;
  background-size: cover;
  display: flex;
  flex-direction: column;
  overflow: auto;

  .top-nav {
    width: 100%;
    padding: 23px 2.8% 0;
    display: flex;
    img {
      width: 150px;
      height: 40px;
    }
  }

  .container {
    width: 100%;
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: auto;
    margin-top: -42px;
    .left {
      .title {
        font-size: 38px;
        font-weight: normal;
        color: white;
        letter-spacing: 3px;
        margin-top: 10px;
        margin-left: 34px;
        margin-bottom: 20px;
        @media screen and (max-width: 1550px) {
          font-size: 32px;
          margin-top: 0;
          margin-bottom: 15px;
        }
      }
      .main-pic {
        width: 560px;
        margin-bottom: 20px;
        margin-top: 50px;
        margin-left: -20px;
        @media screen and (max-width: 1550px) {
          width: 500px;
        }
      }
    }
    .right {
      width: 480px;
      margin-left: 80px;
      padding-top: 10px;
      .login-main-content {
        position: relative;
        width: 100%;
        height: 500px;
        background-color: white;
        border-radius: 6px;
        overflow: hidden;
        .logo-box {
          position: relative;
          width: 100%;
          font-weight: bold;
          font-size: 20px;
          color: #4c4c4c;
          letter-spacing: 2px;
          padding: 0 40px;
          margin: 15px 0 20px;
          &:before {
            position: absolute;
            top: 50%;
            left: 24px;
            transform: translateY(-50%);
            content: '';
            width: 4px;
            height: 16px;
            background-color: #3e6bea;
            border-radius: 2px;
            display: block;
          }
        }
        .use-tip {
          position: absolute;
          left: 0;
          bottom: 18px;
          width: 100%;
          text-align: center;
          color: #999;
          font-size: 12px;
          div + div {
            margin-top: 3px;
          }
          .text {
            vertical-align: middle;
          }
          .icon {
            width: 15px;
            display: inline-block;
            vertical-align: middle;
          }
        }
      }
    }

    @media screen and (max-width: 1550px) {
      margin-top: -20px;
    }
  }
}
</style>
