package com.toonpick.worker.task.executor.impl;

import com.toonpick.common.exception.BadRequestException;
import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.dto.payload.WebtoonCrawItem;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import com.toonpick.worker.infrastructure.messaging.publisher.WebtoonUpdatePublisher;
import com.toonpick.worker.task.context.TaskContext;
import com.toonpick.worker.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonCreateTaskExecutor implements TaskExecutor {

    final private WebtoonUpdatePublisher publisher;

    @Override
    public void execute(TaskContext context) {
        // todo : 웹툰을 생성 작업을 시행한다.

        try{

            List<WebtoonTriggerRequest> requests = (List<WebtoonTriggerRequest>) context.getData();

            // todo : context는 모든 요청을 가져오는가?
            // todo : 그리고 그 요청을 배치로 나누어 작업 하는가?

            List<WebtoonCrawItem> webtoonCrawItems = requests.stream()
                    .map(this::mapToWebtoonCrawItem)
                    .collect(Collectors.toList());

            publisher.sendWebtoonUpdateRequest(webtoonCrawItems);


        } catch (Exception e) {
            // todo : 실패 작업 재실행 로직
        }
    }

    /**
     * 전송가능한 형태로 매핑
     * todo : 적절한 위치로 (Mapper등) 이동
     */
    private WebtoonCrawItem mapToWebtoonCrawItem(WebtoonTriggerRequest request) {
        try {
            return WebtoonCrawItem.builder()
                    .id(Long.valueOf(request.getId()))
                    .platform(request.getPlatform())
                    .url(request.getWebtoon_url())
                    .build();
        } catch (NumberFormatException e) {
            log.error("잘못된 ID 형식: {}", request.getId());
            throw new BadRequestException("잘못된 ID 형식: " + request.getId());
        }
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.WEBTOON_ALL_CRAWL;
    }

}
