package io.renthell.scoringmgmtsrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoringMgmtException extends RuntimeException {

    public enum ErrorCode {
        PARSE_ERROR(3000),
        DEPENDENCY_ERROR(3001);

        private int code;

        ErrorCode(int code) {
            this.code = code;
        }

        public int getValue() {
            return code;
        }
    }

    private ErrorCode errorCode;

    public ScoringMgmtException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
