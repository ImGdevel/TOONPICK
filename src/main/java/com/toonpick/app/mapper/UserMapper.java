package com.toonpick.app.mapper;

import com.toonpick.app.dto.UserDTO;
import com.toonpick.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {InterestWebtoonMapper.class, InterestAuthorMapper.class, InterestGenreMapper.class, ReviewMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDto(User user);

    User userDtoToUser(UserDTO userDTO);
}
