package com.arben.jxaadf.proposalreview;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ProposalReviewTest {

    @Test
    public void testNoArgsConstructor() {
        // Act
        ProposalReview proposalReview = new ProposalReview();

        // Assert
        assertNotNull(proposalReview);
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        int proposalReviewId = 1;
        int proposalId = 2;
        String authorId = "user123";
        String title = "Test Review";
        String description = "This is a test review";
        String createdDate = "2025-05-01";
        int humanScore = 85;

        // Act
        ProposalReview proposalReview = new ProposalReview(proposalReviewId, proposalId, authorId,
                title, description, createdDate, humanScore);

        // Assert
        assertEquals(proposalReviewId, proposalReview.getProposalReviewId());
        assertEquals(proposalId, proposalReview.getProposalId());
        assertEquals(authorId, proposalReview.getAuthorId());
        assertEquals(title, proposalReview.getTitle());
        assertEquals(description, proposalReview.getDescription());
        assertEquals(createdDate, proposalReview.getCreatedDate());
        assertEquals(humanScore, proposalReview.getHumanScore());
    }

    @Test
    public void testSetters() {
        // Arrange
        ProposalReview proposalReview = new ProposalReview();
        int proposalReviewId = 1;
        int proposalId = 2;
        String authorId = "user123";
        String title = "Test Review";
        String description = "This is a test review";
        String createdDate = "2025-05-01";
        int humanScore = 90;

        // Act
        proposalReview.setProposalReviewId(proposalReviewId);
        proposalReview.setProposalId(proposalId);
        proposalReview.setAuthorId(authorId);
        proposalReview.setTitle(title);
        proposalReview.setDescription(description);
        proposalReview.setCreatedDate(createdDate);
        proposalReview.setHumanScore(humanScore);

        // Assert
        assertEquals(proposalReviewId, proposalReview.getProposalReviewId());
        assertEquals(proposalId, proposalReview.getProposalId());
        assertEquals(authorId, proposalReview.getAuthorId());
        assertEquals(title, proposalReview.getTitle());
        assertEquals(description, proposalReview.getDescription());
        assertEquals(createdDate, proposalReview.getCreatedDate());
        assertEquals(humanScore, proposalReview.getHumanScore());
    }

    @Test
    public void testHumanScoreGetterSetter() {
        // Arrange
        ProposalReview proposalReview = new ProposalReview();
        int humanScore = 95;

        // Act
        proposalReview.setHumanScore(humanScore);

        // Assert
        assertEquals(humanScore, proposalReview.getHumanScore());
    }
}
