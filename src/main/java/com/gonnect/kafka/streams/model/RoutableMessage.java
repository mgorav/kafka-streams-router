package com.gonnect.kafka.streams.model;

// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):

import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RoutableMessage {

    private long timestamp;
    private String message;
    private String topicName;
    private List<String> boundedFunctions;

    public RoutableMessage() {
    }

    public RoutableMessage(long timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

}
