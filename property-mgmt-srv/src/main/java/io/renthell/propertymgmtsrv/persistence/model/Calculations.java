package io.renthell.propertymgmtsrv.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cfhernandez on 23/8/17.
 */
@Getter
@Setter
@ToString
public class Calculations {

    private Float rentGrossReturn;
    private Float rentPer;
    private Float rentMt2Price;
    private Float saleMt2Price;
}
