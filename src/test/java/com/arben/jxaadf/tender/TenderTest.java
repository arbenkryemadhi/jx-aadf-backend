package com.arben.jxaadf.tender;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TenderTest {

    @Test
    public void testNoArgsConstructor() {
        // Act
        Tender tender = new Tender();

        // Assert
        assertNotNull(tender);
        assertNotNull(tender.getDocumentLinks(),
                "documentLinks should be initialized to empty list");
        assertTrue(tender.getDocumentLinks().isEmpty(), "documentLinks should be empty");
    }

    @Test
    public void testConstructorWithoutId() {
        // Arrange
        String title = "Test Tender";
        String description = "Tender Description";
        String status = "Active";
        String authorId = "test@example.com";
        String createdDate = "2025-05-01";
        String deadline = "2025-05-31";
        String budget = "10000 EUR";

        // Act
        Tender tender =
                new Tender(title, description, status, authorId, createdDate, deadline, budget);

        // Assert
        assertEquals(title, tender.getTitle());
        assertEquals(description, tender.getDescription());
        assertEquals(status, tender.getStatus());
        assertEquals(authorId, tender.getAuthorId());
        assertEquals(createdDate, tender.getCreatedDate());
        assertEquals(deadline, tender.getDeadline());
        assertEquals(budget, tender.getBudget());
        assertNotNull(tender.getDocumentLinks(), "documentLinks should be initialized");
        assertTrue(tender.getDocumentLinks().isEmpty(), "documentLinks should be empty");
    }

    @Test
    public void testConstructorWithId() {
        // Arrange
        int tenderId = 1;
        String title = "Test Tender";
        String description = "Tender Description";
        String status = "Active";
        String authorId = "test@example.com";
        String createdDate = "2025-05-01";
        String deadline = "2025-05-31";
        String budget = "10000 EUR";

        // Act
        Tender tender = new Tender(tenderId, title, description, status, authorId, createdDate,
                deadline, budget);

        // Assert
        assertEquals(tenderId, tender.getTenderId());
        assertEquals(title, tender.getTitle());
        assertEquals(description, tender.getDescription());
        assertEquals(status, tender.getStatus());
        assertEquals(authorId, tender.getAuthorId());
        assertEquals(createdDate, tender.getCreatedDate());
        assertEquals(deadline, tender.getDeadline());
        assertEquals(budget, tender.getBudget());
        assertNotNull(tender.getDocumentLinks(), "documentLinks should be initialized");
        assertTrue(tender.getDocumentLinks().isEmpty(), "documentLinks should be empty");
    }

    @Test
    public void testConstructorWithAllFields() {
        // Arrange
        int tenderId = 1;
        String title = "Test Tender";
        String description = "Tender Description";
        String status = "Active";
        String authorId = "test@example.com";
        String createdDate = "2025-05-01";
        String deadline = "2025-05-31";
        String budget = "10000 EUR";
        List<String> documentLinks =
                Arrays.asList("http://example.com/doc1", "http://example.com/doc2");

        // Act
        Tender tender = new Tender(tenderId, title, description, status, authorId, createdDate,
                deadline, budget, documentLinks);

        // Assert
        assertEquals(tenderId, tender.getTenderId());
        assertEquals(title, tender.getTitle());
        assertEquals(description, tender.getDescription());
        assertEquals(status, tender.getStatus());
        assertEquals(authorId, tender.getAuthorId());
        assertEquals(createdDate, tender.getCreatedDate());
        assertEquals(deadline, tender.getDeadline());
        assertEquals(budget, tender.getBudget());
        assertEquals(documentLinks, tender.getDocumentLinks());
        assertEquals(2, tender.getDocumentLinks().size());
    }

    @Test
    public void testDocumentLinksGetterAndSetter() {
        // Arrange
        Tender tender = new Tender();
        List<String> documentLinks =
                Arrays.asList("http://example.com/doc1", "http://example.com/doc2");

        // Act
        tender.setDocumentLinks(documentLinks);

        // Assert
        assertEquals(documentLinks, tender.getDocumentLinks());
        assertEquals(2, tender.getDocumentLinks().size());
    }

    @Test
    public void testSetDocumentLinksWithNull() {
        // Arrange
        Tender tender = new Tender();

        // Act
        tender.setDocumentLinks(null);

        // Assert
        assertNotNull(tender.getDocumentLinks(), "documentLinks should not be null");
        assertTrue(tender.getDocumentLinks().isEmpty(), "documentLinks should be empty");
    }

    @Test
    public void testAddDocumentLink() {
        // Arrange
        Tender tender = new Tender();
        String link = "http://example.com/doc1";

        // Act
        tender.addDocumentLink(link);

        // Assert
        assertTrue(tender.getDocumentLinks().contains(link));
        assertEquals(1, tender.getDocumentLinks().size());
    }

    @Test
    public void testAddMultipleDocumentLinks() {
        // Arrange
        Tender tender = new Tender();
        String link1 = "http://example.com/doc1";
        String link2 = "http://example.com/doc2";

        // Act
        tender.addDocumentLink(link1);
        tender.addDocumentLink(link2);

        // Assert
        assertEquals(2, tender.getDocumentLinks().size());
        assertTrue(tender.getDocumentLinks().containsAll(Arrays.asList(link1, link2)));
    }

    @Test
    public void testAddNullOrEmptyDocumentLink() {
        // Arrange
        Tender tender = new Tender();

        // Act
        tender.addDocumentLink(null);
        tender.addDocumentLink("");

        // Assert
        assertTrue(tender.getDocumentLinks().isEmpty());
    }

    @Test
    public void testRemoveDocumentLink() {
        // Arrange
        Tender tender = new Tender();
        String link1 = "http://example.com/doc1";
        String link2 = "http://example.com/doc2";
        tender.setDocumentLinks(new ArrayList<>(Arrays.asList(link1, link2)));

        // Act
        tender.removeDocumentLink(link1);

        // Assert
        assertEquals(1, tender.getDocumentLinks().size());
        assertFalse(tender.getDocumentLinks().contains(link1));
        assertTrue(tender.getDocumentLinks().contains(link2));
    }

    @Test
    public void testSetters() {
        // Arrange
        Tender tender = new Tender();
        int tenderId = 1;
        String title = "Test Tender";
        String description = "Tender Description";
        String status = "Active";
        String authorId = "test@example.com";
        String createdDate = "2025-05-01";
        String deadline = "2025-05-31";
        String budget = "10000 EUR";
        List<String> documentLinks =
                Arrays.asList("http://example.com/doc1", "http://example.com/doc2");

        // Act
        tender.setTenderId(tenderId);
        tender.setTitle(title);
        tender.setDescription(description);
        tender.setStatus(status);
        tender.setAuthorId(authorId);
        tender.setCreatedDate(createdDate);
        tender.setDeadline(deadline);
        tender.setBudget(budget);
        tender.setDocumentLinks(documentLinks);

        // Assert
        assertEquals(tenderId, tender.getTenderId());
        assertEquals(title, tender.getTitle());
        assertEquals(description, tender.getDescription());
        assertEquals(status, tender.getStatus());
        assertEquals(authorId, tender.getAuthorId());
        assertEquals(createdDate, tender.getCreatedDate());
        assertEquals(deadline, tender.getDeadline());
        assertEquals(budget, tender.getBudget());
        assertEquals(documentLinks, tender.getDocumentLinks());
    }
}
