package org.lucas.kafka.cli;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucas.kafka.env.Env;
import org.lucas.kafka.consumer.KafkaConsumerExample;
import org.lucas.kafka.consumer.KafkaFlinkConsumerExample;
import org.lucas.kafka.consumer.KafkaSparkConsumerExample;
import org.lucas.kafka.producer.KafkaProducerExample;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(final String... args) {
        String PAPEL = System.getenv("PAPEL") != null ?
                System.getenv("PAPEL") : "producer";

        logger.info("Kafka Topic: {}", Env.KAFKA_TOPIC);
        logger.info("Kafka Server: {}", Env.KAFKA_SERVER);
        logger.info("Zookeeper Server: {}", Env.ZOOKEEPER_SERVER);
        logger.info("Papel: {}", PAPEL);

        switch (PAPEL.toLowerCase()) {
            case "producer":
                KafkaProducerExample.main();
                break;
            case "consumer.kafka":
                KafkaConsumerExample.main();
                break;
            case "consumer.spark":
                KafkaSparkConsumerExample.main();
                break;
            case "consumer.flink":
                KafkaFlinkConsumerExample.main();
                break;
            default:
                logger.error("Papel inválido");
                break;
        }
    }
}
