package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toonpick.app.service.S3Service;

@RestController
@RequestMapping("/api/public/image")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("image") MultipartFile file) {

        logger.info("file name {}", file.getName());

        String fileUrl = s3Service.uploadFile(file);

        logger.info("result: {}", fileUrl);

        return ResponseEntity.ok(fileUrl);
    }
}
