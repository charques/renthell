package io.renthell.crawlengine;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

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

    public FotocasaPageParser(WebURL webURL, HtmlParseData parseData) {
        this.webURL = webURL;
        this.parseData = parseData;
    }

    public FotocasaItem getItem() {
        FotocasaItem item = null;

        String text = parseData.getText();
        String utag = getInfo(text, "var utag_data =(.*?);");
        String adConfig = getInfo(text, "var advertisementConfig = JSON.parse\\(\\'(.*?)\\'\\);");

        JSONObject utagJson = utagJsonBuilder(utag);
        JSONObject adConfigJsonObj = new JSONObject(adConfig);

        item = FotocasaItem.builder()
                .weburl(webURL)
                .mainObj(utagJson)
                .secondaryObj(adConfigJsonObj)
                .build();

        log.debug(item.toString());

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
