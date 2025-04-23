package cz.tul.ppj;

import cz.tul.ppj.model.State;
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
public class StateRestTest {

    @Autowired
    private StateService stateService;

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

    @Test
    public void testCreate() {
        client.put().uri("/states")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(state1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(State.class).value(result -> assertThat(result).isEqualTo(state1));

        assertTrue("Inserted state should exist.", stateService.exists(state1.getStateId()));

        client.put().uri("/states")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(state1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testGetEmpty() {
        client.get().uri("/states")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[]");
    }

    @Test
    public void testGet() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));

        List<State> states = client.get().uri("/states")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(State.class).returnResult().getResponseBody();

        assertThat(states)
                .hasSize(2)
                .containsExactly(state1, state2);
    }

    @Test
    public void testUpdate() {
        client.patch().uri("/states")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(state1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        stateService.create(state1);
        state1.setName("Bruhia");
        client.patch().uri("/states")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(state1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(State.class).value(result -> assertThat(result).isEqualTo(state1));
    }

    @Test
    public void testDelete() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));

        client.delete().uri("/states/" + state1.getStateId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var retrieved = stateService.get(state2.getStateId());
        assertTrue("Inserted state retrieval should not return null.", retrieved.isPresent());
        retrieved = stateService.get(state1.getStateId());
        assertFalse("Deleted state retrieval should return null.", retrieved.isPresent());

        client.delete().uri("/states/" + state1.getStateId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testDeleteAll() {
        stateService.createBulk(new ArrayList<>(Arrays.asList(state1, state2)));

        client.delete().uri("/states")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var states = stateService.getAll();
        assertTrue("Everything should be deleted.", states.isEmpty());
    }

    @Test
    public void testCreateBlank() {
        client.put().uri("/states")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new State(" ", " "))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    //

    @Test
    public void testStateIdAutoUppercase() {
        client.put().uri("/states")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new State("cz", "Czechia"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(State.class).value(result -> assertThat(result).isEqualTo(new State("CZ", "Czechia")));
    }
}
