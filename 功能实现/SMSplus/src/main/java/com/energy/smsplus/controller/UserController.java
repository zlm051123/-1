package com.energy.smsplus.controller;

import com.energy.smsplus.entity.Achievement;
import com.energy.smsplus.entity.User;
import com.energy.smsplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        boolean success = userService.register(user);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "注册成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "注册失败：用户名已存在或参数错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (user != null) {
            result.put("code", 200);
            result.put("data", user);
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 404);
            result.put("msg", "用户不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    /**
     * 获取用户成就列表
     */
    @GetMapping("/achievements")
    public ResponseEntity<Map<String, Object>> getUserAchievements(@RequestParam Long userId) {
        List<Achievement> achievements = userService.getUserAchievements(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("data", achievements);
        result.put("count", achievements.size());
        return ResponseEntity.ok(result);
    }

    /**
     * 发放成就徽章（管理员/系统调用）
     */
    @PostMapping("/grant-achievement")
    public ResponseEntity<Map<String, Object>> grantAchievement(
            @RequestParam Long userId,
            @RequestParam String achievementName,
            @RequestParam String description) {
        boolean success = userService.grantAchievement(userId, achievementName, description);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "成就发放成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "成就发放失败：已获取该成就或参数错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
}
