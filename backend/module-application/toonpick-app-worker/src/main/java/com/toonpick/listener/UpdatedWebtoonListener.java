package com.toonpick.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.dto.request.WebtoonUpdateCommand;
import com.toonpick.service.WebtoonService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatedWebtoonListener {

    private final Logger logger = LoggerFactory.getLogger(UpdatedWebtoonListener.class);

    private final WebtoonService webtoonService;
    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    private String queueName;

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.webtoon-update-complete}")
    public void handle(String message) {
        try{
            logger.info("데이터 픽업");
            WebtoonUpdateCommand request = objectMapper.readValue(message, WebtoonUpdateCommand.class);
            webtoonService.updateWebtoon(request);
        }catch (Exception e){
            // todo : 에러 처리 (사실상 딱히 할 수 있는게 없음)
        }
    }
}
