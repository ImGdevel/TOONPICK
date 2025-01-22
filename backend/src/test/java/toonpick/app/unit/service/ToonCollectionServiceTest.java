package toonpick.app.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.toon_collection.ToonCollection;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.dto.ToonCollectionResponseDTO;
import toonpick.app.dto.member.MemberProfileResponseDTO;
import toonpick.app.dto.webtoon.WebtoonResponseDTO;
import toonpick.app.exception.exception.ResourceNotFoundException;
import toonpick.app.mapper.ToonCollectionMapper;
import toonpick.app.repository.ToonCollectionRepository;
import toonpick.app.repository.MemberRepository;
import toonpick.app.repository.WebtoonRepository;
import toonpick.app.service.ToonCollectionService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class ToonCollectionServiceTest {

    @Mock
    private ToonCollectionRepository toonCollectionRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private ToonCollectionMapper toonCollectionMapper;

    @InjectMocks
    private ToonCollectionService toonCollectionService;

    private Member member;
    private Webtoon webtoon;
    private ToonCollection collection;
    private ToonCollectionResponseDTO toonCollectionResponseDTO;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testuser")
                .build();

        webtoon = Webtoon.builder()
                .id(1L)
                .title("Test Webtoon")
                .build();

        collection = ToonCollection.builder()
                .member(member)
                .title("My Collection")
                .build();

        toonCollectionResponseDTO = ToonCollectionResponseDTO.builder()
                .title("My Collection")
                .member(MemberProfileResponseDTO.builder()
                        .username("testuser")
                        .nickname("Test Nickname")
                        .profilePicture("profile.jpg")
                        .level(1)
                        .build())
                .webtoons(List.of(WebtoonResponseDTO.builder()
                        .id(1L)
                        .title("Test Webtoon")
                        .build()))
                .build();
    }

    @DisplayName("컬렉션을 생성하는 단위 테스트")
    @Test
    void testCreateCollection_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(toonCollectionRepository.save(any(ToonCollection.class))).thenReturn(collection);
        when(toonCollectionMapper.toonCollectionToToonCollectionResponseDTO(any(ToonCollection.class)))
                .thenReturn(toonCollectionResponseDTO);

        // when
        ToonCollectionResponseDTO result = toonCollectionService.createCollection("testuser", "My Collection");

        // then
        assertNotNull(result);
        assertEquals("My Collection", result.getTitle());
        verify(memberRepository, times(1)).findByUsername("testuser");
        verify(toonCollectionRepository, times(1)).save(any(ToonCollection.class));
        verify(toonCollectionMapper, times(1)).toonCollectionToToonCollectionResponseDTO(any(ToonCollection.class));
    }

    @DisplayName("컬렉션 생성시 회원이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testCreateCollection_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> toonCollectionService.createCollection("testuser", "My Collection"));
    }

    @DisplayName("웹툰을 컬렉션에 추가하는 단위 테스트")
    @Test
    void testAddWebtoon_Success() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));

        // when
        toonCollectionService.addWebtoon(1L, 1L);

        // then
        verify(toonCollectionRepository, times(1)).findById(1L);
        verify(webtoonRepository, times(1)).findById(1L);
        assertTrue(collection.getWebtoons().contains(webtoon));
    }

    @DisplayName("웹툰 추가 시 컬렉션이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testAddWebtoon_CollectionNotFound() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> toonCollectionService.addWebtoon(1L, 1L));
    }

    @DisplayName("웹툰 추가 시 웹툰이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testAddWebtoon_WebtoonNotFound() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> toonCollectionService.addWebtoon(1L, 1L));
    }

    @DisplayName("웹툰을 컬렉션에서 제거하는 단위 테스트")
    @Test
    void testRemoveWebtoon_Success() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));

        collection.addWebtoon(webtoon); // Adding webtoon to collection

        // when
        toonCollectionService.removeWebtoon(1L, 1L);

        // then
        verify(toonCollectionRepository, times(1)).findById(1L);
        verify(webtoonRepository, times(1)).findById(1L);
        assertFalse(collection.getWebtoons().contains(webtoon));
    }

    @DisplayName("웹툰 제거 시 컬렉션이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testRemoveWebtoon_CollectionNotFound() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> toonCollectionService.removeWebtoon(1L, 1L));
    }

    @DisplayName("웹툰 제거 시 웹툰이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testRemoveWebtoon_WebtoonNotFound() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(webtoonRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> toonCollectionService.removeWebtoon(1L, 1L));
    }

    @DisplayName("컬렉션에 여러 웹툰을 추가하는 단위 테스트")
    @Test
    void testAddMultipleWebtoons_Success() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(webtoonRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(webtoon));

        // when
        toonCollectionService.addMultipleWebtoons(1L, List.of(1L, 2L));

        // then
        verify(toonCollectionRepository, times(1)).findById(1L);
        verify(webtoonRepository, times(1)).findAllById(List.of(1L, 2L));
        assertTrue(collection.getWebtoons().containsAll(List.of(webtoon)));
    }

    @DisplayName("컬렉션 삭제 성공 테스트")
    @Test
    void testDeleteCollection_Success() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        // when
        toonCollectionService.deleteCollection(1L);

        // then
        verify(toonCollectionRepository, times(1)).findById(1L);
        verify(toonCollectionRepository, times(1)).delete(collection);
    }

    @DisplayName("컬렉션 제목을 변경하는 단위 테스트")
    @Test
    void testUpdateCollectionTitle_Success() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        // when
        toonCollectionService.updateCollectionTitle(1L, "Updated Title");

        // then
        verify(toonCollectionRepository, times(1)).findById(1L);
        assertEquals("Updated Title", collection.getTitle());
    }

    @DisplayName("컬렉션 제목 변경 시 컬렉션이 존재하지 않으면 예외가 발생하는 단위 테스트")
    @Test
    void testUpdateCollectionTitle_CollectionNotFound() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> toonCollectionService.updateCollectionTitle(1L, "Updated Title"));
    }

    @DisplayName("컬렉션에서 여러 웹툰을 제거하는 단위 테스트")
    @Test
    void testRemoveMultipleWebtoons_Success() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(webtoonRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(webtoon));

        collection.addWebtoon(webtoon);

        // when
        toonCollectionService.removeMultipleWebtoons(1L, List.of(1L, 2L));

        // then
        verify(toonCollectionRepository, times(1)).findById(1L);
        verify(webtoonRepository, times(1)).findAllById(List.of(1L, 2L));
        assertFalse(collection.getWebtoons().contains(webtoon));
    }

    @DisplayName("컬렉션에서 모든 웹툰을 제거하는 단위 테스트")
    @Test
    void testClearAllWebtoons_Success() {
        // given
        when(toonCollectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        collection.addWebtoon(webtoon);

        // when
        toonCollectionService.clearAllWebtoons(1L);

        // then
        verify(toonCollectionRepository, times(1)).findById(1L);
        assertTrue(collection.getWebtoons().isEmpty());
    }

    @DisplayName("회원의 컬렉션을 조회하는 단위 테스트")
    @Test
    void testGetCollectionsByMember_Success() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.of(member));
        when(toonCollectionRepository.findByMember(member)).thenReturn(List.of(collection));
        when(toonCollectionMapper.toonCollectionsToToonCollectionResponseDTOs(anyList()))
                .thenReturn(List.of(toonCollectionResponseDTO));

        // when
        List<ToonCollectionResponseDTO> result = toonCollectionService.getCollectionsByMember("testuser");

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @DisplayName("회원이 없을 때 컬렉션 조회시 예외가 발생하는 단위 테스트")
    @Test
    void testGetCollectionsByMember_MemberNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> toonCollectionService.getCollectionsByMember("testuser"));
    }
}
