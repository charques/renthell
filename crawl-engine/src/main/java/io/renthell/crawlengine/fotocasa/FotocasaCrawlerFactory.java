package io.renthell.crawlengine.fotocasa;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import io.renthell.crawlengine.trackingfeeder.TrackingFeederService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by cfhernandez on 5/7/17.
 */
@Slf4j
public class FotocasaCrawlerFactory implements CrawlController.WebCrawlerFactory {

    FotocasaService fotocasaService;
    TrackingFeederService trackingFeederService;

    public FotocasaCrawlerFactory(FotocasaService fotocasaService) {
        this.fotocasaService = fotocasaService;
    }

    @Override
    public WebCrawler newInstance() {
        log.debug("new FotocasaCrawler created");
        return new FotocasaCrawler(fotocasaService);
    }
}
