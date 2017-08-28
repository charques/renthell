package io.renthell.scoringmgmtsrv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.scoringmgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.persistence.model.ScoringData;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8094" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.database=test",
        "spring.embedded.kafka.brokers=localhost:9092"
    })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScoringMgmtControllerTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @Test
    public void testGetAllScoringStatsSuccess() throws Exception {
        mongoTemplate.createCollection("scoring");
        mongoTemplate.insert(getScoring1());
        mongoTemplate.insert(getScoring2());

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8094/api/scoring-stats"));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<ScoringStatsDto> scoringStatsDtoList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ScoringStatsDto>>(){});

        assertThat(scoringStatsDtoList.size()).isEqualTo(2);
    }

    @Test
    public void testGetOneScoringStatsSuccess() throws Exception {
        mongoTemplate.createCollection("scoring");
        mongoTemplate.insert(getScoring1());

        String url = "http://localhost:8094/api/scoring-stats?" +
                "transactionId=3" + "&" +
                "postalCode=28041" + "&" +
                "year=2018" + "&" +
                "month=7";

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get(url));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<ScoringStatsDto> scoringStatsDtoList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ScoringStatsDto>>(){});

        assertThat(scoringStatsDtoList.size()).isEqualTo(1);
    }

    @Test
    public void testGetMultipleScoringStatsSuccess() throws Exception {
        mongoTemplate.createCollection("scoring");
        mongoTemplate.insert(getScoring1());
        mongoTemplate.insert(getScoring2());

        String url = "http://localhost:8094/api/scoring-stats?" +
                "transactionId=3" + "&" +
                "postalCode=28041" + "&" +
                "year=2018" + "&" +
                "month=7";

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get(url));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<ScoringStatsDto> scoringStatsDtoList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ScoringStatsDto>>(){});

        assertThat(scoringStatsDtoList.size()).isEqualTo(2);
    }

    @Test
    public void testGetAggregatedScoringStatsSuccess() throws Exception {
        mongoTemplate.createCollection("scoring");
        mongoTemplate.insert(getScoring1());
        mongoTemplate.insert(getScoring2());

        String url = "http://localhost:8094/api/scoring-stats?" +
                "aggregate=true" + "&" +
                "transactionId=3" + "&" +
                "postalCode=28041" + "&" +
                "year=2018";

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get(url));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<ScoringStatsDto> scoringStatsDtoList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ScoringStatsDto>>(){});

        assertThat(scoringStatsDtoList.size()).isEqualTo(1);
    }

    @Test
    public void testGetOneScoringStatsNoResult() throws Exception {

        String url = "http://localhost:8094/api/scoring-stats?" +
                "transactionId=3" + "&" +
                "postalCode=28041" + "&" +
                "year=2018" + "&" +
                "month=7";

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get(url));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<ScoringStatsDto> scoringStatsDtoList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ScoringStatsDto>>(){});

        assertThat(scoringStatsDtoList.size()).isEqualTo(0);
    }

    private Scoring getScoring1() {
        Scoring scoring = new Scoring();
        scoring.setTransactionId("3");
        scoring.setPostalCode("28041");
        scoring.setMonth(7);
        scoring.setYear(2018);
        scoring.addScoringDataItem(new ScoringData(800F, 140, 4));
        scoring.addScoringDataItem(new ScoringData(850F, 140, 4));
        scoring.addScoringDataItem(new ScoringData(1000F, 140, 4));
        scoring.addScoringDataItem(new ScoringData(1001F, 140, 4));
        scoring.addScoringDataItem(new ScoringData(1200F, 140, 4));
        scoring.addScoringDataItem(new ScoringData(1500F, 140, 4));
        return scoring;
    }

    private Scoring getScoring2() {
        Scoring scoring = new Scoring();
        scoring.setTransactionId("3");
        scoring.setPostalCode("28041");
        scoring.setMonth(7);
        scoring.setYear(2018);
        scoring.addScoringDataItem(new ScoringData(755F, 120, 3));
        scoring.addScoringDataItem(new ScoringData(850F, 120, 3));
        scoring.addScoringDataItem(new ScoringData(950F, 120, 3));
        scoring.addScoringDataItem(new ScoringData(1015F, 120, 3));
        scoring.addScoringDataItem(new ScoringData(1130F, 120, 3));
        scoring.addScoringDataItem(new ScoringData(1130F, 120, 3));
        scoring.addScoringDataItem(new ScoringData(1130F, 120, 3));
        scoring.addScoringDataItem(new ScoringData(1700F, 120, 3));
        return scoring;
    }
}
