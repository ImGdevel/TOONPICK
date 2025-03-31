package com.toonpick.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberService memberService;
    private final AwsS3StorageService awsS3StorageService;

    private final Logger logger = LoggerFactory.getLogger(MemberProfileService.class);

    /**
     * 프로필 사진 업로드
     */
    public String uploadProfileImage(String username,MultipartFile file){

        // todo : 업로드에 실패했을 때 로직도 작성할 것
        // todo : 규격에 맞는 이미지인지 확인 할 것
        // todo : 이미지 전처리 과정도 포함할 것
        String fileUrl = awsS3StorageService.uploadFile(file);

        // 이미지 갱신
        memberService.updateProfileImage(username, fileUrl);
        return fileUrl;
    }


}
