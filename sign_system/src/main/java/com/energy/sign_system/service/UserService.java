package com.energy.sign_system.service;

import com.energy.sign_system.entity.Achievement;
import com.energy.sign_system.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 注册用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean register(User user);

    /**
     * 根据ID获取用户
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);

    /**
     * 获取用户成就列表
     * @param userId 用户ID
     * @return 成就列表
     */
    List<Achievement> getUserAchievements(Long userId);

    /**
     * 发放成就徽章
     * @param userId 用户ID
     * @param achievementName 成就名称
     * @param description 成就描述
     * @return 是否成功
     */
    boolean grantAchievement(Long userId, String achievementName, String description);
}
