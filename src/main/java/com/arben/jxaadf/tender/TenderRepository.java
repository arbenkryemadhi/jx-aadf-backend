package com.arben.jxaadf.tender;

import java.util.List;
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
                        INSERT INTO tender (title, description, status, author_id, created_date, deadline, budget)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        RETURNING tender_id
                        """;

        // Execute the SQL and get the generated ID
        Integer tenderId =
                jdbcClient.sql(sql).param(tender.getTitle()).param(tender.getDescription())
                        .param(tender.getStatus()).param(tender.getAuthorId())
                        .param(tender.getCreatedDate()).param(tender.getDeadline())
                        .param(tender.getBudget()).query(Integer.class).single();

        // Return the ID directly
        return tenderId;
    }

    public void endTender(int tenderId) {
        String sql = "UPDATE tender SET status = 'Ended' WHERE tender_id = ?";
        jdbcClient.sql(sql).param(tenderId).update();
    }

    public List<Tender> getAllActiveTenders() {
        String sql = "SELECT * FROM tender WHERE status = 'Active'";
        return jdbcClient.sql(sql).query(Tender.class).list();
    }

    public void deleteTender(int tenderId) {
        String sql = "DELETE FROM tender WHERE tender_id = ?";
        jdbcClient.sql(sql).param(tenderId).update();
    }

    public List<Tender> searchTenders(String searchTerm) {
        String sql = "SELECT * FROM tender WHERE title ILIKE ? OR description ILIKE ?";
        return jdbcClient.sql(sql).param("%" + searchTerm + "%").param("%" + searchTerm + "%")
                .query(Tender.class).list();
    }

    public Tender getTenderById(int tenderId) {
        String sql = "SELECT * FROM tender WHERE tender_id = ?";
        return jdbcClient.sql(sql).param(tenderId).query(Tender.class).single();
    }

    public void updateTender(Tender tender) {
        String sql =
                """
                        UPDATE tender
                        SET title = ?, description = ?, status = ?, author_id = ?, created_date = ?, deadline = ?, budget = ?
                        WHERE tender_id = ?
                        """;
        jdbcClient.sql(sql).param(tender.getTitle()).param(tender.getDescription())
                .param(tender.getStatus()).param(tender.getAuthorId())
                .param(tender.getCreatedDate()).param(tender.getDeadline())
                .param(tender.getBudget()).param(tender.getTenderId()).update();
    }

    public void makeWinner(int tenderId, int proposalId) {
        String sql = "UPDATE tender SET winning_proposal_id = ? WHERE tender_id = ?";
        jdbcClient.sql(sql).param(proposalId).param(tenderId).update();
    }

    public List<Tender> getAllTenders() {
        String sql = "SELECT * FROM tender";
        return jdbcClient.sql(sql).query(Tender.class).list();
    }

}
