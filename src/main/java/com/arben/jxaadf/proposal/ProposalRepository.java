package com.arben.jxaadf.proposal;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ProposalRepository {

    private final JdbcClient jdbcClient;

    public ProposalRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public String createProposal(Proposal proposal) {
        try {
            jdbcClient.sql(
                    "INSERT INTO proposal (tender_id, author_id, title, description, price, status, created_date) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)")
                    .params(proposal.getTenderId(), proposal.getAuthorId(), proposal.getTitle(),
                            proposal.getDescription(), proposal.getPrice(), proposal.getStatus(),
                            proposal.getCreatedDate())
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
                            + "description = ?, price = ?, status = ? WHERE proposal_id = ?")
                    .params(proposal.getTenderId(), proposal.getAuthorId(), proposal.getTitle(),
                            proposal.getDescription(), proposal.getPrice(), proposal.getStatus(),
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
                    .query(Proposal.class).list();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return List.of(); // Return empty list if there's an error
        }
    }

    public List<Proposal> getAllTenderProposals(int tenderId) {
        try {
            return jdbcClient.sql("SELECT * FROM proposal WHERE tender_id = ?").param(tenderId)
                    .query(Proposal.class).list();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return List.of(); // Return empty list if there's an error
        }
    }

    public Proposal getProposalById(int proposalId) {
        try {
            return jdbcClient.sql("SELECT * FROM proposal WHERE proposal_id = ?").param(proposalId)
                    .query(Proposal.class).optional().orElse(null);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return null;
        }
    }
}
