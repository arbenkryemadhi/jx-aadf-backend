package com.arben.jxaadf.tender;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.arben.jxaadf.appuser.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TenderController.class)
public class TenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenderRepository tenderRepository;

    @MockBean
    private AppUserRepository appUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Tender testTender;

    @BeforeEach
    void setUp() {
        testTender = new Tender(1, "Test Tender", "Tender Description", "Active",
                "test@example.com", "2025-05-01", "2025-06-01", "10000 EUR",
                Arrays.asList("http://example.com/doc1"), Arrays.asList("staff1@example.com"));
    }

    @Test
    void createTender_ShouldReturnTenderId() throws Exception {
        // Arrange
        when(tenderRepository.createTender(any(Tender.class))).thenReturn(1);

        // Act & Assert
        mockMvc.perform(post("/api/tender/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTender))).andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(tenderRepository, times(1)).createTender(any(Tender.class));
    }

    @Test
    void endTender_ShouldEndTender() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).endTender(anyInt());

        // Act & Assert
        mockMvc.perform(put("/api/tender/end").header("tenderId", "1")).andExpect(status().isOk());

        verify(tenderRepository, times(1)).endTender(1);
    }

    @Test
    void getAllActiveTenders_ShouldReturnListOfTenders() throws Exception {
        // Arrange
        List<Tender> tenders = Arrays.asList(testTender);
        when(tenderRepository.getAllActiveTenders()).thenReturn(tenders);

        // Act & Assert
        mockMvc.perform(get("/api/tender/getallactive")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tenderId").value(testTender.getTenderId()))
                .andExpect(jsonPath("$[0].title").value(testTender.getTitle()));

        verify(tenderRepository, times(1)).getAllActiveTenders();
    }

    @Test
    void deleteTender_ShouldDeleteTender() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).deleteTender(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/api/tender/delete").header("tenderId", "1"))
                .andExpect(status().isOk());

        verify(tenderRepository, times(1)).deleteTender(1);
    }

    @Test
    void searchTenders_ShouldReturnListOfTenders() throws Exception {
        // Arrange
        List<Tender> tenders = Arrays.asList(testTender);
        when(tenderRepository.searchTenders(anyString())).thenReturn(tenders);

        // Act & Assert
        mockMvc.perform(get("/api/tender/search").header("searchTerm", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tenderId").value(testTender.getTenderId()))
                .andExpect(jsonPath("$[0].title").value(testTender.getTitle()));

        verify(tenderRepository, times(1)).searchTenders("Test");
    }

    @Test
    void getTenderById_ShouldReturnTender() throws Exception {
        // Arrange
        when(tenderRepository.getTenderById(anyInt())).thenReturn(testTender);

        // Act & Assert
        mockMvc.perform(get("/api/tender/getbyid").header("tenderId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenderId").value(testTender.getTenderId()))
                .andExpect(jsonPath("$.title").value(testTender.getTitle()));

        verify(tenderRepository, times(1)).getTenderById(1);
    }

    @Test
    void updateTender_ShouldUpdateTender() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).updateTender(any(Tender.class));

        // Act & Assert
        mockMvc.perform(put("/api/tender/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTender))).andExpect(status().isOk());

        verify(tenderRepository, times(1)).updateTender(any(Tender.class));
    }

    @Test
    void makeWinner_ShouldMakeWinner() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).makeWinner(anyInt(), anyInt());

        // Act & Assert
        mockMvc.perform(
                put("/api/tender/makewinner").header("tenderId", "1").header("proposalId", "2"))
                .andExpect(status().isOk());

        verify(tenderRepository, times(1)).makeWinner(1, 2);
    }

    @Test
    void getAllTenders_ShouldReturnListOfTenders() throws Exception {
        // Arrange
        List<Tender> tenders = Arrays.asList(testTender);
        when(tenderRepository.getAllTenders()).thenReturn(tenders);

        // Act & Assert
        mockMvc.perform(get("/api/tender/getall")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tenderId").value(testTender.getTenderId()))
                .andExpect(jsonPath("$[0].title").value(testTender.getTitle()));

        verify(tenderRepository, times(1)).getAllTenders();
    }

    @Test
    void addLink_ShouldAddLink() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).addLink(anyInt(), anyString());
        String link = "http://example.com/doc3";

        // Act & Assert
        mockMvc.perform(put("/api/tender/addlink").header("tenderId", "1")
                .contentType(MediaType.TEXT_PLAIN).content(link)).andExpect(status().isOk());

        verify(tenderRepository, times(1)).addLink(1, link);
    }

    // New tests for the assignedAadfStaff functionality

    @Test
    void addStaffToTender_ShouldAddStaffToTender() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).addStaffToTender(anyInt(), anyString());

        // Act & Assert
        mockMvc.perform(put("/api/tender/addstaff").header("tenderId", "1").header("staffId",
                "staff@example.com")).andExpect(status().isOk());

        verify(tenderRepository, times(1)).addStaffToTender(1, "staff@example.com");
    }

    @Test
    void removeStaffFromTender_ShouldRemoveStaffFromTender() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).removeStaffFromTender(anyInt(), anyString());

        // Act & Assert
        mockMvc.perform(put("/api/tender/removestaff").header("tenderId", "1").header("staffId",
                "staff@example.com")).andExpect(status().isOk());

        verify(tenderRepository, times(1)).removeStaffFromTender(1, "staff@example.com");
    }

    @Test
    void getTendersByStaffId_ShouldReturnListOfTenders() throws Exception {
        // Arrange
        List<Tender> tenders = Arrays.asList(testTender);
        when(tenderRepository.getTendersByStaffId(anyString())).thenReturn(tenders);

        // Act & Assert
        mockMvc.perform(get("/api/tender/getbystaff").param("staffId", "staff@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tenderId").value(testTender.getTenderId()))
                .andExpect(jsonPath("$[0].title").value(testTender.getTitle()));

        verify(tenderRepository, times(1)).getTendersByStaffId("staff@example.com");
    }

    @Test
    void getTendersByStaffId_WithNoTenders_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(tenderRepository.getTendersByStaffId(anyString())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/tender/getbystaff").param("staffId", "nonexistent@example.com"))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(tenderRepository, times(1)).getTendersByStaffId("nonexistent@example.com");
    }

    @Test
    void createTender_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidJson = "{\"title\":\"Test Tender\", invalidJson}";

        // Act & Assert
        mockMvc.perform(post("/api/tender/create").contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)).andExpect(status().isBadRequest());
    }

    @Test
    void addStaffToTenderByEmail_ShouldAddStaffToTender() throws Exception {
        // Arrange
        String email = "staff@example.com";
        String staffId = "staff123";
        when(appUserRepository.getIdFromEmail(email)).thenReturn(staffId);
        doNothing().when(tenderRepository).addStaffToTender(anyInt(), anyString());

        // Act & Assert
        mockMvc.perform(
                put("/api/tender/addstaffemal").header("tenderId", "1").header("email", email))
                .andExpect(status().isOk());

        verify(appUserRepository, times(1)).getIdFromEmail(email);
        verify(tenderRepository, times(1)).addStaffToTender(1, staffId);
    }



    @Test
    void addStaffToTenderByEmail_WithEmptyEmail_ShouldHandleValidation() throws Exception {
        // Arrange
        String email = "";

        // Act & Assert
        mockMvc.perform(
                put("/api/tender/addstaffemal").header("tenderId", "1").header("email", email))
                .andExpect(status().isOk()); // Without explicit validation, empty header is
                                             // permitted

        verify(appUserRepository, times(1)).getIdFromEmail(email);
    }
}
