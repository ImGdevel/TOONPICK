package com.toonpick.service;

import com.toonpick.message.SQSMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.awspring.cloud.sqs.operations.SqsTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsSqsPublisher {

    private final SqsTemplate sqsTemplate;

    public <T extends SQSMessage> void publisher(String queueName , T message) {
        try {
            String jsonMessage = message.toJson();
            sqsTemplate.send(to -> to.queue(queueName).payload(jsonMessage));
            log.info("Message sent to SQS : {}",  jsonMessage);
        } catch (Exception e) {
            log.error("Failed to send message to SQS ", e);
        }
    }
}
