package com.energy.sign_system.service;

import com.energy.sign_system.entity.SignRecord;

import java.util.List;

public interface SignService {
    /**
     * 签到（扫码/手动输入课程码）
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param signCode 签到码/课程码
     * @param location 签到位置
     * @return 签到是否成功
     */
    boolean signIn(Long userId, Long courseId, String signCode, String location);

    /**
     * 签退
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 签退是否成功
     */
    boolean signOut(Long userId, Long courseId);

    /**
     * 获取用户签到记录
     * @param userId 用户ID
     * @return 签到记录列表
     */
    List<SignRecord> getSignRecords(Long userId);

    /**
     * 获取用户某课程的签到记录
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 签到记录
     */
    SignRecord getSignRecord(Long userId, Long courseId);

    /**
     * 生成课程签到码
     * @param courseId 课程ID
     * @param validDuration 有效时长（分钟）
     * @return 生成的签到码
     */
    String generateSignCode(Long courseId, Integer validDuration);

    /**
     * 失效签到码
     * @param courseId 课程ID
     * @return 是否成功
     */
    boolean invalidateSignCode(Long courseId);

    /**
     * 统计用户到课率
     * @param userId 用户ID
     * @return 到课率（0-100）
     */
    double getAttendanceRate(Long userId);
}
