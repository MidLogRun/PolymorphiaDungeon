package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolymorphiaFactoryTest {
    @Test
    public void testCreateRegularPolymorphiaGame(){
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        String name = "one maze game";
        Maze maze = Maze.getNewBuilder().build();
        Polymorphia game = gameFactory.createOneMazeGame(name, maze);

        assertEquals(game.getNumberOfMazes(), 1);
        assertTrue(game.isOver()); //game over logic is when no players or creatures
    }

    @Test
    public void testCreateTwoMazePolymorphiaGame(){
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        String name = "two maze game";
        Maze maze1 = Maze.getNewBuilder().build();
        Maze maze2 = Maze.getNewBuilder().build();
        List<Maze> mazes = List.of(maze1, maze2);
        Polymorphia game = gameFactory.createMultiMazeGame(name, mazes);

        assertEquals(game.getNumberOfMazes(), 2);
        assertTrue(game.isOver()); //game over logic is when no players or creatures
    }


}
