package toonpick.app.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.review.dto.WebtoonReviewCreateDTO;
import toonpick.app.review.dto.WebtoonReviewDTO;
import toonpick.app.review.entity.WebtoonReview;

@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);

    @Mapping(source = "member.id", target = "memberId.username")
    @Mapping(source = "member.nickname", target = "memberId.nickname")
    @Mapping(source = "member.role", target = "memberId.role")
    @Mapping(source = "member.profilePicture", target = "memberId.profilePicture")
    WebtoonReviewDTO toDTO(WebtoonReview review);

    @Mapping(target = "member", ignore = true) // 사용자 정보를 별도로 처리
    WebtoonReview toEntity(WebtoonReviewCreateDTO reviewCreateDTO);
}
