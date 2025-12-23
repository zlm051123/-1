package com.energy.smsplus.service.impl;

import com.energy.smsplus.entity.SignRecord;
import com.energy.smsplus.service.SignService;
import com.energy.smsplus.util.DateUtil;
import com.energy.smsplus.util.LocationDesensitizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.RowMapper;

//import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SignServiceImpl implements SignService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${sign.valid.duration:30}")
    private Integer defaultValidDuration;

    // 签到记录行映射器（将ResultSet转换为SignRecord对象）
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
    public boolean signIn(Long userId, Long courseId, String signCode, String location) {
        // 1. 参数校验
        if (userId == null || courseId == null || signCode == null || signCode.trim().isEmpty()) {
            return false;
        }

        // 2. 验证签到码是否有效
        String checkCodeSql = "SELECT COUNT(*) FROM course_sign_code WHERE course_id = ? AND sign_code = ? AND expire_time > NOW() AND status = 1";
        Integer codeCount = jdbcTemplate.queryForObject(checkCodeSql, new Object[]{courseId, signCode}, Integer.class);
        if (codeCount == null || codeCount == 0) {
            return false; // 签到码无效/已过期
        }

        // 3. 判断是否迟到
        String getCourseTimeSql = "SELECT start_time FROM course WHERE id = ?";
        Date courseStartTime = jdbcTemplate.queryForObject(getCourseTimeSql, new Object[]{courseId}, Date.class);
        int signStatus = DateUtil.isLate(courseStartTime) ? 2 : 1; // 1-成功 2-迟到

        // 4. 位置脱敏
        String desensitizedLocation = LocationDesensitizationUtil.desensitize(location);

        // 5. 检查是否已签到
        String checkSignSql = "SELECT COUNT(*) FROM sign_record WHERE user_id = ? AND course_id = ? AND sign_status IN (1,2)";
        Integer signCount = jdbcTemplate.queryForObject(checkSignSql, new Object[]{userId, courseId}, Integer.class);
        if (signCount != null && signCount > 0) {
            return false; // 已签到
        }

        // 6. 插入签到记录
        String insertSql = "INSERT INTO sign_record (user_id, course_id, sign_code, sign_status, sign_time, location) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(insertSql,
                    userId, courseId, signCode, signStatus,
                    new Date(), desensitizedLocation);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean signOut(Long userId, Long courseId) {
        if (userId == null || courseId == null) {
            return false;
        }
        String sql = "UPDATE sign_record SET sign_out_time = ? WHERE user_id = ? AND course_id = ? AND sign_out_time IS NULL";
        int affectedRows = jdbcTemplate.update(sql, new Date(), userId, courseId);
        return affectedRows > 0;
    }

    @Override
    public List<SignRecord> getSignRecords(Long userId) {
        if (userId == null) {
            return List.of();
        }
        String sql = "SELECT * FROM sign_record WHERE user_id = ? ORDER BY sign_time DESC";
        return jdbcTemplate.query(sql, new Object[]{userId}, signRecordRowMapper);
    }

    @Override
    public SignRecord getSignRecord(Long userId, Long courseId) {
        if (userId == null || courseId == null) {
            return null;
        }
        String sql = "SELECT * FROM sign_record WHERE user_id = ? AND course_id = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, courseId}, signRecordRowMapper);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String generateSignCode(Long courseId, Integer validDuration) {
        if (courseId == null) {
            return null;
        }
        // 使用UUID生成8位随机签到码
        String signCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        // 有效时长默认30分钟
        int duration = validDuration == null ? defaultValidDuration : validDuration;
        // 插入/更新签到码
        String sql = "REPLACE INTO course_sign_code (course_id, sign_code, create_time, expire_time, status) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL ? MINUTE), 1)";
        jdbcTemplate.update(sql, courseId, signCode, duration);
        return signCode;
    }

    @Override
    public boolean invalidateSignCode(Long courseId) {
        if (courseId == null) {
            return false;
        }
        String sql = "UPDATE course_sign_code SET status = 0, expire_time = NOW() WHERE course_id = ?";
        int affectedRows = jdbcTemplate.update(sql, courseId);
        return affectedRows > 0;
    }

    @Override
    public double getAttendanceRate(Long userId) {
        if (userId == null) {
            return 0.0;
        }
        // 总课程数
        String totalSql = "SELECT COUNT(DISTINCT course_id) FROM sign_record WHERE user_id = ?";
        Integer total = jdbcTemplate.queryForObject(totalSql, new Object[]{userId}, Integer.class);
        if (total == null || total == 0) {
            return 100.0;
        }
        // 签到成功数（成功+迟到）
        String successSql = "SELECT COUNT(DISTINCT course_id) FROM sign_record WHERE user_id = ? AND sign_status IN (1,2)";
        Integer success = jdbcTemplate.queryForObject(successSql, new Object[]{userId}, Integer.class);
        success = success == null ? 0 : success;
        // 计算到课率（保留两位小数）
        return Math.round(((double) success / total) * 10000) / 100.0;
    }
}
