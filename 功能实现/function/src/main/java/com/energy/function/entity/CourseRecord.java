package com.energy.function.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_course_record")
public class CourseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;       // 关联用户ID

    @Column(nullable = false)
    private Long courseId;     // 课程ID

    private LocalDateTime courseTime; // 上课时间
}
