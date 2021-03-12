package com.gonnect.kafka.streams.web;

import com.gonnect.kafka.k8s.K8sService;
import com.gonnect.kafka.streams.model.RoutableMessage;
import com.gonnect.kafka.streams.service.KafkaRoutingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Slf4j
public class KafkaStreamsRouterController {

    private final KafkaRoutingService kafkaRoutingService;
    private final K8sService k8sService;

    public KafkaStreamsRouterController(KafkaRoutingService kafkaRoutingService) {
        this.kafkaRoutingService = kafkaRoutingService;
        this.k8sService = new K8sService();
    }

    @PostMapping("/fn-router")
    @ResponseStatus(CREATED)
    public ResponseEntity<RoutableMessage> router(@RequestBody RoutableMessage message) {
        message.setBoundedFunctions(k8sService.getAnnotatedFunctions());
        RoutableMessage routableMessage = new RoutableMessage(message);

        kafkaRoutingService.router(routableMessage);

        return ResponseEntity.ok(routableMessage);
    }

}
