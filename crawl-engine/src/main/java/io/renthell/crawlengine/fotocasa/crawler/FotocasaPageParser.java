package io.renthell.crawlengine.fotocasa.crawler;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import io.renthell.crawlengine.fotocasa.model.FotocasaItem;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.StringBuilder;

/**
 * Created by cfhernandez on 5/7/17.
 */
@Slf4j
public class FotocasaPageParser {

    private WebURL webURL;
    private HtmlParseData parseData;

    public FotocasaPageParser webUrl(WebURL webURL) {
        this.webURL = webURL;
        return this;
    }

    public FotocasaPageParser parseData(HtmlParseData parseData) {
        this.parseData = parseData;
        return this;
    }

    public FotocasaItem build() throws ParseException, JSONException {
        String text = parseData.getText();
        text = removeComplexChars(text);

        String utag = getInfo(text, "var utag_data =(.*?);");
        String adConfig = getInfo(text, "var advertisementConfig = JSON.parse\\(\\'(.*?)\\'\\);");

        if(utag.isEmpty() || adConfig.isEmpty()) {
            throw new ParseException("No info to parse.", 0);
        }

        JSONObject utagJson = null;
        try {
            utagJson = utagJsonBuilder(utag);
        }
        catch (JSONException e) {
            throw new ParseException("Parse error UTAG: " + e.getMessage() + System.getProperty("line.separator") + utag, 0);
        }

        JSONObject adConfigJsonObj = null;
        try {
            adConfigJsonObj = new JSONObject(adConfig);
        }
        catch (JSONException e) {
            throw new ParseException("Parse error ADCONFIG: " + e.getMessage() + System.getProperty("line.separator") + adConfig, 0);
        }

        FotocasaItem item = FotocasaItem.build(webURL, utagJson, adConfigJsonObj);

        return item;
    }

    private String getInfo(String text, String regex) {
        String info = "";
        Pattern patternInfo = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcherInfo = patternInfo.matcher(text);
        if (matcherInfo.find()) {
            info = matcherInfo.group(1);
        }
        return info;
    }

    private String removeComplexChars(String text) {
        text = text.replaceAll("&hellip;", "");
        return text;
    }

    private JSONObject utagJsonBuilder(String utagStrJson) {
        String[] lines = utagStrJson.split(System.getProperty("line.separator"));
        for(int i=0;i<lines.length;i++){
            if(lines[i].startsWith("            features_id")){
                lines[i]="";
            }
            if(lines[i].startsWith("            features")){
                lines[i]="";
            }
        }
        StringBuilder finalStringBuilder=new StringBuilder("");
        for(String s:lines){
            if(!s.equals("")){
                finalStringBuilder.append(s).append(System.getProperty("line.separator"));
            }
        }
        String finalJsonString = finalStringBuilder.toString();
        JSONObject jsonObj = new JSONObject(finalJsonString);
        return jsonObj;
    }
}
