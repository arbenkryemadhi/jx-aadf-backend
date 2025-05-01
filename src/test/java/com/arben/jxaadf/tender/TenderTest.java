package com.arben.jxaadf.tender;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TenderTest {

    @Test
    public void testNoArgsConstructor() {
        // Act
        Tender tender = new Tender();

        // Assert
        assertNotNull(tender);
    }

    @Test
    public void testConstructorWithoutId() {
        // Arrange
        String title = "Test Tender";
        String description = "Tender Description";
        String status = "Active";
        String authorId = "test@example.com";
        String createdDate = "2025-05-01";
        String deadline = "2025-06-01";
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
        String deadline = "2025-06-01";
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
        String deadline = "2025-06-01";
        String budget = "10000 EUR";

        // Act
        tender.setTenderId(tenderId);
        tender.setTitle(title);
        tender.setDescription(description);
        tender.setStatus(status);
        tender.setAuthorId(authorId);
        tender.setCreatedDate(createdDate);
        tender.setDeadline(deadline);
        tender.setBudget(budget);

        // Assert
        assertEquals(tenderId, tender.getTenderId());
        assertEquals(title, tender.getTitle());
        assertEquals(description, tender.getDescription());
        assertEquals(status, tender.getStatus());
        assertEquals(authorId, tender.getAuthorId());
        assertEquals(createdDate, tender.getCreatedDate());
        assertEquals(deadline, tender.getDeadline());
        assertEquals(budget, tender.getBudget());
    }
}
