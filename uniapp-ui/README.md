## LegoAdmin移动端
基于Uniapp框架开发，兼容多端发布一站式解决方案
### 技术栈
| 技术 | 说明 | 版本 |
| --- | --- | --- |
| [uView](https://uviewui.com) | uView基础组件框架 | 2.0.38 |
| [qiun-data-charts](https://www.ucharts.cn/) | uChart图表展示框架 | 2.5.0 |
| [Vue](https://vuejs.org) | 框架 | 2.x |
### 打包工具版本
- `HBuilder`
### 目录结构
``` lua
├── api -- 后端接口
├── components -- 公共组件
├── core -- 公共核心库
├── directives -- 公共指令
├── iconfont -- 图标
├── pages -- 源码目录
│   ├── index -- 首页
│   ├── login -- 登录模块
│   ├── notice -- 消息模块
│   └── user -- 个人信息模块
├── static -- 图片资源
├── store -- vuex状态管理
├── uni_modules -- uniapp插件
├── util -- 工具
├── app.scss -- 公共样式文件
├── App.vue -- vue入口
├── config.js -- 环境配置
├── main.js -- vue配置文件
├── mainfect.json -- uniapp配置文件
├── pages.json -- 页面注册配置
└── uni.scss -- 公共样式变量
```