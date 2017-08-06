package io.renthell.scoringmgmtsrv.exception;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class ScoringMgmtErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    public void handleError(ClientHttpResponse response) throws IOException {

        throw new ScoringMgmtException(ScoringMgmtException.ErrorCode.DEPENDENCY_ERROR,
                response.getStatusCode().toString(), null);
    }
}