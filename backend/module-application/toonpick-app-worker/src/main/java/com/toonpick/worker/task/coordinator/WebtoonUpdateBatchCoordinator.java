package com.toonpick.worker.task.coordinator;


import com.toonpick.worker.task.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 배치 프로세스를 통합 및 순서를 제어하는 오케스트레이터
 */
@Service
@RequiredArgsConstructor
public class WebtoonUpdateBatchCoordinator {

    private final WebtoonUpdateDispatchTask webtoonUpdateDispatchTask;
    private final PopularWebtoonRankingTask popularWebtoonRankingTask;
    private final SimilarWebtoonMappingTask similarWebtoonMappingTask;
    private final WebtoonStatusUpdateTask webtoonStatusUpdateTask;
    private final WebtoonStatisticsAggregationTask webtoonStatisticsAggregationTask;

    /**
     * 모든 배치 작업 실행
     */
    public void executeAllBatches(){

    }

    /**
     * 웹툰 정보 업데이트 작업 실행
     */
    public void executeMetadataSync(){
        webtoonUpdateDispatchTask.dispatchEpisodeUpdateRequests();
    }


    // todo : 그 외 필요한 작업 나열

    /**
     * 5분마다 실행되는 상태 정기 업데이트
     */
    public void executeRegularStatusSync() {
        webtoonStatusUpdateTask.updateCompletedWebtoons();
        webtoonStatusUpdateTask.updateCanceledWebtoons();
    }

}
