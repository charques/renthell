package io.renthell.alertmgmtsrv.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScoringMgmtConfiguration {

    @Value("${endpoints.scoring-mgmt.uri-base}")
    private String SCORING_MGMT_URI_BASE;

    @Value("${endpoints.scoring-mgmt.get-scoring}")
    private String GET_SCORING_PATH;

    public String getScoringUri() {
        return SCORING_MGMT_URI_BASE + GET_SCORING_PATH;
    }
}
