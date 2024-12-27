package toonpick.app.mapper;

import toonpick.app.dto.MemberDTO;
import toonpick.app.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO userToUserDto(Member member);

    Member userDtoToUser(MemberDTO memberDTO);
}
