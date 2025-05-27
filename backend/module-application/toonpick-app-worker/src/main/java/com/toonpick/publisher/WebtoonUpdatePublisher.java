package com.toonpick.publisher;

import com.toonpick.dto.message.WebtoonUpdateCommandMessage;
import com.toonpick.dto.command.WebtoonUpdateCommand;
import com.toonpick.service.AwsSqsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebtoonUpdatePublisher {
    private final AwsSqsPublisher publisher;

    @Value("${spring.cloud.aws.sqs.queue.webtoon-update-request}")
    private String queueName;

    public void publishRequests(List<WebtoonUpdateCommand> webtoonUpdateCommands) {

        WebtoonUpdateCommandMessage msg = WebtoonUpdateCommandMessage.builder()
                .size(webtoonUpdateCommands.size())
                .requests(webtoonUpdateCommands)
                .build();


        publisher.publisher(queueName, msg);
    }
}
