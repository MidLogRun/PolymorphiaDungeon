package csci.ooad.polymorphia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeFactory {
    public Maze createMaze() {
        Maze maze = Maze.getNewBuilder().build();
        return maze;
    }

    public Maze createFullyConnectedMaze(int numAdventurers, int numCreatures, int numRooms) {
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms(numRooms)
                .build();
        return maze;
    }


    public Maze createFullyConnectedMazeWithGateRoom(List<String> adventurerNames, List<String> creatureNames, int numRooms) {
        Maze maze = Maze.getNewBuilder()
                .createAndAddGateRoom()
                .createFullyConnectedRooms(numRooms)
                .createAndAddKeys()
                .createAndAddAdventurers(adventurerNames.toArray(new String[0]))
                .createAndAddCreatures(creatureNames.toArray(new String[0]))
                .build();
        return maze;
    }

    public Maze createFullyConnectedMazeWithGateRoom(List<String> creatureNames, int numRooms) {
        Maze maze = Maze.getNewBuilder()
                .createAndAddGateRoom("GateRoom")
                .createFullyConnectedRooms(numRooms)
                .createAndAddKeys()
                .createAndAddCreatures(creatureNames.toArray(new String[0]))
                .build();
        return maze;
    }


    public Maze createFullyConnectedMazeWithGateRoom(int numAdventurers, int numCreatures, int numRooms) {
        Maze maze = Maze.getNewBuilder()
                .createAndAddGateRoom("Threshold")
                .createFullyConnectedRooms(numRooms)
                .createAndAddAdventurers(numAdventurers)
                .createAndAddCreatures(numCreatures)
                .build();
        return maze;
    }

    public List<Maze> createConnectedMazes(List<String> adventurerNames, List<String> creatureNames, int numberToCreate, int numRooms) throws NoSuchRoomException {
        List<Maze> connectedMazes = new ArrayList<>();

        // Divide adventurers and creatures evenly across mazes

        int baseAdventurersPerMaze = adventurerNames.size() / numberToCreate;
        int extraAdventurers = adventurerNames.size() % numberToCreate;

        int baseCreaturesPerMaze =  creatureNames.size() / numberToCreate;
        int extraCreatures =  creatureNames.size()  % numberToCreate;

        int adventurerIndex = 0;
        int creatureIndex = 0;

        for (int i = 0; i < numberToCreate; i++) {
            // Determine the number of adventurers and creatures for this maze
            int adventurersForThisMaze = baseAdventurersPerMaze + (i < extraAdventurers ? 1 : 0);
            int creaturesForThisMaze = baseCreaturesPerMaze + (i < extraCreatures ? 1 : 0);

            List<String> mazeAdventurers = adventurerNames.subList(adventurerIndex, adventurerIndex + adventurersForThisMaze);
            List<String> mazeCreatures = creatureNames.subList(creatureIndex, creatureIndex + creaturesForThisMaze);

            Maze maze = createFullyConnectedMazeWithGateRoom(new ArrayList<>(mazeAdventurers), new ArrayList<>(mazeCreatures), numRooms);
            connectedMazes.add(maze);

            adventurerIndex += adventurersForThisMaze;
            creatureIndex += creaturesForThisMaze;
        }

        // Connect the mazes
        for (int i = 1; i < numberToCreate; i++) {
            Maze current = connectedMazes.get(i);
            Maze previous = connectedMazes.get(i - 1);
            current.connectMaze(previous);
        }

        return connectedMazes;
    }

    public List<Maze> createConnectedMazes(int numberToCreate, int numAdventurers, int numCreatures, int numRooms) throws NoSuchRoomException {
        List<Maze> connectedMazes = new ArrayList<>();
        for (int i = 0; i < numberToCreate; i++) {
            createFullyConnectedMazeWithGateRoom(numAdventurers, numCreatures, numRooms);
        }

        for (int i = 1; i < numberToCreate; i++) {
            Maze current = connectedMazes.get(i);
            Maze previous = connectedMazes.get(i - 1);
            current.connectMaze(previous);
        }

        return connectedMazes;
    }

}
