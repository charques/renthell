https://sookocheff.com/post/kafka/kafka-quick-start/

brew install kafkacat - for kafka

`kafkacat -P -b 192.168.99.100:9092 -t test` - producer
`kafkacat -C -b 192.168.99.100:9092 -t test`- consumer


`docker exec -ti renthell-kafka ./opt/kafka_2.11-0.10.1.0/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --delete --topic test` - delete topic test
