package io.renthell.alertmgmtsrv.service.rulesengine.rules;

import io.renthell.alertmgmtsrv.service.rulesengine.utils.PropertyRulesUtils;
import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.ScoringStatsDto;
import io.renthell.alertmgmtsrv.web.dto.TransactionDto;

import java.util.List;

public class OverPriceAverageRule implements PropertyRule {

    private int percentage;

    public OverPriceAverageRule(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String identifier() {
        return OverPriceAverageRule.class.getSimpleName();
    }

    @Override
    public Boolean evaluate(PropertyDto propertyDto, ScoringStatsDto scoringStatsDto) {
        String transactionId = scoringStatsDto.getScoring().getTransactionId();

        TransactionDto transactionDto = PropertyRulesUtils.getTransaction(propertyDto, transactionId);
        if(transactionDto != null) {
            double averagePlusPorcentage = scoringStatsDto.getPriceAverage() + (scoringStatsDto.getPriceAverage() * this.percentage/100);
            if(transactionDto.getPrice() > averagePlusPorcentage) {
                return true;
            }
        }
        return false;
    }
}
