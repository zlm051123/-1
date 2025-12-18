package com.energy.function.controller;

import com.energy.function.dto.Result;
import com.energy.function.dto.UserSignInDTO;
import com.energy.function.entity.SignIn;
import com.energy.function.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
//import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sign-in")
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService;

    //生成签到码
    @GetMapping("/generate-code")
    public Result<String> generateSignInCode() {
        return Result.success(signInService.generateSignInCode());
    }

    //用户签到
    @PostMapping("/user-sign")
    public Result<SignIn> userSignIn( @RequestBody UserSignInDTO dto) {
        return Result.success(signInService.userSignIn(dto));
    }

    //查询组签到情况
    @GetMapping("/group")
    public Result<List<SignIn>> getGroupSignIn(@RequestParam String groupId) {
        return Result.success(signInService.getGroupSignIn(groupId));
    }
}
