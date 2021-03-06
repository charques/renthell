package io.renthell.eventstoresrv.configuration;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfiguration {

	@Value("${spring.data.mongodb.uri}")
	private String MONGO_DB_URI;
	
	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
			
		MongoClientOptions.Builder mcoBuilder = MongoClientOptions.builder();
		MongoClientURI mongoClientUri = new MongoClientURI(MONGO_DB_URI, mcoBuilder);
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClientUri);
		return new MongoTemplate(simpleMongoDbFactory);
	}
	
}
