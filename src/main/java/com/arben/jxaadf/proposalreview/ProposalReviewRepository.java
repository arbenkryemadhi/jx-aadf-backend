package com.arben.jxaadf.proposalreview;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProposalReviewRepository {

    private final JdbcClient jdbcClient;

    public ProposalReviewRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public String createProposalReview(ProposalReview proposalReview) {
        try {
            jdbcClient.sql(
                    "INSERT INTO proposal_review (proposal_id, author_id, title, description, created_date, human_score) "
                            + "VALUES (?, ?, ?, ?, ?, ?)")
                    .params(proposalReview.getProposalId(), proposalReview.getAuthorId(),
                            proposalReview.getTitle(), proposalReview.getDescription(),
                            proposalReview.getCreatedDate(), proposalReview.getHumanScore())
                    .update();
            return "Proposal review created successfully";
        } catch (Exception e) {
            return "Error creating proposal review: " + e.getMessage();
        }
    }

    public String updateProposalReview(ProposalReview proposalReview) {
        try {
            int rowsAffected = jdbcClient.sql("UPDATE proposal_review SET "
                    + "proposal_id = ?, author_id = ?, title = ?, description = ?, created_date = ?, human_score = ? "
                    + "WHERE proposal_review_id = ?")
                    .params(proposalReview.getProposalId(), proposalReview.getAuthorId(),
                            proposalReview.getTitle(), proposalReview.getDescription(),
                            proposalReview.getCreatedDate(), proposalReview.getHumanScore(),
                            proposalReview.getProposalReviewId())
                    .update();

            if (rowsAffected > 0) {
                return "Proposal review updated successfully";
            } else {
                return "No proposal review found with id: " + proposalReview.getProposalReviewId();
            }
        } catch (Exception e) {
            return "Error updating proposal review: " + e.getMessage();
        }
    }

    public String deleteProposalReview(int proposalReviewId) {
        try {
            int rowsAffected =
                    jdbcClient.sql("DELETE FROM proposal_review WHERE proposal_review_id = ?")
                            .param(proposalReviewId).update();

            if (rowsAffected > 0) {
                return "Proposal review deleted successfully";
            } else {
                return "No proposal review found with id: " + proposalReviewId;
            }
        } catch (Exception e) {
            return "Error deleting proposal review: " + e.getMessage();
        }
    }

    public ProposalReview getProposalReviewById(int proposalReviewId) {
        return jdbcClient.sql("SELECT * FROM proposal_review WHERE proposal_review_id = ?")
                .param(proposalReviewId)
                .query((rs, rowNum) -> new ProposalReview(rs.getInt("proposal_review_id"),
                        rs.getInt("proposal_id"), rs.getString("author_id"), rs.getString("title"),
                        rs.getString("description"), rs.getString("created_date"),
                        rs.getInt("human_score")))
                .optional().orElse(null);
    }

    public List<ProposalReview> getAllProposalReviewsOfProposal(int proposalId) {
        return jdbcClient.sql("SELECT * FROM proposal_review WHERE proposal_id = ?")
                .param(proposalId)
                .query((rs, rowNum) -> new ProposalReview(rs.getInt("proposal_review_id"),
                        rs.getInt("proposal_id"), rs.getString("author_id"), rs.getString("title"),
                        rs.getString("description"), rs.getString("created_date"),
                        rs.getInt("human_score")))
                .list();
    }

    public List<ProposalReview> getAllProposalReviewsOfUser(String userId) {
        return jdbcClient.sql("SELECT * FROM proposal_review WHERE author_id = ?").param(userId)
                .query((rs, rowNum) -> new ProposalReview(rs.getInt("proposal_review_id"),
                        rs.getInt("proposal_id"), rs.getString("author_id"), rs.getString("title"),
                        rs.getString("description"), rs.getString("created_date"),
                        rs.getInt("human_score")))
                .list();
    }

    public String updateHumanScore(int proposalReviewId, int humanScore) {
        try {
            int rowsAffected = jdbcClient
                    .sql("UPDATE proposal_review SET human_score = ? WHERE proposal_review_id = ?")
                    .params(humanScore, proposalReviewId).update();

            if (rowsAffected > 0) {
                return "Human score updated successfully";
            } else {
                return "No proposal review found with id: " + proposalReviewId;
            }
        } catch (Exception e) {
            return "Error updating human score: " + e.getMessage();
        }
    }
}
