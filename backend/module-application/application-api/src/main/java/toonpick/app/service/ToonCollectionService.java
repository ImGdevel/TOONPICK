package toonpick.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.domain.toon_collection.ToonCollection;
import toonpick.app.dto.ToonCollectionResponseDTO;
import toonpick.type.ErrorCode;
import toonpick.exception.ResourceNotFoundException;
import toonpick.app.mapper.ToonCollectionMapper;
import toonpick.app.repository.MemberRepository;
import toonpick.app.repository.ToonCollectionRepository;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.repository.WebtoonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToonCollectionService {

    private final ToonCollectionRepository toonCollectionRepository;
    private final ToonCollectionMapper toonCollectionMapper;
    private final WebtoonRepository webtoonRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ToonCollectionResponseDTO createCollection(String username, String title) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        ToonCollection collection = ToonCollection.builder()
            .member(member)
            .title(title)
            .build();
        toonCollectionRepository.save(collection);

        return toonCollectionMapper.toonCollectionToToonCollectionResponseDTO(collection);
    }

    @Transactional
    public void addWebtoon(Long collectionId, Long webtoonId) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COLLECTION_NOT_FOUND, collectionId));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));
        collection.addWebtoon(webtoon);
    }

    @Transactional
    public void removeWebtoon(Long collectionId, Long webtoonId) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COLLECTION_NOT_FOUND, collectionId));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));
        collection.removeWebtoon(webtoon);
    }

    @Transactional
    public void addMultipleWebtoons(Long collectionId, List<Long> webtoonIds) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COLLECTION_NOT_FOUND, collectionId));
        List<Webtoon> webtoons = webtoonRepository.findAllById(webtoonIds);
        collection.getWebtoons().addAll(webtoons);
    }

    @Transactional
    public void deleteCollection(Long collectionId) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COLLECTION_NOT_FOUND, collectionId));
        toonCollectionRepository.delete(collection);
    }

    @Transactional
    public void updateCollectionTitle(Long collectionId, String newTitle) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COLLECTION_NOT_FOUND, collectionId));
        collection.updateTitle(newTitle);
    }

    @Transactional
    public void removeMultipleWebtoons(Long collectionId, List<Long> webtoonIds) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COLLECTION_NOT_FOUND, collectionId));
        List<Webtoon> webtoons = webtoonRepository.findAllById(webtoonIds);
        collection.getWebtoons().removeAll(webtoons);
    }

    @Transactional
    public void clearAllWebtoons(Long collectionId) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COLLECTION_NOT_FOUND, collectionId));
        collection.clearWebtoons();
    }

    @Transactional(readOnly = true)
    public List<ToonCollectionResponseDTO> getCollectionsByMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));

        List<ToonCollection> toonCollections = toonCollectionRepository.findByMember(member);

        return toonCollectionMapper.toonCollectionsToToonCollectionResponseDTOs(toonCollections);
    }
}
