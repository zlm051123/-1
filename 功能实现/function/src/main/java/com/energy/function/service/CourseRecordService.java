package com.energy.function.service;

import com.energy.function.entity.CourseRecord;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CourseRecordService {
    // 保存课程记录
    CourseRecord saveCourseRecord(Long userId, Long courseId);

    // 查询个人课程记录
    List<CourseRecord> getPersonalCourseRecord(Long userId);
}
