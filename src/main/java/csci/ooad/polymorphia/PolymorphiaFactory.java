package csci.ooad.polymorphia;

import java.util.List;

public class PolymorphiaFactory {

    public Polymorphia createOneMazeGame(String name, Maze maze) {
        Polymorphia game = Polymorphia.getNewBuilder()
                .withName(name)
                .addMaze(maze)
                .build();
        return game;
    }

    public Polymorphia createMultiMazeGame(String name, List<Maze> mazes) {
        Polymorphia game = Polymorphia.getNewBuilder()
                .withName(name)
                .addMazes(mazes)
                .build();

        return game;
    }
}
