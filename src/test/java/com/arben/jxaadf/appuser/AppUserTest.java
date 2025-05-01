package com.arben.jxaadf.appuser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



public class AppUserTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String appUserId = "user123";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";

        // Act
        AppUser appUser = new AppUser(appUserId, firstName, lastName, email);

        // Assert
        assertEquals(appUserId, appUser.getAppUserId());
        assertEquals(firstName, appUser.getFirstName());
        assertEquals(lastName, appUser.getLastName());
        assertEquals(email, appUser.getEmail());
    }

    @Test
    public void testSetters() {
        // Arrange
        AppUser appUser = new AppUser("oldId", "OldFirstName", "OldLastName", "old@example.com");
        String newAppUserId = "newId";
        String newFirstName = "Jane";
        String newLastName = "Smith";
        String newEmail = "jane.smith@example.com";

        // Act
        appUser.setAppUserId(newAppUserId);
        appUser.setFirstName(newFirstName);
        appUser.setLastName(newLastName);
        appUser.setEmail(newEmail);

        // Assert
        assertEquals(newAppUserId, appUser.getAppUserId());
        assertEquals(newFirstName, appUser.getFirstName());
        assertEquals(newLastName, appUser.getLastName());
        assertEquals(newEmail, appUser.getEmail());
    }
}
