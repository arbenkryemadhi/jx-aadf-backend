package com.arben.jxaadf.filesaws;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/files")
public class FilesAwsController {

    private final FilesAwsRepository filesAwsRepository;

    public FilesAwsController(FilesAwsRepository filesAwsRepository) {
        this.filesAwsRepository = filesAwsRepository;
    }

    @PostMapping(path = "/upload/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPdfFile(@RequestParam("file") MultipartFile file) {
        try {
            // Validate that file is not empty
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Please select a file to upload");
            }

            // Upload file to S3
            FilesAws uploadedFile = filesAwsRepository.uploadPdfFile(file);

            // Return only the file URL as a string
            return ResponseEntity.ok(uploadedFile.getFileUrl());

        } catch (IllegalArgumentException e) {
            // Handle invalid file format
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }
}
