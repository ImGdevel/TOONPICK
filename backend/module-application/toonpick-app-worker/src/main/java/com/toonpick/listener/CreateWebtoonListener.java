package com.toonpick.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.dto.request.WebtoonCreateCommand;
import com.toonpick.dto.request.WebtoonUpdateCommand;
import com.toonpick.service.WebtoonService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateWebtoonListener {

    private final Logger logger = LoggerFactory.getLogger(CreateWebtoonListener.class);

    private final WebtoonService webtoonService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.cloud.aws.sqs.queue.webtoon-create-complete}")
    private String queueName;

    @SqsListener
    public void handle(String message) {
        try{
            WebtoonCreateCommand request = objectMapper.readValue(message, WebtoonCreateCommand.class);
            webtoonService.createWebtoon(request);
        }catch (Exception e){
            // todo : 에러 처리 (사실상 딱히 할 수 있는게 없음)
        }
    }
}
