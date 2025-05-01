package com.arben.jxaadf.tender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TenderRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    private TenderRepository tenderRepository;
    private Tender testTender;

    @BeforeEach
    void setUp() {
        tenderRepository = new TenderRepository(jdbcClient);
        testTender = new Tender(1, "Test Tender", "Test Description", "Active", "test@example.com",
                "2025-05-01", "2025-06-01", "10000 EUR");
    }

    @Test
    void createTender_VerifySqlExecution() {
        // This is a partial test that focuses on verifying the SQL contains the expected text
        // We can't easily mock the full chain with fluent API in this test framework
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            // Execute method (will throw NullPointerException due to mock chain)
            tenderRepository.createTender(testTender);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected due to mock limitations
            // We can still verify the SQL was called
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            // Verify SQL contains the expected text
            assertTrue(capturedSql.contains("INSERT INTO tender"));
            assertTrue(capturedSql.contains("VALUES"));
            assertTrue(capturedSql.contains("RETURNING tender_id"));
        }
    }

    @Test
    void endTender_VerifySqlExecution() {
        // Similar partial test focusing on SQL verification
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;

        try {
            tenderRepository.endTender(tenderId);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("UPDATE tender SET status = 'Ended'"));
            assertTrue(capturedSql.contains("WHERE tender_id = ?"));
        }
    }

    @Test
    void getAllActiveTenders_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            tenderRepository.getAllActiveTenders();
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("SELECT * FROM tender"));
            assertTrue(capturedSql.contains("WHERE status = 'Active'"));
        }
    }

    @Test
    void deleteTender_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;

        try {
            tenderRepository.deleteTender(tenderId);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("DELETE FROM tender"));
            assertTrue(capturedSql.contains("WHERE tender_id = ?"));
        }
    }

    @Test
    void searchTenders_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        String searchTerm = "Test";

        try {
            tenderRepository.searchTenders(searchTerm);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("SELECT * FROM tender"));
            assertTrue(capturedSql.contains("WHERE title ILIKE ?"));
            assertTrue(capturedSql.contains("OR description ILIKE ?"));
        }
    }

    @Test
    void getTenderById_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;

        try {
            tenderRepository.getTenderById(tenderId);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("SELECT * FROM tender"));
            assertTrue(capturedSql.contains("WHERE tender_id = ?"));
        }
    }

    @Test
    void updateTender_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            tenderRepository.updateTender(testTender);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("UPDATE tender"));
            assertTrue(capturedSql.contains("SET title = ?"));
            assertTrue(capturedSql.contains("description = ?"));
            assertTrue(capturedSql.contains("status = ?"));
            assertTrue(capturedSql.contains("author_id = ?"));
            assertTrue(capturedSql.contains("WHERE tender_id = ?"));
        }
    }

    @Test
    void makeWinner_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;
        int proposalId = 5;

        try {
            tenderRepository.makeWinner(tenderId, proposalId);
            fail("Should have thrown exception");
        } catch (NullPointerException e) {
            // Expected
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("UPDATE tender SET winning_proposal_id = ?"));
            assertTrue(capturedSql.contains("WHERE tender_id = ?"));
        }
    }

    @Test
    void verifySqlQueries_AllMethods() {
        // We use an ArgumentCaptor to capture all SQL query strings from all methods
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        // Execute all methods (will throw NPEs but we'll catch them)
        // This allows us to verify all SQL statements in a single test
        executeAllRepositoryMethodsWithExceptionHandling();

        // Verify the SQL queries
        verify(jdbcClient, atLeast(8)).sql(sqlCaptor.capture());

        // Get the captured values
        var capturedQueries = sqlCaptor.getAllValues();

        // Verify that we have the expected SQL statements
        assertTrue(capturedQueries.stream().anyMatch(sql -> sql.contains("INSERT INTO tender")));
        assertTrue(capturedQueries.stream().anyMatch(sql -> sql.contains("author_id")));
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("UPDATE tender SET status = 'Ended'")));
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("SELECT * FROM tender WHERE status = 'Active'")));
        assertTrue(capturedQueries.stream().anyMatch(sql -> sql.contains("DELETE FROM tender")));
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("SELECT * FROM tender WHERE title ILIKE ?")));
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("SELECT * FROM tender WHERE tender_id = ?")));
        assertTrue(capturedQueries.stream().anyMatch(sql -> sql.contains("UPDATE tender SET")));
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("UPDATE tender SET winning_proposal_id = ?")));
    }

    private void executeAllRepositoryMethodsWithExceptionHandling() {
        try {
            tenderRepository.createTender(testTender);
        } catch (Exception e) {
        }
        try {
            tenderRepository.endTender(1);
        } catch (Exception e) {
        }
        try {
            tenderRepository.getAllActiveTenders();
        } catch (Exception e) {
        }
        try {
            tenderRepository.deleteTender(1);
        } catch (Exception e) {
        }
        try {
            tenderRepository.searchTenders("Test");
        } catch (Exception e) {
        }
        try {
            tenderRepository.getTenderById(1);
        } catch (Exception e) {
        }
        try {
            tenderRepository.updateTender(testTender);
        } catch (Exception e) {
        }
        try {
            tenderRepository.makeWinner(1, 5);
        } catch (Exception e) {
        }
    }
}
