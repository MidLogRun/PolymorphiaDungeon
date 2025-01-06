package csci.ooad.polymorphia;

public class PolymorphiaFactory {

    public Polymorphia createOneMazeGame(String name, Maze maze) {

        Polymorphia game = Polymorphia.getNewBuilder().withName(name).addMaze(maze).build();
        return game;


    }
}
