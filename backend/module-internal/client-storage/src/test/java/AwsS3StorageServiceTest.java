import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.toonpick.type.ErrorCode;
import com.toonpick.service.AwsS3StorageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class AwsS3StorageServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    private AwsS3StorageService awsS3StorageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        awsS3StorageService = new AwsS3StorageService(amazonS3, bucketName);
    }

    @DisplayName("파일을 업로드 성공 유닛 테스트")
    @Test
    void testUploadFile_Success() throws Exception {
        // Given
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String originalFileName = "test.jpg";
        String contentType = "image/jpeg";
        byte[] content = "test data".getBytes();

        Mockito.when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
        Mockito.when(mockFile.getContentType()).thenReturn(contentType);
        Mockito.when(mockFile.getSize()).thenReturn((long) content.length);
        Mockito.when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));

        String expectedBaseUrl = "https://" + bucketName + ".s3.amazonaws.com/";
        Mockito.when(amazonS3.getUrl(ArgumentMatchers.eq(bucketName), ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            String key = invocation.getArgument(1);
            return new URL(expectedBaseUrl + key);
        });

        // When
        String resultUrl = awsS3StorageService.uploadFile(mockFile);

        // Then
        Assertions.assertTrue(resultUrl.startsWith(expectedBaseUrl));
        Mockito.verify(amazonS3).putObject(ArgumentMatchers.any(PutObjectRequest.class));
    }


    @DisplayName("잘못된 파일 업로드 예외 유닛 테스트")
    @Test
    void testUploadFile_Fail_InvalidFile() throws Exception {
        // Given
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.getInputStream()).thenThrow(new IOException("Test IOException"));

        // When & Then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> awsS3StorageService.uploadFile(mockFile));
        Assertions.assertEquals(ErrorCode.IMAGE_UPLOAD_FAILED.getMessage(), exception.getMessage());
        Mockito.verify(amazonS3, Mockito.never()).putObject(ArgumentMatchers.any(PutObjectRequest.class));
    }

    @DisplayName("S3에 업로드 예외 유닛 테스트")
    @Test
    void testUploadFile_Fail_AmazonServiceException() throws Exception {
        // Given
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String fileName = "test.jpg";

        Mockito.when(mockFile.getOriginalFilename()).thenReturn(fileName);
        Mockito.when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));

        doThrow(new AmazonServiceException("AWS error"))
            .when(amazonS3)
            .putObject(ArgumentMatchers.any(PutObjectRequest.class));

        // When & Then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> awsS3StorageService.uploadFile(mockFile));
        Assertions.assertEquals(ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3.getMessage(), exception.getMessage());
    }
}
