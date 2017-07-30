package io.renthell.crawlengine.fotocasa.model;

import edu.uci.ics.crawler4j.url.WebURL;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * Created by cfhernandez on 5/7/17.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FotocasaItem {
    @Id
    private String id;
    private String promotionId;
    private String publishedTypeId;
    private String pageName;
    private String city;
    private String cityId;
    private String cityZone;
    private String companyIdWas;
    private String county;
    private String countyId;
    private String districtId;
    private String heatingId;
    private String mts2;
    private String mts2Max;
    private String mts2Min;
    private String neighbourhood;
    private String neighbourhoodId;
    private String postalCode;
    private String property;
    private String propertyId;
    private String propertyState;
    private String propertyStateId;
    private String propertySub;
    private String propertySubId;
    private String query;
    private String regionLevel2;
    private String regionLevel2Id;
    private String reload;
    private String rooms;
    private String searchTerms;
    private String site;
    private String street;
    private String waterId;
    private String publisherId;
    private String publisherType;
    private String title;
    private String bathrooms;
    private String cityZoneId;
    private String country;
    private String countryId;
    private String createDate;
    private String district;
    private String eventName;
    private String hostName;
    private Boolean isMsite;
    private String listType;
    private String locality;
    private String localityId;
    private String moreFilters;
    private String otherFilters;
    private String publishDate;
    private String recommendationId;
    private String regionLevel1;
    private String regionLevel1Id;
    private String sellTypeId;
    private String userRoleId;
    private Boolean xitiIsEnabled;
    private String userId;
    private String company;

    private String energeticCert;
    private String city2;
    private String neighbourhood2;
    private String cityStr;
    private String propertyStr;
    private String zone;
    private String propertyType;
    private String conservation;
    private String lat;
    private String lng;
    private String featuresId;
    private String features;
    private List<FotocasaTransactionItem> transactions;
    private String webUrl;

    private Boolean updated = false;

    public static FotocasaItem build(WebURL weburl, JSONObject mainObj, JSONObject secondaryObj) {
        return new FotocasaItemBuilder(weburl, mainObj, secondaryObj).build();
    }

    static class FotocasaItemBuilder {

        private WebURL weburl;
        private JSONObject mainObj;
        private JSONObject secondaryObj;

        private FotocasaItemBuilder(WebURL weburl, JSONObject mainObj, JSONObject secondaryObj) {
            this.weburl = weburl;
            this.mainObj = mainObj;
            this.secondaryObj = secondaryObj;
        }

        public FotocasaItem build() {
            FotocasaItem build = new FotocasaItem();

            check(mainObj, secondaryObj);

            if(weburl != null) {
                build.webUrl = weburl.getURL();
            }
            if(mainObj != null) {
                build.id = getString(mainObj,"ad_id");
                build.promotionId = getString(mainObj,"promotion_id");
                build.publishedTypeId = getString(mainObj,"ad_publisher_type_id");
                build.pageName = getString(mainObj,"ads_name_page");
                build.city = getString(mainObj,"city");
                build.cityId = getString(mainObj,"city_id");
                build.cityZone = getString(mainObj,"city_zone");
                build.companyIdWas = getString(mainObj,"company_idwas");
                build.county = getString(mainObj,"county");
                build.countyId = getString(mainObj,"county_id");
                build.districtId = getString(mainObj,"district_id");
                build.heatingId = getString(mainObj,"heating_id");
                build.mts2 = getString(mainObj,"mts2");
                build.mts2Max = getString(mainObj,"mts2_max");
                build.mts2Min = getString(mainObj,"mts2_min");
                build.neighbourhood = getString(mainObj,"neighbourhood");
                build.neighbourhoodId = getString(mainObj,"neighbourhood_id");
                build.postalCode = getString(mainObj,"postal_code");
                build.property = getString(mainObj,"property");
                build.propertyId = getString(mainObj,"property_id");
                build.propertyState = getString(mainObj,"property_state");
                build.propertyStateId = getString(mainObj,"property_state_id");
                build.propertySub = getString(mainObj,"property_sub");
                build.propertySubId = getString(mainObj,"property_sub_id");
                build.query = getString(mainObj,"query");
                build.regionLevel2 = getString(mainObj,"region_level2");
                build.regionLevel2Id = getString(mainObj,"region_level2_id");
                build.reload = getString(mainObj,"reload");
                build.rooms = getString(mainObj,"rooms");
                build.searchTerms = getString(mainObj,"search_terms");
                build.site = getString(mainObj,"site");
                build.street = getString(mainObj,"street");
                build.waterId = getString(mainObj,"water_id");
                build.publisherId = getString(mainObj,"ad_publisher_id");
                build.publisherType = getString(mainObj,"ad_publisher_type");
                build.title = getString(mainObj,"ad_title");
                build.bathrooms = getString(mainObj,"bathrooms");
                build.cityZoneId = getString(mainObj,"city_zone_id");
                build.country = getString(mainObj,"country");
                build.countryId = getString(mainObj,"country_id");
                build.createDate = getString(mainObj,"create_date");
                build.district = getString(mainObj,"district");
                build.eventName = getString(mainObj,"event_name");
                build.hostName = getString(mainObj,"host_name");
                build.isMsite = getBoolean(mainObj,"is_msite");
                build.listType = getString(mainObj,"list_type");
                build.locality = getString(mainObj,"locality");
                build.localityId = getString(mainObj,"locality_id");
                build.moreFilters = getString(mainObj,"more_filters");
                build.otherFilters = getString(mainObj,"other_filters");
                build.publishDate = getString(mainObj,"publish_date");
                build.recommendationId = getString(mainObj,"recommendation_id");
                build.regionLevel1 = getString(mainObj,"region_level1");
                build.regionLevel1Id = getString(mainObj,"region_level1_id");
                build.sellTypeId = getString(mainObj,"sell_type_id");
                build.userRoleId = getString(mainObj,"ad_id");
                build.xitiIsEnabled = getBoolean(mainObj, "xiti_is_enabled");
                build.userId = getString(mainObj,"user_id");
                build.company = getString(mainObj,"company");

                FotocasaTransactionItem.FotocasaTransactionItemBuilder transactionBuilder = FotocasaTransactionItem.builder()
                        .transactionId(getString(mainObj,"transaction_id"))
                        .transaction(getString(mainObj,"transaction"))
                        .price(getString(mainObj,"price"))
                        .priceMax(getString(mainObj,"price_max"))
                        .priceMin(getString(mainObj,"price_min"));

                if(secondaryObj != null) {
                    build.lng = getString(secondaryObj,"Lng");
                    build.lat = getString(secondaryObj,"Lat");
                    build.conservation = getString(secondaryObj,"Conservation");
                    build.propertyType = getString(secondaryObj,"PropertyType");
                    build.zone = getString(secondaryObj,"Zone1");
                    build.propertyStr = getString(secondaryObj,"oasPropertySubStr");
                    build.cityStr = getString(secondaryObj,"oasGeoCityStr");
                    build.neighbourhood2 = getString(secondaryObj,"Neighbourhood");
                    build.features = getString(secondaryObj,"PropertyFeature");
                    build.city2 = getString(secondaryObj,"City");
                    build.energeticCert = getString(secondaryObj,"oasEnergeticCert");
                    build.featuresId = getString(secondaryObj,"oasFeatures");

                    transactionBuilder.priceOas(getString(secondaryObj,"oasPrice"));
                    transactionBuilder.priceRange(getString(secondaryObj,"PriceRange"));
                }

                if(transactionBuilder != null) {
                    build.transactions = new ArrayList<FotocasaTransactionItem>();
                    build.transactions.add(transactionBuilder.build());
                }
            }

            return build;
        }

        private String getString(JSONObject obj, String key) {
            try {
                String str = obj.getString(key);
                if (str.startsWith(",")) {
                    str = str.substring(1, str.length());
                }
                if (str.endsWith(",")) {
                    str = str.substring(0, str.length() - 1);
                }
                return str;
            }
            catch (JSONException e){
                return "";
            }
        }

        private Boolean getBoolean(JSONObject obj, String key) {
            try {
                return obj.getBoolean(key);
            }
            catch (JSONException e){
                return false;
            }
        }

        private void check(JSONObject mainObj, JSONObject secondaryObj) {
            notNull(mainObj, "mainObj cannot be null");
            notNull(secondaryObj, "secondaryObj cannot be null");
        }

    }
}