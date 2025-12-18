package com.energy.function.service.impl;

import com.energy.function.dto.UserSignInDTO;
import com.energy.function.entity.SignIn;
import com.energy.function.entity.User;
import com.energy.function.repository.SignInRepository;
import com.energy.function.repository.UserRepository;
import com.energy.function.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    private final SignInRepository signInRepository;
    private final UserRepository userRepository;

//    public SignInServiceImpl(SignInRepository signInRepository, UserRepository userRepository) {
//        this.signInRepository = signInRepository;
//        this.userRepository = userRepository;
//    }

    @Override
    public String generateSignInCode() {
        //模拟2FAS签到码
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Override
    public SignIn userSignIn(UserSignInDTO dto) {
        //校验用户存在
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 构建签到记录
        SignIn signIn = new SignIn();
        signIn.setUserId(dto.getUserId());
//        signIn.setCourseId(dto.getCourseId());
        signIn.setSignInCode(dto.getSignInCode());
        signIn.setSeatNumber(dto.getSeatNumber());
        signIn.setSignInTime(LocalDateTime.now().toString());
        signIn.setGroupId(user.getGroupId());

        return signInRepository.save(signIn);
    }

    @Override
    public List<SignIn> getGroupSignIn(String groupId) {
        return signInRepository.findByGroupId(groupId);
    }

}
