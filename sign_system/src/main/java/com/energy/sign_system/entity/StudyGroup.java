package com.energy.sign_system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class StudyGroup {
    private Long id;                 // 小组ID
    private String groupName;        // 小组名称
    private Long leaderId;           // 组长ID
    private Integer memberCount;     // 成员数量
    private Date createTime;         // 创建时间
    private Integer status;          // 状态：0-解散 1-正常
}

