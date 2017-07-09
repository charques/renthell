package io.renthell.crawlengine;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Created by cfhernandez on 4/7/17.
 */
@Component
@Getter
@Setter
public class CrawlStats {
    private int totalProcessedPages;

    public void incProcessedPages() {
        this.totalProcessedPages++;
    }

}
