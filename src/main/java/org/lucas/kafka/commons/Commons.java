package org.lucas.kafka.commons;

public class Commons {
    public final static String KAFKA_TOPIC = System.getenv("KAFKA_TOPIC") != null ?
            System.getenv("KAFKA_TOPIC") : "example";
    public final static String KAFKA_SERVER = System.getenv("KAFKA_SERVER") != null ?
            System.getenv("KAFKA_SERVER") : "localhost:9092";
    public final static String ZOOKEEPER_SERVER = System.getenv("ZOOKEEPER_SERVER") != null ?
            System.getenv("ZOOKEEPER_SERVER") : "localhost:32181";
}
