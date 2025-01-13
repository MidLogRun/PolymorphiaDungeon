package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolymorphiaFactoryTest {
    @Test
    public void testCreateRegularPolymorphiaGame() {
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        String name = "one maze game";
        Maze maze = Maze.getNewBuilder().build();
        Polymorphia game = gameFactory.createOneMazeGame(name, maze);

        assertEquals(game.getNumberOfMazes(), 1);
        assertTrue(game.isOver()); //game over logic is when no players or creatures
    }

    @Test
    public void testCreateTwoMazePolymorphiaGame() {
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        String name = "two maze game";
        Maze maze1 = Maze.getNewBuilder().build();
        Maze maze2 = Maze.getNewBuilder().build();

        List<Maze> mazes = List.of(maze1, maze2);
        Polymorphia game = gameFactory.createMultiMazeGame(name, mazes);

        assertEquals(game.getNumberOfMazes(), 2);
        assertTrue(game.isOver()); //game over logic is when no players or creatures
    }

    @Test
    public void testCreateOneMazeGameAndPlay() {
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        String name = "one maze game";
        Maze oneRoomMaze = Maze.getNewBuilder()
                .createRoom("starting room")
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();

        Polymorphia game = gameFactory.createOneMazeGame(name, oneRoomMaze);
        game.play();

        assertTrue(game.isOver());
    }

    @Test
    public void testCreateTwoMazeGameAndPlay() throws NoSuchRoomException {
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        MazeFactory mazeFactory = new MazeFactory();

        int numberOfMazes = 2;
        int numberOfRoomsPerMaze = 1;

        Maze mazeOne = mazeFactory.createFullyConnectedMazeWithGateRoom(Collections.singletonList("Tim"), Collections.singletonList("Dragon"), 1);
        Maze mazeTwo = mazeFactory.createFullyConnectedMazeWithGateRoom(Collections.singletonList("Ogre"), 1);
        mazeOne.connectMaze(mazeTwo);

        List<Maze> mazes = List.of(mazeOne, mazeTwo);

        Polymorphia game = gameFactory.createMultiMazeGame("two maze game", mazes);

        //Act
        game.play();

        //Assert
        assertTrue(game.isOver());
    }

    @Test
    public void testCreateFiveMazeGameAndPlay() throws NoSuchRoomException {
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        MazeFactory mazeFactory = new MazeFactory();

        int numberOfMazes = 5;
        int numberOfRoomsPerMaze = 1;

        List<String> adventurerNames = List.of("Tim", "Barnson", "Dupri", "Caladan");
        List<String> creatureNames = List.of("Evil Doer", "Tree goblin", "Dragon", "Mouse of Madness", "Meal worm");

        List<Maze> mazes = mazeFactory.createConnectedMazes(adventurerNames, creatureNames, numberOfMazes, numberOfRoomsPerMaze);

        Polymorphia game = gameFactory.createMultiMazeGame("five maze game", mazes);
        game.play();
        assertTrue(game.isOver());
    }


}
