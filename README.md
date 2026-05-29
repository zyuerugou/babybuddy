<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">BabyBuddy</h1>
<h4 align="center">婴儿喂养跟踪平台</h4>
<h5 align="center">基于 RuoYi-Vue v3.9.0 构建的前后端分离管理系统</h5>

## 平台简介

BabyBuddy 是一个婴儿喂养跟踪管理平台，帮助父母记录和追踪宝宝的日常活动、生长发育数据，并通过可视化报表直观展示宝宝的成长趋势。

**核心功能：**

- **仪表盘**：首页汇总展示，支持快捷操作（计时器、添加喂奶、添加换尿布、添加身高/体重测量）
- **婴儿信息管理**：维护宝宝基本信息（姓名、性别、出生日期、照片），支持多宝宝管理
- **行为记录**：记录喂奶（母乳/配方奶/辅食及喂养量）、换尿布（湿/脏/混合）、玩耍等日常行为
- **生长测量**：记录身高、体重等生长数据
- **计时器**：实用计时工具，帮助追踪喂奶或其他活动时长
- **报表中心**：
  - **生长曲线**：身高/体重随时间变化的曲线图，支持7天/30天/90天/全部时间范围
  - **喂养间隔**：历史喂养记录及喂养间隔统计（最小/最大/平均间隔）
  - **每日喂养量**：每日喂养次数与喂养量趋势图

**技术栈：**

| 层级   | 技术                                                          |
| ------ | ------------------------------------------------------------- |
| 前端   | Vue 2 + Element UI + ECharts + Axios                          |
| 后端   | Spring Boot 2.5 + Spring Security + JWT                       |
| ORM    | MyBatis                                                       |
| 数据库 | MySQL 8.0                                                     |
| 缓存   | Redis                                                         |
| 连接池 | Druid                                                         |

## 项目结构

```
babybuddy/
├── ruoyi-admin/          # 应用入口 & 配置文件
│   ├── src/main/resources/
│   │   ├── application.yml          # 主配置
│   │   ├── application-druid.yml    # 数据源配置
│   │   ├── db/
│   │   │   └── init-baby.sql        # 婴儿模块建表脚本
│   │   └── logback.xml              # 日志配置
│   └── pom.xml
├── ruoyi-baby/           # 婴儿管理业务模块
│   ├── src/main/java/com/ruoyi/baby/
│   │   ├── controller/              # REST 控制器
│   │   │   ├── BabyController.java
│   │   │   ├── BehaviorController.java
│   │   │   ├── MeasurementController.java
│   │   │   └── ReportController.java
│   │   ├── domain/                  # 实体类
│   │   │   ├── Baby.java
│   │   │   ├── Behavior.java
│   │   │   └── Measurement.java
│   │   ├── mapper/                  # MyBatis Mapper 接口
│   │   └── service/                 # 业务服务层
│   │       ├── impl/
│   │       └── I*Service.java
│   └── src/main/resources/mapper/baby/
│       ├── BabyMapper.xml
│       ├── BehaviorMapper.xml
│       └── MeasurementMapper.xml
├── ruoyi-common/         # 通用工具类 & 基础类
├── ruoyi-framework/      # 框架核心（安全、数据源等）
├── ruoyi-ui/             # Vue 前端项目
│   └── src/views/baby/
│       ├── dashboard.vue             # 首页仪表盘
│       ├── info.vue                  # 婴儿信息 CRUD
│       ├── behavior/index.vue        # 行为记录 CRUD
│       ├── measurement/index.vue     # 测量记录 CRUD
│       ├── timer/index.vue           # 计时器
│       └── report/
│           ├── growth.vue            # 生长曲线报表
│           ├── feedingInterval.vue   # 喂养间隔报表
│           └── dailyFeeding.vue      # 每日喂养量报表
├── sql/
│   ├── ry_20250522.sql               # RuoYi 系统表（先执行）
│   ├── quartz.sql                    # Quartz 调度表（先执行）
│   └── init-baby-module.sql          # 婴儿模块表+菜单权限（后执行）
└── pom.xml                # Maven 父 POM
```

## 数据模型

| 表名              | 说明         | 主要字段                                                                    |
| ----------------- | ------------ | --------------------------------------------------------------------------- |
| `bb_baby`         | 婴儿信息     | `baby_id`, `name`, `gender`, `birth_date`, `photo`                          |
| `bb_behavior`     | 行为记录     | `behavior_id`, `baby_id`, `behavior_type`, `feed_method`, `feed_amount`, `diaper_type`, `start_time`, `end_time`, `duration` |
| `bb_measurement`  | 生长测量     | `measure_id`, `baby_id`, `measure_type`, `value`, `measure_date`            |

**行为类型（behavior_type）：** `feeding`（喂奶）、`diaper`（换尿布）、`play`（玩耍）

**喂奶方式（feed_method）：** `breast`（母乳）、`formula`（配方奶）、`solids`（辅食）

