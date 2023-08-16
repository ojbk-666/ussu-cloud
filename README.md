<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">ussu-cloud</h1>
<h4 align="center">基于 Vue/Element UI 和 Spring Boot/Spring Cloud & Alibaba 前后端分离的分布式微服务架构</h4>

## 平台简介

ussu是一套全部开源的快速开发平台。

* 采用前后端分离的模式。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。

## 系统模块

~~~
cc.ussu
├── ussu-gateway         // 网关模块
├── ussu-auth            // 认证中心
├── ussu-api             // 接口模块
│       └── ussu-api-system                          // 系统接口
│       └── ussu-api-log                             // 日志接口
├── ussu-common          // 通用模块
│       └── ussu-common-core                         // 核心模块
│       └── ussu-common-datascope                    // 权限范围
│       └── ussu-common-datasource                   // 多数据源
│       └── ussu-common-log                          // 日志记录
│       └── ussu-common-redis                        // 缓存服务
│       └── ussu-common-seata                        // 分布式事务
│       └── ussu-common-security                     // 安全模块
│       └── ussu-common-swagger                      // 系统接口
├── ussu-modules         // 业务模块
│       └── ussu-modules-system                              // 系统模块
│       └── ussu-modules-file                                // 文件服务
├── ussu-monitor          // 监控
├──pom.xml                // 公共依赖
~~~

