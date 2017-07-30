package io.renthell.eventstoresrv.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaConfiguration {

	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;

	@Value("${kafka.topics.events}")
	private String eventsTopic;

}
