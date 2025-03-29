package toonpick.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import toonpick.message.SQSMessage;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
@Slf4j
public class SQSMessageSender {

    private final SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.queue.request.name}")
    private String queueName;

    public <T extends SQSMessage> void sendMessage(T message) {
        try {
            String jsonMessage = message.toJson();
            sqsTemplate.send(to -> to.queue(queueName).payload(jsonMessage));
            log.info("Message sent to SQS : {}",  jsonMessage);
        } catch (Exception e) {
            log.error("Failed to send message to SQS ", e);
        }
    }
}
