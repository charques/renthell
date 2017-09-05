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
    public RuleResult evaluate(PropertyDto propertyDto, ScoringStatsDto scoringStatsDto) {
        String transactionId = scoringStatsDto.getScoring().getTransactionId();

        TransactionDto transactionDto = PropertyRulesUtils.getTransaction(propertyDto, transactionId);
        if(transactionDto != null) {
            double averageMinusPorcentage = scoringStatsDto.getPriceAverage() - (scoringStatsDto.getPriceAverage() * this.percentage/100);
            if(transactionDto.getPrice() < averageMinusPorcentage) {
                StringBuilder strBldr = new StringBuilder()
                        .append("[property id: ").append(propertyDto.getIdentifier()).append("], ")
                        .append("[transaction id: ").append(transactionId).append("], ")
                        .append("[transaction price: ").append(transactionDto.getPrice()).append("], ")
                        .append("[average price: ").append(scoringStatsDto.getPriceAverage()).append("], ")
                        .append("[percentage: ").append(percentage).append("], ")
                        .append("[average price - percentage: ").append(averageMinusPorcentage).append("]");
                return new RuleResult(UnderPriceAverageRule.class.getSimpleName(), strBldr.toString());
            }
        }
        return null;
    }
}
