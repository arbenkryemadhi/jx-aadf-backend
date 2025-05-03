package com.arben.jxaadf.proposalreview;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

@WebMvcTest(ProposalReviewController.class)
public class ProposalReviewControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProposalReviewRepository proposalReviewRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private ProposalReview testProposalReview;

        @BeforeEach
        void setUp() {
                testProposalReview = new ProposalReview(1, 2, "user123", "Test Review",
                                "This is a test review", "2025-05-01", 85);
        }

        @Test
        void createProposalReview_ShouldReturnSuccess() throws Exception {
                // Arrange
                when(proposalReviewRepository.createProposalReview(any(ProposalReview.class)))
                                .thenReturn("Proposal review created successfully");

                // Act & Assert
                mockMvc.perform(post("/api/proposalreview/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testProposalReview)))
                                .andExpect(status().isOk()).andExpect(content()
                                                .string("Proposal review created successfully"));

                verify(proposalReviewRepository, times(1))
                                .createProposalReview(any(ProposalReview.class));
        }

        @Test
        void updateProposalReview_ShouldReturnSuccess() throws Exception {
                // Arrange
                when(proposalReviewRepository.updateProposalReview(any(ProposalReview.class)))
                                .thenReturn("Proposal review updated successfully");

                // Act & Assert
                mockMvc.perform(put("/api/proposalreview/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testProposalReview)))
                                .andExpect(status().isOk()).andExpect(content()
                                                .string("Proposal review updated successfully"));

                verify(proposalReviewRepository, times(1))
                                .updateProposalReview(any(ProposalReview.class));
        }

        @Test
        void deleteProposalReview_ShouldDeleteProposalReview() throws Exception {
                // Arrange
                when(proposalReviewRepository.deleteProposalReview(anyInt()))
                                .thenReturn("Proposal review deleted successfully");

                // Act & Assert
                mockMvc.perform(delete("/api/proposalreview/delete").param("proposalReviewId", "1"))
                                .andExpect(status().isOk()).andExpect(content()
                                                .string("Proposal review deleted successfully"));

                verify(proposalReviewRepository, times(1)).deleteProposalReview(1);
        }

        @Test
        void getProposalReviewById_ShouldReturnProposalReview() throws Exception {
                // Arrange
                when(proposalReviewRepository.getProposalReviewById(anyInt()))
                                .thenReturn(testProposalReview);

                // Act & Assert
                mockMvc.perform(get("/api/proposalreview/getreview").param("proposalReviewId", "1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.proposalReviewId")
                                                .value(testProposalReview.getProposalReviewId()))
                                .andExpect(jsonPath("$.title").value(testProposalReview.getTitle()))
                                .andExpect(jsonPath("$.description")
                                                .value(testProposalReview.getDescription()));

                verify(proposalReviewRepository, times(1)).getProposalReviewById(1);
        }

        @Test
        void getAllProposalReviewsOfProposal_ShouldReturnList() throws Exception {
                // Arrange
                List<ProposalReview> proposalReviews = Arrays.asList(testProposalReview);
                when(proposalReviewRepository.getAllProposalReviewsOfProposal(anyInt()))
                                .thenReturn(proposalReviews);

                // Act & Assert
                mockMvc.perform(get("/api/proposalreview/getbyproposalid").param("proposalId", "2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].proposalReviewId")
                                                .value(testProposalReview.getProposalReviewId()))
                                .andExpect(jsonPath("$[0].title")
                                                .value(testProposalReview.getTitle()));

                verify(proposalReviewRepository, times(1)).getAllProposalReviewsOfProposal(2);
        }

        @Test
        void getAllProposalReviewsOfUser_ShouldReturnList() throws Exception {
                // Arrange
                List<ProposalReview> proposalReviews = Arrays.asList(testProposalReview);
                when(proposalReviewRepository.getAllProposalReviewsOfUser(anyString()))
                                .thenReturn(proposalReviews);

                // Act & Assert
                mockMvc.perform(get("/api/proposalreview/getbyuserid").param("userId", "user123"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].proposalReviewId")
                                                .value(testProposalReview.getProposalReviewId()))
                                .andExpect(jsonPath("$[0].title")
                                                .value(testProposalReview.getTitle()));

                verify(proposalReviewRepository, times(1)).getAllProposalReviewsOfUser("user123");
        }

        @Test
        void getAllProposalReviewsOfProposal_WithInvalidProposalId_ShouldReturnEmptyList()
                        throws Exception {
                // Arrange
                when(proposalReviewRepository.getAllProposalReviewsOfProposal(999))
                                .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/proposalreview/getbyproposalid").param("proposalId",
                                "999")).andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isEmpty());

                verify(proposalReviewRepository, times(1)).getAllProposalReviewsOfProposal(999);
        }

        @Test
        void getAllProposalReviewsOfUser_WithInvalidUserId_ShouldReturnEmptyList()
                        throws Exception {
                // Arrange
                when(proposalReviewRepository.getAllProposalReviewsOfUser("nonexistent"))
                                .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/proposalreview/getbyuserid").param("userId",
                                "nonexistent")).andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isEmpty());

                verify(proposalReviewRepository, times(1))
                                .getAllProposalReviewsOfUser("nonexistent");
        }

        @Test
        void addHumanScore_ShouldReturnSuccess() throws Exception {
                // Arrange
                when(proposalReviewRepository.updateHumanScore(anyInt(), anyInt()))
                                .thenReturn("Human score updated successfully");

                // Act & Assert
                mockMvc.perform(put("/api/proposalreview/addhumanscore")
                                .param("proposalReviewId", "1").param("humanScore", "90"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Human score updated successfully"));

                verify(proposalReviewRepository, times(1)).updateHumanScore(1, 90);
        }
}
