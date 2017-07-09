package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

/**
 * Created by cfhernandez on 4/7/17.
 */
public class FotocasaCrawler extends WebCrawler {

    private static final Logger logger = LoggerFactory.getLogger(FotocasaCrawler.class);

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

    CrawlStats crawlStats;
    FotocasaService fotocasaService;

    public FotocasaCrawler(FotocasaService fotocasaService) {
        this.crawlStats = new CrawlStats();
        this.fotocasaService = fotocasaService;
    }

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith("http://www.fotocasa.es/vivienda/madrid-capital/");
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {

        crawlStats.incProcessedPages();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            WebURL webUrl = page.getWebURL();

            logger.debug("Number of outgoing links: {}", links.size());

            crawlStats.incTotalLinks(links.size());

            FotocasaPageParser parser = new FotocasaPageParser(webUrl, htmlParseData);
            FotocasaItem item = parser.getItem();

            if(item != null) {
                CompletableFuture<FotocasaItem> item1;
                try {
                    item1 = fotocasaService.saveItem(item);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // We dump this crawler statistics after processing every 50 pages
        if ((crawlStats.getTotalProcessedPages() % 10) == 0) {
            dumpMyData();
        }
    }

    /**
     * This function is called by controller to get the local data of this crawler when job is
     * finished
     */
    @Override
    public Object getMyLocalData() {
        return crawlStats;
    }

    /**
     * This function is called by controller before finishing the job.
     * You can put whatever stuff you need here.
     */
    @Override
    public void onBeforeExit() {
        dumpMyData();
    }

    public void dumpMyData() {
        int id = getMyId();
        // You can configure the log to output to file
        logger.info("Crawler {} > Processed Pages: {}", id, crawlStats.getTotalProcessedPages());
        logger.info("Crawler {} > Total Links Found: {}", id, crawlStats.getTotalLinks());
    }
}