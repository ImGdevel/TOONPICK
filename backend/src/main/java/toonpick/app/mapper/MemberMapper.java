package toonpick.app.mapper;

import toonpick.app.dto.member.MemberDTO;
import toonpick.app.dto.member.MemberProfileDetailsResponseDTO;
import toonpick.app.dto.member.MemberProfileResponseDTO;
import toonpick.app.dto.member.MemberResponseDTO;
import toonpick.app.domain.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO memberToMemberDTO(Member member);

    MemberResponseDTO memberToMemberResponseDTO(Member member);

    MemberProfileResponseDTO memberToProfileResponseDTO(Member member);

    MemberProfileDetailsResponseDTO memberToProfileDetailsResponseDTO(Member member);

}
