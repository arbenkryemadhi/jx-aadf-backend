package com.arben.jxaadf.staff;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StaffTest {

    @Test
    public void testNoArgsConstructor() {
        // Act
        Staff staff = new Staff();

        // Assert
        assertNotNull(staff);
    }

    @Test
    public void testConstructorWithArgs() {
        // Arrange
        String id = "1";
        String email = "staff@example.com";

        // Act
        Staff staff = new Staff(id, email);

        // Assert
        assertEquals(id, staff.getId());
        assertEquals(email, staff.getEmail());
    }

    @Test
    public void testSetters() {
        // Arrange
        Staff staff = new Staff();
        String id = "2";
        String email = "newstaff@example.com";

        // Act
        staff.setId(id);
        staff.setEmail(email);

        // Assert
        assertEquals(id, staff.getId());
        assertEquals(email, staff.getEmail());
    }

    @Test
    public void testGetters() {
        // Arrange
        String id = "3";
        String email = "test@example.com";
        Staff staff = new Staff(id, email);

        // Act & Assert
        assertEquals(id, staff.getId());
        assertEquals(email, staff.getEmail());
    }
}
