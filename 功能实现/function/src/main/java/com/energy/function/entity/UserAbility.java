package com.energy.function.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "t_user_ability")
public class UserAbility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;    //关联用户ID

    private String algorithmAbility;   // 算法能力（如A+）
    private String securityAbility;    // 安全能力（如D-）
    private String multilingualAbility;// 多语言能力（如S）
    private String projectDevAbility;  // 项目开发能力（如B+）
}

