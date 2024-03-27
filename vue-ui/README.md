## LegoAdmin前端
基于element-ui框架开发，多模块动态路由VUE前端框架
### 技术栈
| 技术 | 说明 | 版本 |
| --- | --- | --- |
| [Vue](https://vuejs.org) | 框架 | 2.6.11 |
| [Vuex](https://vuex.vuejs.org) | 全局状态管理框架 | 3.6.2 |
| [Vue-router](https://router.vuejs.org) | 路由框架 | 3.4.9 |
| [element-ui](https://element.eleme.cn) | ElementUI框架 | 2.15.14 |
| [bpmn-process-designer](https://gitee.com/MiyueSC/bpmn-process-designer) | Bpmn流程图设计框架 | 2.0.0 |
| [Axios](https://github.com/axios/axios) | HTTP请求框架 | 0.18.0 |
| [sass](https://sass-lang.com) | SCSS样式打包框架 | 1.32.7 |
| [sass-loader](https://sass-lang.com) | 样式打包框架 | 12.0.0 |
### 打包工具版本
- `node` >= 10.0.0
- `npm` >= 6.0.0
### 打包命令
- 如果为开发阶段按需修改.env.development配置文件VUE_APP_PROXY_TARGET后端服务地址
- `npm install` 安装依赖
- `npm run dev` 开发环境运行
- `npm run build` 版本打包，打包后会生成dist目录
### 目录结构
``` lua
├── build -- webpack 配置文件
├── .env.development -- 开发环境配置
├── .env.production -- 生产环境配置
├── src -- 源码目录
│   ├── api -- api请求接口
│   ├── assets -- 静态图片资源文件
│   ├── components -- 通用组件
│   ├── directives -- 通用指令
│   ├── filters -- 通用过滤器
│   ├── mixins -- 通用混入
│   ├── router -- vue-router路由配置
│   ├── store -- vuex状态管理
│   ├── styles -- 全局css样式
│   ├── utils -- 工具类
│   └── views -- 前端页面
│       ├── admin -- 管理后台模块
│       ├── home -- 首页
│       ├── layout -- 模块主页框架
│       ├── login -- 登录
│       ├── user -- 用户中心
│       ├── ...
│       └── Xxx -- 自定义模块
└── public -- 首页静态资源
```