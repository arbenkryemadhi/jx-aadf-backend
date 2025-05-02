package com.arben.jxaadf.tender;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Array;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class TenderRepository {

    private final JdbcClient jdbcClient;

    public TenderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // RowMapper to properly handle document_links array
    private final RowMapper<Tender> tenderRowMapper = (rs, rowNum) -> {
        Tender tender = new Tender(rs.getInt("tender_id"), rs.getString("title"),
                rs.getString("description"), rs.getString("status"), rs.getString("author_id"),
                rs.getString("created_date"), rs.getString("deadline"), rs.getString("budget"));

        // Handle the document_links array
        Array documentLinksArray = rs.getArray("document_links");
        if (documentLinksArray != null) {
            try {
                String[] links = (String[]) documentLinksArray.getArray();
                tender.setDocumentLinks(Arrays.asList(links));
            } catch (SQLException e) {
                tender.setDocumentLinks(new ArrayList<>());
            }
        } else {
            tender.setDocumentLinks(new ArrayList<>());
        }

        // Handle the assigned_aadf_staff array
        Array assignedStaffArray = rs.getArray("assigned_aadf_staff");
        if (assignedStaffArray != null) {
            try {
                String[] staffIds = (String[]) assignedStaffArray.getArray();
                tender.setAssignedAadfStaff(Arrays.asList(staffIds));
            } catch (SQLException e) {
                tender.setAssignedAadfStaff(new ArrayList<>());
            }
        } else {
            tender.setAssignedAadfStaff(new ArrayList<>());
        }

        return tender;
    };

    public int createTender(Tender tender) {
        String sql =
                """
                        INSERT INTO tender (title, description, status, author_id, created_date, deadline, budget, document_links, assigned_aadf_staff)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        RETURNING tender_id
                        """;

        // Execute the SQL and get the generated ID
        Integer tenderId = jdbcClient.sql(sql).param(tender.getTitle())
                .param(tender.getDescription()).param(tender.getStatus())
                .param(tender.getAuthorId()).param(tender.getCreatedDate())
                .param(tender.getDeadline()).param(tender.getBudget())
                .param(tender.getDocumentLinks() != null && !tender.getDocumentLinks().isEmpty()
                        ? tender.getDocumentLinks().toArray(new String[0])
                        : null)
                .param(tender.getAssignedAadfStaff() != null
                        && !tender.getAssignedAadfStaff().isEmpty()
                                ? tender.getAssignedAadfStaff().toArray(new String[0])
                                : null)
                .query(Integer.class).single();

        // Return the ID directly
        return tenderId;
    }

    public void endTender(int tenderId) {
        String sql = "UPDATE tender SET status = 'Ended' WHERE tender_id = ?";
        jdbcClient.sql(sql).param(tenderId).update();
    }

    public List<Tender> getAllActiveTenders() {
        String sql = "SELECT * FROM tender WHERE status = 'Active'";
        return jdbcClient.sql(sql).query(tenderRowMapper).list();
    }

    public void deleteTender(int tenderId) {
        String sql = "DELETE FROM tender WHERE tender_id = ?";
        jdbcClient.sql(sql).param(tenderId).update();
    }

    public List<Tender> searchTenders(String searchTerm) {
        String sql = "SELECT * FROM tender WHERE title ILIKE ? OR description ILIKE ?";
        return jdbcClient.sql(sql).param("%" + searchTerm + "%").param("%" + searchTerm + "%")
                .query(tenderRowMapper).list();
    }

    public Tender getTenderById(int tenderId) {
        String sql = "SELECT * FROM tender WHERE tender_id = ?";
        return jdbcClient.sql(sql).param(tenderId).query(tenderRowMapper).single();
    }

    public void updateTender(Tender tender) {
        String sql =
                """
                        UPDATE tender
                        SET title = ?, description = ?, status = ?, author_id = ?, created_date = ?, deadline = ?, budget = ?, document_links = ?, assigned_aadf_staff = ?
                        WHERE tender_id = ?
                        """;
        jdbcClient.sql(sql).param(tender.getTitle()).param(tender.getDescription())
                .param(tender.getStatus()).param(tender.getAuthorId())
                .param(tender.getCreatedDate()).param(tender.getDeadline())
                .param(tender.getBudget())
                .param(tender.getDocumentLinks() != null && !tender.getDocumentLinks().isEmpty()
                        ? tender.getDocumentLinks().toArray(new String[0])
                        : null)
                .param(tender.getAssignedAadfStaff() != null
                        && !tender.getAssignedAadfStaff().isEmpty()
                                ? tender.getAssignedAadfStaff().toArray(new String[0])
                                : null)
                .param(tender.getTenderId()).update();
    }

    public void makeWinner(int tenderId, int proposalId) {
        String sql = "UPDATE tender SET winning_proposal_id = ? WHERE tender_id = ?";
        jdbcClient.sql(sql).param(proposalId).param(tenderId).update();
    }

    public List<Tender> getAllTenders() {
        String sql = "SELECT * FROM tender";
        return jdbcClient.sql(sql).query(tenderRowMapper).list();
    }

    public void addLink(int tenderId, String link) {
        String sql =
                "UPDATE tender SET document_links = array_append(document_links, ?) WHERE tender_id = ?";
        jdbcClient.sql(sql).param(link).param(tenderId).update();
    }

    public void addStaffToTender(int tenderId, String staffId) {
        String sql =
                "UPDATE tender SET assigned_aadf_staff = array_append(assigned_aadf_staff, ?) WHERE tender_id = ?";
        jdbcClient.sql(sql).param(staffId).param(tenderId).update();
    }

    public void removeStaffFromTender(int tenderId, String staffId) {
        String sql =
                "UPDATE tender SET assigned_aadf_staff = array_remove(assigned_aadf_staff, ?) WHERE tender_id = ?";
        jdbcClient.sql(sql).param(staffId).param(tenderId).update();
    }

    public List<Tender> getTendersByStaffId(String staffId) {
        String sql = "SELECT * FROM tender WHERE ? = ANY(assigned_aadf_staff)";
        return jdbcClient.sql(sql).param(staffId).query(tenderRowMapper).list();
    }
}
