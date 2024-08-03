package toonpick.app.mapper;

import toonpick.app.dto.UserDTO;
import toonpick.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {InterestWebtoonMapper.class, InterestAuthorMapper.class, InterestGenreMapper.class, ReviewMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDto(User user);

    User userDtoToUser(UserDTO userDTO);
}
