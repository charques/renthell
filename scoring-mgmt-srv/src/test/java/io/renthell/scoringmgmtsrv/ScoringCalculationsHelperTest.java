package io.renthell.scoringmgmtsrv;

import io.renthell.scoringmgmtsrv.config.SimpleConfiguration;
import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.persistence.model.ScoringData;
import io.renthell.scoringmgmtsrv.service.ScoringCalculationsHelper;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import org.assertj.core.data.Offset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SimpleConfiguration.class)
public class ScoringCalculationsHelperTest {

  final String RENT_TRANSACTION = "3";

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testScoringStatsCalculationsWithoutAggregation() {
    Scoring scoring = new Scoring();
    scoring.setTransactionId(RENT_TRANSACTION); // rent
    scoring.addScoringDataItem(new ScoringData(800F, 140, 4));
    scoring.addScoringDataItem(new ScoringData(850F, 140, 4));
    scoring.addScoringDataItem(new ScoringData(1000F, 140, 4));
    scoring.addScoringDataItem(new ScoringData(1001F, 140, 4));
    scoring.addScoringDataItem(new ScoringData(1200F, 140, 4));
    scoring.addScoringDataItem(new ScoringData(1500F, 140, 4));

    List<Scoring> scoringList = new ArrayList<>();
    scoringList.add(scoring);

    List<ScoringStatsDto> stats = ScoringCalculationsHelper.generateScoringStatsList(false,
            RENT_TRANSACTION, 2018, 7, "28041", scoringList);

    assertThat(stats.size()).isEqualTo(1);
    assertThat(stats.get(0).getPriceAverage()).isCloseTo(1058.5F, Offset.offset(0.001));
    assertThat(stats.get(0).getPriceMedian()).isCloseTo(1000.5F, Offset.offset(0.001));
    assertThat(stats.get(0).getPriceMts2Average()).isCloseTo(7.56F, Offset.offset(0.001));
    assertThat(stats.get(0).getPriceMts2Median()).isCloseTo(7.14F, Offset.offset(0.01));

    assertThat(stats.get(0).getFirstRange().getPercentage()).isEqualTo(33);
    assertThat(stats.get(0).getFirstRange().getRange()).isEqualTo("[801-1000]");

    assertThat(stats.get(0).getSecondRange().getPercentage()).isEqualTo(33);
    assertThat(stats.get(0).getSecondRange().getRange()).isEqualTo("[1001-1200]");
  }

  @Test
  public void testScoringStatsCalculationsWithAggregation() {
    Scoring scoring1 = new Scoring();
    scoring1.setTransactionId(RENT_TRANSACTION); // rent
    scoring1.addScoringDataItem(new ScoringData(800F, 140, 4));
    scoring1.addScoringDataItem(new ScoringData(850F, 140, 4));
    scoring1.addScoringDataItem(new ScoringData(1000F, 140, 4));
    scoring1.addScoringDataItem(new ScoringData(1001F, 140, 4));
    scoring1.addScoringDataItem(new ScoringData(1200F, 140, 4));
    scoring1.addScoringDataItem(new ScoringData(1500F, 140, 4));

    Scoring scoring2 = new Scoring();
    scoring2.setTransactionId(RENT_TRANSACTION); // rent
    scoring2.addScoringDataItem(new ScoringData(755F, 120, 3));
    scoring2.addScoringDataItem(new ScoringData(850F, 120, 3));
    scoring2.addScoringDataItem(new ScoringData(950F, 120, 3));
    scoring2.addScoringDataItem(new ScoringData(1015F, 120, 3));
    scoring2.addScoringDataItem(new ScoringData(1130F, 120, 3));
    scoring2.addScoringDataItem(new ScoringData(1130F, 120, 3));
    scoring2.addScoringDataItem(new ScoringData(1130F, 120, 3));
    scoring2.addScoringDataItem(new ScoringData(1700F, 120, 3));


    List<Scoring> scoringList = new ArrayList<>();
    scoringList.add(scoring1);
    scoringList.add(scoring2);

    List<ScoringStatsDto> stats = ScoringCalculationsHelper.generateScoringStatsList(true,
            RENT_TRANSACTION, 2018, 7, "28041", scoringList);

    assertThat(stats.size()).isEqualTo(1);
    assertThat(stats.get(0).getPriceAverage()).isCloseTo(1072.21F, Offset.offset(0.01));
    assertThat(stats.get(0).getPriceMedian()).isCloseTo(1008.0F, Offset.offset(0.01));
    assertThat(stats.get(0).getPriceMts2Average()).isCloseTo(8.39F, Offset.offset(0.01));
    assertThat(stats.get(0).getPriceMts2Median()).isCloseTo(8.18F, Offset.offset(0.01));

    assertThat(stats.get(0).getFirstRange().getPercentage()).isEqualTo(42);
    assertThat(stats.get(0).getFirstRange().getRange()).isEqualTo("[1001-1200]");

    assertThat(stats.get(0).getSecondRange().getPercentage()).isEqualTo(28);
    assertThat(stats.get(0).getSecondRange().getRange()).isEqualTo("[801-1000]");
  }

}