**尿布类型（diaper_type）：** `wet`（湿）、`dirty`（脏）、`mixed`（混合）

**测量类型（measure_type）：** `height`（身高）、`weight`（体重）

## 环境要求

- **JDK** >= 1.8
- **MySQL** >= 8.0
- **Redis** >= 5.0
- **Maven** >= 3.6
- **Node.js** >= 14.x

## 快速开始

### 1. 克隆项目

```bash
git clone <your-repo-url>
cd babybuddy
```

### 2. 数据库初始化

按顺序执行以下 SQL 脚本：

```sql
-- 1. RuoYi 系统表
source sql/ry_20250522.sql;

-- 2. Quartz 调度表
source sql/quartz.sql;

-- 3. 婴儿模块表及菜单权限
source sql/init-baby-module.sql;
```

### 3. 修改配置

编辑 `ruoyi-admin/src/main/resources/application-druid.yml`：

```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://你的IP:3306/babybuddy?useUnicode=true&...
        username: 你的用户名
        password: 你的密码
```

### 4. 启动 Redis

确保 Redis 服务已启动（默认 `localhost:6379`）。

### 5. 启动后端

```bash
cd ruoyi-admin
mvn spring-boot:run
```

或在 IDE 中运行 `RuoYiApplication.java` 主类。

后端默认运行在 `http://localhost:8080`。

### 6. 启动前端

```bash
cd ruoyi-ui
npm install
npm run dev
```

前端默认运行在 `http://localhost:80`。

### 7. 登录系统

浏览器打开 `http://localhost`，使用默认账号登录：

- **账号：** `admin`
- **密码：** `admin123`

## API 接口

### 婴儿管理

| 方法     | 路径                        | 说明           |
| -------- | --------------------------- | -------------- |
| `GET`    | `/baby/baby/list`           | 查询婴儿列表   |
| `GET`    | `/baby/baby/{babyId}`       | 查询婴儿详情   |
| `POST`   | `/baby/baby`                | 新增婴儿       |
| `PUT`    | `/baby/baby`                | 修改婴儿       |
| `DELETE` | `/baby/baby/{babyIds}`      | 删除婴儿       |

### 行为记录

| 方法     | 路径                          | 说明           |
| -------- | ----------------------------- | -------------- |
| `GET`    | `/baby/behavior/list`         | 查询行为列表   |
| `GET`    | `/baby/behavior/{behaviorId}` | 查询行为详情   |
| `POST`   | `/baby/behavior`              | 新增行为       |
| `PUT`    | `/baby/behavior`              | 修改行为       |
| `DELETE` | `/baby/behavior/{behaviorId}` | 删除行为       |

### 生长测量

| 方法     | 路径                            | 说明           |
| -------- | ------------------------------- | -------------- |
| `GET`    | `/baby/measurement/list`        | 查询测量列表   |
| `GET`    | `/baby/measurement/{measureId}` | 查询测量详情   |
| `POST`   | `/baby/measurement`             | 新增测量       |
| `PUT`    | `/baby/measurement`             | 修改测量       |
| `DELETE` | `/baby/measurement/{measureId}` | 删除测量       |

### 报表

| 方法  | 路径                               | 参数                          | 说明           |
| ----- | ---------------------------------- | ----------------------------- | -------------- |
| `GET` | `/baby/report/growth`              | `range`(7/30/90/all), `babyId` | 生长曲线       |
| `GET` | `/baby/report/feeding-interval`    | `babyId`                      | 喂养间隔统计   |
| `GET` | `/baby/report/daily-feeding`       | `beginTime`, `endTime`, `babyId` | 每日喂养量统计 |

## 权限说明

婴儿模块权限标识一览：

| 权限标识                 | 说明         |
| ------------------------ | ------------ |
| `baby:dashboard:query`   | 仪表盘查看   |
| `baby:info:query`        | 婴儿信息查询 |
| `baby:info:add`          | 婴儿信息新增 |
| `baby:info:edit`         | 婴儿信息修改 |
| `baby:info:remove`       | 婴儿信息删除 |
| `baby:behavior:list`     | 行为记录查询 |
| `baby:behavior:add`      | 行为记录新增 |
| `baby:behavior:edit`     | 行为记录修改 |
| `baby:behavior:remove`   | 行为记录删除 |
| `baby:measurement:list`  | 测量记录查询 |
| `baby:measurement:add`   | 测量记录新增 |
| `baby:measurement:edit`  | 测量记录修改 |
| `baby:measurement:remove`| 测量记录删除 |
| `baby:timer:use`         | 计时器使用   |
| `baby:report:growth`     | 生长曲线查看 |
| `baby:report:interval`   | 行为间隔查看 |
| `baby:report:feeding`    | 喂养量查看   |

执行 `init-baby-module.sql` 后，管理员角色会自动获得上述全部权限。

## 致谢

本项目基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 构建，感谢若依团队提供优秀的快速开发框架。

## License

MIT License