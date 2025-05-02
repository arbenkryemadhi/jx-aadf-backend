package com.arben.jxaadf.filesaws;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FilesAwsController.class)
class FilesAwsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilesAwsRepository filesAwsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private FilesAws mockFilesAws;
    private MockMultipartFile pdfFile;
    private MockMultipartFile emptyFile;
    private MockMultipartFile nonPdfFile;

    @BeforeEach
    void setUp() {
        // Set up test files
        pdfFile = new MockMultipartFile("file", "test.pdf", "application/pdf",
                "PDF test content".getBytes());

        emptyFile = new MockMultipartFile("file", "empty.pdf", "application/pdf", new byte[0]);

        nonPdfFile =
                new MockMultipartFile("file", "test.jpg", "image/jpeg", "JPEG content".getBytes());

        // Create mock response from repository
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        mockFilesAws =
                new FilesAws("test.pdf", "https://test-bucket.s3.amazonaws.com/test-123456.pdf",
                        "application/pdf", currentTime, "test-bucket");
    }

    @Test
    void testUploadPdfFileSuccess() throws Exception {
        // Mock repository behavior
        when(filesAwsRepository.uploadPdfFile(any())).thenReturn(mockFilesAws);

        // Perform the request and verify response
        MvcResult result = mockMvc
                .perform(multipart("/api/files/upload/pdf").file(pdfFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("File uploaded successfully"))
                .andExpect(jsonPath("$.file").exists())
                .andExpect(jsonPath("$.file.fileName").value("test.pdf")).andReturn();

        // Verify repository was called
        verify(filesAwsRepository).uploadPdfFile(any());
    }

    @Test
    void testUploadEmptyFile() throws Exception {
        // Perform the request with an empty file
        mockMvc.perform(multipart("/api/files/upload/pdf").file(emptyFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Please select a file to upload"));

        // Verify repository was not called
        verify(filesAwsRepository, never()).uploadPdfFile(any());
    }

    @Test
    void testUploadNonPdfFile() throws Exception {
        // Mock repository behavior to throw exception for non-PDF files
        when(filesAwsRepository.uploadPdfFile(any()))
                .thenThrow(new IllegalArgumentException("Only PDF files are allowed!"));

        // Perform the request with non-PDF file
        mockMvc.perform(multipart("/api/files/upload/pdf").file(nonPdfFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Only PDF files are allowed!"));
    }

    @Test
    void testHandleIOException() throws Exception {
        // Mock repository to throw IOException
        when(filesAwsRepository.uploadPdfFile(any()))
                .thenThrow(new IOException("Failed to read file"));

        // Perform the request
        mockMvc.perform(multipart("/api/files/upload/pdf").file(pdfFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false)).andExpect(
                        jsonPath("$.message").value("Failed to upload file: Failed to read file"));
    }
}
