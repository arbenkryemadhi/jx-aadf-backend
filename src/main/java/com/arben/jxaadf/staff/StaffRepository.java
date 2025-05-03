package com.arben.jxaadf.staff;

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
}
