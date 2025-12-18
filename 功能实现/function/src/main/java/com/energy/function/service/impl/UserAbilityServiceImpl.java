package com.energy.function.service.impl;

import com.energy.function.entity.UserAbility;
import com.energy.function.repository.UserAbilityRepository;
import com.energy.function.repository.UserRepository;
import com.energy.function.service.UserAbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAbilityServiceImpl implements UserAbilityService {
    private final UserAbilityRepository userAbilityRepository;

    @Override
    public UserAbility saveUserAbility(Long userId, String algorithm, String security, String multilingual, String projectDev) {
        Optional<UserAbility> existing = userAbilityRepository.findByUserId(userId);
        UserAbility ability = existing.orElse(new UserAbility());
        ability.setUserId(userId);
        ability.setAlgorithmAbility(algorithm);
        ability.setSecurityAbility(security);
        ability.setMultilingualAbility(multilingual);
        ability.setProjectDevAbility(projectDev);
        return userAbilityRepository.save(ability);
    }

    @Override
    public UserAbility getPersonalAbility(Long userId) {
        return userAbilityRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("暂无能力评分"));
    }
}