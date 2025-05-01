package com.arben.jxaadf.appuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private StatementSpec statementSpec;

    private AppUserRepository appUserRepository;
    private AppUser testUser;

    @BeforeEach
    void setUp() {
        appUserRepository = new AppUserRepository(jdbcClient);
        testUser = new AppUser("user123", "John", "Doe", "john.doe@example.com");

        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.params(any(Object[].class))).thenReturn(statementSpec);
    }

    @Test
    void createAppUser_Success() {
        // Arrange
        when(statementSpec.update()).thenReturn(1);

        // Act
        appUserRepository.createAppUser(testUser);

        // Assert
        verify(jdbcClient).sql(contains("INSERT INTO app_user"));
        verify(statementSpec).params(eq(testUser.getAppUserId()), eq(testUser.getFirstName()),
                eq(testUser.getLastName()), eq(testUser.getEmail()));
        verify(statementSpec).update();
    }

    @Test
    void createAppUser_Failure_ThrowsException() {
        // Arrange
        when(statementSpec.update()).thenReturn(0);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            appUserRepository.createAppUser(testUser);
        });

        assertTrue(exception.getMessage().contains("Insert failed"));
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).update();
    }

    @Test
    void deleteAppUser_Success() {
        // Arrange
        when(statementSpec.update()).thenReturn(1);
        String userId = "user123";

        // Act
        appUserRepository.deleteAppUser(userId);

        // Assert
        verify(jdbcClient).sql(contains("DELETE FROM app_user"));
        verify(statementSpec).params(eq(userId));
        verify(statementSpec).update();
    }

    @Test
    void deleteAppUser_Failure_ThrowsException() {
        // Arrange
        when(statementSpec.update()).thenReturn(0);
        String userId = "nonexistentUser";

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            appUserRepository.deleteAppUser(userId);
        });

        assertTrue(exception.getMessage().contains("Delete failed"));
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).update();
    }

    @Test
    void verifySqlQueries() {
        // We use an ArgumentCaptor to capture the SQL query strings
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        // Mock the behavior for testing
        when(statementSpec.update()).thenReturn(1);

        // Execute both methods
        appUserRepository.createAppUser(testUser);
        appUserRepository.deleteAppUser("user123");

        // Verify the SQL queries
        verify(jdbcClient, times(2)).sql(sqlCaptor.capture());

        // Get the captured values
        var capturedQueries = sqlCaptor.getAllValues();

        // Verify insert query
        assertEquals(
                "INSERT INTO app_user (app_user_id, first_name, last_name, email) VALUES (?, ?, ?, ?)",
                capturedQueries.get(0));

        // Verify delete query
        assertEquals("DELETE FROM app_user WHERE app_user_id = ?", capturedQueries.get(1));
    }
}
