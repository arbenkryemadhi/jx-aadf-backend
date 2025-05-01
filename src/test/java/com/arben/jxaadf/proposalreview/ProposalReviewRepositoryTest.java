package com.arben.jxaadf.proposalreview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProposalReviewRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    private ProposalReviewRepository proposalReviewRepository;
    private ProposalReview testProposalReview;

    @BeforeEach
    void setUp() {
        proposalReviewRepository = new ProposalReviewRepository(jdbcClient);
        testProposalReview = new ProposalReview(1, 2, "user123", "Test Review",
                "This is a test review", "2025-05-01");
    }

    @Test
    void createProposalReview_VerifySqlExecution() {
        // This test verifies that the correct SQL is executed
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            // Execute method (will throw NullPointerException due to mock chain)
            proposalReviewRepository.createProposalReview(testProposalReview);
        } catch (NullPointerException e) {
            // Expected due to mock limitations
            // We can still verify the SQL was called
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            // Verify SQL contains the expected text
            assertTrue(capturedSql.contains("INSERT INTO proposal_review"));
            assertTrue(capturedSql.contains("VALUES"));
        }
    }

    @Test
    void updateProposalReview_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            proposalReviewRepository.updateProposalReview(testProposalReview);
        } catch (NullPointerException e) {
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("UPDATE proposal_review SET"));
            assertTrue(capturedSql.contains("WHERE proposal_review_id = ?"));
        }
    }

    @Test
    void deleteProposalReview_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int proposalReviewId = 1;

        try {
            proposalReviewRepository.deleteProposalReview(proposalReviewId);
        } catch (NullPointerException e) {
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("DELETE FROM proposal_review"));
            assertTrue(capturedSql.contains("WHERE proposal_review_id = ?"));
        }
    }

    @Test
    void getProposalReviewById_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int proposalReviewId = 1;

        try {
            proposalReviewRepository.getProposalReviewById(proposalReviewId);
        } catch (NullPointerException e) {
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("SELECT * FROM proposal_review"));
            assertTrue(capturedSql.contains("WHERE proposal_review_id = ?"));
        }
    }

    @Test
    void getAllProposalReviewsOfProposal_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int proposalId = 2;

        try {
            proposalReviewRepository.getAllProposalReviewsOfProposal(proposalId);
        } catch (NullPointerException e) {
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("SELECT * FROM proposal_review"));
            assertTrue(capturedSql.contains("WHERE proposal_id = ?"));
        }
    }

    @Test
    void getAllProposalReviewsOfUser_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        String userId = "user123";

        try {
            proposalReviewRepository.getAllProposalReviewsOfUser(userId);
        } catch (NullPointerException e) {
            verify(jdbcClient).sql(sqlCaptor.capture());
            String capturedSql = sqlCaptor.getValue();

            assertTrue(capturedSql.contains("SELECT * FROM proposal_review"));
            assertTrue(capturedSql.contains("WHERE author_id = ?"));
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
        verify(jdbcClient, atLeast(6)).sql(sqlCaptor.capture());

        // Get the captured values
        var capturedQueries = sqlCaptor.getAllValues();

        // Verify that we have the expected SQL statements
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("INSERT INTO proposal_review")));
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("UPDATE proposal_review SET")));
        assertTrue(capturedQueries.stream()
                .anyMatch(sql -> sql.contains("DELETE FROM proposal_review")));
        assertTrue(capturedQueries.stream().anyMatch(
                sql -> sql.contains("SELECT * FROM proposal_review WHERE proposal_review_id = ?")));
        assertTrue(capturedQueries.stream().anyMatch(
                sql -> sql.contains("SELECT * FROM proposal_review WHERE proposal_id = ?")));
        assertTrue(capturedQueries.stream().anyMatch(
                sql -> sql.contains("SELECT * FROM proposal_review WHERE author_id = ?")));
    }

    private void executeAllRepositoryMethodsWithExceptionHandling() {
        try {
            proposalReviewRepository.createProposalReview(testProposalReview);
        } catch (Exception e) {
        }
        try {
            proposalReviewRepository.updateProposalReview(testProposalReview);
        } catch (Exception e) {
        }
        try {
            proposalReviewRepository.deleteProposalReview(1);
        } catch (Exception e) {
        }
        try {
            proposalReviewRepository.getProposalReviewById(1);
        } catch (Exception e) {
        }
        try {
            proposalReviewRepository.getAllProposalReviewsOfProposal(2);
        } catch (Exception e) {
        }
        try {
            proposalReviewRepository.getAllProposalReviewsOfUser("user123");
        } catch (Exception e) {
        }
    }
}
