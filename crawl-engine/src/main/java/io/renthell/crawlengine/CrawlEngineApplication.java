package io.renthell.crawlengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlEngineApplication implements CommandLineRunner {

	@Autowired
	private CrawlEngine crawlEngine;

	public static void main(String[] args) {
		SpringApplication.run(CrawlEngineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//String rootFolder = args[0];
		//int numberOfCrawlers = Integer.parseInt(args[1]);

		crawlEngine.init();
	}
}
