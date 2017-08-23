package io.renthell.propertymgmtsrv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.propertymgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.propertymgmtsrv.persistence.model.Calculations;
import io.renthell.propertymgmtsrv.persistence.model.Property;
import io.renthell.propertymgmtsrv.persistence.model.Transaction;
import io.renthell.propertymgmtsrv.configuration.EventStoreConfiguration;
import io.renthell.propertymgmtsrv.web.dto.PropertyDto;
import io.renthell.propertymgmtsrv.web.dto.PropertyInputDto;
import io.renthell.propertymgmtsrv.web.dto.TransactionDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8091" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.database=test",
        "spring.embedded.kafka.brokers=localhost:9092"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PropertyMgmtControllerTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private EventStoreConfiguration eventStoreConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testAddPropertyTransactionSuccess() throws Exception {

        // mock event store rest api
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        final String EVENT_STORE_URI = eventStoreConfiguration.addPropertyCommandUri();
        mockServer.expect(requestTo(EVENT_STORE_URI)).andRespond(withCreatedEntity(new URI("/commands/get-raw-event/0")));

        // add property transaction
        PropertyInputDto property = getTestPropertyInputDto();
        String body = jsonMapper.writeValueAsString(property);

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8091/api/property-transaction")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testAddPropertyTransactionError() throws Exception {

        // mock event store rest api
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        final String EVENT_STORE_URI = eventStoreConfiguration.addPropertyCommandUri();
        mockServer.reset();
        mockServer.expect(requestTo(EVENT_STORE_URI)).andRespond(withBadRequest());

        // add property transaction
        PropertyDto property = new PropertyDto();
        property.setIdentifier("142550444");

        String body = jsonMapper.writeValueAsString(property);

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8091/api/property-transaction")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testGetPropertyTransactionSuccess() throws Exception {
        Property propertyFongo = getTestProperty("142550444");
        Transaction transactionFongo = propertyFongo.getTransactions().get(0);

        mongoTemplate.createCollection("property");
        mongoTemplate.insert(propertyFongo);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8091/api/property-transaction/" + propertyFongo.getIdentifier()));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        PropertyDto propertyResponse = jsonMapper.readValue(result.getResponse().getContentAsString(), PropertyDto.class);
        TransactionDto transactionResponse = propertyResponse.getTransactions().get(0);

        Assert.assertEquals(propertyFongo.getIdentifier(), propertyResponse.getIdentifier());
        Assert.assertEquals(transactionFongo.getTransactionId(), transactionResponse.getTransactionId());
    }

    @Test
    public void testGetPropertyTransactionNoResult() throws Exception {
        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8091/api/property-transaction/0" ));
        resultAction.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testGetPropertyTransactionListSuccess() throws Exception {
        Property propertyFongo1 = getTestProperty("142550444");
        Transaction transactionFongo1 = propertyFongo1.getTransactions().get(0);
        Property propertyFongo2 = getTestProperty("142550442");

        mongoTemplate.createCollection("property");
        mongoTemplate.insert(propertyFongo1);
        mongoTemplate.insert(propertyFongo2);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8091/api/property-transaction"));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        List<PropertyDto> propertyResponseList = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PropertyDto>>(){});
        PropertyDto propertyResponse = propertyResponseList.get(0);
        TransactionDto transactionResponse = propertyResponse.getTransactions().get(0);

        Assert.assertTrue(propertyResponseList.size() == 2);
        Assert.assertEquals(propertyFongo1.getIdentifier(), propertyResponse.getIdentifier());
        Assert.assertEquals(transactionFongo1.getTransactionId(), transactionResponse.getTransactionId());
    }

    private PropertyInputDto getTestPropertyInputDto() {
        PropertyInputDto property = new PropertyInputDto();
        property.setIdentifier("142550444");
        property.setPublishDate(new Date());
        property.setRegion("Madrid");
        property.setCity("Madrid Capital");
        property.setDistrict("Retiro");
        property.setNeighbourhood("Jerónimos");
        property.setStreet("Alfonso XII");
        property.setPostalCode("28014");
        property.setProperty("Flat");
        property.setPropertySub("Flat");
        property.setPropertyState("VeryGood");
        property.setPropertyType("Vivienda");
        property.setMts2("140");
        property.setRooms("4");
        property.setBathrooms("3");
        property.setHeating("0");
        property.setEnergeticCert("0");
        property.setFeatures("aire-acondicionado|||calefaccion|||garaje-privado|||ascensor");
        property.setLat("40.4138");
        property.setLng("-3.68511");
        property.setFeed("https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300");

        property.setTransactionId("3");
        property.setTransaction("alquiler");
        property.setPrice("1890");
        property.setPriceMin("");
        property.setPriceMax("");
        property.setPriceRange("1501-2000");

        return property;
    }

    private Property getTestProperty(String identifier) {
        Property property = new Property();
        property.setIdentifier(identifier);
        property.setPublishDate(new Date());
        property.setRegion("Madrid");
        property.setCity("Madrid Capital");
        property.setDistrict("Retiro");
        property.setNeighbourhood("Jerónimos");
        property.setStreet("Alfonso XII");
        property.setPostalCode("28014");
        property.setProperty("Flat");
        property.setPropertySub("Flat");
        property.setPropertyState("VeryGood");
        property.setPropertyType("Vivienda");
        property.setMts2(140);
        property.setRooms("4");
        property.setBathrooms("3");
        property.setHeating("0");
        property.setEnergeticCert("0");
        property.setFeatures("aire-acondicionado|||calefaccion|||garaje-privado|||ascensor");
        property.setLat("40.4138");
        property.setLng("-3.68511");
        property.setFeed("https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300");

        List<Transaction> transactionsFongo = new ArrayList<>();
        Transaction transactionFongo = new Transaction();
        transactionFongo.setTransactionId("3");
        transactionFongo.setTransaction("alquiler");
        transactionFongo.setPrice(1890F);
        transactionFongo.setPriceMin(0F);
        transactionFongo.setPriceMax(0F);
        transactionFongo.setPriceRange("1501-2000");
        transactionsFongo.add(transactionFongo);

        property.updateCalculations();

        property.setTransactions(transactionsFongo);

        return property;
    }
}
