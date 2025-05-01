package com.arben.jxaadf.proposal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ProposalTest {

    @Test
    public void testNoArgsConstructor() {
        // Act
        Proposal proposal = new Proposal();

        // Assert
        assertNotNull(proposal);
    }

    @Test
    public void testConstructorWithoutId() {
        // Arrange
        int tenderId = 1;
        String authorId = "test@example.com";
        String title = "Test Proposal";
        String description = "Proposal Description";
        String price = "5000 EUR";
        String status = "Pending";
        String createdDate = "2025-05-01";

        // Act
        Proposal proposal =
                new Proposal(tenderId, authorId, title, description, price, status, createdDate);

        // Assert
        assertEquals(tenderId, proposal.getTenderId());
        assertEquals(authorId, proposal.getAuthorId());
        assertEquals(title, proposal.getTitle());
        assertEquals(description, proposal.getDescription());
        assertEquals(price, proposal.getPrice());
        assertEquals(status, proposal.getStatus());
        assertEquals(createdDate, proposal.getCreatedDate());
    }

    @Test
    public void testConstructorWithId() {
        // Arrange
        int proposalId = 1;
        int tenderId = 1;
        String authorId = "test@example.com";
        String title = "Test Proposal";
        String description = "Proposal Description";
        String price = "5000 EUR";
        String status = "Pending";
        String createdDate = "2025-05-01";

        // Act
        Proposal proposal = new Proposal(proposalId, tenderId, authorId, title, description, price,
                status, createdDate);

        // Assert
        assertEquals(proposalId, proposal.getProposalId());
        assertEquals(tenderId, proposal.getTenderId());
        assertEquals(authorId, proposal.getAuthorId());
        assertEquals(title, proposal.getTitle());
        assertEquals(description, proposal.getDescription());
        assertEquals(price, proposal.getPrice());
        assertEquals(status, proposal.getStatus());
        assertEquals(createdDate, proposal.getCreatedDate());
    }

    @Test
    public void testSetters() {
        // Arrange
        Proposal proposal = new Proposal();
        int proposalId = 1;
        int tenderId = 1;
        String authorId = "test@example.com";
        String title = "Test Proposal";
        String description = "Proposal Description";
        String price = "5000 EUR";
        String status = "Pending";
        String createdDate = "2025-05-01";

        // Act
        proposal.setProposalId(proposalId);
        proposal.setTenderId(tenderId);
        proposal.setAuthorId(authorId);
        proposal.setTitle(title);
        proposal.setDescription(description);
        proposal.setPrice(price);
        proposal.setStatus(status);
        proposal.setCreatedDate(createdDate);

        // Assert
        assertEquals(proposalId, proposal.getProposalId());
        assertEquals(tenderId, proposal.getTenderId());
        assertEquals(authorId, proposal.getAuthorId());
        assertEquals(title, proposal.getTitle());
        assertEquals(description, proposal.getDescription());
        assertEquals(price, proposal.getPrice());
        assertEquals(status, proposal.getStatus());
        assertEquals(createdDate, proposal.getCreatedDate());
    }
}
