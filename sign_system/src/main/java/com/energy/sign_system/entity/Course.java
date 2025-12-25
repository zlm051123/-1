package com.energy.sign_system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Course {
    private Long id;                 // 课程ID
    private String courseName;       // 课程名称
    private String courseCode;       // 课程编码（手动输入用）
    private Long teacherId;          // 授课老师ID
    private Date startTime;          // 课程开始时间
    private Date endTime;            // 课程结束时间
    private String classroom;        // 教室位置
    private Integer status;          // 状态：0-未开始 1-进行中 2-已结束
}
