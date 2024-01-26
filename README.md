# lego-admin

### 介绍

一款属于后端开发人员的前后端分离的新一代低代码框架LegoAdmin
演示地址：(https://admin.zqliang.cn)  
帐号：test 密码：test123

### 软件架构

前端基于vue element-ui框架分模块设计，后端使用SpringBoot2框架，持久层同时支持mybatis-plus3（业务查询）、hibernate5（事务业务受理）。
支持微服务模块化开发部署，数据源可彻底隔离，基于ShardingSphere5定制开发多模型分库分片表数据源。
一键代码生成，自定义表单设计等，属于后端开发人员的新一代低代码开发平台LegoAdmin

### 安装教程

#### 配置依赖环境

- `mysql5.7`
- `JDK1.8`
- `maven3`
- `node` >= 10.0.0 建议 v19.9.0
- `npm` >= 6.0.0 建议 9.6.3

#### 打包运行

* 创建mysql数据库lego-admin
* 拷贝lego-admin/src/main/resources/application-template.properties到同目录并命名为application.properties
* 修改application.properties配置中的数据库链接和账号密码
* 根目录下双击执行dbInit.bat，初始化数据库
* 根目录下双击执行build.bat，会自动打包前后端
* 根目录下双击执行startup.bat，会自动运行

#### 访问路径

* 项目默认端口8443
* 管理台路径http://localhost:8443
* 默认账号密码admin/hello@1234

### 系统展示

* 登陆
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/2%E7%99%BB%E5%BD%95.png)
* 统计大屏
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/1%E9%A6%96%E9%A1%B5.png)
* 代码生成
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/7%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90.png)
* 表单设计
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/8%E8%A1%A8%E5%8D%95%E8%AE%BE%E8%AE%A1.png)
* 分库分表
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/9%E5%88%86%E5%BA%93%E5%88%86%E8%A1%A8.png)
* 企业定制
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/3%E4%BC%81%E4%B8%9A%E5%AE%9A%E5%88%B6.png)
* 应用管理
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/4%E5%BA%94%E7%94%A8%E7%AE%A1%E7%90%86.png)
* 组织架构
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/5%E7%BB%84%E7%BB%87%E6%9E%B6%E6%9E%84.png)
* 权限管理
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/6%E6%9D%83%E9%99%90%E7%AE%A1%E7%90%86.png)

