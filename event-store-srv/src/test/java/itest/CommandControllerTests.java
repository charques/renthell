package itest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.commands.AddPropertyTransactionCmd;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    private ObjectMapper jsonMapper;

    @Before
    public void setUp() {
        jsonMapper = new ObjectMapper();
    }

    /*@Test
    public void testGetPerson() throws Exception {

        AddPropertyTransactionCmd addPropertyFongo = new AddPropertyTransactionCmd();
        addPropertyFongo.set

        {"correlationId":"c39b2782-5e0b-4ad0-9402-d9eeca124475","identifier":"142550444","publishDate":"18/05/2017 8:01:36","region":"Madrid","city":"Madrid Capital","district":"Retiro","neighbourhood":"Jerónimos","street":"Alfonso XII","postalCode":"28014","property":"Flat","propertySub":"Flat","propertyState":"VeryGood","propertyType":"Vivienda","mts2":"140","rooms":"4","bathrooms":"3","heating":"0","energeticCert":"0","features":"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor","lat":"40.4138","lng":"-3.68511","feed":"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300","transactionId":"3","transaction":"alquiler","price":"1890","priceMin":"","priceMax":"","priceRange":"1501-2000"}
        Person personFongo = new Person();
        personFongo.setId(1);
        personFongo.setName("Name1");
        personFongo.setAddress("Address1");
        mongoTemplate.createCollection("person");
        mongoTemplate.insert(personFongo);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8090/api/person/1"));
        resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MvcResult result = resultAction.andReturn();
        Person personResponse = jsonMapper.readValue(result.getResponse().getContentAsString(), Person.class);
        Assert.assertEquals(1, personResponse.getId());
        Assert.assertEquals("Name1", personResponse.getName());
        Assert.assertEquals("Address1", personResponse.getAddress());

    }*/

    @Test
    public void testCreatePerson() throws Exception {

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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8090/commands/add-property-transaction")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        JsonNode eventJson = jsonMapper.readTree(content);
        JsonNode payloadJson = jsonMapper.readTree(eventJson.get("payload").textValue());
        Assert.assertEquals(payloadJson.get("identifier").textValue(), "142550444");
    }
}
