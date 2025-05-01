package com.arben.jxaadf.tender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TenderController.class)
public class TenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenderRepository tenderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Tender testTender;

    @BeforeEach
    void setUp() {
        testTender = new Tender(1, "Test Tender", "Test Description", "Active", "test@example.com",
                "2025-05-01", "2025-06-01", "10000 EUR");
    }

    @Test
    void createTender_ShouldReturnTenderId() throws Exception {
        // Arrange
        when(tenderRepository.createTender(any(Tender.class))).thenReturn(1);
        String tenderJson = objectMapper.writeValueAsString(testTender);

        // Act & Assert
        mockMvc.perform(post("/api/tender/create").contentType(MediaType.APPLICATION_JSON)
                .content(tenderJson)).andExpect(status().isOk()).andExpect(content().string("1"));

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
    void getAllActiveTenders_ShouldReturnList() throws Exception {
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
    void searchTenders_ShouldReturnMatchingTenders() throws Exception {
        // Arrange
        List<Tender> tenders = Arrays.asList(testTender);
        when(tenderRepository.searchTenders(anyString())).thenReturn(tenders);

        // Act & Assert
        mockMvc.perform(get("/api/tender/search").header("searchTerm", "Test"))
                .andExpect(status().isOk())
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
        String tenderJson = objectMapper.writeValueAsString(testTender);

        // Act & Assert
        mockMvc.perform(put("/api/tender/update").contentType(MediaType.APPLICATION_JSON)
                .content(tenderJson)).andExpect(status().isOk());

        verify(tenderRepository, times(1)).updateTender(any(Tender.class));
    }

    @Test
    void makeWinner_ShouldSetWinner() throws Exception {
        // Arrange
        doNothing().when(tenderRepository).makeWinner(anyInt(), anyInt());

        // Act & Assert
        mockMvc.perform(
                put("/api/tender/makewinner").header("tenderId", "1").header("proposalId", "5"))
                .andExpect(status().isOk());

        verify(tenderRepository, times(1)).makeWinner(1, 5);
    }

    @Test
    void createTender_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidJson = "{\"title\":\"Test Tender\", invalidJson}";

        // Act & Assert
        mockMvc.perform(post("/api/tender/create").contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)).andExpect(status().isBadRequest());
    }
}
