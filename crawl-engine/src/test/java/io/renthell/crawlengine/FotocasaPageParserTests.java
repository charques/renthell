package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import io.renthell.crawlengine.fotocasa.model.FotocasaItem;
import io.renthell.crawlengine.fotocasa.crawler.FotocasaPageParser;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

public class FotocasaPageParserTests {

	@Test
	public void parseAlquilerTextTest() throws ParseException, JSONException {
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

		FotocasaItem item = (new FotocasaPageParser()).webUrl(webUrl).parseData(parseData).build();

		Assert.assertNotNull(item.getId());
		Assert.assertNotNull(item.getWebUrl());
	}

	@Test
	public void parseEmptyTextTest() throws ParseException, JSONException {
		WebURL webUrl = new WebURL();

		HtmlParseData parseData = new HtmlParseData();
		parseData.setText("");

		try {
			FotocasaItem item = (new FotocasaPageParser())
					.webUrl(webUrl)
					.parseData(parseData)
					.build();
		} catch (ParseException | JSONException e) {
			Assert.assertNotNull(e);
		}


	}

}
