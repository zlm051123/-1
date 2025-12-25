package com.energy.sign_system.service.impl;

import com.energy.sign_system.entity.StudyGroup;
import com.energy.sign_system.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 学习小组行映射器
    private final RowMapper<StudyGroup> studyGroupRowMapper = new RowMapper<StudyGroup>() {
        @Override
        public StudyGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            StudyGroup group = new StudyGroup();
            group.setId(rs.getLong("id"));
            group.setGroupName(rs.getString("group_name"));
            group.setLeaderId(rs.getLong("leader_id"));
            group.setMemberCount(rs.getInt("member_count"));
            group.setCreateTime(rs.getTimestamp("create_time"));
            group.setStatus(rs.getInt("status"));
            return group;
        }
    };

    @Override
    public Long createGroup(String groupName, Long leaderId) {
        if (groupName == null || groupName.trim().isEmpty() || leaderId == null) {
            return null;
        }
        // 插入小组
        String insertSql = "INSERT INTO study_group (group_name, leader_id, member_count, create_time, status) VALUES (?, ?, 1, ?, 1)";
        try {
            jdbcTemplate.update(insertSql, groupName, leaderId, new Date());
            // 获取自增ID
            String getIdSql = "SELECT LAST_INSERT_ID()";
            return jdbcTemplate.queryForObject(getIdSql, Long.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean joinGroup(Long groupId, Long userId) {
        if (groupId == null || userId == null) {
            return false;
        }
        // 1. 检查小组是否存在且正常
        String checkGroupSql = "SELECT COUNT(*) FROM study_group WHERE id = ? AND status = 1";
        Integer groupCount = jdbcTemplate.queryForObject(checkGroupSql, new Object[]{groupId}, Integer.class);
        if (groupCount == null || groupCount == 0) {
            return false;
        }
        // 2. 检查用户是否已加入其他小组
        String checkUserSql = "SELECT COUNT(*) FROM user_group_relation WHERE user_id = ? AND status = 1";
        Integer userCount = jdbcTemplate.queryForObject(checkUserSql, new Object[]{userId}, Integer.class);
        if (userCount != null && userCount > 0) {
            return false;
        }
        // 3. 插入关联关系
        String insertRelSql = "INSERT INTO user_group_relation (user_id, group_id, join_time, status) VALUES (?, ?, ?, 1)";
        // 4. 更新小组人数
        String updateCountSql = "UPDATE study_group SET member_count = member_count + 1 WHERE id = ?";
        try {
            jdbcTemplate.update(insertRelSql, userId, groupId, new Date());
            jdbcTemplate.update(updateCountSql, groupId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean quitGroup(Long groupId, Long userId) {
        if (groupId == null || userId == null) {
            return false;
        }
        // 1. 标记关联关系为失效
        String updateRelSql = "UPDATE user_group_relation SET status = 0, quit_time = ? WHERE user_id = ? AND group_id = ? AND status = 1";
        // 2. 更新小组人数
        String updateCountSql = "UPDATE study_group SET member_count = member_count - 1 WHERE id = ? AND member_count > 1";
        try {
            jdbcTemplate.update(updateRelSql, new Date(), userId, groupId);
            jdbcTemplate.update(updateCountSql, groupId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public StudyGroup getUserGroup(Long userId) {
        if (userId == null) {
            return null;
        }
        String sql = "SELECT g.* FROM study_group g " +
                "JOIN user_group_relation r ON g.id = r.group_id " +
                "WHERE r.user_id = ? AND r.status = 1 AND g.status = 1 LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, studyGroupRowMapper);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<StudyGroup> getGroupRanking() {
        // 按到课率降序（示例：关联签到表计算）
        String sql = "SELECT g.*, " +
                "(SELECT COUNT(DISTINCT r.user_id) FROM user_group_relation r " +
                "JOIN sign_record s ON r.user_id = s.user_id " +
                "WHERE r.group_id = g.id AND s.sign_status IN (1,2)) / g.member_count * 100 AS attendance_rate " +
                "FROM study_group g WHERE g.status = 1 " +
                "ORDER BY attendance_rate DESC";
        return jdbcTemplate.query(sql, studyGroupRowMapper);
    }
}
