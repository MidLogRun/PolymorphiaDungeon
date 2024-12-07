package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.server.PolymorphiaServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {PolymorphiaServerApplication.class, PolymorphiaController.class})
class PolymorphiaControllerTest {
    static String DEFAULT_GAME_ID = "MyGame";

    PolymorphiaController polymorphiaController;

    @Autowired
    public PolymorphiaControllerTest(PolymorphiaController polymorphiaController) {
        this.polymorphiaController = polymorphiaController;
    }

    @Test
    public void contextLoads() {
    }

    @Test
    void getGames() {
        ResponseEntity<?> response = polymorphiaController.getGames();
        assertTrue(response.getStatusCode().is2xxSuccessful());
        // TODO: Add more assertions
        assertTrue(response.getBody() instanceof List);
        assertTrue(response.hasBody());
    }

    @Test
    void createGame() {
        String playerName = "Professor";
        PolymorphiaParameters polymorphiaParameters = new PolymorphiaParameters(DEFAULT_GAME_ID, playerName,
                2, 2, 7, 1,
                2, 2, 2, 10, 2);
        ResponseEntity<?> response = polymorphiaController.createGame(polymorphiaParameters);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

        //What is the body of the response supposed to be??
        PolymorphiaJsonAdaptor jsonAdaptor = (PolymorphiaJsonAdaptor) response.getBody();
        assert jsonAdaptor != null;
        assertEquals(jsonAdaptor.getTurn(), 0);
        assertEquals(jsonAdaptor.getName(), DEFAULT_GAME_ID);
        System.out.println(jsonAdaptor);

    }

    @Test
    void getGame() {
        createGame();

        ResponseEntity<?> response = polymorphiaController.getGame(DEFAULT_GAME_ID);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        PolymorphiaJsonAdaptor jsonAdaptor = (PolymorphiaJsonAdaptor) response.getBody();
        assert jsonAdaptor != null;

        assertEquals(jsonAdaptor.getTurn(), 0);
        assertEquals(jsonAdaptor.getName(), DEFAULT_GAME_ID);
    }

    @Test
    void playTurnWithNoHumanPlayer() {
        PolymorphiaController controller = new PolymorphiaController();
        ResponseEntity<?> response = controller.playTurn(PolymorphiaController.DEFAULT_GAME_ID, "NULL");
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void playTurnWithHumanPlayer() {
        String gameId = "playerGame";
        PolymorphiaParameters polymorphiaParameters = new PolymorphiaParameters(gameId, "gamer",
                2, 2, 7, 1,
                2, 2, 2, 10, 2);

        polymorphiaController.createGame(polymorphiaParameters);
        ResponseEntity<?> response = polymorphiaController.playTurn(gameId, "NULL");
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        response = polymorphiaController.playTurn(gameId, "DO_NOTHING");
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());


    }

}