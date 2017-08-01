package io.renthell.propertymgmtsrv;

import io.renthell.propertymgmtsrv.configuration.KafkaConfiguration;
import io.renthell.propertymgmtsrv.eventhandlers.consumer.PropertyAddedEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class PropertyMgmtSrvApplication implements CommandLineRunner {

	@Autowired
	private KafkaConfiguration kafkaConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(PropertyMgmtSrvApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		int numConsumers = 1;
		String groupId = "consumer-group";
		List<String> topics = Arrays.asList(kafkaConfiguration.getEventsTopic());
		ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

		final List<PropertyAddedEventConsumer> consumers = new ArrayList<>();
		for (int i = 0; i < numConsumers; i++) {
			PropertyAddedEventConsumer consumer = new PropertyAddedEventConsumer(i, kafkaConfiguration);
			consumers.add(consumer);
			executor.submit(consumer);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (PropertyAddedEventConsumer consumer : consumers) {
					consumer.shutdown();
				}
				executor.shutdown();
				try {
					executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
