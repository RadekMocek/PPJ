package cz.tul.ppj;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.model.dto.CityDTO;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {PpjApplication.class})
@ActiveProfiles({"test"})
public class CityRestTest {

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

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

    @Test
    public void testCreate() {
        stateService.create(state1);

        var cityDTO11 = new CityDTO();
        cityDTO11.setStateId(city11.getStateId());
        cityDTO11.setCityName(city11.getName());

        client.put().uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cityDTO11)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(City.class).value(result -> assertThat(result).isEqualTo(city11));

        assertTrue("Inserted city should exist.", cityService.exists(city11.getCityKey()));

        client.put().uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cityDTO11)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testReferentialIntegrity() {
        // Not creating state1 here
        var cityDTO11 = new CityDTO();
        cityDTO11.setStateId(city11.getStateId());
        cityDTO11.setCityName(city11.getName());

        client.put().uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cityDTO11)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testCreateBlank() {
        stateService.create(state1);
        var cityDTO = new CityDTO();
        cityDTO.setStateId(city11.getStateId());
        cityDTO.setCityName(" ");
        client.put().uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cityDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testStateIdAutoUppercase() {
        stateService.create(state1);
        var cityDTO11 = new CityDTO();
        cityDTO11.setStateId(city11.getStateId().toLowerCase());
        cityDTO11.setCityName(city11.getName());
        client.put().uri("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cityDTO11)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(City.class).value(result -> assertThat(result).isEqualTo(city11));
    }

    @Test
    public void testGetEmpty() {
        client.get().uri("/cities")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[]");
    }

    @Test
    public void testGet() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12, city21, city22)));

        List<City> cities = client.get().uri("/cities")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(City.class).returnResult().getResponseBody();

        assertThat(cities)
                .hasSize(4)
                .containsExactlyInAnyOrder(city11, city12, city21, city22);
    }

    @Test
    public void testDelete() {
        stateService.create(state1);
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12)));

        client.delete().uri("/cities/" + state1.getStateId() + "/" + city11.getName())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var retrieved = cityService.get(city12.getCityKey());
        assertTrue("Inserted state retrieval should not return null.", retrieved.isPresent());
        retrieved = cityService.get(city11.getCityKey());
        assertFalse("Deleted state retrieval should return null.", retrieved.isPresent());

        client.delete().uri("/cities/" + state1.getStateId() + "/" + city11.getName())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testDeleteAll() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));
        cityService.createBulk(new ArrayList<>(Arrays.asList(city11, city12, city21, city22)));

        client.delete().uri("/cities")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var cities = cityService.getAll();
        assertTrue("All cities should be deleted.", cities.isEmpty());
    }

}
