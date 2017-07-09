package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by cfhernandez on 4/7/17.
 */
@Component
@Slf4j
@NoArgsConstructor
public class CrawlEngine {

    @Autowired
    private FotocasaService fotocasaService;

    public void init(int numberOfCrawlers, int executionTime) throws InterruptedException {
        String pwd = System.getenv().get("PWD");
        String crawlStorageFolder = pwd + "/tmp/crawl/root";
        int maxDepthOfCrawling = 2;
        int politenessDelay = 1000;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setPolitenessDelay(politenessDelay);
        config.setUserAgentString("RentHellBot");

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        FotocasaUrlSeedConfig seedsConfig = FotocasaUrlSeedConfig.builder()
            .addCrawlArea("alquiler", "madrid-capital", 5)
            //.addCrawlArea("comprar", "madrid-capital", 300)
            .build();

        List<String> seeds = seedsConfig.getSeeds();
        for(int i = 0; i < seeds.size(); i++) {
            controller.addSeed(seeds.get(i));
        }
        //controller.addSeed("http://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-ascensor-amueblado-dodge-143204775?RowGrid=4&tti=1&opi=300");
        //controller.addSeed("http://www.fotocasa.es/vivienda/madrid-capital/guindalera-139115105?RowGrid=4&tti=7&opi=300");
        //controller.addSeed("http://www.fotocasa.es/vivienda/madrid-capital/calefaccion-terraza-ascensor-los-angeles-143230578");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        FotocasaCrawlerFactory factory = new FotocasaCrawlerFactory(fotocasaService);
        controller.startNonBlocking(factory, numberOfCrawlers);
        //controller.start(FotocasaCrawler.class, numberOfCrawlers);

        // Wait for 30 seconds
        Thread.sleep(executionTime * 1000);

        // Send the shutdown request and then wait for finishing
        controller.shutdown();
        controller.waitUntilFinish();

        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        int totalProcessedPages = 0;
        for (Object localData : crawlersLocalData) {
            CrawlStats stat = (CrawlStats) localData;
            totalProcessedPages += stat.getTotalProcessedPages();
        }

        log.info("Aggregated Statistics:");
        log.info("\tProcessed Pages: {}", totalProcessedPages);
    }
}
