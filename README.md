# 轻客管家系统

一个基于 SpringBoot + Vue3 的企业级管理系统，包含用户管理、部门管理、角色管理、线索管理、课程管理、活动管理等模块。

## 技术栈

### 后端
- **框架**: SpringBoot 3.4.7
- **ORM**: MybatisPlus 3.5.8
- **数据库**: MySQL 8.0
- **JDK**: Java 21
- **认证**: JWT
- **构建工具**: Maven

### 前端
- **框架**: Vue 3
- **UI组件**: ElementPlus
- **构建工具**: Vite
- **HTTP客户端**: Axios

## 功能模块

- ✅ 用户管理（CRUD + 文件上传 + 角色关联）
- ✅ 部门管理（CRUD + 状态管理）
- ✅ 角色管理（CRUD + 权限管理）
- ✅ 线索管理（CRUD + 跟进记录 + 状态流转）
- ✅ 课程管理（CRUD + 学科/人群筛选）
- ✅ 活动管理（CRUD + 折扣/代金券 + 状态判断）
- ✅ 登录认证（JWT + 注册 + 全局异常处理）

## 快速开始

### 后端启动

```bash
cd web-Project/maven-project01
mvn spring-boot:run
```

后端服务运行在 http://localhost:8080

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务运行在 http://localhost:5173

### 数据库配置

1. 创建数据库 `qk_manager`
2. 执行 SQL 脚本（位于 `web-Project/maven-project01/src/main/resources/`）
3. 修改 `application.yml` 中的数据库连接信息

## 项目结构

```
qingkeguanjia/
├── frontend/              # 前端项目
│   ├── src/
│   │   ├── api/          # API 接口
│   │   ├── components/   # 公共组件
│   │   ├── router/       # 路由配置
│   │   ├── utils/        # 工具类
│   │   └── views/        # 页面组件
│   └── package.json
└── web-Project/          # 后端项目
    └── maven-project01/
        ├── src/main/java/com/qk/
        │   ├── controller/  # 控制器
        │   ├── service/     # 服务层
        │   ├── mapper/      # 数据访问层
        │   ├── entity/      # 实体类
        │   ├── dto/         # 数据传输对象
        │   ├── config/      # 配置类
        │   └── utils/       # 工具类
        └── pom.xml
```

## 开发说明

### 统一响应规范

- 成功响应: `Result.success(data)`
- 分页响应: `PageResult.of(total, rows)`

### 全局异常处理

所有异常由 `GlobalExceptionHandler` 统一处理，返回标准错误响应。

管理员账号 admin
密码 是默认一二三四五六 数字版

### 文件上传

- 头像上传: `POST /user/upload`
- 本地存储路径: `uploads/avatar/`
- 访问地址: `http://localhost:8080/uploads/`

## License
