package com.arben.jxaadf.filesaws;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@WebMvcTest(FilesAwsController.class)
public class FilesAwsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilesAwsRepository filesAwsRepository;

    private MockMultipartFile validPdfFile;
    private MockMultipartFile emptyFile;
    private FilesAws mockFilesAws;

    @BeforeEach
    void setUp() {
        // Sample valid PDF file
        validPdfFile = new MockMultipartFile("file", "test.pdf", "application/pdf",
                "PDF content".getBytes());

        // Empty file for testing validation
        emptyFile = new MockMultipartFile("file", "empty.pdf", "application/pdf", new byte[0]);

        // Mock response object
        mockFilesAws = new FilesAws("test.pdf",
                "https://bucket-name.s3.amazonaws.com/20250503-123456-abcd1234.pdf",
                "application/pdf", "2025-05-03T12:34:56", "bucket-name");
    }

    @Test
    void uploadPdfFile_WithValidFile_ShouldReturnSuccessAndUrl() throws Exception {
        // Mock repository behavior for successful upload
        when(filesAwsRepository.uploadPdfFile(any(MultipartFile.class))).thenReturn(mockFilesAws);

        // Perform API call
        mockMvc.perform(multipart("/api/files/upload/pdf").file(validPdfFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isOk())
                .andExpect(content().string(mockFilesAws.getFileUrl()));

        // Verify repository method was called
        verify(filesAwsRepository, times(1)).uploadPdfFile(any(MultipartFile.class));
    }

    @Test
    void uploadPdfFile_WithEmptyFile_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/files/upload/pdf").file(emptyFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isBadRequest())
                .andExpect(content().string("Please select a file to upload"));
    }

    @Test
    void uploadPdfFile_WithRepositoryException_ShouldReturnBadRequest() throws Exception {
        // Mock repository throwing an IllegalArgumentException (invalid file type)
        when(filesAwsRepository.uploadPdfFile(any(MultipartFile.class)))
                .thenThrow(new IllegalArgumentException("Only PDF files are allowed!"));

        mockMvc.perform(multipart("/api/files/upload/pdf").file(validPdfFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isBadRequest())
                .andExpect(content().string("Only PDF files are allowed!"));
    }

    @Test
    void uploadPdfFile_WithIOException_ShouldReturnInternalServerError() throws Exception {
        // Mock repository throwing an IOException
        when(filesAwsRepository.uploadPdfFile(any(MultipartFile.class)))
                .thenThrow(new IOException("Failed to process file"));

        mockMvc.perform(multipart("/api/files/upload/pdf").file(validPdfFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to upload file: Failed to process file"));
    }
}
