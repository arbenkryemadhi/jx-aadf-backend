package com.arben.jxaadf.tender;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class TenderRepository {

    private final JdbcClient jdbcClient;

    public TenderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public int createTender(Tender tender) {
        String sql =
                """
                        INSERT INTO tender (title, description, status, author, created_date, deadline, budget)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        RETURNING tender_id
                        """;

        // Execute the SQL and get the generated ID
        Integer tenderId = jdbcClient.sql(sql).param(tender.getTitle())
                .param(tender.getDescription()).param(tender.getStatus()).param(tender.getAuthor())
                .param(tender.getCreatedDate()).param(tender.getDeadline())
                .param(tender.getBudget()).query(Integer.class).single();

        // Return the ID directly
        return tenderId;
    }

}
