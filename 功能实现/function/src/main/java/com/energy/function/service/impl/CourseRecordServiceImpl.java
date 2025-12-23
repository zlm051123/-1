package com.energy.function.service.impl;

import com.energy.function.entity.CourseRecord;
import com.energy.function.repository.CourseRecordRepository;
import com.energy.function.service.CourseRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseRecordServiceImpl implements CourseRecordService {
    private final CourseRecordRepository courseRecordRepository;

    @Override
    public CourseRecord saveCourseRecord(Long userId, Long courseId) {
        CourseRecord record = new CourseRecord();
        record.setUserId(userId);
        record.setCourseId(courseId);
        record.setCourseTime(LocalDateTime.now());
        return courseRecordRepository.save(record);
    }

    @Override
    public List<CourseRecord> getPersonalCourseRecord(Long userId) {
        return courseRecordRepository.findByUserId(userId);
    }
}
