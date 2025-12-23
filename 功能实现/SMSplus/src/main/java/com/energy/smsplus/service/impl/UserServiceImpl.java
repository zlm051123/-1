package com.energy.smsplus.service.impl;

import com.energy.smsplus.entity.Achievement;
import com.energy.smsplus.entity.User;
import com.energy.smsplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 用户行映射器
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            user.setEmail(rs.getString("email"));
            user.setStatus(rs.getInt("status"));
            user.setCreateTime(rs.getTimestamp("create_time"));
            user.setUpdateTime(rs.getTimestamp("update_time"));
            return user;
        }
    };

    // 成就行映射器
    private final RowMapper<Achievement> achievementRowMapper = new RowMapper<Achievement>() {
        @Override
        public Achievement mapRow(ResultSet rs, int rowNum) throws SQLException {
            Achievement achievement = new Achievement();
            achievement.setId(rs.getLong("id"));
            achievement.setUserId(rs.getLong("user_id"));
            achievement.setAchievementName(rs.getString("achievement_name"));
            achievement.setAchievementIcon(rs.getString("achievement_icon"));
            achievement.setObtainTime(rs.getTimestamp("obtain_time"));
            achievement.setDescription(rs.getString("description"));
            return achievement;
        }
    };

    @Override
    public boolean register(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return false;
        }
        // 检查用户名是否重复
        String checkSql = "SELECT COUNT(*) FROM user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{user.getUsername()}, Integer.class);
        if (count != null && count > 0) {
            return false;
        }
        // 插入用户
        String insertSql = "INSERT INTO user (username, password, phone, email, status, create_time, update_time) VALUES (?, ?, ?, ?, 1, ?, ?)";
        try {
            jdbcTemplate.update(insertSql,
                    user.getUsername(),
                    user.getPassword(), // 实际开发需加密
                    user.getPhone(),
                    user.getEmail(),
                    new Date(),
                    new Date());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        String sql = "SELECT * FROM user WHERE id = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, userRowMapper);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Achievement> getUserAchievements(Long userId) {
        if (userId == null) {
            return List.of();
        }
        String sql = "SELECT * FROM achievement WHERE user_id = ? ORDER BY obtain_time DESC";
        return jdbcTemplate.query(sql, new Object[]{userId}, achievementRowMapper);
    }

    @Override
    public boolean grantAchievement(Long userId, String achievementName, String description) {
        if (userId == null || achievementName == null || achievementName.trim().isEmpty()) {
            return false;
        }
        // 检查是否已获取该成就
        String checkSql = "SELECT COUNT(*) FROM achievement WHERE user_id = ? AND achievement_name = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{userId, achievementName}, Integer.class);
        if (count != null && count > 0) {
            return false;
        }
        // 插入成就
        String insertSql = "INSERT INTO achievement (user_id, achievement_name, achievement_icon, obtain_time, description) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(insertSql,
                    userId,
                    achievementName,
                    "/icons/" + achievementName + ".png", // 示例图标路径
                    new Date(),
                    description);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
