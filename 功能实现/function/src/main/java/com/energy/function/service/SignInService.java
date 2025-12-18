package com.energy.function.service;

import com.energy.function.dto.UserSignInDTO;
import com.energy.function.entity.SignIn;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SignInService {

    //生成2FAS签到码
    String generateSignInCode();

    //用户签到
    SignIn userSignIn(UserSignInDTO dto);

    //查询组内签到情况
    List<SignIn> getGroupSignIn(String groupId);
}
