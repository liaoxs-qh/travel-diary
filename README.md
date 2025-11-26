# 旅行日记后端服务

基于 Spring Boot 3.2 + MyBatis-Plus 的旅行日记后端服务。

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE travel_diary CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构
USE travel_diary;
SOURCE ../docs/database_schema.sql;
```

### 3. 配置文件

修改 `src/main/resources/application-dev.yml`：

- 数据库连接信息
- Redis连接信息
- 微信小程序 AppID 和 Secret
- 阿里云OSS配置（如需使用）

### 4. 启动项目

```bash
# 方式1: Maven命令
mvn spring-boot:run

# 方式2: IDE运行
# 直接运行 TravelDiaryApplication.java

# 方式3: 打包运行
mvn clean package
java -jar target/travel-diary-0.0.1-SNAPSHOT.jar
```

### 5. 访问

- 应用地址: http://localhost:8080
- API文档: http://localhost:8080/doc.html
- 健康检查: http://localhost:8080/api/v1/test/health

## 项目结构

```
src/main/java/com/travel/diary/
├── controller/         # 控制器层
├── service/           # 业务逻辑层
├── mapper/            # 数据访问层
├── entity/            # 实体类
├── dto/               # 数据传输对象
├── config/            # 配置类
├── security/          # 安全相关
├── common/            # 公共类
└── util/              # 工具类
```

## 技术栈

- Spring Boot 3.2
- Spring Security + JWT
- MyBatis-Plus 3.5
- MySQL 8.0
- Redis 6.x
- Knife4j (API文档)
- Lombok

## 开发说明

### 添加新功能

1. 在 `entity` 包创建实体类
2. 在 `mapper` 包创建Mapper接口
3. 在 `service` 包创建Service接口和实现类
4. 在 `controller` 包创建Controller
5. 使用 `@Tag` 和 `@Operation` 注解生成API文档

### 统一响应格式

使用 `Result<T>` 类封装响应：

```java
return Result.success(data);
return Result.error("错误信息");
```

### 异常处理

抛出 `BusinessException` 会被全局异常处理器捕获：

```java
throw new BusinessException("业务异常信息");
```

## 注意事项

1. 当前Security配置为开发模式，所有接口都可访问
2. 生产环境需要配置JWT认证和权限控制
3. 记得修改 `jwt.secret` 为安全的密钥
4. OSS配置需要替换为真实的AccessKey

## 下一步

- [ ] 实现微信登录功能
- [ ] 实现日记CRUD接口
- [ ] 实现图片上传功能
- [ ] 配置JWT认证
- [ ] 编写单元测试
