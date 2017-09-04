package io.renthell.scoringmgmtsrv;

import io.renthell.scoringmgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.persistence.model.ScoringData;
import io.renthell.scoringmgmtsrv.service.ScoringCalculationsHelper;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import org.assertj.core.data.Offset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8095" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.database=test",
        "spring.embedded.kafka.brokers=localhost:9092"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScoringCalculationsHelperTest {

  final String RENT_TRANSACTION = "3";

  @Autowired
  private ScoringCalculationsHelper scoringCalculationsHelper;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testScoringStatsCalculationsWithoutAggregation() {
    Scoring scoring = new Scoring();
    scoring.setTransactionId(RENT_TRANSACTION); // rent
    scoring.setRooms(3);
    scoring.addScoringDataItem(new ScoringData(800F, 140));
    scoring.addScoringDataItem(new ScoringData(850F, 140));
    scoring.addScoringDataItem(new ScoringData(1000F, 140));
    scoring.addScoringDataItem(new ScoringData(1001F, 140));
    scoring.addScoringDataItem(new ScoringData(1200F, 140));
    scoring.addScoringDataItem(new ScoringData(1500F, 140));

    List<Scoring> scoringList = new ArrayList<>();
    scoringList.add(scoring);

    List<ScoringStatsDto> stats = scoringCalculationsHelper.generateScoringStatsList(false,
            RENT_TRANSACTION, 2018, 7, "28041", 3, scoringList);

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
    scoring1.setRooms(3);
    scoring1.addScoringDataItem(new ScoringData(800F, 140));
    scoring1.addScoringDataItem(new ScoringData(850F, 140));
    scoring1.addScoringDataItem(new ScoringData(1000F, 140));
    scoring1.addScoringDataItem(new ScoringData(1001F, 140));
    scoring1.addScoringDataItem(new ScoringData(1200F, 140));
    scoring1.addScoringDataItem(new ScoringData(1500F, 140));

    Scoring scoring2 = new Scoring();
    scoring2.setTransactionId(RENT_TRANSACTION); // rent
    scoring2.setRooms(3);
    scoring2.addScoringDataItem(new ScoringData(755F, 120));
    scoring2.addScoringDataItem(new ScoringData(850F, 120));
    scoring2.addScoringDataItem(new ScoringData(950F, 120));
    scoring2.addScoringDataItem(new ScoringData(1015F, 120));
    scoring2.addScoringDataItem(new ScoringData(1130F, 120));
    scoring2.addScoringDataItem(new ScoringData(1130F, 120));
    scoring2.addScoringDataItem(new ScoringData(1130F, 120));
    scoring2.addScoringDataItem(new ScoringData(1700F, 120));


    List<Scoring> scoringList = new ArrayList<>();
    scoringList.add(scoring1);
    scoringList.add(scoring2);

    List<ScoringStatsDto> stats = scoringCalculationsHelper.generateScoringStatsList(true,
            RENT_TRANSACTION, 2018, 7, "28041", 3, scoringList);

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
