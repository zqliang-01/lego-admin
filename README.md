# LegoAdmin

<a target="_blank" href="http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=wBCgfng_HilFFVLXWimzrd63aPh8We-_&authKey=6lKMaBUa5MbsLcB44lOCT2aO52h8jw8lLZX8qYdTIxyfVMawTdm4p2bwfGI8QtOH&noverify=0&group_code=962119957">技术交流群</a> 962119957

### 介绍

- 一款彻底开源的前后端分离的低代码开发框架
- 演示地址：https://admin.zqliang.cn
- 帐号：test 密码：test123

### 项目地址

- [Github](https://github.com/zqliang-01/lego-admin.git)
- [Gitee](https://gitee.com/zqliang_01/lego-admin.git)

### 技术栈

| 技术                                                                    | 说明                     | 版本         |
|-----------------------------------------------------------------------|------------------------|------------|
| [SpringBoot](https://spring.io/projects/spring-boot)                  | Java 框架                | 3.2.2      |
| [SpringCloud](https://spring.io/projects/spring-cloud)                | SpringCloud 全家桶        | 2023.0.0   |
| [SpringCloudAlibaba](https://spring.io/projects/spring-cloud-alibaba) | SpringCloudAlibaba 全家桶 | 2022.0.0.0 |
| [mybatis-plus](https://mybatis.plus)                                  | 数据持久化框架                | 3.5.11      |
| [flowable](https://www.flowable.com)                                  | 流程引擎                   | 7.0.1      |
| [ShardingSphere](https://shardingsphere.apache.org)                   | 分库分表框架                 | 5.4.1      |
| [sa-token](https://sa-token.cc)                                       | 权限认证框架                 | 1.38.0     |
| [openfeign](https://cloud.spring.io/spring-cloud-openfeign)           | 远程调用框架                 | 4.1.0      |
| [hutool](https://hutool.mydoc.io)                                     | 工具类库                   | 5.8.37     |
| [easyexcel](https://easyexcel.opensource.alibaba.com)                 | excel 操作框架             | 3.3.3      |
| [velocity](https://velocity-technology.com)                           | 模板引擎                   | 2.3        |
| [SpringDataJpa](https://github.com/spring-projects/spring-data-jpa)   | 数据持久化框架                | 3.2.2     |
| [xxl-job](https://www.xuxueli.com/xxl-job/)                           | 分布式定时任务框架              | 2.4.1      |

### 软件架构

前端基于 vue element-ui 框架分模块设计，后端使用 SpringBoot3 框架，持久层同时支持 mybatis-plus3（业务查询）、hibernate6（事务业务受理）。
支持微服务模块化开发部署，数据源可彻底隔离，基于 ShardingSphere5 定制开发多模型分库分片表数据源。
一键代码生成，自定义表单设计，flowable工作流设计等，一款开箱即用的新一代低代码开发框架 LegoAdmin

### 安装教程

#### 配置依赖环境

- `mysql5.7`
- `JDK17`
- `maven3.8`
- `node` >= 10.0.0 建议 v21
- `npm` >= 6.0.0 建议 v10

#### 打包运行

- 创建 mysql 数据库 lego-admin
- 拷贝 lego-parent/lego-admin/src/main/resources/application-template.properties 到同目录并命名为 application.properties
- 修改 application.properties 配置中的数据库链接和账号密码
- 根目录下双击执行 dbInit.bat，初始化数据库
- 根目录下双击执行 build.bat，会自动打包前后端
- 根目录下双击执行 startup.bat，会自动运行

#### 访问路径

- 项目默认端口 8443
- 管理台路径：http://localhost:8443
- 默认账号密码 admin/hello@1234

### 系统展示

<table>
  <tr>
    <td>
- 登陆
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/2%E7%99%BB%E5%BD%95.png"/>
    </td>
    <td>
- 统计大屏
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/1%E9%A6%96%E9%A1%B5.png"/>
    </td>
  </tr>
  <tr>
    <td>
- 代码生成
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/7%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90.png"/>
    </td>
    <td>
- 表单设计
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/8%E8%A1%A8%E5%8D%95%E8%AE%BE%E8%AE%A1.png"/>
    </td>
  </tr>
  <tr>
    <td>
- 流程设计
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/10%E6%B5%81%E7%A8%8B%E8%AE%BE%E8%AE%A1.png"/>
    </td>
    <td>
- 任务审批
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/11%E4%BB%BB%E5%8A%A1%E5%AE%A1%E6%89%B9.png"/>
    </td>
  </tr>
  <tr>
    <td>
- 定时任务
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/13%E5%AE%9A%E6%97%B6%E4%BB%BB%E5%8A%A1.png"/>
    </td>
    <td>
- 操作日志
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/14%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%97.png"/>
    </td>
  </tr>
  <tr>
    <td>
- 统计分析
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/12%E7%BB%9F%E8%AE%A1%E5%88%86%E6%9E%90.png"/>
    </td>
    <td>
- 分库分表
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/9%E5%88%86%E5%BA%93%E5%88%86%E8%A1%A8.png"/>
    </td>
  </tr>
  <tr>
    <td>
- 企业定制
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/3%E4%BC%81%E4%B8%9A%E5%AE%9A%E5%88%B6.png"/>
    </td>
    <td>
- 应用管理
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/4%E5%BA%94%E7%94%A8%E7%AE%A1%E7%90%86.png"/>
    </td>
  </tr>
  <tr>
    <td>
- 组织架构
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/5%E7%BB%84%E7%BB%87%E6%9E%B6%E6%9E%84.png"/>
    </td>
    <td>
- 权限管理
  <img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/6%E6%9D%83%E9%99%90%E7%AE%A1%E7%90%86.png"/>
    </td>
  </tr>
</table>

### 小程序端
<img src="https://gitee.com/zqliang_01/show-image/raw/master/lego-admin/%E5%B0%8F%E7%A8%8B%E5%BA%8F%E7%A0%81.png"/>