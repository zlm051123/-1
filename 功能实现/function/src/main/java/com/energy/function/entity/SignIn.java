package com.energy.function.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "t_sign_in")
public class SignIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;   //关联用户ID
//
//    @Column(nullable = false)
//    private Long courseId;  //课程ID

    @Column(nullable = false)
    private String signInCode;   //2FAS签到码

    @Column(nullable = false)
    private String seatNumber;     //座位号


    private String signInTime;    //签到时间

    @Column(nullable = false)
    private String groupId;    //所属组
}
