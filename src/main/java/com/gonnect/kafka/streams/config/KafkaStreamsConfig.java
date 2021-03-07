package com.gonnect.kafka.streams.config;

import com.gonnect.kafka.streams.stream.KafkaStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(KafkaStreams.class)
public class KafkaStreamsConfig {
}
