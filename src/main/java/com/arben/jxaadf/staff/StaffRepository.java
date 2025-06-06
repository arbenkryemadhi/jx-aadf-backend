package com.arben.jxaadf.staff;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class StaffRepository {

    private final JdbcClient jdbcClient;

    public StaffRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean isStaff(String email) {
        String sql = "SELECT COUNT(*) FROM staff WHERE staff_email = ?";
        Integer count = jdbcClient.sql(sql).params(email).query(Integer.class).single();
        return count != null && count > 0;
    }


    public boolean isAdmin(String email) {
        String sql = "SELECT COUNT(*) FROM staff WHERE admin_email = ?";
        Integer count = jdbcClient.sql(sql).params(email).query(Integer.class).single();
        return count != null && count > 0;
    }

    public void createStaff(String email) {
        String sql = "INSERT INTO staff (staff_email) VALUES (?)";
        jdbcClient.sql(sql).params(email).update();
    }


    public void removeStaff(String email) {
        String sql = "DELETE FROM staff WHERE staff_email = ?";
        jdbcClient.sql(sql).params(email).update();
    }


    public List<String> getAllStaffEmails() {
        String sql = "SELECT staff_email FROM staff WHERE staff_email IS NOT NULL";
        return jdbcClient.sql(sql).query(String.class).list();
    }
}
