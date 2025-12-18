package com.energy.function.service;

import com.energy.function.entity.UserAbility;
import org.springframework.stereotype.Service;

@Service
public interface UserAbilityService {

    //保存/更新能力评分
    UserAbility saveUserAbility(Long userId, String algorithm, String security, String multilingual, String projectDev);


    //获取个人能力评分
    UserAbility getPersonalAbility(Long userId);
}
