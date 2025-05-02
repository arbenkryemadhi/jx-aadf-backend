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

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/files")
public class FilesAwsController {

    private final FilesAwsRepository filesAwsRepository;

    public FilesAwsController(FilesAwsRepository filesAwsRepository) {
        this.filesAwsRepository = filesAwsRepository;
    }

    @PostMapping(path = "/upload/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadPdfFile(
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate that file is not empty
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Please select a file to upload");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Upload file to S3
            FilesAws uploadedFile = filesAwsRepository.uploadPdfFile(file);

            // Create success response
            response.put("success", true);
            response.put("message", "File uploaded successfully");
            response.put("file", uploadedFile);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle invalid file format
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // Handle other exceptions
            response.put("success", false);
            response.put("message", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
