package com.energy.smsplus.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SignRecord {
    private Long id;                 // 主键ID
    private Long userId;             // 用户ID
    private Long courseId;           // 课程ID
    private String signCode;         // 签到码/课程码
    private Integer signStatus;      // 签到状态：0-未签到 1-成功 2-迟到 3-失败
    private Date signTime;           // 签到时间
    private Date signOutTime;        // 签退时间
    private String location;         // 签到位置（脱敏后）
    private String remark;           // 备注（如迟到原因）
}
