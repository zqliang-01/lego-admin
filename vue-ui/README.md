## LegoAdmin前端
基于element-ui框架开发
### 技术栈
| 技术 | 说明 | 版本 |
| --- | --- | --- |
| [Vue](https://vuejs.org) | 框架 | 2.5.17 |
| [Vue-router](https://router.vuejs.org) | 路由框架 | 3.0.1 |
| [Vuex](https://vuex.vuejs.org) | 全局状态管理框架 | 3.0.1 |
| [Element](https://element.eleme.io) | UI框架 | 2.12.0 |
| [Axios](https://github.com/axios/axios) | HTTP框架 | 0.18.0 |
| [element-ui](https://element.eleme.cn) | ElementUI框架 | 2.15.14 |
| [node-sass](https://sass-lang.com) | 样式打包框架，依赖Python | 4.7.2 |
| [sass-loader](https://sass-lang.com) | 样式打包框架 | 7.0.3 |
### 打包工具版本
- `node` >= 6.0.0 建议 v14.15.3
- `npm` >= 3.0.0 建议 6.14.9
- `cnpm` >= 3.0.0 建议 6.1.1
### 打包命令
- 拷贝config目录下index-template.js在同目录新建index.js文件，如果为开发阶段则按需修改后端服务地址
- `npm install` 安装依赖
- `npm run dev` 开发环境运行
- `cnpm run build` 版本包，打包后会生成dist目录
### 目录结构
``` lua
├── build -- webpack 配置文件
├── config -- 项目配置文件
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
│       ├── ...
│       └── Xxx -- 自定义模块
└── static -- 静态资源
```