package com.arben.jxaadf.tender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.jdbc.core.simple.JdbcClient.MappedQuerySpec;

@ExtendWith(MockitoExtension.class)
public class TenderRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private StatementSpec statementSpec;

    @Mock
    private MappedQuerySpec<Integer> integerQuerySpec;

    @Mock
    private MappedQuerySpec<Tender> tenderQuerySpec;

    @Mock
    private ResultSet resultSet;

    @Mock
    private Array sqlArray;

    private TenderRepository tenderRepository;
    private Tender testTender;

    @BeforeEach
    void setUp() {
        tenderRepository = new TenderRepository(jdbcClient);
        testTender = new Tender(1, "Test Tender", "Description", "Active", "user123", "2025-05-01",
                "2025-05-31", "10000 EUR");
        List<String> documentLinks =
                Arrays.asList("http://example.com/doc1", "http://example.com/doc2");
        testTender.setDocumentLinks(documentLinks);
        testTender.setAssignedAadfStaff(Arrays.asList("staff1@example.com", "staff2@example.com"));
    }


    @SuppressWarnings("unchecked")
    @Test
    void tenderRowMapper_HandlesNullDocumentLinks() throws SQLException {
        // Arrange - setup mocks for getTenderById which uses the row mapper
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyInt())).thenReturn(statementSpec);
        when(statementSpec.query(any(RowMapper.class))).thenAnswer(invocation -> {
            // Get the row mapper that was passed
            RowMapper<Tender> rowMapper = invocation.getArgument(0);

            // Mock ResultSet behavior
            when(resultSet.getInt("tender_id")).thenReturn(1);
            when(resultSet.getString("title")).thenReturn("Test Tender");
            when(resultSet.getString("description")).thenReturn("Description");
            when(resultSet.getString("status")).thenReturn("Active");
            when(resultSet.getString("author_id")).thenReturn("user123");
            when(resultSet.getString("created_date")).thenReturn("2025-05-01");
            when(resultSet.getString("deadline")).thenReturn("2025-05-31");
            when(resultSet.getString("budget")).thenReturn("10000 EUR");
            when(resultSet.getArray("assigned_aadf_staff")).thenReturn(null);

            // Mock null Array for document_links
            when(resultSet.getArray("document_links")).thenReturn(null);

            // Create a tender using the row mapper and return it in a mock MappedQuerySpec
            Tender tender = rowMapper.mapRow(resultSet, 0);
            MappedQuerySpec<Tender> mockedQuerySpec = mock(MappedQuerySpec.class);
            when(mockedQuerySpec.single()).thenReturn(tender);
            return mockedQuerySpec;
        });

        // Act
        Tender result = tenderRepository.getTenderById(1);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getDocumentLinks(),
                "Document links should be initialized as empty list when null in database");
        assertTrue(result.getDocumentLinks().isEmpty(), "Document links should be empty");
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAllMethods_UseTenderRowMapper() {
        // This test verifies that all methods that return Tenders use our custom row mapper
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);

        // Verify getAllActiveTenders uses the custom row mapper
        when(statementSpec.query(any(RowMapper.class))).thenAnswer(inv -> {
            MappedQuerySpec<Tender> mockedListSpec = mock(MappedQuerySpec.class);
            when(mockedListSpec.list()).thenReturn(Collections.emptyList());
            return mockedListSpec;
        });

        tenderRepository.getAllActiveTenders();
        verify(statementSpec).query(any(RowMapper.class));

        // Reset mocks between tests
        reset(jdbcClient, statementSpec);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);

        // Verify searchTenders uses the custom row mapper
        when(statementSpec.param(anyString())).thenReturn(statementSpec);
        when(statementSpec.query(any(RowMapper.class))).thenAnswer(inv -> {
            MappedQuerySpec<Tender> mockedListSpec = mock(MappedQuerySpec.class);
            when(mockedListSpec.list()).thenReturn(Collections.emptyList());
            return mockedListSpec;
        });

        tenderRepository.searchTenders("test");
        verify(statementSpec, atLeast(1)).query(any(RowMapper.class));

        // Reset mocks again
        reset(jdbcClient, statementSpec);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);

        // Verify getAllTenders uses the custom row mapper
        when(statementSpec.query(any(RowMapper.class))).thenAnswer(inv -> {
            MappedQuerySpec<Tender> mockedListSpec = mock(MappedQuerySpec.class);
            when(mockedListSpec.list()).thenReturn(Collections.emptyList());
            return mockedListSpec;
        });

        tenderRepository.getAllTenders();
        verify(statementSpec).query(any(RowMapper.class));
    }

    @Test
    void createTender_VerifySqlExecution() {
        // This is a partial test that focuses on verifying the SQL contains the expected text
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            // Execute method (will throw NullPointerException due to mock chain)
            tenderRepository.createTender(testTender);
        } catch (NullPointerException e) {
            // Expected due to mock limitations
        }

        // We can still verify the SQL was called
        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        // Verify SQL contains the expected text
        assertTrue(capturedSql.contains("INSERT INTO tender"));
        assertTrue(capturedSql.contains("VALUES"));
        assertTrue(capturedSql.contains("RETURNING tender_id"));
    }

    @Test
    void endTender_VerifySqlExecution() {
        // Similar partial test focusing on SQL verification
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;

        try {
            tenderRepository.endTender(tenderId);
        } catch (NullPointerException e) {
            // Expected - ignore
        }

        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        assertTrue(capturedSql.contains("UPDATE tender SET status = 'Ended'"));
        assertTrue(capturedSql.contains("WHERE tender_id = ?"));
    }

    @Test
    void getAllActiveTenders_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            tenderRepository.getAllActiveTenders();
        } catch (NullPointerException e) {
            // Expected - ignore
        }

        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        assertTrue(capturedSql.contains("SELECT * FROM tender"));
        assertTrue(capturedSql.contains("WHERE status = 'Active'"));
    }

    @Test
    void deleteTender_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;

        try {
            tenderRepository.deleteTender(tenderId);
        } catch (NullPointerException e) {
            // Expected - ignore
        }

        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        assertTrue(capturedSql.contains("DELETE FROM tender"));
        assertTrue(capturedSql.contains("WHERE tender_id = ?"));
    }

    @Test
    void searchTenders_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        String searchTerm = "Test";

        try {
            tenderRepository.searchTenders(searchTerm);
        } catch (NullPointerException e) {
            // Expected - ignore
        }

        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        assertTrue(capturedSql.contains("SELECT * FROM tender"));
        assertTrue(capturedSql.contains("WHERE title ILIKE ?"));
        assertTrue(capturedSql.contains("OR description ILIKE ?"));
    }

    @Test
    void getTenderById_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;

        try {
            tenderRepository.getTenderById(tenderId);
        } catch (NullPointerException e) {
            // Expected - ignore
        }

        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        assertTrue(capturedSql.contains("SELECT * FROM tender"));
        assertTrue(capturedSql.contains("WHERE tender_id = ?"));
    }

    @Test
    void updateTender_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        try {
            tenderRepository.updateTender(testTender);
        } catch (NullPointerException e) {
            // Expected - ignore
        }

        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        assertTrue(capturedSql.contains("UPDATE tender"));
        assertTrue(capturedSql.contains("SET title = ?"));
        assertTrue(capturedSql.contains("description = ?"));
        assertTrue(capturedSql.contains("status = ?"));
        assertTrue(capturedSql.contains("author_id = ?"));
        assertTrue(capturedSql.contains("document_links = ?"));
        assertTrue(capturedSql.contains("WHERE tender_id = ?"));
    }

    @Test
    void makeWinner_VerifySqlExecution() {
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;
        int proposalId = 5;

        try {
            tenderRepository.makeWinner(tenderId, proposalId);
        } catch (NullPointerException e) {
            // Expected - ignore
        }

        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();

        assertTrue(capturedSql.contains("UPDATE tender SET winning_proposal_id = ?"));
        assertTrue(capturedSql.contains("WHERE tender_id = ?"));
    }

    @Test
    void createTender_WithAssignedStaff_VerifySqlExecution() {
        // This test verifies that the correct SQL is executed with assigned staff
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        when(jdbcClient.sql(any())).thenReturn(statementSpec);
        when(statementSpec.param(any())).thenReturn(statementSpec);
        when(statementSpec.query(eq(Integer.class))).thenReturn(integerQuerySpec);
        when(integerQuerySpec.single()).thenReturn(1);

        // Act
        int result = tenderRepository.createTender(testTender);

        // Assert
        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("INSERT INTO tender"));
        assertTrue(capturedSql.contains("assigned_aadf_staff"));
        assertEquals(1, result);
    }

    @Test
    void removeStaffFromTender_VerifySqlExecution() {
        // This test verifies the SQL to remove staff from a tender
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        int tenderId = 1;
        String staffId = "staff@example.com";

        when(jdbcClient.sql(any())).thenReturn(statementSpec);
        when(statementSpec.param(any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        tenderRepository.removeStaffFromTender(tenderId, staffId);

        // Assert
        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("UPDATE tender"));
        assertTrue(capturedSql.contains("array_remove"));
        assertTrue(capturedSql.contains("assigned_aadf_staff"));

        verify(statementSpec).param(staffId);
        verify(statementSpec).param(tenderId);
    }

    @Test
    void getTendersByStaffId_VerifySqlExecution() {
        // This test verifies the SQL to get tenders by staff ID
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        String staffId = "staff@example.com";

        when(jdbcClient.sql(any())).thenReturn(statementSpec);
        when(statementSpec.param(any())).thenReturn(statementSpec);
        when(statementSpec.query(any(RowMapper.class))).thenReturn(tenderQuerySpec);
        when(tenderQuerySpec.list()).thenReturn(Collections.emptyList());

        // Act
        tenderRepository.getTendersByStaffId(staffId);

        // Assert
        verify(jdbcClient).sql(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("SELECT * FROM tender"));
        assertTrue(capturedSql.contains("= ANY(assigned_aadf_staff)"));

        verify(statementSpec).param(staffId);
    }
}
