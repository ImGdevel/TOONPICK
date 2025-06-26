import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.toonpick.common.type.ErrorCode;
import com.toonpick.internal.storage.service.AwsS3StorageService;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class AwsS3StorageServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private AwsS3StorageService storageService;

    @Mock
    private MultipartFile multipartFile;

    private AwsS3StorageService awsS3StorageService;

    private final String bucketName = "test-bucket";
    private final String region = "ap-northeast-2";

    @BeforeEach
    void setUp() {
        awsS3StorageService = new AwsS3StorageService(s3Client, bucketName, region);
    }

    @Test
    @DisplayName("S3 파일 업로드 성공 시 URL 반환")
    void testUploadFile_Success() throws IOException {
        // given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-image.png");
        when(mockFile.getContentType()).thenReturn("image/png");
        when(mockFile.getBytes()).thenReturn("fake-image-content".getBytes());

        // when
        String url = storageService.uploadFile(mockFile);

        // then
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertThat(url).contains("https://test-bucket.s3.ap-northeast-2.amazonaws.com/");

        Mockito.mockingDetails(s3Client).printInvocations();
    }


    @Test
    @DisplayName("IOException 발생 시 예외 발생")
    void testUploadFile_IOException() throws Exception {
        // given
        when(multipartFile.getOriginalFilename()).thenReturn("bad-file.png");
        when(multipartFile.getBytes()).thenThrow(new IOException("Boom"));

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> awsS3StorageService.uploadFile(multipartFile));
        assertThat(ex.getMessage()).isEqualTo(ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
    }

    @Test
    @DisplayName("S3Client 실패 시 예외 발생")
    void testUploadFile_S3ClientFailure() throws Exception {
        // given
        when(multipartFile.getOriginalFilename()).thenReturn("s3-fail.png");
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getBytes()).thenReturn("fail-data".getBytes());

        doThrow(new RuntimeException("S3 down")).when(s3Client)
            .putObject(any(PutObjectRequest.class), any(RequestBody.class));

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> awsS3StorageService.uploadFile(multipartFile));
        assertThat(ex.getMessage()).isEqualTo(ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3.getMessage());
    }
}
