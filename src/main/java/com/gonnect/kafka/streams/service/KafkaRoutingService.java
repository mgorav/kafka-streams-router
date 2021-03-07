package com.gonnect.kafka.streams.service;

import com.gonnect.kafka.streams.model.RoutableMessage;
import com.gonnect.kafka.streams.stream.KafkaStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class KafkaRoutingService {

    @Autowired
    private KafkaStreams kafkaStreams;

    public void router(final RoutableMessage routableMessage) {
        log.info("Sending routableMessage {}", routableMessage);

        MessageChannel messageChannel = kafkaStreams.outboundRoute();
        boolean sent = messageChannel.send(MessageBuilder
                .withPayload(routableMessage)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());

        log.info("Sent {} routable message {}", sent, routableMessage);
    }

}
