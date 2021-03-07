package com.gonnect.kafka.streams.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaStreams {

    String INPUT = "master-channel-in";
    String OUTPUT = "master-channel-out";

    @Input(INPUT)
    SubscribableChannel inboundRoute();

    @Output(OUTPUT)
    MessageChannel outboundRoute();

}
