package com.energy.sign_system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;                 // 用户ID
    private String username;         // 用户名
    private String password;         // 密码（加密存储）
    private String phone;            // 手机号
    private String email;            // 邮箱
    private Integer status;          // 状态：0-禁用 1-正常
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间
}
