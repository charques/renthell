package io.renthell.scoringmgmtsrv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.scoringmgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.scoringmgmtsrv.model.Scoring;
import io.renthell.scoringmgmtsrv.model.ScoringStats;
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
        "server.port=8980" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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
    public void testGetScoringStatsSuccess() throws Exception {
        Scoring scoringFongo1 = new Scoring();
        scoringFongo1.setTransactionId("3");
        scoringFongo1.setRegion("madrid");
        scoringFongo1.setRegionCode("28");
        scoringFongo1.setCity("madrid capital");
        scoringFongo1.setPostalCode("28041");
        scoringFongo1.setDistrict("villaverde");
        scoringFongo1.setMonth(7);
        scoringFongo1.setYear(2018);
        scoringFongo1.setRooms(3);
        scoringFongo1.addPrice(850F);
        scoringFongo1.addPrice(900F);
        scoringFongo1.addPrice(1000F);
        scoringFongo1.addPrice(1200F);
        scoringFongo1.addPrice(1500F);

        Scoring scoringFongo2 = new Scoring();
        scoringFongo2.setTransactionId("3");
        scoringFongo2.setRegion("madrid");
        scoringFongo2.setRegionCode("28");
        scoringFongo2.setCity("madrid capital");
        scoringFongo2.setPostalCode("28041");
        scoringFongo2.setDistrict("villaverde");
        scoringFongo2.setMonth(7);
        scoringFongo2.setYear(2018);
        scoringFongo2.setRooms(3);
        scoringFongo2.addPrice(850F);
        scoringFongo2.addPrice(900F);
        scoringFongo2.addPrice(1500F);

        mongoTemplate.createCollection("scoring");
        mongoTemplate.insert(scoringFongo1);
        mongoTemplate.insert(scoringFongo2);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8090/api/scoring-stats"));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<ScoringStats> scoringStatsList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ScoringStats>>(){});

        assertThat(scoringStatsList.size()).isEqualTo(2);
    }

}
