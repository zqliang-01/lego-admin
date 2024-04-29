# LegoAdmin

<a target="_blank" href="http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=wBCgfng_HilFFVLXWimzrd63aPh8We-_&authKey=6lKMaBUa5MbsLcB44lOCT2aO52h8jw8lLZX8qYdTIxyfVMawTdm4p2bwfGI8QtOH&noverify=0&group_code=962119957">技术交流群</a> 962119957

### 介绍

* 一款彻底开源的前后端分离的低代码开发框架
* 演示地址：https://admin.zqliang.cn
* 帐号：test 密码：test123

### 技术栈
| 技术                                                          | 说明        | 版本     |
|-------------------------------------------------------------|-----------|--------|
| [SpringBoot](https://spring.io/projects/spring-boot)        | Java框架    | 2.7.11 |
| [mybatis-plus](https://mybatis.plus)                        | 数据持久化框架   | 3.4.2  |
| [flowable](https://www.flowable.com)                        | 流程引擎      | 6.8.0  |
| [ShardingSphere](https://shardingsphere.apache.org)         | 分库分表框架    | 5.4.1  |
| [sa-token](https://sa-token.cc)                             | 权限认证框架    | 2.12.0 |
| [openfeign](https://cloud.spring.io/spring-cloud-openfeign) | 远程调用框架    | 3.1.7  |
| [hutool](https://hutool.mydoc.io)                           | 工具类库      | 5.8.26 |
| [easyexcel](https://easyexcel.opensource.alibaba.com)       | excel操作框架 | 3.2.1  |
| [velocity](https://velocity-technology.com)                 | 模板引擎      | 2.3    |
| [hibernate](https://hibernate.org)                          | 数据持久化框架   | 2.7.11 |

### 软件架构

前端基于vue element-ui框架分模块设计，后端使用SpringBoot2框架，持久层同时支持mybatis-plus3（业务查询）、hibernate5（事务业务受理）。
支持微服务模块化开发部署，数据源可彻底隔离，基于ShardingSphere5定制开发多模型分库分片表数据源。
一键代码生成，自定义表单设计，flowable工作流设计等，一款开箱即用的新一代低代码开发框架LegoAdmin

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
* 管理台路径：http://localhost:8443
* 默认账号密码 admin/hello@1234

### 系统展示

* 登陆
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/2%E7%99%BB%E5%BD%95.png)
* 统计大屏
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/1%E9%A6%96%E9%A1%B5.png)
* 代码生成
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/7%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90.png)
* 表单设计
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/8%E8%A1%A8%E5%8D%95%E8%AE%BE%E8%AE%A1.png)
* 流程设计
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/10%E6%B5%81%E7%A8%8B%E8%AE%BE%E8%AE%A1.png)
* 任务审批
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/11%E4%BB%BB%E5%8A%A1%E5%AE%A1%E6%89%B9.png)
* 统计分析
  ![Image text](https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/12%E7%BB%9F%E8%AE%A1%E5%88%86%E6%9E%90.png)
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

