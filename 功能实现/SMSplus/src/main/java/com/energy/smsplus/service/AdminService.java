package com.energy.smsplus.service;

import com.energy.smsplus.entity.SignRecord;

import java.util.List;
import java.util.Map;

public interface AdminService {
    /**
     * 批量导出签到数据
     * @param courseId 课程ID（null表示全部）
     * @return 签到数据列表
     */
    List<SignRecord> exportSignData(Long courseId);

    /**
     * 手动补签
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param signStatus 签到状态
     * @return 是否成功
     */
    boolean manualSign(Long userId, Long courseId, Integer signStatus);

    /**
     * 获取签到热力图数据
     * @return 热力图数据（key:教室/课程，value:签到人数）
     */
    Map<String, Integer> getSignHeatMap();

    /**
     * 获取实时到课率
     * @param courseId 课程ID
     * @return 到课率（0-100）
     */
    double getRealTimeAttendance(Long courseId);
}