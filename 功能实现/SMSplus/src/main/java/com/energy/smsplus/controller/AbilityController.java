package com.energy.smsplus.controller;

import com.energy.smsplus.entity.AbilityModel;
import com.energy.smsplus.service.AbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ability")
public class AbilityController {

    @Autowired
    private AbilityService abilityService;

    /**
     * 获取用户能力模型
     */
    @GetMapping("/model")
    public ResponseEntity<Map<String, Object>> getAbilityModel(@RequestParam Long userId) {
        AbilityModel model = abilityService.getAbilityModel(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (model != null) {
            result.put("code", 200);
            result.put("data", model);
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 404);
            result.put("msg", "能力模型不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    /**
     * 更新用户能力模型
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateAbilityModel(@RequestBody AbilityModel abilityModel) {
        boolean success = abilityService.updateAbilityModel(abilityModel);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "能力模型更新成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "能力模型更新失败：参数错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 根据课程表现更新能力值（异步）
     */
    @PostMapping("/update-by-course")
    public ResponseEntity<Map<String, Object>> updateAbilityByCourse(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam Integer score) {
        // 异步调用，直接返回成功
        abilityService.updateAbilityByCourse(userId, courseId, score);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("msg", "能力值更新请求已接收，正在处理");
        return ResponseEntity.ok(result);
    }
}
