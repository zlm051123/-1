package com.energy.sign_system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Achievement {
    private Long id;                 // 成就ID
    private Long userId;             // 用户ID
    private String achievementName;  // 成就名称（如"全勤之星"）
    private String achievementIcon;  // 徽章图标路径
    private Date obtainTime;         // 获取时间
    private String description;      // 成就描述
}
