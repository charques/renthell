package io.renthell.alertmgmtsrv.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Getter
@Setter
@ToString
@Document(collection = "alert")
public class Alert {

    @Id
    private String id;

    private String propertyId;
    private String transactionId;
    private String alertDescriptor;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

}
