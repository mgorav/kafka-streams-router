package com.gonnect.kafka.streams.web;

import com.gonnect.kafka.k8s.K8sService;
import com.gonnect.kafka.streams.model.RoutableMessage;
import com.gonnect.kafka.streams.service.KafkaRoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaStreamsRouterController {

    private final KafkaRoutingService kafkaRoutingService;
    private final K8sService k8sService;

    public KafkaStreamsRouterController(KafkaRoutingService kafkaRoutingService) {
        this.kafkaRoutingService = kafkaRoutingService;
        this.k8sService = new K8sService();
    }

    @GetMapping("/router")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RoutableMessage> router(@RequestParam("message") String message) {
        RoutableMessage routableMessage = RoutableMessage.builder()
                .message(message)
                .boundedFunctions(k8sService.getAnnotatedFunctions())
                .topicName("master-channel")
                .timestamp(System.currentTimeMillis())
                .build();

        kafkaRoutingService.router(routableMessage);

        return ResponseEntity.ok(routableMessage);
    }

}
