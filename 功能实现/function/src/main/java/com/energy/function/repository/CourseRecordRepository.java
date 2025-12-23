package com.energy.function.repository;

import com.energy.function.entity.CourseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRecordRepository extends JpaRepository<CourseRecord, Long> {
    List<CourseRecord> findByUserId(Long userId);
}
