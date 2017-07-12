package io.renthell.crawlengine.fotocasa;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import io.renthell.crawlengine.CrawlStats;
import io.renthell.crawlengine.trackingfeeder.TrackingFeederService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONException;

import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * Created by cfhernandez on 4/7/17.
 */
@Slf4j
public class FotocasaCrawler extends WebCrawler {

    private static final Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
                    "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    CrawlStats crawlStats;
    FotocasaService fotocasaService;
    TrackingFeederService trackingFeederService;

    public FotocasaCrawler(FotocasaService fotocasaService, TrackingFeederService trackingFeederService) {
        this.crawlStats = new CrawlStats();
        this.fotocasaService = fotocasaService;
        this.trackingFeederService = trackingFeederService;
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
        Boolean shouldVisit = !FILTERS.matcher(href).matches()
                && href.startsWith("http://www.fotocasa.es/vivienda/");
        return shouldVisit;
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
            WebURL webUrl = page.getWebURL();

            log.info(webUrl.getURL());

            try {
                FotocasaItem item = (new FotocasaPageParser())
                        .webUrl(webUrl)
                        .parseData(htmlParseData)
                        .build();
                log.debug(item.toString());

                FotocasaItem saved = fotocasaService.saveItem(item);

                // TODO: solo si hay cambios
                trackingFeederService.addPropertyTransaction(item);

            } catch (ParseException | JSONException e) {
                log.warn(e.getMessage());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        // We dump this crawler statistics after processing every 50 pages
        if ((crawlStats.getTotalProcessedPages() % 50) == 0) {
            dumpMyData();
        }
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {

        if (statusCode != HttpStatus.SC_OK) {

            if (statusCode == HttpStatus.SC_NOT_FOUND) {
                log.warn("Broken link: {}, this link was found in page: {}", webUrl.getURL(),
                        webUrl.getParentUrl());
            } else {
                log.warn("Non success status for link: {} status code: {}, description: ",
                        webUrl.getURL(), statusCode, statusDescription);
            }
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
        log.info("Crawler {} > Processed Pages: {}", id, crawlStats.getTotalProcessedPages());
    }
}