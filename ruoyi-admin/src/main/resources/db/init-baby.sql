-- ============================
-- 婴儿跟踪模块初始化脚本 (MySQL)
-- ============================

-- 婴儿信息表
CREATE TABLE IF NOT EXISTS bb_baby (
    baby_id      int primary key auto_increment,
    name         varchar(100) NOT NULL DEFAULT '',
    gender       varchar(10) DEFAULT '',
    birth_date   datetime,
    photo        varchar(500) DEFAULT '',
    create_by    varchar(64) DEFAULT '',
    create_time  datetime,
    update_by    varchar(64) DEFAULT '',
    update_time  datetime,
    remark       varchar(500) DEFAULT NULL
) engine=innodb;

-- 行为记录表
CREATE TABLE IF NOT EXISTS bb_behavior (
    behavior_id   int primary key auto_increment,
    baby_id       int DEFAULT NULL,
    behavior_type varchar(20) NOT NULL,
    feed_method   varchar(20) DEFAULT NULL,
    feed_amount   double DEFAULT NULL,
    diaper_type   varchar(20) DEFAULT NULL,
    play_type     varchar(20) DEFAULT NULL,
    start_time    datetime,
    end_time      datetime,
    duration      int DEFAULT NULL,
    create_by     varchar(64) DEFAULT '',
    create_time   datetime,
    update_by     varchar(64) DEFAULT '',
    update_time   datetime,
    remark        varchar(500) DEFAULT NULL
) engine=innodb;

-- 生长测量表
CREATE TABLE IF NOT EXISTS bb_measurement (
    measure_id   int primary key auto_increment,
    baby_id      int DEFAULT NULL,
    measure_type varchar(20) NOT NULL,
    value        double NOT NULL,
    measure_date datetime NOT NULL,
    create_by    varchar(64) DEFAULT '',
    create_time  datetime,
    update_by    varchar(64) DEFAULT '',
    update_time  datetime,
    remark       varchar(500) DEFAULT NULL
) engine=innodb;