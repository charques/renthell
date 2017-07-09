package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class FotocasaPageParserTests {

	@Test
	public void parseTextAlquilerTest() {
		WebURL webUrl = new WebURL();
		webUrl.setURL("http://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-ascensor-amueblado-dodge-143204775?RowGrid=4&tti=1&opi=300");

		HtmlParseData parseData = new HtmlParseData();
		String text = null;
		try {
			text = IOUtils.toString(this.getClass().getResourceAsStream("fotocasa_alquiler_text.txt"), "UTF-8");
			parseData.setText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FotocasaPageParser parser = new FotocasaPageParser(webUrl, parseData);
		FotocasaItem item = parser.getItem();

		Assert.assertNotNull(item.getId());
		Assert.assertNotNull(item.getWebUrl());
	}

}
