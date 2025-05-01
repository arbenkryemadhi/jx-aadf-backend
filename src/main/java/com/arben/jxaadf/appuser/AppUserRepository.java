package com.arben.jxaadf.appuser;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class AppUserRepository {

    private final JdbcClient jdbcClient;

    public AppUserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void createAppUser(AppUser appUser) {
        String sql =
                "INSERT INTO app_user (app_user_id, first_name, last_name, email) VALUES (?, ?, ?, ?)";

        int noOfRowsAffected = jdbcClient.sql(sql).params(appUser.getAppUserId(),
                appUser.getFirstName(), appUser.getLastName(), appUser.getEmail()).update();

        Assert.state(noOfRowsAffected == 1, "Insert failed, no rows affected.");
    }

    public void deleteAppUser(String appUserId) {
        String sql = "DELETE FROM app_user WHERE app_user_id = ?";
        int noOfRowsAffected = jdbcClient.sql(sql).params(appUserId).update();
        Assert.state(noOfRowsAffected == 1, "Delete failed, no rows affected.");
    }

}
