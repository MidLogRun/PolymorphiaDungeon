package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolymorphiaFactoryTest {
    @Test
    public void testCreateRegularPolymorphiaGame(){
        PolymorphiaFactory gameFactory = new PolymorphiaFactory();
        String name = "one maze game";
        Maze maze = Maze.getNewBuilder().build();
        Polymorphia game = gameFactory.createOneMazeGame(name, maze);

        assertTrue(game.isOver());
        assertEquals(game.getNumberOfMazes(), 1);
    }
}
