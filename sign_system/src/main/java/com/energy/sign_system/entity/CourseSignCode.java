package com.energy.sign_system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CourseSignCode {
    private Long id;                 // 主键ID
    private Long courseId;           // 课程ID
    private String signCode;         // 签到码
    private Date createTime;         // 创建时间
    private Date expireTime;         // 过期时间
    private Integer status;          // 状态：0-已失效 1-有效
}
