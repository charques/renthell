package io.renthell.scoringmgmtsrv.web.dto;

import io.renthell.scoringmgmtsrv.persistence.model.ScoringData;
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
 * Created by cfhernandez on 1/9/17.
 */
@Getter
@Setter
@ToString
public class ScoringDto {

    private String transactionId;
    private Integer month;
    private Integer year;
    private String postalCode;
    private Integer rooms;

}
