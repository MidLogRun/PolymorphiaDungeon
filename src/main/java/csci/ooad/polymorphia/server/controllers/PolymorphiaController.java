package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Polymorphia;
import csci.ooad.polymorphia.characters.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
public class PolymorphiaController {
    private static final Logger logger = LoggerFactory.getLogger(PolymorphiaController.class);
    static String DEFAULT_GAME_ID = "MyGame";

    HashMap<String, Polymorphia> games = new HashMap<>();
    public int numGames = 0;


    private boolean checkParams(PolymorphiaParameters params) {
        if (params.numRooms() == 0)
            return false;
        if (params.numAdventurers() == 0 && params.numKnights() == 0 && params.numCowards() == 0 && params.numGluttons() == 0)
            return false;
        if (params.numDemons() == 0 && params.numCreatures() == 0)
            return false;
        return true;
    }


    public PolymorphiaController() {
        final int defaultNumItems = 1;
        final int defaultNumCreatures = 4;
        final int defaultNumAdventurers = 2;
        final int defaultNumDemons = 1;

        Maze defaultMaze = Maze.getNewBuilder()
                .create3x3Grid()
                .createAndAddFoodItems(defaultNumItems)
                .createAndAddArmor(defaultNumItems)
                .createAndAddCreatures(defaultNumCreatures)
                .createAndAddAdventurers(defaultNumAdventurers)
                .createAndAddDemons(defaultNumDemons)
                .build();

        Polymorphia defaultGame = new Polymorphia(DEFAULT_GAME_ID, defaultMaze);
        games.put(DEFAULT_GAME_ID, defaultGame);
    }

    public PolymorphiaController(@RequestBody PolymorphiaParameters params) {
        createGame(params);
    }

    @GetMapping("/api/games")
    public ResponseEntity<?> getGames() {
        List<String> gameNames = games.keySet().stream().toList();
        return new ResponseEntity<>(gameNames, HttpStatus.OK);
    }

    @GetMapping("/api/game/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable(name = "gameId", required = false) String gameId) {

        Polymorphia game = games.get(gameId);
        if (game != null){
            PolymorphiaJsonAdaptor jsonBody = new PolymorphiaJsonAdaptor(gameId, game);
            return new ResponseEntity<>(jsonBody, HttpStatus.OK);
        }
        else if (game.isOver()){
            PolymorphiaJsonAdaptor jsonBody = new PolymorphiaJsonAdaptor(gameId, game);
            return new ResponseEntity<>(jsonBody, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
    }


    @PostMapping("/api/game/create")
    public ResponseEntity<?> createGame(@Validated @RequestBody PolymorphiaParameters params) {
        if (!checkParams(params)) {
            return new ResponseEntity<>("Invalid parameters!", HttpStatus.BAD_REQUEST);
        }

        Maze gameMaze;
        if (params.playerName() == "NULL"){
            gameMaze = Maze.getNewBuilder()
                    .createFullyConnectedRooms(params.numRooms())
                    .createAndAddAdventurers(params.numAdventurers())
                    .createAndAddCowards(params.numCowards())
                    .createAndAddKnights(params.numKnights())
                    .createAndAddGluttons(params.numGluttons())
                    .createAndAddDemons(params.numDemons())
                    .createAndAddCreatures(params.numCreatures())
                    .createAndAddArmor(params.numArmor())
                    .createAndAddFoodItems(params.numFood())
                    .build();
        } else {
            gameMaze = Maze.getNewBuilder()
                    .createFullyConnectedRooms(params.numRooms())
                    .createAndAddAdventurers(params.numAdventurers())
                    .createAndAddAPIPlayer(params.playerName())
                    .createAndAddCowards(params.numCowards())
                    .createAndAddKnights(params.numKnights())
                    .createAndAddGluttons(params.numGluttons())
                    .createAndAddDemons(params.numDemons())
                    .createAndAddCreatures(params.numCreatures())
                    .createAndAddArmor(params.numArmor())
                    .createAndAddFoodItems(params.numFood())
                    .build();
        }


        String gameName = params.name();
        Polymorphia game = new Polymorphia(gameName, gameMaze);
        games.put(gameName, game);
        PolymorphiaJsonAdaptor jsonBody = new PolymorphiaJsonAdaptor(gameName, game);
        numGames++;
        return new ResponseEntity<>(jsonBody, HttpStatus.CREATED);
    }


    @PutMapping("/api/game/{gameId}/playTurn/{command}")
    public ResponseEntity<?> playTurn(@PathVariable(name = "gameId") String gameId, @PathVariable(name = "command") String command) {
        Polymorphia game = games.get(gameId);
        PolymorphiaJsonAdaptor jsonBody = new PolymorphiaJsonAdaptor(gameId, game);
        if (game.isOver()){
            return new ResponseEntity<>(jsonBody, HttpStatus.ACCEPTED);
        }

        game.playTurn(command);

        if (game.nowApiPlayerTurn()) {
            List<Strategy.CommandOption> availableCommands = game.getApiPlayerOptions();
            jsonBody.setAvailableCommandOptions(availableCommands);
            return new ResponseEntity<>(jsonBody, HttpStatus.OK);
        }


        return new ResponseEntity<>(jsonBody, HttpStatus.OK);

    }

}
