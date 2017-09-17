package io.renthell.alertmgmtsrv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.alertmgmtsrv.config.ConfigServerWithFongoConfiguration;
import io.renthell.alertmgmtsrv.configuration.EventStoreConfiguration;
import io.renthell.alertmgmtsrv.configuration.PropertyMgmtConfiguration;
import io.renthell.alertmgmtsrv.configuration.ScoringMgmtConfiguration;
import io.renthell.alertmgmtsrv.producer.Sender;
import io.renthell.alertmgmtsrv.web.dto.AlertDto;
import io.renthell.alertmgmtsrv.web.eventhandler.EventConsumer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ConfigServerWithFongoConfiguration.class }, properties = {
        "server.port=8093" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.data.mongodb.database=test" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddedPropertyEventConsumerTest {

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

  @Autowired
  private PropertyMgmtConfiguration propertyMgmtConfiguration;

  @Autowired
  private ScoringMgmtConfiguration scoringMgmtConfiguration;

  @Autowired
  private EventStoreConfiguration eventStoreConfiguration;

  @Autowired
  private RestTemplate restTemplate;

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

    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();

    // mock property get
    String urlString1 = propertyMgmtConfiguration.getPropertyUri();
    UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(urlString1);
    Map<String, String> uriParams1 = new HashMap<String, String>();
    uriParams1.put("id", "142550444");

    URI uri1 = builder1.buildAndExpand(uriParams1).toUri();
    String propertyResponseBody = "{\"identifier\":\"142550444\",\"publishDate\":\"30/08/2017 21:33:15\",\"region\":\"Madrid\",\"regionCode\":null,\"city\":\"Madrid Capital\",\"district\":\"Retiro\",\"neighbourhood\":\"Jerónimos\",\"street\":\"Alfonso XII\",\"postalCode\":\"28014\",\"property\":\"Flat\",\"propertySub\":\"Flat\",\"propertyState\":\"VeryGood\",\"propertyType\":\"Vivienda\",\"mts2\":\"140\",\"rooms\":\"4\",\"bathrooms\":\"3\",\"heating\":\"0\",\"energeticCert\":\"0\",\"features\":\"aire-acondicionado|||calefaccion|||garaje-privado|||ascensor\",\"lat\":\"40.4138\",\"lng\":\"-3.68511\",\"feed\":\"https://www.fotocasa.es/vivienda/madrid-capital/aire-acondicionado-calefaccion-parking-ascensor-alfonso-xii-142550444?RowGrid=11&tti=3&opi=300\",\"transactions\":[{\"transactionId\":\"3\",\"transaction\":\"alquiler\",\"price\":\"1890.0\",\"priceMin\":\"0.0\",\"priceMax\":\"0.0\",\"priceRange\":\"1501-2000\"}],\"calculations\":{\"rentGrossReturn\":null,\"rentPer\":null,\"rentMt2Price\":13.5,\"saleMt2Price\":null},\"updated\":null}";
    mockServer.expect(requestTo(uri1.toString())).andRespond(withSuccess(propertyResponseBody, MediaType.APPLICATION_JSON));

    // mock scoring get
    String urlString2 = scoringMgmtConfiguration.getScoringUri();
    UriComponentsBuilder builder2 = UriComponentsBuilder.fromUriString(urlString2)
            .queryParam("aggregate", "false")
            .queryParam("transactionId", "3")
            .queryParam("month", "8")
            .queryParam("year", "2017")
            .queryParam("postalCode", "28014")
            .queryParam("rooms", "4");

    URI uri2 = builder2.build().toUri();
    String scoringResponseBody = "[{\"scoring\":{\"transactionId\":\"3\",\"month\":7,\"year\":2017,\"postalCode\":\"28014\"},\"priceAverage\":1058.5,\"priceMedian\":1000.5,\"priceMts2Average\":7.560714324315389,\"priceMts2Median\":7.14642858505249,\"firstRange\":{\"percentage\":33,\"range\":\"[801-1000]\"},\"secondRange\":{\"percentage\":33,\"range\":\"[1001-1200]\"},\"aggregated\":false}]";
    mockServer.expect(requestTo(uri2.toString())).andRespond(withSuccess(scoringResponseBody, MediaType.APPLICATION_JSON));

    // mock event store rest api
    final String EVENT_STORE_URI = eventStoreConfiguration.addAlertCommandUri();
    mockServer.expect(requestTo(EVENT_STORE_URI)).andRespond(withSuccess());

    // produce event
    String confirmPropertyEventString = "{\"id\":\"7e32b1a0-7b56-4690-8456-69154092ea02\",\"creationDate\":\"Tue Aug 22 06:18:22 GMT 2017\",\"correlationId\":\"7360ee14-7cdb-458b-ac23-27084bfcb147\",\"payload\":\"{\\\"identifier\\\":\\\"142550444\\\",\\\"transactionId\\\":\\\"3\\\"}\",\"type\":\"io.renthell.eventstoresrv.web.events.PropertyTransactionConfirmEvent\"}";
    sender.send(EVENTS_TOPIC, confirmPropertyEventString);

    eventConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(eventConsumer.getLatch().getCount()).isEqualTo(0);

    // get alerts
    ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8093/api/alert"));
    resultAction.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    MvcResult result = resultAction.andReturn();
    List<AlertDto> alertResponseList = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<AlertDto>>(){});
    AlertDto alert = alertResponseList.get(0);

    Assert.assertTrue(alertResponseList.size() == 1);
    Assert.assertEquals(alert.getAlertDescriptor(), "OverPriceAverageRule: [property id: 142550444], [transaction id: 3], [transaction price: 1890.0], [average price: 1058.5], [percentage: 15], [average price + percentage: 1217.275]");
  }

}
