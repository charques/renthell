package io.renthell.crawlengine;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CrawlEngineApplication implements CommandLineRunner {

    private final String DEFAULT_NUM_CRAWLERS = "10";
    private final String DEFAULT_EXECUTION_TIME = "1200";

	@Autowired
	private CrawlEngine crawlEngine;

	public static void main(String[] args) {
	    SpringApplication.run(CrawlEngineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Options options = new Options();
		options.addOption(
            Option.builder()
                .longOpt("crawlers")
                .desc("number of crawlers")
                .type(Integer.class)
                .hasArg()
                .required(false)
                .argName("number")
                .build()
        );
        options.addOption(
            Option.builder()
                .longOpt("time")
                .desc("execution time")
                .type(Integer.class)
                .hasArg()
                .required(false)
                .argName("seconds")
                .build()
        );

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("renthell-crawler-engine", options);
			System.exit(1);
			return;
		}

		int numberOfCrawlers = Integer.parseInt(cmd.getOptionValue("crawlers", DEFAULT_NUM_CRAWLERS));
		int executionTime = Integer.parseInt(cmd.getOptionValue("time", DEFAULT_EXECUTION_TIME));

		log.info("Config: NumberOfCrawlers {}", numberOfCrawlers);
        log.info("Config: ExecutionTime {}", executionTime);

		crawlEngine.init(numberOfCrawlers, executionTime);
	}
}
