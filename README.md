mvn clean package

sudo docker build -t tf_kafka .


sudo docker-compose up -d


sudo docker logs trabalhofinal_kafka-producer_1 -f

sudo docker logs trabalhofinal_kafka-consumer-spark_1 -f

sudo docker logs trabalhofinal_kafka-consumer-flink_1 -f
