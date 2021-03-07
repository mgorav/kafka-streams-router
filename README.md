# Kafka Stream Function Router - Dynamic consumer binding

## Overview

The goal of this project is to create kafka connector using Spring Cloud Stream, which routes the incoming messages to
a "function" build using OpenFaas which is annotated with annotation topic.

Hence, it connects Kafka topics to OpenFaaS Functions. Once deployed the kafka-connector and pointing to broker, user
can connect functions to topics by adding a simple annotation to a function's stack.yml file.

This is paid offering from OpenFaas, however, this provides open source offering. Also this project is deployable on
k8s. It uses jkube with zero k8s configuration.

Following picture shows the design:

![KafkaStreamRouter](./KafkaStreamFunctionRouter.png)

## Installation

- Docker
- Kafka (http://strimzi.io)
- OpenFaas
- K8s cluster (k3d)

## Run

````
mvn k8s:build k8s:push k8s:resource k8s:apply -Pkubernetes
````

Observe log:

````
mvn k8s:log -Pkubernetes
````

## Test

To send a message to the consuming function using below REST call:

````
http://localhost:8081/router?message=magic
````

The output:

````
{
    "timestamp": 1615123388892,
    "message": "magic",
    "topicName": "master-channel",
    "boundedFunctions": [
        "python-kafka"
    ]
}
````

The message magic will be delivered to function - "python-kafka"
