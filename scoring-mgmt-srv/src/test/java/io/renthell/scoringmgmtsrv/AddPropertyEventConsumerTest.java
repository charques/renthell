package io.renthell.scoringmgmtsrv;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.scoringmgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
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

import io.renthell.scoringmgmtsrv.web.eventhandler.EventConsumer;
import io.renthell.scoringmgmtsrv.producer.Sender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8093" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.data.mongodb.database=test" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddPropertyEventConsumerTest {

  private static String EVENTS_TOPIC = "test";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper jsonMapper;

  @Autowired
  private Sender sender;

  @Autowired
  private EventConsumer eventConsumer;

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
    String addPropertyEventString = "{\"id\":\"7e32b1a0-7b56-4690-8456-69154092ea02\",\"creationDate\":\"Tue Aug 22 06:18:22 GMT 2017\",\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"payload\":\"{\\\"correlationId\\\":\\\"7360ee14-7cdb-458b-ac23-27084bfcb147\\\",\\\"identifier\\\":\\\"142550444\\\",\\\"publishDate\\\":\\\"01/07/2017 10:00:00\\\",\\\"region\\\":\\\"Madrid\\\",\\\"city\\\":\\\"Madrid Capital\\\",\\\"district\\\":\\\"Retiro\\\",\\\"neighbourhood\\\":\\\"Jerónimos\\\",\\\"street\\\":\\\"Alfonso XII\\\",\\\"postalCode\\\":\\\"28014\\\",\\\"property\\\":\\\"Flat\\\",\\\"propertySub\\\":\\\"Flat\\\",\\\"propertyState\\\":\\\"VeryGood\\\",\\\"propertyType\\\":\\\"Vivienda\\\",\\\"mts2\\\":\\\"140\\\",\\\"rooms\\\":\\\"3\\\",\\\"bathrooms\\\":\\\"3\\\",\\\"heating\\\":\\\"0\\\",\\\"energeticCert\\\":\\\"0\\\",\\\"features\\\":\\\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\\\",\\\"lat\\\":\\\"40.4138\\\",\\\"lng\\\":\\\"-3.68511\\\",\\\"feed\\\":\\\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\\\",\\\"transactionId\\\":\\\"3\\\",\\\"transaction\\\":\\\"alquiler\\\",\\\"price\\\":\\\"1890\\\",\\\"priceMin\\\":\\\"\\\",\\\"priceMax\\\":\\\"\\\",\\\"priceRange\\\":\\\"1501-2000\\\"}\",\"type\":\"io.renthell.eventstoresrv.web.events.PropertyTransactionAddEvent\"}";
    sender.send(EVENTS_TOPIC, addPropertyEventString);

    eventConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(eventConsumer.getLatch().getCount()).isEqualTo(0);

    // get scoring stats related with the property added
    String url = "http://localhost:8093/api/scoring-stats?" +
            "transactionId=3" + "&" +
            "postalCode=28014" + "&" +
            "year=2017" + "&" +
            "month=7";

    ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get(url));
    resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    MvcResult result = resultAction.andReturn();
    List<ScoringStatsDto> scoringStatsDtoList = jsonMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<ScoringStatsDto>>(){});

    assertThat(scoringStatsDtoList.size()).isEqualTo(1);
  }

  @Test
  public void testConsumeAddPropertyEvent_Error() throws Exception {
    String addPropertyEventString = "{\"id\":\"981633b1-8d85-49b7-a072-6e2bfd5d615f\",\"creationDate\":\"28/08/2017 07:09:07\",\"correlationId\":\"de174fdb-2713-49e5-84aa-d27316edc64f\",\"payload\":\"{\\\"id\\\":null,\\\"correlationId\\\":\\\"de174fdb-2713-49e5-84aa-d27316edc64f\\\",\\\"identifier\\\":\\\"142463151\\\",\\\"publishDate\\\":\\\"10/05/2017 23:48:05\\\",\\\"region\\\":\\\"Madrid\\\",\\\"regionCode\\\":\\\"28\\\",\\\"city\\\":\\\"Pozuelo de Alarcón\\\",\\\"district\\\":\\\"Somosaguas\\\",\\\"neighbourhood\\\":\\\"\\\",\\\"street\\\":\\\"ARROYO\\\",\\\"postalCode\\\":\\\"28223\\\",\\\"property\\\":\\\"Flat\\\",\\\"propertySub\\\":\\\"House\\\",\\\"propertyState\\\":\\\"Good\\\",\\\"propertyType\\\":\\\"Vivienda\\\",\\\"mts2\\\":\\\"\\\",\\\"rooms\\\":\\\"5\\\",\\\"bathrooms\\\":\\\"6\\\",\\\"heating\\\":\\\"0\\\",\\\"energeticCert\\\":\\\"0\\\",\\\"features\\\":\\\"piscina\\\",\\\"lat\\\":\\\"40.422638417726176\\\",\\\"lng\\\":\\\"-3.791138022704397\\\",\\\"feed\\\":\\\"https://www.fotocasa.es/vivienda/pozuelo-de-alarcon/piscina-somosaguas-142463151?RowGrid=28&tti=3&opi=300\\\",\\\"transactionId\\\":\\\"3\\\",\\\"transaction\\\":\\\"alquiler\\\",\\\"price\\\":\\\"6000\\\",\\\"priceMin\\\":\\\"\\\",\\\"priceMax\\\":\\\"\\\",\\\"priceRange\\\":\\\"5001-10000\\\"}\",\"type\":\"io.renthell.eventstoresrv.web.events.PropertyTransactionAddEvent\"}";
    sender.send(EVENTS_TOPIC, addPropertyEventString);

    eventConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(eventConsumer.getLatch().getCount()).isEqualTo(1);
  }

  @Test
  public void testConsumeAddPropertyEvent_Update() throws Exception {
    eventConsumer.setLatch(2);

    // add property event - alquiler
    String addPropertyEventString1 = "{\"id\":\"7e32b1a0-7b56-4690-8456-69154092ea02\",\"creationDate\":\"Tue Aug 22 06:18:22 GMT 2017\",\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"payload\":\"{\\\"correlationId\\\":\\\"7360ee14-7cdb-458b-ac23-27084bfcb147\\\",\\\"identifier\\\":\\\"142550444\\\",\\\"publishDate\\\":\\\"01/07/2017 12:00:00\\\",\\\"region\\\":\\\"Madrid\\\",\\\"city\\\":\\\"Madrid Capital\\\",\\\"district\\\":\\\"Retiro\\\",\\\"neighbourhood\\\":\\\"Jerónimos\\\",\\\"street\\\":\\\"Alfonso XII\\\",\\\"postalCode\\\":\\\"28014\\\",\\\"property\\\":\\\"Flat\\\",\\\"propertySub\\\":\\\"Flat\\\",\\\"propertyState\\\":\\\"VeryGood\\\",\\\"propertyType\\\":\\\"Vivienda\\\",\\\"mts2\\\":\\\"140\\\",\\\"rooms\\\":\\\"4\\\",\\\"bathrooms\\\":\\\"3\\\",\\\"heating\\\":\\\"0\\\",\\\"energeticCert\\\":\\\"0\\\",\\\"features\\\":\\\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\\\",\\\"lat\\\":\\\"40.4138\\\",\\\"lng\\\":\\\"-3.68511\\\",\\\"feed\\\":\\\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\\\",\\\"transactionId\\\":\\\"3\\\",\\\"transaction\\\":\\\"alquiler\\\",\\\"price\\\":\\\"1891\\\",\\\"priceMin\\\":\\\"\\\",\\\"priceMax\\\":\\\"\\\",\\\"priceRange\\\":\\\"1502-2000\\\"}\",\"type\":\"io.renthell.eventstoresrv.web.events.PropertyTransactionAddEvent\"}";
    sender.send(EVENTS_TOPIC, addPropertyEventString1);

    // second add property event - alquiler
    String addPropertyEventString2 = "{\"id\":\"7e32b1a0-7b56-4690-8456-69154092ea02\",\"creationDate\":\"Tue Aug 22 06:18:23 GMT 2017\",\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"payload\":\"{\\\"correlationId\\\":\\\"7360ee14-7cdb-458b-ac23-27084bfcb147\\\",\\\"identifier\\\":\\\"142550444\\\",\\\"publishDate\\\":\\\"01/07/2017 10:00:00\\\",\\\"region\\\":\\\"Madrid\\\",\\\"city\\\":\\\"Madrid Capital\\\",\\\"district\\\":\\\"Retiro\\\",\\\"neighbourhood\\\":\\\"Jerónimos\\\",\\\"street\\\":\\\"Alfonso XII\\\",\\\"postalCode\\\":\\\"28014\\\",\\\"property\\\":\\\"Flat\\\",\\\"propertySub\\\":\\\"Flat\\\",\\\"propertyState\\\":\\\"VeryGood\\\",\\\"propertyType\\\":\\\"Vivienda\\\",\\\"mts2\\\":\\\"140\\\",\\\"rooms\\\":\\\"4\\\",\\\"bathrooms\\\":\\\"3\\\",\\\"heating\\\":\\\"0\\\",\\\"energeticCert\\\":\\\"0\\\",\\\"features\\\":\\\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\\\",\\\"lat\\\":\\\"40.4138\\\",\\\"lng\\\":\\\"-3.68511\\\",\\\"feed\\\":\\\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\\\",\\\"transactionId\\\":\\\"3\\\",\\\"transaction\\\":\\\"alquiler\\\",\\\"price\\\":\\\"1591\\\",\\\"priceMin\\\":\\\"\\\",\\\"priceMax\\\":\\\"\\\",\\\"priceRange\\\":\\\"1502-2000\\\"}\",\"type\":\"io.renthell.eventstoresrv.web.events.PropertyTransactionAddEvent\"}";
    sender.send(EVENTS_TOPIC, addPropertyEventString2);

    eventConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(eventConsumer.getLatch().getCount()).isEqualTo(0);

    // get scoring stats related with the property added
    String url = "http://localhost:8093/api/scoring-stats?" +
            "transactionId=3" + "&" +
            "postalCode=28014" + "&" +
            "year=2017" + "&" +
            "month=7" + "&" +
            "rooms=4";

    ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get(url));
    resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    MvcResult result = resultAction.andReturn();
    List<ScoringStatsDto> scoringStatsDtoList = jsonMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<ScoringStatsDto>>(){});

    assertThat(scoringStatsDtoList.size()).isEqualTo(1);
    //assertThat(scoringStatsDtoList.get(0).getScoring().getScoringDataList().size()).isEqualTo(2);
  }
}
