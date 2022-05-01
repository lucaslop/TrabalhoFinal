package org.lucas.kafka.producer;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lucas.kafka.env.Env;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KafkaProducerExample {
    private static final Logger logger = LogManager.getLogger(KafkaProducerExample.class);

    public static void main(final String... args) {
        // Create topic
        createTopic();

        String[] words = new String[]{"Cachorro", "gato", "Tigre", "Urso", "Leão", "Foca", "Urubu", "Macaco", "Onça", "Gavião"};
        Random ran = new Random(System.currentTimeMillis());

        final Producer<String, String> producer = createProducer();
        int PRODUCER_INTERVAL = System.getenv("PRODUCER_INTERVAL") != null ?
                Integer.parseInt(System.getenv("PRODUCER_INTERVAL")) : 100;

        try {
            while (true) {
                String word = words[ran.nextInt(words.length)];
                String uuid = UUID.randomUUID().toString();

                ProducerRecord<String, String> record = new ProducerRecord<>(Env.KAFKA_TOPIC, uuid, word);
                RecordMetadata metadata = producer.send(record).get();

                logger.info("Enviando ({}, {}) para o {} @ {}.", uuid, word, Env.KAFKA_TOPIC, metadata.timestamp());

                Thread.sleep(PRODUCER_INTERVAL);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred.", e);
        } finally {
            producer.flush();
            producer.close();
        }
    }

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Env.KAFKA_SERVER);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerExample");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private static void createTopic() {
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;

        ZkClient zkClient = new ZkClient(
                Env.ZOOKEEPER_SERVER,
                sessionTimeoutMs,
                connectionTimeoutMs,
                ZKStringSerializer$.MODULE$);

        boolean isSecureKafkaCluster = false;
        ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(Env.ZOOKEEPER_SERVER), isSecureKafkaCluster);

        int partitions = 1;
        int replication = 1;

        // Add topic configuration here
        Properties topicConfig = new Properties();
        if (!AdminUtils.topicExists(zkUtils, Env.KAFKA_TOPIC)) {
            AdminUtils.createTopic(zkUtils, Env.KAFKA_TOPIC, partitions, replication, topicConfig, RackAwareMode.Safe$.MODULE$);
            logger.info("Topic {} created.", Env.KAFKA_TOPIC);
        } else {
            logger.info("Topic {} already exists.", Env.KAFKA_TOPIC);
        }

        zkClient.close();
    }
}
