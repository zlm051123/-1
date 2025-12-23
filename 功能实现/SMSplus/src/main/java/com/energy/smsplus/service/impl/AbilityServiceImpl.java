package com.energy.smsplus.service.impl;

import com.energy.smsplus.entity.AbilityModel;
import com.energy.smsplus.service.AbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Service
public class AbilityServiceImpl implements AbilityService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 能力模型行映射器
    private final RowMapper<AbilityModel> abilityModelRowMapper = new RowMapper<AbilityModel>() {
        @Override
        public AbilityModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            AbilityModel model = new AbilityModel();
            model.setId(rs.getLong("id"));
            model.setUserId(rs.getLong("user_id"));
            model.setTechnical(rs.getInt("technical"));
            model.setTeamwork(rs.getInt("teamwork"));
            model.setCreativity(rs.getInt("creativity"));
            model.setCommunication(rs.getInt("communication"));
            model.setResponsibility(rs.getInt("responsibility"));
            model.setUpdateTime(rs.getTimestamp("update_time"));
            return model;
        }
    };

    @Override
    public AbilityModel getAbilityModel(Long userId) {
        if (userId == null) {
            return null;
        }
        String sql = "SELECT * FROM ability_model WHERE user_id = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, abilityModelRowMapper);
        } catch (Exception e) {
            // 未初始化则自动初始化
            initAbilityModel(userId);
            return getAbilityModel(userId);
        }
    }

    @Override
    public boolean updateAbilityModel(AbilityModel abilityModel) {
        if (abilityModel == null || abilityModel.getUserId() == null) {
            return false;
        }
        // 校验能力值范围（0-100）
        validateAbilityValue(abilityModel);
        // 更新语句
        String sql = "REPLACE INTO ability_model (user_id, technical, teamwork, creativity, communication, responsibility, update_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    abilityModel.getUserId(),
                    abilityModel.getTechnical(),
                    abilityModel.getTeamwork(),
                    abilityModel.getCreativity(),
                    abilityModel.getCommunication(),
                    abilityModel.getResponsibility(),
                    new Date());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean initAbilityModel(Long userId) {
        if (userId == null) {
            return false;
        }
        // 默认初始值50
        AbilityModel model = new AbilityModel(userId, 50, 50, 50, 50, 50);
        return updateAbilityModel(model);
    }

    /**
     * 异步更新能力值（避免前端卡顿）
     */
    @Async
    @Override
    public void updateAbilityByCourse(Long userId, Long courseId, Integer score) {
        if (userId == null || courseId == null || score == null) {
            return;
        }
        // 获取当前能力模型
        AbilityModel model = getAbilityModel(userId);
        if (model == null) {
            initAbilityModel(userId);
            model = getAbilityModel(userId);
        }
        // 根据课程表现调整能力值（示例逻辑：技术能力+2，责任心+1）
        int newTechnical = Math.min(model.getTechnical() + 2, 100);
        int newResponsibility = Math.min(model.getResponsibility() + 1, 100);
        model.setTechnical(newTechnical);
        model.setResponsibility(newResponsibility);
        // 保存更新
        updateAbilityModel(model);
    }

    /**
     * 校验能力值范围（0-100）
     */
    private void validateAbilityValue(AbilityModel model) {
        model.setTechnical(clamp(model.getTechnical(), 0, 100));
        model.setTeamwork(clamp(model.getTeamwork(), 0, 100));
        model.setCreativity(clamp(model.getCreativity(), 0, 100));
        model.setCommunication(clamp(model.getCommunication(), 0, 100));
        model.setResponsibility(clamp(model.getResponsibility(), 0, 100));
    }

    /**
     * 数值范围限制
     */
    private int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
