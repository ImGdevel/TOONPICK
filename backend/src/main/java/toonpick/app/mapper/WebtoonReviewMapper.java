package toonpick.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.WebtoonReview;

@Mapper
public interface WebtoonReviewMapper {
    WebtoonReviewMapper INSTANCE = Mappers.getMapper(WebtoonReviewMapper.class);

    @Mapping(source = "user.id", target = "userId.username")
    @Mapping(source = "user.nickname", target = "userId.nickname")
    @Mapping(source = "user.role", target = "userId.role")
    @Mapping(source = "user.profilePicture", target = "userId.profilePicture")
    WebtoonReviewDTO toDTO(WebtoonReview review);

    @Mapping(target = "user", ignore = true) // 사용자 정보를 별도로 처리
    WebtoonReview toEntity(WebtoonReviewCreateDTO reviewCreateDTO);
}
