package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by cfhernandez on 4/7/17.
 */
@Component
public class CrawlEngine {

    private static final Logger logger = LoggerFactory.getLogger(CrawlEngine.class);

    @Autowired
    private FotocasaService fotocasaService;

    public CrawlEngine() {

    }

    public void init() throws InterruptedException {
        String crawlStorageFolder = "/Users/cfhernandez/projects_ws/renthell/crawl-engine/tmp/crawl/root";
        int numberOfCrawlers = 10;
        int maxDepthOfCrawling = 1;
        int maxPagesToFetch = 2000;
        int politenessDelay = 1000;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setMaxPagesToFetch(maxPagesToFetch);
        config.setPolitenessDelay(politenessDelay);

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
        for(int i = 23; i < 100; i++) {
            //controller.addSeed("http://www.fotocasa.es/es/alquiler/casas/madrid-provincia/todas-las-zonas/l/"+i);
        }
        //controller.addSeed("http://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-ascensor-amueblado-dodge-143204775?RowGrid=4&tti=1&opi=300");
        controller.addSeed("http://www.fotocasa.es/vivienda/madrid-capital/guindalera-139115105?RowGrid=4&tti=7&opi=300");
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        FotocasaCrawlerFactory factory = new FotocasaCrawlerFactory(fotocasaService);
        controller.startNonBlocking(factory, numberOfCrawlers);
        //controller.start(FotocasaCrawler.class, numberOfCrawlers);

        // Wait for 30 seconds
        Thread.sleep(1200 * 1000);

        // Send the shutdown request and then wait for finishing
        controller.shutdown();
        controller.waitUntilFinish();

        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        long totalLinks = 0;
        int totalProcessedPages = 0;
        for (Object localData : crawlersLocalData) {
            CrawlStats stat = (CrawlStats) localData;
            totalLinks += stat.getTotalLinks();
            totalProcessedPages += stat.getTotalProcessedPages();
        }

        logger.info("Aggregated Statistics:");
        logger.info("\tProcessed Pages: {}", totalProcessedPages);
        logger.info("\tTotal Links found: {}", totalLinks);
    }
}
