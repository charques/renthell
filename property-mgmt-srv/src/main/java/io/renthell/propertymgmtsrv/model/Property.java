package io.renthell.propertymgmtsrv.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by cfhernandez on 1/8/17.
 */
@Getter
@Setter
@ToString
public class Property {

    @NotNull
    private String identifier;

    private String publishDate;

    private String region;
    private String regionId;
    private String city;
    private String district;
    private String neighbourhood;
    private String street;
    private String postalCode;

    private String property;
    private String propertySub;
    private String propertyState;
    private String propertyType;

    private String mts2;
    private String rooms;
    private String bathrooms;
    private String heating;
    private String energeticCert;

    private String features;

    private String lat;
    private String lng;
    private String feed;

    private List<Transaction> transactions;

    private Boolean updated;
}
