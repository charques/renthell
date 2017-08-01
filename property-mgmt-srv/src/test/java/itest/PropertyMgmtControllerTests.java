package itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itest.config.ConfigServerWithFongoConfiguration;
import io.renthell.propertymgmtsrv.api.model.PropertyTransactionApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8980" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.data.mongodb.database=test" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PropertyMgmtControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @Test
    public void testAddPropertyTransaction() throws Exception {

        PropertyTransactionApi property = new PropertyTransactionApi();
        property.setIdentifier("142550444");
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

        String body = jsonMapper.writeValueAsString(property);

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8090/api/property-transaction")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
