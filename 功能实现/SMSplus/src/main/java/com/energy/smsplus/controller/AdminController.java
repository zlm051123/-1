package com.energy.smsplus.controller;

import com.energy.smsplus.entity.SignRecord;
import com.energy.smsplus.service.AdminService;
import com.energy.smsplus.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SignService signService;

    /**
     * 生成课程签到码
     */
    @PostMapping("/sign-code/generate")
    public ResponseEntity<Map<String, Object>> generateSignCode(
            @RequestParam Long courseId,
            @RequestParam(required = false, defaultValue = "30") Integer validDuration) {
        String signCode = signService.generateSignCode(courseId, validDuration);
        Map<String, Object> result = new java.util.HashMap<>();
        if (signCode != null) {
            result.put("code", 200);
            result.put("msg", "签到码生成成功");
            result.put("signCode", signCode);
            result.put("validDuration", validDuration);
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "签到码生成失败：课程ID无效");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 失效课程签到码
     */
    @PostMapping("/sign-code/invalidate")
    public ResponseEntity<Map<String, Object>> invalidateSignCode(@RequestParam Long courseId) {
        boolean success = signService.invalidateSignCode(courseId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "签到码已失效");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "签到码失效失败：课程ID无效或无有效签到码");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 批量导出签到数据
     */
    @GetMapping("/sign-data/export")
    public ResponseEntity<Map<String, Object>> exportSignData(@RequestParam(required = false) Long courseId) {
        List<SignRecord> signData = adminService.exportSignData(courseId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("data", signData);
        result.put("count", signData.size());
        result.put("msg", "签到数据导出成功");
        return ResponseEntity.ok(result);
    }

    /**
     * 手动补签
     */
    @PostMapping("/sign/manual")
    public ResponseEntity<Map<String, Object>> manualSign(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam Integer signStatus) {
        boolean success = adminService.manualSign(userId, courseId, signStatus);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "手动补签成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "手动补签失败：参数错误（状态值0-3）或系统异常");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 获取签到热力图数据
     */
    @GetMapping("/sign/heat-map")
    public ResponseEntity<Map<String, Object>> getSignHeatMap() {
        Map<String, Integer> heatMap = adminService.getSignHeatMap();
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("data", heatMap);
        result.put("msg", "热力图数据查询成功");
        return ResponseEntity.ok(result);
    }

    /**
     * 获取课程实时到课率
     */
    @GetMapping("/sign/real-time-attendance")
    public ResponseEntity<Map<String, Object>> getRealTimeAttendance(@RequestParam Long courseId) {
        double rate = adminService.getRealTimeAttendance(courseId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("attendanceRate", rate);
        result.put("msg", "实时到课率查询成功");
        return ResponseEntity.ok(result);
    }
}
