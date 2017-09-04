package io.renthell.alertmgmtsrv.service.rulesengine.rules;

import io.renthell.alertmgmtsrv.service.rulesengine.utils.PropertyRulesUtils;
import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.ScoringStatsDto;
import io.renthell.alertmgmtsrv.web.dto.TransactionDto;

public class UnderPriceAverageRule implements PropertyRule {

    private int percentage;

    public UnderPriceAverageRule(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String identifier() {
        return UnderPriceAverageRule.class.getSimpleName();
    }

    @Override
    public Boolean evaluate(PropertyDto propertyDto, ScoringStatsDto scoringStatsDto) {
        String transactionId = scoringStatsDto.getScoring().getTransactionId();

        TransactionDto transactionDto = PropertyRulesUtils.getTransaction(propertyDto, transactionId);
        if(transactionDto != null) {
            double averageMinusPorcentage = scoringStatsDto.getPriceAverage() - (scoringStatsDto.getPriceAverage() * this.percentage/100);
            if(transactionDto.getPrice() < averageMinusPorcentage) {
                return true;
            }
        }
        return false;
    }
}
