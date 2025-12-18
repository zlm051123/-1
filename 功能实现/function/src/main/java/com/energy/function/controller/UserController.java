package com.energy.function.controller;

import com.energy.function.dto.Result;
import com.energy.function.entity.User;
import com.energy.function.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    //新增用户（测试用）
    @PostMapping("/save")
    public Result<User> saveUser(@RequestBody User user) {
        return Result.success(userRepository.save(user));
    }
}
