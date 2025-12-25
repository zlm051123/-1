package com.energy.sign_system.service;


import com.energy.sign_system.entity.StudyGroup;

import java.util.List;

public interface GroupService {
    /**
     * 创建学习小组
     * @param groupName 小组名称
     * @param leaderId 组长ID
     * @return 小组ID
     */
    Long createGroup(String groupName, Long leaderId);

    /**
     * 加入学习小组
     * @param groupId 小组ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean joinGroup(Long groupId, Long userId);

    /**
     * 退出学习小组
     * @param groupId 小组ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean quitGroup(Long groupId, Long userId);

    /**
     * 获取用户所在小组
     * @param userId 用户ID
     * @return 小组信息
     */
    StudyGroup getUserGroup(Long userId);

    /**
     * 获取小组排行榜（按到课率）
     * @return 小组列表（按到课率降序）
     */
    List<StudyGroup> getGroupRanking();
}