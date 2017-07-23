package io.renthell.crawlengine.fotocasa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

/**
 * Created by cfhernandez on 9/7/17.
 */
@Getter
public class FotocasaUrlSeedConfig {

    private List<String> seeds;

    public FotocasaUrlSeedConfig() {
        this.seeds = new ArrayList<String>();
    }

    public static FotocasaUrlSeedConfigBuilder builder() {
        return new FotocasaUrlSeedConfigBuilder();
    }

    private void addSeed(String seed) {
        this.seeds.add(seed);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class CrawlArea {
        private String transaction;
        private String area;
        private int pages;
    }

    public static class FotocasaUrlSeedConfigBuilder {

        private final String URL_BASE = "https://www.fotocasa.es/es/";
        private final String URL_PROPERTY_TYPE = "/casas/";
        private final String URL_SEARCH = "/todas-las-zonas/l/";

        private List<CrawlArea> areas;

        public FotocasaUrlSeedConfigBuilder() {
            areas = new ArrayList<CrawlArea>();
        }

        public FotocasaUrlSeedConfigBuilder addCrawlArea(String transaction, String area, int pages) {
            areas.add(new CrawlArea(transaction, area, pages));
            return this;
        }

        public FotocasaUrlSeedConfig build() {
            FotocasaUrlSeedConfig build = new FotocasaUrlSeedConfig();

            check(areas);

            for(int i = 0; i < areas.size(); i++) {
                CrawlArea area = areas.get(i);
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append(URL_BASE)
                    .append(area.getTransaction())
                    .append(URL_PROPERTY_TYPE)
                    .append(area.getArea())
                    .append(URL_SEARCH);
                for(int j = 0; j < area.getPages(); j++) {
                    strBuilder.delete(strBuilder.lastIndexOf("/"), strBuilder.length())
                            .append("/")
                            .append(j);
                    build.addSeed(strBuilder.toString());
                }
            }
            return build;
        }

        private void check(List<CrawlArea> areas) {
            notEmpty(areas, "areas cannot be empty");
        }

    }

}
