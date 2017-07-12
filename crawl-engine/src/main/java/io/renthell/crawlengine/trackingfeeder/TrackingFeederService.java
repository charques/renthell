package io.renthell.crawlengine.trackingfeeder;

import io.renthell.crawlengine.fotocasa.FotocasaItem;
import io.renthell.crawlengine.fotocasa.FotocasaTransactionItem;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Created by cfhernandez on 11/7/17.
 */
@Service
@Slf4j
public class TrackingFeederService {

    public void addPropertyTransaction(FotocasaItem item) {
        FotocasaTransactionItem t = item.getTransactions().get(0);

        JSONObject request = new JSONObject();
        request.put("identifier", item.getId());
        request.put("publishDate", item.getPublishDate());
        request.put("region", item.getRegionLevel2());
        request.put("regionId", item.getRegionLevel2Id());
        request.put("city", item.getCityStr());
        request.put("district", item.getDistrict());
        request.put("neighbourhood", item.getNeighbourhood());
        request.put("street", item.getStreet());
        request.put("postalCode", item.getPostalCode());
        request.put("property", item.getProperty());
        request.put("propertySub", item.getPropertySub());
        request.put("propertyState", item.getPropertyState());
        request.put("propertyType", item.getPropertyType());
        request.put("mts2", item.getMts2());
        request.put("rooms", item.getRooms());
        request.put("bathrooms", item.getBathrooms());
        request.put("heating", item.getHeatingId());
        request.put("energeticCert", item.getEnergeticCert());
        request.put("features", item.getFeatures());
        request.put("lat", item.getLat());
        request.put("lng", item.getLng());
        request.put("feed", item.getWebUrl());

        request.put("transactionId", t.getTransactionId());
        request.put("transaction", t.getTransaction());
        request.put("price", t.getPriceOas());
        request.put("priceMin", t.getPriceMin());
        request.put("priceMax", t.getPriceMax());
        request.put("priceRange", t.getPriceRange());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String urlString = "http://localhost:8080/commands/propertytransaction";
        ResponseEntity<String> pTransactionResponse = restTemplate.exchange(urlString, HttpMethod.POST, entity, String.class);
        if (pTransactionResponse.getStatusCode() == HttpStatus.OK) {
            //JSONObject userJson = new JSONObject(pTransactionResponse.getBody());
        } else if (pTransactionResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            // nono... bad credentials
        } else if (pTransactionResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
            log.error(entity.toString());
        }

    }
}
