package toonpick.app.unit.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import toonpick.app.batch.WebtoonItemReader;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.WebtoonRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class WebtoonItemReaderTest {

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private WebtoonMapper webtoonMapper;

    @InjectMocks
    private WebtoonItemReader itemReader;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(itemReader, "pageSize", 100);
        ReflectionTestUtils.setField(itemReader, "currentPage", 0);
    }

    @Test
    @DisplayName("Webtoon 데이터를 올바르게 읽어오는지 테스트")
    void testRead() {
        // given
        List<Webtoon> mockWebtoons = List.of(mock(Webtoon.class));
        when(webtoonRepository.findWebtoonsForUpdate(any(), any(), any())).thenReturn(mockWebtoons);
        when(webtoonMapper.webtoonToWebtoonUpdateRequest(any())).thenReturn(new WebtoonUpdateRequest());

        // when
        List<WebtoonUpdateRequest> result = itemReader.read();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(webtoonRepository, times(1)).findWebtoonsForUpdate(any(), any(), any());
    }
}
