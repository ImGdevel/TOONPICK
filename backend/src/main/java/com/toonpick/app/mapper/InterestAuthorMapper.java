package com.toonpick.app.mapper;

import com.toonpick.app.dto.InterestAuthorDTO;
import com.toonpick.app.entity.InterestAuthor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, AuthorMapper.class})
public interface InterestAuthorMapper {
    InterestAuthorMapper INSTANCE = Mappers.getMapper(InterestAuthorMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "author", target = "author")
    InterestAuthorDTO interestAuthorToInterestAuthorDto(InterestAuthor interestAuthor);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "author", target = "author")
    InterestAuthor interestAuthorDtoToInterestAuthor(InterestAuthorDTO interestAuthorDTO);
}