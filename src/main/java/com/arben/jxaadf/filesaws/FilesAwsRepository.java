package com.arben.jxaadf.filesaws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Repository
public class FilesAwsRepository {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public FilesAwsRepository(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public FilesAws uploadPdfFile(MultipartFile file) throws IOException {
        // Validate file is a PDF
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed!");
        }

        // Generate unique file name
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = generateUniqueFileName(originalFileName);

        // Upload file to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName)
                .key(uniqueFileName).contentType("application/pdf").build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // Create file URL
        String fileUrl = s3Client.utilities()
                .getUrl(GetUrlRequest.builder().bucket(bucketName).key(uniqueFileName).build())
                .toString();

        // Create and return FilesAws object
        return new FilesAws(originalFileName, fileUrl, "application/pdf",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), bucketName);
    }

    private String generateUniqueFileName(String originalFileName) {
        String timestamp =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String randomUUID = UUID.randomUUID().toString().substring(0, 8);
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        return timestamp + "-" + randomUUID + extension;
    }
}
