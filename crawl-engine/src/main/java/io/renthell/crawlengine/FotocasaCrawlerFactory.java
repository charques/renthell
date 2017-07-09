package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by cfhernandez on 5/7/17.
 */
@Slf4j
public class FotocasaCrawlerFactory implements CrawlController.WebCrawlerFactory {

    FotocasaService service;

    public FotocasaCrawlerFactory(FotocasaService service) {
        this.service = service;
    }

    @Override
    public WebCrawler newInstance() {
        log.debug("new FotocasaCrawler created");
        return new FotocasaCrawler(service);
    }
}
