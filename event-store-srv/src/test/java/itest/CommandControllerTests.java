package itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.commands.AddPropertyTransactionCmd;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import itest.config.ConfigServerWithFongoConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8980" }, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.data.mongodb.database=test" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommandControllerTests {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @Before
    public void setUp() {
    }

    @Test
    public void testGetRawEvent() throws Exception {

        String correlationId = UUID.randomUUID().toString();
        RawEvent rawEventFongo = new RawEvent(correlationId, "{}", "Test");

        mongoTemplate.createCollection("rawevent");
        mongoTemplate.insert(rawEventFongo);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8090/commands/get-raw-event/" + rawEventFongo.getId()));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        RawEvent rawEventResponse = jsonMapper.readValue(result.getResponse().getContentAsString(), RawEvent.class);
        Assert.assertEquals(rawEventFongo.getId(), rawEventResponse.getId());
    }

    @Test
    public void testGetRawEventError() throws Exception {

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8090/commands/get-raw-event/xxx"));
        resultAction.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testAddPropertyTransaction() throws Exception {

        AddPropertyTransactionCmd addPropertyCmd = new AddPropertyTransactionCmd();
        addPropertyCmd.setIdentifier("142550444");
        addPropertyCmd.setRegion("Madrid");
        addPropertyCmd.setCity("Madrid Capital");
        addPropertyCmd.setDistrict("Retiro");
        addPropertyCmd.setNeighbourhood("Jerónimos");
        addPropertyCmd.setStreet("Alfonso XII");
        addPropertyCmd.setPostalCode("28014");
        addPropertyCmd.setProperty("Flat");
        addPropertyCmd.setPropertySub("Flat");
        addPropertyCmd.setPropertyState("VeryGood");
        addPropertyCmd.setPropertyType("Vivienda");
        addPropertyCmd.setMts2("140");
        addPropertyCmd.setRooms("4");
        addPropertyCmd.setBathrooms("3");
        addPropertyCmd.setHeating("0");
        addPropertyCmd.setEnergeticCert("0");
        addPropertyCmd.setFeatures("aire-acondicionado|||calefaccion|||garaje-privado|||ascensor");
        addPropertyCmd.setLat("40.4138");
        addPropertyCmd.setLng("-3.68511");
        addPropertyCmd.setFeed("https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300");
        addPropertyCmd.setTransactionId("3");
        addPropertyCmd.setTransaction("alquiler");
        addPropertyCmd.setPrice("1890");
        addPropertyCmd.setPriceMin("");
        addPropertyCmd.setPriceMax("");
        addPropertyCmd.setPriceRange("1501-2000");

        String body = jsonMapper.writeValueAsString(addPropertyCmd);

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8090/commands/add-property-transaction")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}