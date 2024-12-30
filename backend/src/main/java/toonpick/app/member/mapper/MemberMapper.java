package toonpick.app.member.mapper;

import toonpick.app.member.dto.MemberDTO;
import toonpick.app.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO userToUserDto(Member member);

    Member userDtoToUser(MemberDTO memberDTO);
}
