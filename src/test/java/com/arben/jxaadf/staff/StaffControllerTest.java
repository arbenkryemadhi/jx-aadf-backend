package com.arben.jxaadf.staff;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StaffController.class)
public class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffRepository staffRepository;

    private String staffEmail;
    private String nonStaffEmail;

    @BeforeEach
    void setUp() {
        staffEmail = "staff@example.com";
        nonStaffEmail = "notstaff@example.com";
    }

    @Test
    void isStaff_WhenEmailIsStaff_ShouldReturnTrue() throws Exception {
        // Arrange
        when(staffRepository.isStaff(staffEmail)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/staff/isaadfstaff").param("email", staffEmail))
                .andExpect(status().isOk()).andExpect(content().string("true"));
    }

    @Test
    void isStaff_WhenEmailIsNotStaff_ShouldReturnFalse() throws Exception {
        // Arrange
        when(staffRepository.isStaff(nonStaffEmail)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/staff/isaadfstaff").param("email", nonStaffEmail))
                .andExpect(status().isOk()).andExpect(content().string("false"));
    }

    @Test
    void isStaff_WithEmptyEmail_ShouldStillCallRepository() throws Exception {
        // Arrange
        String emptyEmail = "";
        when(staffRepository.isStaff(emptyEmail)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/staff/isaadfstaff").param("email", emptyEmail))
                .andExpect(status().isOk()).andExpect(content().string("false"));
    }

    @Test
    void isStaff_WithInvalidEmail_ShouldProcessNormally() throws Exception {
        // Arrange
        String invalidEmail = "not-an-email";
        when(staffRepository.isStaff(invalidEmail)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/staff/isaadfstaff").param("email", invalidEmail))
                .andExpect(status().isOk()).andExpect(content().string("false"));
    }
}
