package toonpick.app.infra.aws.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import toonpick.app.exception.ErrorCode;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;
    private final Region region;
    private final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public S3Service(S3Client s3Client,
                     @Value("${spring.cloud.aws.s3.bucket}") String bucketName,
                     @Value("${spring.cloud.aws.region.static}") String region) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = Region.of(region);
    }

    /**
     * AWS S3에 이미지 업로드 (범용)
     */
    public String uploadFile(MultipartFile file) {
        String fileName = generateFileName(file.getOriginalFilename());

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

    private String generateFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}
