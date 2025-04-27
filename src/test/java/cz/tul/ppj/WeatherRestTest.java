package cz.tul.ppj;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.dto.WeatherAverageDTO;
import cz.tul.ppj.model.dto.WeatherFetchDTO;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import cz.tul.ppj.service.jpa.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

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
    public void testGetEmpty() {
        client.get().uri("/weathers/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[]");
        client.get().uri("/weathers/summary")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[]");
    }

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

    @Test
    public void testGetAll() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12, city21, city22)));
        weatherService.createBulk(new ArrayList<>(Arrays.asList(weather111, weather112, weather121, weather122, weather222)));

        List<Weather> weathers = client.get().uri("/weathers/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Weather.class).returnResult().getResponseBody();

        assertThat(weathers)
                .hasSize(5)
                .containsExactlyInAnyOrder(weather111, weather112, weather121, weather122, weather222);

        List<Map<String, Object>> weatherSummaries = client.get().uri("/weathers/summary")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(weatherSummaries).hasSize(3);
        assertThat(weatherSummaries.getFirst().get("stateId")).isEqualTo("CZ");
        assertThat(weatherSummaries.getFirst().get("cityName")).isEqualTo("Liberec");
        assertThat(weatherSummaries.getFirst().get("weatherCount")).isEqualTo(2);
        assertThat(weatherSummaries.getLast().get("stateId")).isEqualTo("GE");
        assertThat(weatherSummaries.getLast().get("cityName")).isEqualTo("Batumi");
        assertThat(weatherSummaries.getLast().get("weatherCount")).isEqualTo(1);
    }

    @Test
    public void testDelete() {
        stateService.create(state1);
        cityService.create(city11);
        weatherService.createBulk(new ArrayList<>(Arrays.asList(weather111, weather112)));

        client.delete().uri("/weathers/" + state1.getStateId() + "/" + city11.getName() + "/" + weather111.getTimestamp())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var retrieved = weatherService.get(weather112.getWeatherKey());
        assertTrue("Inserted weather retrieval should not return null.", retrieved.isPresent());
        retrieved = weatherService.get(weather111.getWeatherKey());
        assertFalse("Deleted weather retrieval should return null.", retrieved.isPresent());
    }

    @Test
    public void testDeleteSummary() {
        stateService.create(state1);
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12)));
        weatherService.createBulk(new ArrayList<>(Arrays.asList(weather111, weather112, weather121)));

        client.delete().uri("/weathers/" + state1.getStateId() + "/" + city11.getName())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var weathers = weatherService.getAll();
        assertEquals("There should be only one weather remaining.", 1, weathers.size());
        assertEquals("Weather 121 should be the remaining one.", weather121, weathers.getFirst());
    }

    @Test
    public void testDeleteAll() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12, city21, city22)));
        weatherService.createBulk(new ArrayList<>(Arrays.asList(weather111, weather112, weather121, weather122, weather211, weather212, weather221, weather222)));

        client.delete().uri("/weathers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var weathers = weatherService.getAll();
        assertTrue("All weathers should be deleted.", weathers.isEmpty());
    }

    @Test
    public void testCreateBadRequest() {
        var dto = new WeatherFetchDTO();
        client.put().uri("/weathers").contentType(MediaType.APPLICATION_JSON).bodyValue(dto).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
        dto.setCitySelect("ACBDEF");
        dto.setDatestamp("ABCDEF");
        dto.setnDays(20);
        client.put().uri("/weathers").contentType(MediaType.APPLICATION_JSON).bodyValue(dto).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
        dto.setnDays(7);
        client.put().uri("/weathers").contentType(MediaType.APPLICATION_JSON).bodyValue(dto).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
        dto.setDatestamp("2025-01-01");
        client.put().uri("/weathers").contentType(MediaType.APPLICATION_JSON).bodyValue(dto).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
        dto.setCitySelect("Liberec,CZ");
        client.put().uri("/weathers").contentType(MediaType.APPLICATION_JSON).bodyValue(dto).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
        stateService.create(state1);
        client.put().uri("/weathers").contentType(MediaType.APPLICATION_JSON).bodyValue(dto).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
    }

    @Test
    public void testGetAverages() {
        var weathers = new ArrayList<>(Arrays.asList(
                new Weather(1743508800, city12, 1.1f, 0.4f, 0, 280, ""),
                new Weather(1743595200, city12, 2.2f, 1.5f, 10, 270, ""),
                new Weather(1743681600, city12, 3.3f, 2.6f, 20, 260, ""),
                new Weather(1743768000, city12, 4.4f, 3.7f, 30, 250, ""),
                new Weather(1743854400, city12, 5.5f, 4.8f, 40, 240, ""),
                new Weather(1743940800, city12, 6.7f, 5.9f, 50, 230, ""),
                new Weather(1744027200, city12, 7.8f, 6.0f, 60, 220, ""),
                new Weather(1744113600, city12, 8.9f, 7.1f, 70, 210, ""),
                new Weather(1744200000, city12, 9.0f, 8.2f, 80, 200, ""),
                new Weather(1744286400, city12, 1.1f, 9.3f, 100, 190, ""),
                new Weather(1744372800, city12, 2.2f, 0.4f, 110, 180, ""),
                new Weather(1744459200, city12, 3.3f, 1.5f, 120, 170, ""),
                new Weather(1744545600, city12, 4.4f, 2.6f, 130, 160, ""),
                new Weather(1744632000, city12, 5.5f, 3.7f, 140, 150, "")
        ));
        stateService.create(state1);
        cityService.create(city12);
        client.get().uri("/weathers/averages?citySelect=" + city12.getName() + "," + city12.getStateId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WeatherAverageDTO.class).value(result -> assertThat(result).isEqualTo(null));
        weatherService.createBulk(weathers);
        weathers.sort((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));
        var weatherAverage = new WeatherAverageDTO(
                city12.getStateId(), city12.getName(), 14, 1744632000, 1744113600, 1743508800,
                5.5f, 3.7f, 140, 150,
                weathers.stream().limit(7).mapToDouble(Weather::getTemperature).average().orElse(-1),
                weathers.stream().limit(7).mapToDouble(Weather::getFeelsLike).average().orElse(-1),
                weathers.stream().limit(7).mapToInt(Weather::getPressure).average().orElse(-1),
                weathers.stream().limit(7).mapToInt(Weather::getHumidity).average().orElse(-1),
                weathers.stream().mapToDouble(Weather::getTemperature).average().orElse(-1),
                weathers.stream().mapToDouble(Weather::getFeelsLike).average().orElse(-1),
                weathers.stream().mapToInt(Weather::getPressure).average().orElse(-1),
                weathers.stream().mapToInt(Weather::getHumidity).average().orElse(-1)
        );
        client.get().uri("/weathers/averages?citySelect=" + city12.getName() + "," + city12.getStateId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WeatherAverageDTO.class).value(result -> assertThat(result).isEqualTo(weatherAverage));
    }
}
