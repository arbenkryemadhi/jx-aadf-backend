package com.arben.jxaadf.proposal;

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

@WebMvcTest(ProposalController.class)
public class ProposalControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProposalRepository proposalRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private Proposal testProposal;

        @BeforeEach
        void setUp() {
                testProposal = new Proposal(1, 1, "test@example.com", "Test Proposal",
                                "Proposal Description", "5000 EUR", "Pending", "2025-05-01");
                testProposal.setDocumentLinks(Arrays.asList("http://example.com/doc1",
                                "http://example.com/doc2"));
        }

        @Test
        void createProposal_ShouldCreateProposalWithDocumentLinks() throws Exception {
                // Arrange
                when(proposalRepository.createProposal(any(Proposal.class)))
                                .thenReturn("Proposal created successfully");

                // Act & Assert
                mockMvc.perform(post("/api/proposal/create").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testProposal)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Proposal created successfully"));

                verify(proposalRepository, times(1)).createProposal(any(Proposal.class));
        }

        @Test
        void updateProposal_ShouldUpdateProposalWithDocumentLinks() throws Exception {
                // Arrange
                when(proposalRepository.updateProposal(any(Proposal.class)))
                                .thenReturn("Proposal updated successfully");

                // Act & Assert
                mockMvc.perform(put("/api/proposal/update").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testProposal)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Proposal updated successfully"));

                verify(proposalRepository, times(1)).updateProposal(any(Proposal.class));
        }

        @Test
        void deleteProposal_ShouldDeleteProposal() throws Exception {
                // Arrange
                when(proposalRepository.deleteProposal(anyInt()))
                                .thenReturn("Proposal deleted successfully");

                // Act & Assert
                mockMvc.perform(delete("/api/proposal/delete").param("proposalId", "1"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Proposal deleted successfully"));

                verify(proposalRepository, times(1)).deleteProposal(1);
        }

        @Test
        void getAllUserProposals_ShouldReturnList() throws Exception {
                // Arrange
                List<Proposal> proposals = Arrays.asList(testProposal);
                when(proposalRepository.getAllUserProposals(anyString())).thenReturn(proposals);

                // Act & Assert
                mockMvc.perform(get("/api/proposal/getalluser").param("userId", "test@example.com"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].proposalId")
                                                .value(testProposal.getProposalId()))
                                .andExpect(jsonPath("$[0].title").value(testProposal.getTitle()));

                verify(proposalRepository, times(1)).getAllUserProposals("test@example.com");
        }

        @Test
        void getAllTenderProposals_ShouldReturnList() throws Exception {
                // Arrange
                List<Proposal> proposals = Arrays.asList(testProposal);
                when(proposalRepository.getAllTenderProposals(anyInt())).thenReturn(proposals);

                // Act & Assert
                mockMvc.perform(get("/api/proposal/getalltender").param("tenderId", "1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].proposalId")
                                                .value(testProposal.getProposalId()))
                                .andExpect(jsonPath("$[0].title").value(testProposal.getTitle()));

                verify(proposalRepository, times(1)).getAllTenderProposals(1);
        }

        @Test
        void getProposalById_ShouldReturnProposal() throws Exception {
                // Arrange
                when(proposalRepository.getProposalById(anyInt())).thenReturn(testProposal);

                // Act & Assert
                mockMvc.perform(get("/api/proposal/getbyid").param("proposalId", "1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.proposalId")
                                                .value(testProposal.getProposalId()))
                                .andExpect(jsonPath("$.title").value(testProposal.getTitle()))
                                .andExpect(jsonPath("$.description")
                                                .value(testProposal.getDescription()))
                                .andExpect(jsonPath("$.documentLinks").isArray())
                                .andExpect(jsonPath("$.documentLinks[0]")
                                                .value("http://example.com/doc1"))
                                .andExpect(jsonPath("$.documentLinks[1]")
                                                .value("http://example.com/doc2"));

                verify(proposalRepository, times(1)).getProposalById(1);
        }

        @Test
        void getAllUserProposals_WithInvalidUserId_ShouldReturnEmptyList() throws Exception {
                // Arrange
                when(proposalRepository.getAllUserProposals("nonexistent@example.com"))
                                .thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/api/proposal/getalluser").param("userId",
                                "nonexistent@example.com")).andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$").isEmpty());

                verify(proposalRepository, times(1)).getAllUserProposals("nonexistent@example.com");
        }

        @Test
        void addDocumentLink_ShouldAddLinkToProposal() throws Exception {
                // Arrange
                when(proposalRepository.addDocumentLink(anyInt(), anyString()))
                                .thenReturn("Document link added successfully");

                String link = "http://example.com/newdoc";

                // Act & Assert
                mockMvc.perform(put("/api/proposal/addlink").param("proposalId", "1")
                                .contentType(MediaType.TEXT_PLAIN).content(link))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Document link added successfully"));

                verify(proposalRepository, times(1)).addDocumentLink(1, link);
        }

        @Test
        void removeDocumentLink_ShouldRemoveLinkFromProposal() throws Exception {
                // Arrange
                when(proposalRepository.removeDocumentLink(anyInt(), anyString()))
                                .thenReturn("Document link removed successfully");

                String link = "http://example.com/doc1";

                // Act & Assert
                mockMvc.perform(put("/api/proposal/removelink").param("proposalId", "1")
                                .contentType(MediaType.TEXT_PLAIN).content(link))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Document link removed successfully"));

                verify(proposalRepository, times(1)).removeDocumentLink(1, link);
        }

        @Test
        void addDocumentLink_ProposalNotFound_ShouldReturnErrorMessage() throws Exception {
                // Arrange
                when(proposalRepository.addDocumentLink(anyInt(), anyString()))
                                .thenReturn("Proposal not found");

                String link = "http://example.com/newdoc";

                // Act & Assert
                mockMvc.perform(put("/api/proposal/addlink").param("proposalId", "999")
                                .contentType(MediaType.TEXT_PLAIN).content(link))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Proposal not found"));

                verify(proposalRepository, times(1)).addDocumentLink(999, link);
        }

        @Test
        void removeDocumentLink_LinkNotFound_ShouldReturnErrorMessage() throws Exception {
                // Arrange
                when(proposalRepository.removeDocumentLink(anyInt(), anyString()))
                                .thenReturn("Proposal not found or link doesn't exist");

                String link = "http://example.com/nonexistent";

                // Act & Assert
                mockMvc.perform(put("/api/proposal/removelink").param("proposalId", "1")
                                .contentType(MediaType.TEXT_PLAIN).content(link))
                                .andExpect(status().isOk()).andExpect(content().string(
                                                "Proposal not found or link doesn't exist"));

                verify(proposalRepository, times(1)).removeDocumentLink(1, link);
        }
}
