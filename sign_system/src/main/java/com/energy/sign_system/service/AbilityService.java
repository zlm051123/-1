package com.energy.sign_system.service;

import com.energy.sign_system.entity.AbilityModel;

public interface AbilityService {
    /**
     * 获取用户能力模型
     * @param userId 用户ID
     * @return 能力模型
     */
    AbilityModel getAbilityModel(Long userId);

    /**
     * 更新用户能力模型
     * @param abilityModel 能力模型
     * @return 是否成功
     */
    boolean updateAbilityModel(AbilityModel abilityModel);

    /**
     * 初始化用户能力模型（默认值50）
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean initAbilityModel(Long userId);

    /**
     * 根据课程表现更新能力值
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param score 课程表现得分（0-100）
     */
    void updateAbilityByCourse(Long userId, Long courseId, Integer score);
}
