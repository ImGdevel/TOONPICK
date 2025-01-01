package toonpick.app.member.mapper;

import toonpick.app.member.dto.MemberDTO;
import toonpick.app.member.dto.MemberProfileDTO;
import toonpick.app.member.entity.Member;
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
