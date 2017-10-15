package io.renthell.alertmgmtsrv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.alertmgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.alertmgmtsrv.persistence.model.Alert;
import io.renthell.alertmgmtsrv.web.dto.AlertDto;
import org.junit.Assert;
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

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8091" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.database=test",
        "spring.embedded.kafka.brokers=localhost:9092"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AlertMgmtControllerTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @Test
    public void testGetPropertyTransaction_Success() throws Exception {
        Alert alertFongo = getTestAlert("142550444");

        mongoTemplate.createCollection("alert");
        mongoTemplate.insert(alertFongo);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8091/api/alert/" + alertFongo.getId()));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        AlertDto propertyResponse = jsonMapper.readValue(result.getResponse().getContentAsString(), AlertDto.class);

        Assert.assertEquals(alertFongo.getId(), propertyResponse.getId());
        Assert.assertEquals(alertFongo.getPropertyId(), alertFongo.getPropertyId());
    }

    @Test
    public void testGetPropertyTransaction_NoResult() throws Exception {
        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8091/api/alert/0" ));
        resultAction.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testGetPropertyTransaction_ListSuccess() throws Exception {
        Alert alertFongo1 = getTestAlert("142550444");
        Alert alertFongo2 = getTestAlert("142550442");

        mongoTemplate.createCollection("alert");
        mongoTemplate.insert(alertFongo1);
        mongoTemplate.insert(alertFongo2);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8091/api/alert"));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<AlertDto> propertyResponseList = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<AlertDto>>(){});
        AlertDto propertyResponse = propertyResponseList.get(0);

        Assert.assertTrue(propertyResponseList.size() == 2);
        Assert.assertEquals(alertFongo1.getId(), propertyResponse.getId());
        Assert.assertEquals(alertFongo1.getPropertyId(), alertFongo1.getPropertyId());
    }

    private Alert getTestAlert(String identifier) {
        Alert alert = new Alert();
        alert.setCreatedDate(new Date());
        alert.setPropertyId(identifier);
        alert.setAlertDescriptor("TestDescriptor");
        alert.setTransactionId("3");

        return alert;
    }
}
