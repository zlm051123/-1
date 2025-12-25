package com.energy.sign_system.service.impl;

import com.energy.sign_system.entity.SignRecord;
import com.energy.sign_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 签到记录行映射器
    private final RowMapper<SignRecord> signRecordRowMapper = new RowMapper<SignRecord>() {
        @Override
        public SignRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            SignRecord record = new SignRecord();
            record.setId(rs.getLong("id"));
            record.setUserId(rs.getLong("user_id"));
            record.setCourseId(rs.getLong("course_id"));
            record.setSignCode(rs.getString("sign_code"));
            record.setSignStatus(rs.getInt("sign_status"));
            record.setSignTime(rs.getTimestamp("sign_time"));
            record.setSignOutTime(rs.getTimestamp("sign_out_time"));
            record.setLocation(rs.getString("location"));
            record.setRemark(rs.getString("remark"));
            return record;
        }
    };

    @Override
    public List<SignRecord> exportSignData(Long courseId) {
        String sql;
        Object[] params;
        if (courseId == null) {
            sql = "SELECT * FROM sign_record ORDER BY course_id, sign_time";
            params = new Object[]{};
        } else {
            sql = "SELECT * FROM sign_record WHERE course_id = ? ORDER BY sign_time";
            params = new Object[]{courseId};
        }
        return jdbcTemplate.query(sql, params, signRecordRowMapper);
    }

    @Override
    public boolean manualSign(Long userId, Long courseId, Integer signStatus) {
        if (userId == null || courseId == null || signStatus == null) {
            return false;
        }
        // 校验签到状态（0-未签到 1-成功 2-迟到 3-失败）
        if (signStatus < 0 || signStatus > 3) {
            return false;
        }
        // 插入/更新补签记录
        String sql = "REPLACE INTO sign_record (user_id, course_id, sign_code, sign_status, sign_time, remark) VALUES (?, ?, 'MANUAL', ?, ?, '管理员补签')";
        try {
            jdbcTemplate.update(sql, userId, courseId, signStatus, new Date());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, Integer> getSignHeatMap() {
        Map<String, Integer> heatMap = new HashMap<>();
        // 按教室统计签到人数
        String sql = "SELECT c.classroom, COUNT(DISTINCT s.user_id) AS count " +
                "FROM sign_record s " +
                "JOIN course c ON s.course_id = c.id " +
                "WHERE s.sign_status IN (1,2) " +
                "GROUP BY c.classroom";
        jdbcTemplate.query(sql, (ResultSet rs) -> {
            String classroom = rs.getString("classroom");
            Integer count = rs.getInt("count");
            heatMap.put(classroom, count);
        });
        return heatMap;
    }

    @Override
    public double getRealTimeAttendance(Long courseId) {
        if (courseId == null) {
            return 0.0;
        }
        // 总应到人数（课程关联的用户数）
        String totalSql = "SELECT COUNT(DISTINCT user_id) FROM user_course_relation WHERE course_id = ?";
        Integer total = jdbcTemplate.queryForObject(totalSql, new Object[]{courseId}, Integer.class);
        if (total == null || total == 0) {
            return 0.0;
        }
        // 实到人数（成功+迟到）
        String actualSql = "SELECT COUNT(DISTINCT user_id) FROM sign_record WHERE course_id = ? AND sign_status IN (1,2)";
        Integer actual = jdbcTemplate.queryForObject(actualSql, new Object[]{courseId}, Integer.class);
        actual = actual == null ? 0 : actual;
        // 计算到课率（保留两位小数）
        return Math.round(((double) actual / total) * 10000) / 100.0;
    }
}
