package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class FotocasaUrlSeedConfigTests {

	@Test
	public void fotoCasaUrlSeedConfigTest() {
		FotocasaUrlSeedConfig config = FotocasaUrlSeedConfig.builder()
				.addCrawlArea("alquiler", "madrid-provincia", 300)
				.addCrawlArea("comprar", "madrid-provincia", 300)
				.build();

		Assert.assertNotNull(config.getSeeds().size() == 600);
		Assert.assertTrue(config.getSeeds().get(0).startsWith("http://www.fotocasa.es/es/alquiler/casas/madrid-provincia/todas-las-zonas/l/0"));
		Assert.assertTrue(config.getSeeds().get(599).startsWith("http://www.fotocasa.es/es/comprar/casas/madrid-provincia/todas-las-zonas/l/299"));
	}

}
