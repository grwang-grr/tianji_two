[README.md](https://github.com/user-attachments/files/30196381/README.md)
# 天机学堂 (Tianji Academy) — 项目文档

## 项目简介

**天机学堂** 是一个基于 Spring Cloud Alibaba 微服务架构的在线教育平台，提供课程管理、在线学习、考试测评、支付交易、优惠促销、证书颁发等功能。

- **项目名称**: tjxt (天机学堂)
- **组织**: com.tianji
- **版本**: 1.0.0
- **开发方**: 传智教育·研究院

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Java | 11 |
| 框架 | Spring Boot | 2.7.2 |
| 微服务 | Spring Cloud | 2021.0.3 |
| 微服务 | Spring Cloud Alibaba | 2021.0.1.0 |
| ORM | MyBatis-Plus | 3.5.3.1 |
| 注册/配置中心 | Nacos | — |
| 网关 | Spring Cloud Gateway | — |
| 消息队列 | RabbitMQ | — |
| 缓存 | Redis / Redisson | 3.13.6 |
| 搜索引擎 | Elasticsearch | 7.12.1 |
| 分布式事务 | Seata | 1.7.1 |
| 分布式调度 | XXL-Job | 2.3.1 |
| 数据库 | MySQL | 8.0 |
| 对象存储 | MinIO / Tencent COS | — |
| 视频点播 | Tencent VOD | — |
| 短信 | 阿里云短信 / 腾讯云短信 | — |
| 支付 | 支付宝 / 微信支付 | — |
| 前端 | Vue 3 + Vite + TDesign + Pinia | — |
| API文档 | Knife4j (Swagger) | 3.0.3 |
| 容器化 | Docker | — |

## 文档导航

| 文档 | 描述 |
|------|------|
| [架构总览]| 系统架构、微服务拓扑、技术架构 |
| [数据库设计]| 各服务数据库表结构 |
| [消息队列设计]| RabbitMQ 交换机、队列、路由Key |
| [部署运维] | 构建部署、Docker、CI/CD |

### 基础设施模块

| 模块 | 文档 | 端口 | 描述 |
|------|------|------|------|
| tj-gateway | [网关服务] | 10010 | API 网关、路由、鉴权 |
| tj-common | [公共库] | — | 公共工具、自动配置、基础类 |
| tj-api | [API模块]| — | Feign 接口定义、DTO |

### 核心业务模块

| 模块 | 文档 | 端口 | 描述 |
|------|------|------|------|
| tj-auth | [认证授权]| 8081 | 登录、JWT、角色权限 |
| tj-user | [用户中心] | 8082 | 学生/教师/员工管理 |
| tj-course | [课程管理]| 8093 | 课程、分类、章节 |
| tj-learning | [学习中心]| 8090 | 学习计划、笔记、问答、积分、签到 |
| tj-exam | [考试中心] | 8089 | 题库管理、考试记录 |
| tj-trade | [交易中心] | 8088 | 购物车、订单、退款 |
| tj-pay | [支付中心] | 8087 | 支付宝/微信支付 |
| tj-search | [搜索服务] | 8083 | Elasticsearch 课程搜索 |
| tj-media | [媒资中心] | 8084 | 文件上传、视频点播 |
| tj-promotion | [促销服务] | 8092 | 优惠券、兑换码 |
| tj-remark | [评价服务] | 8091 | 点赞管理 |
| tj-message | [消息中心] | 8085 | 短信、站内信、通知模板 |
| tj-notification | [通知中心] | 8095 | 实时推送、WebSocket |
| tj-certificate | [证书中心] | 8096 | 证书颁发、PDF生成、验证 |
| tj-data | [数据中心] | 8097 | 数据看板、统计排行 |

## 项目模块总览

```
tjxt (父POM)
├── tj-gateway          — API网关 (Spring Cloud Gateway)
├── tj-common           — 公共模块 (工具类、自动配置)
├── tj-api              — API定义 (Feign接口、DTO)
├── tj-auth             — 认证授权 (JWT、RBAC)
├── tj-user             — 用户中心
├── tj-course           — 课程管理
├── tj-learning         — 学习中心
├── tj-exam             — 考试中心
├── tj-trade            — 交易中心
├── tj-pay              — 支付中心
├── tj-search           — 搜索服务
├── tj-media            — 媒资中心
├── tj-promotion        — 促销服务
├── tj-remark           — 评价服务
├── tj-message          — 消息中心
├── tj-notification     — 通知中心
├── tj-certificate      — 证书中心
└── tj-data             — 数据中心
```

## 功能全景

```
┌──────────────────────────────────────────────────┐
│                    天机学堂                         │
├────────────┬────────────┬────────────┬───────────┤
│  课程管理   │  学习中心   │  考试测评   │  交易支付  │
│  - 课程CRUD │  - 学习计划 │  - 题库管理 │  - 购物车  │
│  - 分类管理 │  - 学习记录 │  - 考试提交 │  - 订单管理 │
│  - 章节管理 │  - 笔记系统 │  - 成绩统计 │  - 退款处理 │
│  - 教师分配 │  - 问答互动 │            │  - 支付集成 │
├────────────┼────────────┼────────────┼───────────┤
│  促销优惠   │  社交互动   │  证书体系   │  数据分析  │
│  - 优惠券   │  - 点赞系统 │  - 证书颁发 │  - 运营看板 │
│  - 兑换码   │  - 课程评价 │  - PDF生成  │  - Top排行 │
│  - 折扣计算 │  - 课程收藏 │  - 真伪验证 │  - 实时统计 │
├────────────┴────────────┴────────────┴───────────┤
│  基础设施: 认证授权 / 消息通知 / 文件存储 / 搜索推荐  │
└──────────────────────────────────────────────────┘
```

## 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- MySQL 8.0
- Redis
- RabbitMQ
- Nacos Server (192.168.88.106:8848)
- Elasticsearch 7.12.1
- MinIO / Tencent COS

### 构建项目
```bash
# 编译所有模块
mvn clean install -DskipTests

# 构建单个模块
mvn clean package -pl tj-gateway -DskipTests
```

### 启动顺序
1. Nacos Server
2. MySQL / Redis / RabbitMQ / Elasticsearch
3. tj-gateway (端口 10010)
4. tj-auth (端口 8081)
5. tj-user (端口 8082)
6. 其余业务服务 (按需启动)
