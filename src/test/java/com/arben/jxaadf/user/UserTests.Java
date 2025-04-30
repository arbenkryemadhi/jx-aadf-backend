package com.arben.jxaadf.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest 
public class UserTests {

    @Test
    public void testUserConstructor() {
        // Arrange
        String id = "123";
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        
        // Act
        User user = new User(id, firstname, lastname, email);
        
        // Assert
        assertEquals(id, user.getId());
        assertEquals(firstname, user.getFirstname());
        assertEquals(lastname, user.getLastname());
        assertEquals(email, user.getEmail());
    }
    
    @Test
    public void testSetters() {
        // Arrange
        User user = new User("123", "John", "Doe", "john.doe@example.com");
        
        // Act & Assert
        String newId = "456";
        user.setId(newId);
        assertEquals(newId, user.getId());
        
        String newFirstname = "Jane";
        user.setFirstname(newFirstname);
        assertEquals(newFirstname, user.getFirstname());
        
        String newLastname = "Smith";
        user.setLastname(newLastname);
        assertEquals(newLastname, user.getLastname());
        
        String newEmail = "jane.smith@example.com";
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }
    
    @Test
    public void testGetters() {
        // Arrange
        String id = "123";
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        User user = new User(id, firstname, lastname, email);
        
        // Act & Assert
        assertEquals(id, user.getId());
        assertEquals(firstname, user.getFirstname());
        assertEquals(lastname, user.getLastname());
        assertEquals(email, user.getEmail());
    }
}
