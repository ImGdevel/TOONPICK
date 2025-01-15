package toonpick.app.mapper;

import toonpick.app.dto.MemberDTO;
import toonpick.app.dto.MemberProfileDTO;
import toonpick.app.domain.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO memberToMemberDto(Member member);

    Member memberDtoToMember(MemberDTO memberDTO);

    MemberProfileDTO memberToProfileDto(Member member);

    Member profileDtoToMember(MemberProfileDTO profileDTO);
}
