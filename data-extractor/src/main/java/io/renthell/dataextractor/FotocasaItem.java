package io.renthell.dataextractor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by cfhernandez on 9/7/17.
 */
@Getter
@Setter
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
}
