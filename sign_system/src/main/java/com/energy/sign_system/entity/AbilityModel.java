package com.energy.sign_system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AbilityModel {
    private Long id;                 // 主键ID
    private Long userId;             // 用户ID
    private Integer technical;       // 技术能力（0-100）
    private Integer teamwork;        // 团队协作（0-100）
    private Integer creativity;      // 创新能力（0-100）
    private Integer communication;   // 沟通能力（0-100）
    private Integer responsibility;  // 责任心（0-100）
    private Date updateTime;

    // 无参构造器
    public AbilityModel() {}

    // 有参构造器（核心字段）
    public AbilityModel(Long userId, Integer technical, Integer teamwork, Integer creativity, Integer communication, Integer responsibility) {
        this.userId = userId;
        this.technical = technical;
        this.teamwork = teamwork;
        this.creativity = creativity;
        this.communication = communication;
        this.responsibility = responsibility;
        this.updateTime = new Date();
    }

    // 完整getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getTechnical() { return technical; }
    public void setTechnical(Integer technical) { this.technical = technical; }
    public Integer getTeamwork() { return teamwork; }
    public void setTeamwork(Integer teamwork) { this.teamwork = teamwork; }
    public Integer getCreativity() { return creativity; }
    public void setCreativity(Integer creativity) { this.creativity = creativity; }
    public Integer getCommunication() { return communication; }
    public void setCommunication(Integer communication) { this.communication = communication; }
    public Integer getResponsibility() { return responsibility; }
    public void setResponsibility(Integer responsibility) { this.responsibility = responsibility; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
// 更新时间
}
