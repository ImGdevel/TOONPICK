package toonpick.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import toonpick.dto.MemberDTO;
import toonpick.dto.MemberProfileDetailsResponseDTO;
import toonpick.dto.MemberProfileResponseDTO;
import toonpick.dto.MemberResponseDTO;
import toonpick.entity.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO memberToMemberDTO(Member member);

    MemberResponseDTO memberToMemberResponseDTO(Member member);

    MemberProfileResponseDTO memberToProfileResponseDTO(Member member);

    MemberProfileDetailsResponseDTO memberToProfileDetailsResponseDTO(Member member);

}
