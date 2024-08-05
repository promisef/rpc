前言

> 

## 说明

> 

## 技术栈

- **后端：**`SpringBoot`+`MybatisPlus`+`SpringCloud`+`SpringCloudAlibaba`+`RabbitMQ`
- **数据库：**`Mysql`+`Redis`+`Elasticsearch`

## 功能模块

```
```
### config

这是用户访问的模块，用户访问的前端页面都在这个模块，通过openfeign调用其他服务。

### fault

这是用户相关的服务，包括登录、注册、关注、收藏、投币等，用户的点赞、观看数等数据通过rabbitmq发送到fileservice并进行更新。

### fileservice

这是文件、投稿相关的服务，这个模块不对外直接开放，通过gateway路由以及虚拟路径来访问文件。并且，用户上传的文件投稿以及其他公共文件不在同一路径，只能通过服务调用的方式来进行下载。本服务通过elasticsearch进行高性能的搜索，通过定时任务、消息队列来非实时更新数据。


## 运行项目

- 设置main.js的src和api
- 在userservice配置自己邮件服务
- 启动elasticsearch,rabbitmq,redis,nacos等服务
- 配置fileservice文件的存储路径，将doc中的pfx文件放在指定目录开启https
- 启动各个微服务
