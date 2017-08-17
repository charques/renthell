package io.renthell.scoringmgmtsrv;

import io.renthell.scoringmgmtsrv.config.SimpleConfiguration;
import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SimpleConfiguration.class)
public class ScoringStatsDtoTest {

  ScoringStatsDto stats = null;
  @Before
  public void setUp() throws Exception {
    Scoring scoring = new Scoring();
    scoring.addPrice(850F);
    scoring.addPrice(900F);
    scoring.addPrice(1000F);
    scoring.addPrice(1200F);
    scoring.addPrice(1500F);

    stats = new ScoringStatsDto(scoring);
  }

  @Test
  public void testAverage() {
    assertThat(stats.getAverage()).isEqualTo(1090F);
  }

  @Test
  public void testMedian() {
    assertThat(stats.getMedian()).isEqualTo(1000F);
  }

}
