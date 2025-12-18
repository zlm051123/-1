package com.energy.function.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = " t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;   //用户名

    @Column(nullable = false)
    private String groupId  ;    //所属组Id
}
