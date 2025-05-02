package com.arben.jxaadf.proposal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testConstructorWithDocumentLinks() {
        // Arrange
        int proposalId = 1;
        int tenderId = 1;
        String authorId = "test@example.com";
        String title = "Test Proposal";
        String description = "Proposal Description";
        String price = "5000 EUR";
        String status = "Pending";
        String createdDate = "2025-05-01";
        List<String> documentLinks =
                Arrays.asList("http://example.com/doc1", "http://example.com/doc2");

        // Act
        Proposal proposal = new Proposal(proposalId, tenderId, authorId, title, description, price,
                status, createdDate, documentLinks);

        // Assert
        assertEquals(proposalId, proposal.getProposalId());
        assertEquals(tenderId, proposal.getTenderId());
        assertEquals(authorId, proposal.getAuthorId());
        assertEquals(title, proposal.getTitle());
        assertEquals(description, proposal.getDescription());
        assertEquals(price, proposal.getPrice());
        assertEquals(status, proposal.getStatus());
        assertEquals(createdDate, proposal.getCreatedDate());
        assertEquals(documentLinks, proposal.getDocumentLinks());
        assertEquals(2, proposal.getDocumentLinks().size());
    }

    @Test
    public void testConstructorWithNullDocumentLinks() {
        // Act
        Proposal proposal = new Proposal(1, 1, "author", "title", "description", "price", "status",
                "date", null);

        // Assert
        assertNotNull(proposal.getDocumentLinks());
        assertEquals(0, proposal.getDocumentLinks().size());
    }

    @Test
    public void testDocumentLinksInitializedInAllConstructors() {
        // Act
        Proposal proposal1 = new Proposal(); // No-args constructor
        Proposal proposal2 =
                new Proposal(1, "author", "title", "description", "price", "status", "date"); // Without
                                                                                              // ID
        Proposal proposal3 =
                new Proposal(1, 1, "author", "title", "description", "price", "status", "date"); // With
                                                                                                 // ID

        // Assert
        assertNotNull(proposal1.getDocumentLinks());
        assertNotNull(proposal2.getDocumentLinks());
        assertNotNull(proposal3.getDocumentLinks());
    }

    @Test
    public void testSetDocumentLinks() {
        // Arrange
        Proposal proposal = new Proposal();
        List<String> documentLinks =
                Arrays.asList("http://example.com/doc1", "http://example.com/doc2");

        // Act
        proposal.setDocumentLinks(documentLinks);

        // Assert
        assertEquals(documentLinks, proposal.getDocumentLinks());
        assertEquals(2, proposal.getDocumentLinks().size());
    }

    @Test
    public void testSetDocumentLinksWithNull() {
        // Arrange
        Proposal proposal = new Proposal();

        // Act
        proposal.setDocumentLinks(null);

        // Assert
        assertNotNull(proposal.getDocumentLinks());
        assertEquals(0, proposal.getDocumentLinks().size());
    }

    @Test
    public void testAddDocumentLink() {
        // Arrange
        Proposal proposal = new Proposal();
        String link = "http://example.com/doc";

        // Act
        proposal.addDocumentLink(link);

        // Assert
        assertEquals(1, proposal.getDocumentLinks().size());
        assertEquals(link, proposal.getDocumentLinks().get(0));
    }

    @Test
    public void testAddMultipleDocumentLinks() {
        // Arrange
        Proposal proposal = new Proposal();
        String link1 = "http://example.com/doc1";
        String link2 = "http://example.com/doc2";

        // Act
        proposal.addDocumentLink(link1);
        proposal.addDocumentLink(link2);

        // Assert
        assertEquals(2, proposal.getDocumentLinks().size());
        assertTrue(proposal.getDocumentLinks().contains(link1));
        assertTrue(proposal.getDocumentLinks().contains(link2));
    }

    @Test
    public void testAddNullOrEmptyDocumentLink() {
        // Arrange
        Proposal proposal = new Proposal();

        // Act
        proposal.addDocumentLink(null);
        proposal.addDocumentLink("");

        // Assert
        assertEquals(0, proposal.getDocumentLinks().size());
    }

    @Test
    public void testRemoveDocumentLink() {
        // Arrange
        Proposal proposal = new Proposal();
        String link = "http://example.com/doc";
        proposal.addDocumentLink(link);

        // Act
        proposal.removeDocumentLink(link);

        // Assert
        assertEquals(0, proposal.getDocumentLinks().size());
    }

    @Test
    public void testRemoveNonExistentDocumentLink() {
        // Arrange
        Proposal proposal = new Proposal();
        String link1 = "http://example.com/doc1";
        String link2 = "http://example.com/doc2";
        proposal.addDocumentLink(link1);

        // Act
        proposal.removeDocumentLink(link2);

        // Assert
        assertEquals(1, proposal.getDocumentLinks().size());
        assertEquals(link1, proposal.getDocumentLinks().get(0));
    }
}
