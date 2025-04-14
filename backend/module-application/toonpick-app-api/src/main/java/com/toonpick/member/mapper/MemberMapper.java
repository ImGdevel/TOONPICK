package com.toonpick.member.mapper;


import com.toonpick.member.response.MemberResponseDTO;
import com.toonpick.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.toonpick.dto.MemberDTO;
import com.toonpick.member.response.MemberProfileDetailsResponseDTO;
import com.toonpick.member.response.MemberProfileResponseDTO;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO memberToMemberDTO(Member member);

    MemberResponseDTO memberToMemberResponseDTO(Member member);

    MemberProfileResponseDTO memberToProfileResponseDTO(Member member);

    MemberProfileDetailsResponseDTO memberToProfileDetailsResponseDTO(Member member);

}
