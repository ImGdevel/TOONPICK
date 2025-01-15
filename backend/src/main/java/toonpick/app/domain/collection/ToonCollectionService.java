package toonpick.app.domain.collection;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.repository.WebtoonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToonCollectionService {

    private final ToonCollectionRepository toonCollectionRepository;
    private final WebtoonRepository webtoonRepository;

    @Transactional
    public ToonCollection createCollection(Member member, String title) {
        ToonCollection collection = ToonCollection.builder()
            .member(member)
            .title(title)
            .build();
        return toonCollectionRepository.save(collection);
    }

    @Transactional
    public void addWebtoon(Long collectionId, Long webtoonId) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new EntityNotFoundException("Webtoon not found"));
        collection.addWebtoon(webtoon);
    }

    @Transactional
    public void removeWebtoon(Long collectionId, Long webtoonId) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new EntityNotFoundException("Webtoon not found"));
        collection.removeWebtoon(webtoon);
    }

    @Transactional
    public void addMultipleWebtoons(Long collectionId, List<Long> webtoonIds) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        List<Webtoon> webtoons = webtoonRepository.findAllById(webtoonIds);
        collection.getWebtoons().addAll(webtoons);
    }

    @Transactional
    public void deleteCollection(Long collectionId) {
        toonCollectionRepository.deleteById(collectionId);
    }

    @Transactional
    public void updateCollectionTitle(Long collectionId, String newTitle) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        collection.updateTitle(newTitle);
    }

    @Transactional
    public void removeMultipleWebtoons(Long collectionId, List<Long> webtoonIds) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        List<Webtoon> webtoons = webtoonRepository.findAllById(webtoonIds);
        collection.getWebtoons().removeAll(webtoons);
    }

    @Transactional
    public void clearAllWebtoons(Long collectionId) {
        ToonCollection collection = toonCollectionRepository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Collection not found"));
        collection.clearWebtoons();
    }

    @Transactional(readOnly = true)
    public List<ToonCollection> getCollectionsByMember(Member member) {
        return toonCollectionRepository.findByMember(member);
    }
}
