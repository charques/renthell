package io.renthell.crawlengine;

import org.springframework.stereotype.Component;

/**
 * Created by cfhernandez on 4/7/17.
 */
@Component
public class CrawlStats {
    private int totalProcessedPages;
    private long totalLinks;

    public int getTotalProcessedPages() {
        return totalProcessedPages;
    }

    public void setTotalProcessedPages(int totalProcessedPages) {
        this.totalProcessedPages = totalProcessedPages;
    }

    public void incProcessedPages() {
        this.totalProcessedPages++;
    }

    public long getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(long totalLinks) {
        this.totalLinks = totalLinks;
    }

    public void incTotalLinks(int count) {
        this.totalLinks += count;
    }

}
