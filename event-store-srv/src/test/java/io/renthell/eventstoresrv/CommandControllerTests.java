package io.renthell.eventstoresrv;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.web.command.AddPropertyTransactionCmd;
import io.renthell.eventstoresrv.persistence.model.RawEvent;
import io.renthell.eventstoresrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.eventstoresrv.web.events.BaseEvent;
import io.renthell.eventstoresrv.web.events.PropertyTransactionAddedEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
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

    private static String EVENTS_TOPIC = "test";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, EVENTS_TOPIC);

    @Before
    public void setUp() throws Exception {
        // wait until the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafka.getPartitionsPerTopic());
        }
        embeddedKafka.restart(0);
    }

    @Test
    public void testGetEvent() throws Exception {

        String correlationId = UUID.randomUUID().toString();
        //01/07/2017 10:00:00
        String payload = "{\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"identifier\":\"142550444\",\"publishDate\":\"01/07/2017 10:00:00\",\"region\":\"Madrid\",\"regionCode\":\"28\",\"city\":\"Madrid Capital\",\"district\":\"Retiro\",\"neighbourhood\":\"Jerónimos\",\"street\":\"Alfonso XII\",\"postalCode\":\"28014\",\"property\":\"Flat\",\"propertySub\":\"Flat\",\"propertyState\":\"VeryGood\",\"propertyType\":\"Vivienda\",\"mts2\":\"140\",\"rooms\":\"4\",\"bathrooms\":\"3\",\"heating\":\"0\",\"energeticCert\":\"0\",\"features\":\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\",\"lat\":\"40.4138\",\"lng\":\"-3.68511\",\"feed\":\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\",\"transactionId\":\"3\",\"transaction\":\"alquiler\",\"price\":\"1890\",\"priceMin\":\"\",\"priceMax\":\"\",\"priceRange\":\"1501-2000\"}";
        String type = "io.renthell.eventstoresrv.web.events.PropertyTransactionAddedEvent";
        RawEvent rawEventFongo = new RawEvent(correlationId, payload, type);

        mongoTemplate.createCollection("rawevent");
        mongoTemplate.insert(rawEventFongo);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8980/commands/get-event/" + rawEventFongo.getId()));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        BaseEvent baseEventResponse = jsonMapper.readValue(result.getResponse().getContentAsString(), PropertyTransactionAddedEvent.class);
        Assert.assertEquals(rawEventFongo.getId(), baseEventResponse.getId());
    }

    @Test
    public void testGetEventError() throws Exception {

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8980/commands/get-event/xxx"));
        resultAction.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testAddPropertyTransaction() throws Exception {

        AddPropertyTransactionCmd addPropertyCmd = new AddPropertyTransactionCmd();
        addPropertyCmd.setIdentifier("142550444");
        addPropertyCmd.setPublishDate("01/07/2017 10:00:00");
        addPropertyCmd.setRegion("Madrid");
        addPropertyCmd.setRegionCode("28");
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

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8980/commands/add-property-transaction")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
