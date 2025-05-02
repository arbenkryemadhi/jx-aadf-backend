package com.arben.jxaadf.filesaws;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FilesAwsTest {

    @Test
    void testFilesAwsConstructorAndGetters() {
        // Setup test data
        String fileName = "test.pdf";
        String fileUrl = "https://s3.amazonaws.com/test-bucket/test.pdf";
        String fileType = "application/pdf";
        String uploadDate = "2025-05-03T10:15:30";
        String bucketName = "test-bucket";

        // Create FilesAws object using the constructor
        FilesAws filesAws = new FilesAws(fileName, fileUrl, fileType, uploadDate, bucketName);

        // Verify all properties are correctly set
        assertEquals(fileName, filesAws.getFileName());
        assertEquals(fileUrl, filesAws.getFileUrl());
        assertEquals(fileType, filesAws.getFileType());
        assertEquals(uploadDate, filesAws.getUploadDate());
        assertEquals(bucketName, filesAws.getBucketName());
    }

    @Test
    void testFilesAwsSetters() {
        // Create empty FilesAws object
        FilesAws filesAws = new FilesAws();

        // Setup test data
        String fileName = "test2.pdf";
        String fileUrl = "https://s3.amazonaws.com/test-bucket/test2.pdf";
        String fileType = "application/pdf";
        String uploadDate = "2025-05-03T11:20:30";
        String bucketName = "test-bucket-2";

        // Set properties using setters
        filesAws.setFileName(fileName);
        filesAws.setFileUrl(fileUrl);
        filesAws.setFileType(fileType);
        filesAws.setUploadDate(uploadDate);
        filesAws.setBucketName(bucketName);

        // Verify all properties are correctly set
        assertEquals(fileName, filesAws.getFileName());
        assertEquals(fileUrl, filesAws.getFileUrl());
        assertEquals(fileType, filesAws.getFileType());
        assertEquals(uploadDate, filesAws.getUploadDate());
        assertEquals(bucketName, filesAws.getBucketName());
    }
}
