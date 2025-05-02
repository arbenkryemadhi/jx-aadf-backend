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
        String sql = "SELECT COUNT(*) FROM staff WHERE email = ?";
        Integer count = jdbcClient.sql(sql).params(email).query(Integer.class).single();
        return count != null && count > 0;
    }
}
