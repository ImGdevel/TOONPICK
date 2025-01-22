package toonpick.app.unit.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import toonpick.app.exception.ErrorCode;
import toonpick.app.service.S3Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    private S3Service s3Service;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(amazonS3, bucketName);
    }

    @DisplayName("파일을 업로드 성공 유닛 테스트")
    @Test
    void testUploadFile_Success() throws Exception {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        String originalFileName = "test.jpg";
        String contentType = "image/jpeg";
        byte[] content = "test data".getBytes();

        when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
        when(mockFile.getContentType()).thenReturn(contentType);
        when(mockFile.getSize()).thenReturn((long) content.length);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));

        String expectedBaseUrl = "https://" + bucketName + ".s3.amazonaws.com/";
        when(amazonS3.getUrl(eq(bucketName), anyString())).thenAnswer(invocation -> {
            String key = invocation.getArgument(1);
            return new URL(expectedBaseUrl + key);
        });

        // When
        String resultUrl = s3Service.uploadFile(mockFile);

        // Then
        assertTrue(resultUrl.startsWith(expectedBaseUrl));
        verify(amazonS3).putObject(any(PutObjectRequest.class));
    }


    @DisplayName("잘못된 파일 업로드 예외 유닛 테스트")
    @Test
    void testUploadFile_Fail_InvalidFile() throws Exception {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenThrow(new IOException("Test IOException"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.uploadFile(mockFile));
        assertEquals(ErrorCode.IMAGE_UPLOAD_FAILED.getMessage(), exception.getMessage());
        verify(amazonS3, never()).putObject(any(PutObjectRequest.class));
    }

    @DisplayName("S3에 업로드 예외 유닛 테스트")
    @Test
    void testUploadFile_Fail_AmazonServiceException() throws Exception {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        String fileName = "test.jpg";

        when(mockFile.getOriginalFilename()).thenReturn(fileName);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));

        doThrow(new AmazonServiceException("AWS error"))
            .when(amazonS3)
            .putObject(any(PutObjectRequest.class));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.uploadFile(mockFile));
        assertEquals(ErrorCode.IMAGE_UPLOAD_FAILED_TO_S3.getMessage(), exception.getMessage());
    }
}
