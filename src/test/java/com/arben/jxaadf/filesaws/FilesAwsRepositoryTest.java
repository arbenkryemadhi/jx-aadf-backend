package com.arben.jxaadf.filesaws;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class FilesAwsRepositoryTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Utilities s3Utilities;

    @InjectMocks
    private FilesAwsRepository filesAwsRepository;

    @Captor
    private ArgumentCaptor<PutObjectRequest> putObjectRequestCaptor;

    @Captor
    private ArgumentCaptor<RequestBody> requestBodyCaptor;

    private final String TEST_BUCKET_NAME = "test-bucket";

    @BeforeEach
    void setUp() {
        // Set the bucket name using reflection since we don't have access to the actual
        // application.properties
        ReflectionTestUtils.setField(filesAwsRepository, "bucketName", TEST_BUCKET_NAME);

        // Mock the S3Utilities behavior
        when(s3Client.utilities()).thenReturn(s3Utilities);
    }

    @Test
    void testUploadPdfFileSuccess() throws IOException {
        // Create test PDF file
        byte[] fileContent = "PDF test content".getBytes();
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "test-document.pdf", "application/pdf", fileContent);

        // Mock S3 URL response
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/test-document.pdf";
        when(s3Utilities.getUrl(any(GetUrlRequest.class))).thenReturn(new URL(expectedUrl));

        // Execute the method being tested
        FilesAws result = filesAwsRepository.uploadPdfFile(mockFile);

        // Verify the S3 client was called to upload the file
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        // Verify the returned object has the correct properties
        assertEquals("test-document.pdf", result.getFileName());
        assertEquals(expectedUrl, result.getFileUrl());
        assertEquals("application/pdf", result.getFileType());
        assertNotNull(result.getUploadDate());
        assertEquals(TEST_BUCKET_NAME, result.getBucketName());
    }



    @Test
    void testGenerateUniqueFileName() throws Exception {
        // Create test file
        MultipartFile mockFile = new MockMultipartFile("file", "original-name.pdf",
                "application/pdf", "test content".getBytes());

        // Mock S3 URL response
        when(s3Utilities.getUrl(any(GetUrlRequest.class))).thenReturn(
                new URL("https://test-bucket.s3.amazonaws.com/some-generated-name.pdf"));

        // Upload file to trigger the filename generation
        filesAwsRepository.uploadPdfFile(mockFile);

        // Capture the PutObjectRequest to verify the key format
        verify(s3Client).putObject(putObjectRequestCaptor.capture(), any(RequestBody.class));

        // Get the captured key and validate its format
        String key = putObjectRequestCaptor.getValue().key();

        // Verify it has the PDF extension
        assertTrue(key.endsWith(".pdf"), "Generated filename should end with .pdf");

        // Verify format includes date pattern and random string
        // Format should be something like: 20250503-123456-abcd1234.pdf
        String pattern = "\\d{8}-\\d{6}-[0-9a-f]{8}\\.pdf";
        assertTrue(key.matches(pattern), "Generated key doesn't match expected pattern. Generated: "
                + key + ", Expected pattern: " + pattern);
    }
}
