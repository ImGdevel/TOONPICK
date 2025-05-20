package com.toonpick.webtoon.service;


import com.toonpick.webtoon.response.RecommendWebtoonGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RecommendWebtoonGroupService {



    /**
     * 이번주 BEST 추천 그룹 반환
     */
    public RecommendWebtoonGroups getRecommendWebtoonGroups(){

        // 그냥 최근 포인트가 가장 높은 웹툰 10개 추천
        // 해당 추천 웹툰은 그냥 쿼리 반환

        return RecommendWebtoonGroups.builder().build();
    }

    /**
     * 유저 기반 추천 그룹 : Paged 기반으로 전환
     */
    public RecommendWebtoonGroups getMemberRecommendWebtoonsGroups(){
        // 서비스 기반에서 어떤 부분으로
        //

        return RecommendWebtoonGroups.builder().build();
    }



}
