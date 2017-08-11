package io.renthell.propertymgmtsrv.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomErrorType {

    private String errorMessage;

    private int errorCode;

    public CustomErrorType(String errorMessage, int errorCode){
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
