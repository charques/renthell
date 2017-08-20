package io.renthell.propertymgmtsrv;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.propertymgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.propertymgmtsrv.web.eventhandler.PropertyAddedEventConsumer;
import io.renthell.propertymgmtsrv.persistence.model.Property;
import io.renthell.propertymgmtsrv.producer.Sender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8090" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.data.mongodb.database=test" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PropertyAddedEventConsumerTest {

  private static String EVENTS_TOPIC = "test";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper jsonMapper;

  @Autowired
  private Sender sender;

  @Autowired
  private PropertyAddedEventConsumer propertyAddedEventConsumer;

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
  public void testConsumeAddPropertyEvent_Add() throws Exception {
    // add property event
    String addPropertyEventString = "{\"id\":\"7e32b1a0-7b56-4690-8456-69154092ea02\",\"transactionTime\":1502011458.752000000,\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"payload\":\"{\\\"correlationId\\\":\\\"7360ee14-7cdb-458b-ac23-27084bfcb147\\\",\\\"identifier\\\":\\\"142550444\\\",\\\"publishDate\\\":null,\\\"region\\\":\\\"Madrid\\\",\\\"city\\\":\\\"Madrid Capital\\\",\\\"district\\\":\\\"Retiro\\\",\\\"neighbourhood\\\":\\\"Jerónimos\\\",\\\"street\\\":\\\"Alfonso XII\\\",\\\"postalCode\\\":\\\"28014\\\",\\\"property\\\":\\\"Flat\\\",\\\"propertySub\\\":\\\"Flat\\\",\\\"propertyState\\\":\\\"VeryGood\\\",\\\"propertyType\\\":\\\"Vivienda\\\",\\\"mts2\\\":\\\"140\\\",\\\"rooms\\\":\\\"4\\\",\\\"bathrooms\\\":\\\"3\\\",\\\"heating\\\":\\\"0\\\",\\\"energeticCert\\\":\\\"0\\\",\\\"features\\\":\\\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\\\",\\\"lat\\\":\\\"40.4138\\\",\\\"lng\\\":\\\"-3.68511\\\",\\\"feed\\\":\\\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\\\",\\\"transactionId\\\":\\\"3\\\",\\\"transaction\\\":\\\"alquiler\\\",\\\"price\\\":\\\"1890\\\",\\\"priceMin\\\":\\\"\\\",\\\"priceMax\\\":\\\"\\\",\\\"priceRange\\\":\\\"1501-2000\\\"}\",\"type\":\"io.renthell.eventstoresrv.events.PropertyTransactionAddedEvent\"}";
    sender.send(EVENTS_TOPIC, addPropertyEventString);

    propertyAddedEventConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(propertyAddedEventConsumer.getLatch().getCount()).isEqualTo(0);
  }

  @Test
  public void testConsumeAddPropertyEvent_Update() throws Exception {

    // add property event - alquiler
    String addPropertyEventString1 = "{\"id\":\"7e32b1a0-7b56-4690-8456-69154092ea02\",\"transactionTime\":1502011458.752000000,\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"payload\":\"{\\\"correlationId\\\":\\\"7360ee14-7cdb-458b-ac23-27084bfcb147\\\",\\\"identifier\\\":\\\"142550444\\\",\\\"publishDate\\\":null,\\\"region\\\":\\\"Madrid\\\",\\\"city\\\":\\\"Madrid Capital\\\",\\\"district\\\":\\\"Retiro\\\",\\\"neighbourhood\\\":\\\"Jerónimos\\\",\\\"street\\\":\\\"Alfonso XII\\\",\\\"postalCode\\\":\\\"28014\\\",\\\"property\\\":\\\"Flat\\\",\\\"propertySub\\\":\\\"Flat\\\",\\\"propertyState\\\":\\\"VeryGood\\\",\\\"propertyType\\\":\\\"Vivienda\\\",\\\"mts2\\\":\\\"140\\\",\\\"rooms\\\":\\\"4\\\",\\\"bathrooms\\\":\\\"3\\\",\\\"heating\\\":\\\"0\\\",\\\"energeticCert\\\":\\\"0\\\",\\\"features\\\":\\\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\\\",\\\"lat\\\":\\\"40.4138\\\",\\\"lng\\\":\\\"-3.68511\\\",\\\"feed\\\":\\\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\\\",\\\"transactionId\\\":\\\"3\\\",\\\"transaction\\\":\\\"alquiler\\\",\\\"price\\\":\\\"1890\\\",\\\"priceMin\\\":\\\"\\\",\\\"priceMax\\\":\\\"\\\",\\\"priceRange\\\":\\\"1501-2000\\\"}\",\"type\":\"io.renthell.eventstoresrv.events.PropertyTransactionAddedEvent\"}";
    sender.send(EVENTS_TOPIC, addPropertyEventString1);

    // second add property event - venta
    DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
    String dateToReplace = df.format(new Date());

    String addPropertyEventString2 = "{\"id\":\"7e32b1a0-7b56-4690-8456-69154092ea02\",\"transactionTime\":1502011458.752000000,\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"payload\":\"{\\\"correlationId\\\":\\\"7360ee14-7cdb-458b-ac23-27084bfcb147\\\",\\\"identifier\\\":\\\"142550444\\\",\\\"publishDate\\\":\\\"01/07/2017 10:00:00\\\",\\\"region\\\":\\\"Madrid\\\",\\\"city\\\":\\\"Madrid Capital\\\",\\\"district\\\":\\\"Retiro\\\",\\\"neighbourhood\\\":\\\"Jerónimos\\\",\\\"street\\\":\\\"Alfonso XII\\\",\\\"postalCode\\\":\\\"28014\\\",\\\"property\\\":\\\"Flat\\\",\\\"propertySub\\\":\\\"Flat\\\",\\\"propertyState\\\":\\\"VeryGood\\\",\\\"propertyType\\\":\\\"Vivienda\\\",\\\"mts2\\\":\\\"140\\\",\\\"rooms\\\":\\\"4\\\",\\\"bathrooms\\\":\\\"3\\\",\\\"heating\\\":\\\"0\\\",\\\"energeticCert\\\":\\\"0\\\",\\\"features\\\":\\\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\\\",\\\"lat\\\":\\\"40.4138\\\",\\\"lng\\\":\\\"-3.68511\\\",\\\"feed\\\":\\\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\\\",\\\"transactionId\\\":\\\"1\\\",\\\"transaction\\\":\\\"venta\\\",\\\"price\\\":\\\"3000000\\\",\\\"priceMin\\\":\\\"\\\",\\\"priceMax\\\":\\\"\\\",\\\"priceRange\\\":\\\"2500000-300000\\\"}\",\"type\":\"io.renthell.eventstoresrv.events.PropertyTransactionAddedEvent\"}";
    addPropertyEventString2 = addPropertyEventString2.replace("01/07/2017", dateToReplace);
    sender.send(EVENTS_TOPIC, addPropertyEventString2);

    propertyAddedEventConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(propertyAddedEventConsumer.getLatch().getCount()).isEqualTo(0);

    // get property updated
    ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8090/api/property-transaction/142550444"));
    resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    MvcResult result = resultAction.andReturn();
    Property propertyResponse = jsonMapper.readValue(result.getResponse().getContentAsString(), Property.class);

    Assert.assertEquals(propertyResponse.getTransactions().size(), 2);
  }
}
