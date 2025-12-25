-- 创建数据库
CREATE DATABASE IF NOT EXISTS sign_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sign_system;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                                      `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（建议加密存储）',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 课程表
CREATE TABLE IF NOT EXISTS `course` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程ID',
                                        `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `course_code` VARCHAR(20) NOT NULL COMMENT '课程编码',
    `teacher_id` BIGINT NOT NULL COMMENT '授课老师ID',
    `start_time` DATETIME NOT NULL COMMENT '课程开始时间',
    `end_time` DATETIME NOT NULL COMMENT '课程结束时间',
    `classroom` VARCHAR(50) DEFAULT NULL COMMENT '教室位置',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-未开始 1-进行中 2-已结束',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course_code` (`course_code`),
    KEY `idx_teacher_id` (`teacher_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 课程签到码表
CREATE TABLE IF NOT EXISTS `course_sign_code` (
                                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                  `course_id` BIGINT NOT NULL COMMENT '课程ID',
                                                  `sign_code` VARCHAR(32) NOT NULL COMMENT '签到码',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-已失效 1-有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course_code` (`course_id`, `sign_code`),
    KEY `idx_expire_time` (`expire_time`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程签到码表';

-- 签到记录表
CREATE TABLE IF NOT EXISTS `sign_record` (
                                             `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                             `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                             `course_id` BIGINT NOT NULL COMMENT '课程ID',
                                             `sign_code` VARCHAR(32) NOT NULL COMMENT '签到码/课程码',
    `sign_status` TINYINT NOT NULL COMMENT '签到状态：0-未签到 1-成功 2-迟到 3-失败',
    `sign_time` DATETIME NOT NULL COMMENT '签到时间',
    `sign_out_time` DATETIME DEFAULT NULL COMMENT '签退时间',
    `location` VARCHAR(128) DEFAULT NULL COMMENT '签到位置（脱敏后）',
    `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注（如补签原因）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_course` (`user_id`, `course_id`),
    KEY `idx_sign_time` (`sign_time`),
    KEY `idx_sign_status` (`sign_status`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';

-- 能力模型表
CREATE TABLE IF NOT EXISTS `ability_model` (
                                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                               `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                               `technical` TINYINT NOT NULL DEFAULT 50 COMMENT '技术能力（0-100）',
                                               `teamwork` TINYINT NOT NULL DEFAULT 50 COMMENT '团队协作（0-100）',
                                               `creativity` TINYINT NOT NULL DEFAULT 50 COMMENT '创新能力（0-100）',
                                               `communication` TINYINT NOT NULL DEFAULT 50 COMMENT '沟通能力（0-100）',
                                               `responsibility` TINYINT NOT NULL DEFAULT 50 COMMENT '责任心（0-100）',
                                               `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                               PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='五维能力模型表';

-- 学习小组表
CREATE TABLE IF NOT EXISTS `study_group` (
                                             `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '小组ID',
                                             `group_name` VARCHAR(50) NOT NULL COMMENT '小组名称',
    `leader_id` BIGINT NOT NULL COMMENT '组长ID',
    `member_count` INT NOT NULL DEFAULT 1 COMMENT '成员数量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-解散 1-正常',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_name` (`group_name`),
    KEY `idx_leader_id` (`leader_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习小组表';

-- 用户-小组关联表
CREATE TABLE IF NOT EXISTS `user_group_relation` (
                                                     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                     `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                     `group_id` BIGINT NOT NULL COMMENT '小组ID',
                                                     `join_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
                                                     `quit_time` DATETIME DEFAULT NULL COMMENT '退出时间',
                                                     `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-已退出 1-正常',
                                                     PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_group` (`user_id`, `group_id`),
    KEY `idx_group_id` (`group_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-小组关联表';

-- 成就徽章表
CREATE TABLE IF NOT EXISTS `achievement` (
                                             `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '成就ID',
                                             `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                             `achievement_name` VARCHAR(50) NOT NULL COMMENT '成就名称',
    `achievement_icon` VARCHAR(200) DEFAULT NULL COMMENT '徽章图标路径',
    `obtain_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '成就描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_achievement` (`user_id`, `achievement_name`),
    KEY `idx_obtain_time` (`obtain_time`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就徽章表';

-- 用户-课程关联表（用于统计应到人数）
CREATE TABLE IF NOT EXISTS `user_course_relation` (
                                                      `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                      `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                      `course_id` BIGINT NOT NULL COMMENT '课程ID',
                                                      `join_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
                                                      `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-退课 1-正常',
                                                      PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_course` (`user_id`, `course_id`),
    KEY `idx_course_id` (`course_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-课程关联表';