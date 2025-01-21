package toonpick.app.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import toonpick.app.exception.ErrorCode;


import java.io.IOException;
import java.util.UUID;


@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    private final String bucketName;

    private final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public S3Service(AmazonS3 amazonS3,
                     @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    /**
     * aws s3에 image upload (범용)
     */
    public String uploadFile(MultipartFile file) {
        String fileName = generateFileName(file.getOriginalFilename());

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
        } catch (AmazonServiceException e){
            logger.error("{} : {}", ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3, e);
            throw new RuntimeException(ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3.getMessage());
        }

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private String generateFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}
