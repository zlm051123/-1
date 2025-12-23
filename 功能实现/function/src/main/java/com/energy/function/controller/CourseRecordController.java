package com.energy.function.controller;

import com.energy.function.dto.Result;
import com.energy.function.entity.CourseRecord;
import com.energy.function.service.CourseRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/course-record")
@RequiredArgsConstructor
public class CourseRecordController {
    private final CourseRecordService courseRecordService;

    // 查询个人课程记录
    @GetMapping("/personal")
    public Result<List<CourseRecord>> getPersonalCourseRecord(@RequestParam Long userId) {
        return Result.success(courseRecordService.getPersonalCourseRecord(userId));
    }
}
