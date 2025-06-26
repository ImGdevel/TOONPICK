package com.toonpick.worker.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 웬툰 상태 (연재/완결/휴재/연재중단) 변경
 */
@Service
@RequiredArgsConstructor
public class WebtoonStatusUpdateTask {

    /**
     * 완결 기준(에피소드 종료 후 7일이후)에 부합하는
     * 웹툰을 찾아 상태를 [완결] 웹툰으로 전환
     */
    public void updateCompletedWebtoons(){

    }

    /**
     * 공식적으로 연재중단, 혹은 별도 복귀 공지 없지 6개월 이상 휴재 상태
     * 웹툰을 찾아 상태를 [연재중단] 으로 전환
     */
    public void updateCanceledWebtoons(){

    }

}
