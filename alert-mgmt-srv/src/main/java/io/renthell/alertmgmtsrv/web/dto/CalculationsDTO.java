package io.renthell.alertmgmtsrv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cfhernandez on 23/8/17.
 */
@Getter
@Setter
@ToString
public class CalculationsDTO {
    private Float rentGrossReturn;
    private Float rentPer;
    private Float rentMt2Price;
    private Float saleMt2Price;
}
