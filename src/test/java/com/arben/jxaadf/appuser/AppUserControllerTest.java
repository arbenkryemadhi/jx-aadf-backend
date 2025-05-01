package com.arben.jxaadf.appuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(AppUserController.class)
public class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserRepository appUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser("user123", "John", "Doe", "john.doe@example.com");
        doNothing().when(appUserRepository).createAppUser(any(AppUser.class));
    }

    @Test
    void createAppUser_ShouldCreateNewUser() throws Exception {
        String userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/api/appuser/create").contentType(MediaType.APPLICATION_JSON)
                .content(userJson)).andExpect(status().isOk());

        verify(appUserRepository, times(1)).createAppUser(any(AppUser.class));
    }

    @Test
    void createAppUser_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        String invalidJson = "{\"firstName\":\"John\", invalidJson}";

        mockMvc.perform(post("/api/appuser/create").contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)).andExpect(status().isBadRequest());
    }
}
