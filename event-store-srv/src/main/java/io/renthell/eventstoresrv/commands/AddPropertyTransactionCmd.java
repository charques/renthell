package io.renthell.eventstoresrv.commands;

import io.renthell.eventstoresrv.common.command.Command;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Getter
@Setter
public class AddPropertyTransactionCmd extends Command {

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

    private String transactionId;
    private String transaction;
    private String price;
    private String priceMin;
    private String priceMax;
    private String priceRange;

    private Boolean updated;
}