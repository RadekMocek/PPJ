package cz.tul.ppj;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import cz.tul.ppj.service.jpa.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {PpjApplication.class})
@ActiveProfiles({"test"})
public class WeatherRestTest {

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    @Autowired
    private WeatherService weatherService;

    @LocalServerPort
    private int port;

    WebTestClient client;

    @Before
    public void init() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port + "/api").build();
        stateService.deleteAll();
    }

    private final State state1 = new State("CZ", "Czechia");
    private final State state2 = new State("GE", "Georgia");
    private final City city11 = new City(state1, "Prague");
    private final City city12 = new City(state1, "Liberec");
    private final City city21 = new City(state2, "Tbilisi");
    private final City city22 = new City(state2, "Batumi");
    private final Weather weather111 = new Weather(1745157600, city11, 294.96f, 294.58f, 1006, 53, "scattered clouds");
    private final Weather weather112 = new Weather(1745240400, city11, 291.38f, 290.85f, 1011, 61, "broken clouds");
    private final Weather weather121 = new Weather(1745157600, city12, 293.57f, 292.89f, 1007, 47, "clear sky");
    private final Weather weather122 = new Weather(1745240400, city12, 287.56f, 287.07f, 1011, 77, "overcast clouds");
    private final Weather weather211 = new Weather(1745157600, city21, 288.99f, 287.96f, 1016, 51, "scattered clouds");
    private final Weather weather212 = new Weather(1745240400, city21, 290.99f, 290.27f, 1014, 55, "scattered clouds");
    private final Weather weather221 = new Weather(1745157600, city22, 284.14f, 283.72f, 1015, 93, "mist");
    private final Weather weather222 = new Weather(1745240400, city22, 287.14f, 286.74f, 1015, 82, "clear sky");

    @Test
    public void testGet() {
        stateService.create(state1);
        cityService.create(city11);
        weatherService.create(weather111);

        client.get().uri("/weathers/" + city11.getStateId() + "/" + city11.getName() + "/" + weather111.getTimestamp())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Weather.class).value(result -> assertThat(result).isEqualTo(weather111));

        client.get().uri("/weathers/" + city11.getStateId() + "/" + city11.getName() + "/" + weather112.getTimestamp())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

}
