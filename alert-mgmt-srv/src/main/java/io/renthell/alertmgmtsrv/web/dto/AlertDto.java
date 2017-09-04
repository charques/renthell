package io.renthell.alertmgmtsrv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Setter
@Getter
@ToString
public class AlertDto {

    private String id;

    private String propertyId;
    private String transactionId;
    private String alertDescriptor;

    private Date createdDate;
}

