package com.energy.sign_system.controller;

import com.energy.sign_system.entity.StudyGroup;
import com.energy.sign_system.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 创建学习小组
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createGroup(
            @RequestParam String groupName,
            @RequestParam Long leaderId) {
        Long groupId = groupService.createGroup(groupName, leaderId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (groupId != null) {
            result.put("code", 200);
            result.put("msg", "小组创建成功");
            result.put("groupId", groupId);
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "小组创建失败：参数错误或系统异常");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 加入学习小组
     */
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinGroup(
            @RequestParam Long groupId,
            @RequestParam Long userId) {
        boolean success = groupService.joinGroup(groupId, userId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "加入小组成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "加入小组失败：小组不存在/已加入其他小组/系统异常");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 退出学习小组
     */
    @PostMapping("/quit")
    public ResponseEntity<Map<String, Object>> quitGroup(
            @RequestParam Long groupId,
            @RequestParam Long userId) {
        boolean success = groupService.quitGroup(groupId, userId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "退出小组成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 400);
            result.put("msg", "退出小组失败：小组不存在/未加入该小组/系统异常");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 获取用户所在小组
     */
    @GetMapping("/user-group")
    public ResponseEntity<Map<String, Object>> getUserGroup(@RequestParam Long userId) {
        StudyGroup group = groupService.getUserGroup(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        if (group != null) {
            result.put("code", 200);
            result.put("data", group);
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 404);
            result.put("msg", "未加入任何小组");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    /**
     * 获取小组排行榜（按到课率）
     */
    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Object>> getGroupRanking() {
        List<StudyGroup> ranking = groupService.getGroupRanking();
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("data", ranking);
        result.put("count", ranking.size());
        return ResponseEntity.ok(result);
    }
}