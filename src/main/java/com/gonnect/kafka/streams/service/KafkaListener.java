package com.gonnect.kafka.streams.service;

import com.gonnect.kafka.k8s.K8sService;
import com.gonnect.kafka.streams.model.RoutableMessage;
import com.gonnect.kafka.streams.stream.KafkaStreams;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KafkaListener {

    private final WebClient client;
    private final K8sService k8sService;

    public KafkaListener() {
        this.client = WebClient.builder()
                .baseUrl("http://gateway.openfaas:8080/function")
                .clientConnector(new ReactorClientHttpConnector())
                .build();
        k8sService = new K8sService();
    }

    @StreamListener(KafkaStreams.INPUT)
    public void handleRoutableMessage(@Payload RoutableMessage routableMessage, @Headers Map<String, Object> headers) {
        log.info("Received routable message: {}. Partition: {}. Offset: {}", routableMessage,
                headers.get(KafkaHeaders.RECEIVED_PARTITION_ID), headers.get(KafkaHeaders.OFFSET));

        List<String> functionsBoundToATopic = k8sService.getAnnotatedFunctions();

        if (!functionsBoundToATopic.isEmpty()) {
            functionsBoundToATopic.forEach(function -> {
                invokeFunction(routableMessage, function);
            });
        }
    }

    private void invokeFunction(RoutableMessage routableMessage, String functionName) {
        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        RequestBodySpec bodySpec = uriSpec.uri(
                uriBuilder -> uriBuilder.pathSegment(functionName).build());
        WebClient.RequestHeadersSpec headersSpec = bodySpec.body(
                BodyInserters.fromPublisher(Mono.just(routableMessage.getMessage()), String.class)
        );

        Mono<String> response = headersSpec.retrieve().bodyToMono(String.class);

        log.info("Received message from function {}: {}", functionName, response.block());
    }


}
