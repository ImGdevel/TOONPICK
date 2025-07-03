package com.toonpick.webtoon.controller;

import com.toonpick.webtoon.response.RecommendWebtoonGroups;
import com.toonpick.webtoon.service.RecommendWebtoonGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/public/webtoons/recommend")
@RequiredArgsConstructor
public class RecommendWebtoonGroupsController {


    private final RecommendWebtoonGroupService recommendWebtoonGroupService;

    // todo : 추천 그룹 추천 그룹이 디스플레이 되는 방식은 어떻게 하는게 좋을까?
    // todo : 로그인이 안되었다면? 중요도가 높은

    /**
     * 추천 순위
     * 가장 상단 1위 : 이번주 BEST 웹툰
     * 2순위 : (로그인시) 사용자 활동 기반으로 추천하는 웹툰 순위
     * 3순위 : (로그인시) 사용자와 겹치는 카테고리의 웹툰 리스트 추천 (예를 들어 남성+나이대 등등)
     * 4순위 : 완결 웹툰 중 추천할만한 인기 웹툰
     * 5순위 : 완전 랜덤
     */

    @GetMapping
    ResponseEntity<List<RecommendWebtoonGroups>> getRecommendGroups(){

        RecommendWebtoonGroups group1 = recommendWebtoonGroupService.getRecommendWebtoonGroups();
        List<RecommendWebtoonGroups> groups = new ArrayList<>();
        groups.add(group1);

        return ResponseEntity.ok(groups);
    }



}
