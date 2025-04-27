package com.toonpick.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.toonpick.type.ErrorCode;

import java.io.IOException;

@Service
public class AwsS3StorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final Region region;
    private final Logger logger = LoggerFactory.getLogger(AwsS3StorageService.class);

    public AwsS3StorageService(S3Client s3Client,
                               @Value("${spring.cloud.aws.s3.bucket}") String bucketName,
                               @Value("${spring.cloud.aws.region.static}") String region) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = Region.of(region);
    }

    /**
     * AWS S3에 파일 형태의 데이터 업로드
     */
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
        } catch (Exception e) {
            logger.error("{} : {}", ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3, e);
            throw new RuntimeException(ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3.getMessage());
        }

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), fileName);
    }

    /**
     * AWS S3에 byte 형태의 데이터 업로드
     */
    public String uploadBytes(byte[] bytes, String fileName, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        } catch (Exception e) {
            logger.error("{} : {}", ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3, e);
            throw new RuntimeException(ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3.getMessage());
        }

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), fileName);
    }

}
