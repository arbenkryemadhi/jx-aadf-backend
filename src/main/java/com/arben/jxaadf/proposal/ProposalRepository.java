package com.arben.jxaadf.proposal;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Array;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProposalRepository {

    private final JdbcClient jdbcClient;

    public ProposalRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // RowMapper to properly handle document_links array
    private final RowMapper<Proposal> proposalRowMapper = (rs, rowNum) -> {
        Proposal proposal = new Proposal(rs.getInt("proposal_id"), rs.getInt("tender_id"),
                rs.getString("author_id"), rs.getString("title"), rs.getString("description"),
                rs.getString("price"), rs.getString("status"), rs.getString("created_date"));

        // Handle the document_links array
        Array documentLinksArray = rs.getArray("documents_links");
        if (documentLinksArray != null) {
            try {
                String[] links = (String[]) documentLinksArray.getArray();
                proposal.setDocumentLinks(links != null ? Arrays.asList(links) : new ArrayList<>());
            } catch (SQLException e) {
                proposal.setDocumentLinks(new ArrayList<>());
            }
        } else {
            proposal.setDocumentLinks(new ArrayList<>());
        }

        return proposal;
    };

    public String createProposal(Proposal proposal) {
        try {
            jdbcClient.sql(
                    "INSERT INTO proposal (tender_id, author_id, title, description, price, status, created_date, documents_links) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                    .params(proposal.getTenderId(), proposal.getAuthorId(), proposal.getTitle(),
                            proposal.getDescription(), proposal.getPrice(), proposal.getStatus(),
                            proposal.getCreatedDate(),
                            proposal.getDocumentLinks() != null
                                    && !proposal.getDocumentLinks().isEmpty()
                                            ? proposal.getDocumentLinks().toArray(new String[0])
                                            : null)
                    .update();
            return "Proposal created successfully";
        } catch (Exception e) {
            return "Error creating proposal: " + e.getMessage();
        }
    }

    public String updateProposal(Proposal proposal) {
        try {
            int rowsAffected = jdbcClient
                    .sql("UPDATE proposal SET tender_id = ?, author_id = ?, title = ?, "
                            + "description = ?, price = ?, status = ?, documents_links = ? WHERE proposal_id = ?")
                    .params(proposal.getTenderId(), proposal.getAuthorId(), proposal.getTitle(),
                            proposal.getDescription(), proposal.getPrice(), proposal.getStatus(),
                            proposal.getDocumentLinks() != null
                                    && !proposal.getDocumentLinks().isEmpty()
                                            ? proposal.getDocumentLinks().toArray(new String[0])
                                            : null,
                            proposal.getProposalId())
                    .update();

            if (rowsAffected > 0) {
                return "Proposal updated successfully";
            } else {
                return "Proposal not found";
            }
        } catch (Exception e) {
            return "Error updating proposal: " + e.getMessage();
        }
    }

    public String deleteProposal(int proposalId) {
        try {
            int rowsAffected = jdbcClient.sql("DELETE FROM proposal WHERE proposal_id = ?")
                    .param(proposalId).update();

            if (rowsAffected > 0) {
                return "Proposal deleted successfully";
            } else {
                return "Proposal not found";
            }
        } catch (Exception e) {
            return "Error deleting proposal: " + e.getMessage();
        }
    }

    public List<Proposal> getAllUserProposals(String userId) {
        try {
            return jdbcClient.sql("SELECT * FROM proposal WHERE author_id = ?").param(userId)
                    .query(proposalRowMapper).list();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return List.of(); // Return empty list if there's an error
        }
    }

    public List<Proposal> getAllTenderProposals(int tenderId) {
        try {
            return jdbcClient.sql("SELECT * FROM proposal WHERE tender_id = ?").param(tenderId)
                    .query(proposalRowMapper).list();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return List.of(); // Return empty list if there's an error
        }
    }

    public Proposal getProposalById(int proposalId) {
        try {
            return jdbcClient.sql("SELECT * FROM proposal WHERE proposal_id = ?").param(proposalId)
                    .query(proposalRowMapper).optional().orElse(null);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return null;
        }
    }

    public String addDocumentLink(int proposalId, String link) {
        try {
            int rowsAffected = jdbcClient.sql(
                    "UPDATE proposal SET documents_links = array_append(documents_links, ?) WHERE proposal_id = ?")
                    .params(link, proposalId).update();

            if (rowsAffected > 0) {
                return "Document link added successfully";
            } else {
                return "Proposal not found";
            }
        } catch (Exception e) {
            return "Error adding document link: " + e.getMessage();
        }
    }

    public String removeDocumentLink(int proposalId, String link) {
        try {
            int rowsAffected = jdbcClient.sql(
                    "UPDATE proposal SET documents_links = array_remove(documents_links, ?) WHERE proposal_id = ?")
                    .params(link, proposalId).update();

            if (rowsAffected > 0) {
                return "Document link removed successfully";
            } else {
                return "Proposal not found or link doesn't exist";
            }
        } catch (Exception e) {
            return "Error removing document link: " + e.getMessage();
        }
    }
}
