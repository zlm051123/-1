package com.energy.sign_system.controller;

import com.energy.sign_system.entity.SignRecord;
import com.energy.sign_system.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sign")
public class SignController {

    @Autowired
    private SignService signService;

    /**
     * 签到接口
     */
    @PostMapping("/in")
    public ResponseEntity<Map<String, Object>> signIn(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam String signCode,
            @RequestParam String location) {
        boolean success = signService.signIn(userId, courseId, signCode, location);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "签到成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "签到失败：签到码无效/已过期或已签到");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 签退接口
     */
    @PostMapping("/out")
    public ResponseEntity<Map<String, Object>> signOut(
            @RequestParam Long userId,
            @RequestParam Long courseId) {
        boolean success = signService.signOut(userId, courseId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "签退成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "签退失败：未找到有效签到记录");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 获取用户签到记录
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getSignRecords(@RequestParam Long userId) {
        List<SignRecord> records = signService.getSignRecords(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("data", records);
        result.put("count", records.size());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取用户到课率
     */
    @GetMapping("/attendance-rate")
    public ResponseEntity<Map<String, Object>> getAttendanceRate(@RequestParam Long userId) {
        double rate = signService.getAttendanceRate(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("attendanceRate", rate);
        result.put("msg", "查询成功");
        return ResponseEntity.ok(result);
    }
}
