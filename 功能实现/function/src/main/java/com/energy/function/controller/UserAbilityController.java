package com.energy.function.controller;

import com.energy.function.dto.Result;
import com.energy.function.entity.UserAbility;
import com.energy.function.service.UserAbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-ability")
@RequiredArgsConstructor
public class UserAbilityController {
    private final UserAbilityService userAbilityService;

    //保存能力评分
    @PostMapping("/save")
    public Result<UserAbility> saveUserAbility(
            @RequestParam Long userId,
            @RequestParam String algorithm,
            @RequestParam String security,
            @RequestParam String multilingual,
            @RequestParam String projectDev) {
        return Result.success(userAbilityService.saveUserAbility(userId, algorithm, security, multilingual, projectDev));
    }

    //获取个人能力评分
    @GetMapping("/personal")
    public Result<UserAbility> getPersonalAbility(@RequestParam Long userId) {
        return Result.success(userAbilityService.getPersonalAbility(userId));
    }
}
