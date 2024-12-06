package csci.ooad.polymorphia;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IMazeObserver;
import csci.ooad.layout.intf.IMazeSubject;
import csci.ooad.polymorphia.characters.*;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.observer.MazeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Polymorphia implements IMazeSubject, IObservable {
    private static final Logger logger = LoggerFactory.getLogger(Polymorphia.class);
    private static int gameNumber = 1;

    List<IMazeObserver> observers = new ArrayList<>();

    public void attach(IMazeObserver observer) {
        observers.add(observer);
    }

    @Override
    public List<IMazeObserver> getObservers() {
        return observers;
    }

    private final String name;
    private final Maze maze;
    private Integer turnCount = 0;
    private final Random rand = new Random();
    public List<Character> pendingCharacters = new ArrayList<>();
    private boolean turnPending = false;

    public Polymorphia(Maze maze) {
        this("Polymorphia Game " + gameNumber, maze);
    }

    public Polymorphia(String name, Maze maze) {
        this.name = name;
        this.maze = maze;
        gameNumber++;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.join("\n", status()) + "\n" + maze.toString();
    }

    private List<String> status() {
        return List.of(
                "Polymorphia " + getName() + " after turn " + turnCount,
                "Live adventurers(" + numberOfLivingAdventurers() + "): " +
                        getLivingAdventurers().stream().map(Character::getName).collect(Collectors.joining(",")),
                "Live creatures(" + numberOfLivingCreatures() + "): " +
                        getLivingCreatures().stream().map(Character::getName).collect(Collectors.joining(","))
        );
    }

    public int numberOfLivingAdventurers() {
        return getLivingAdventurers().size();
    }

    public List<Adventurer> getLivingAdventurers() {
        return maze.getLivingAdventurers();
    }

    public int numberOfLivingCreatures() {
        return getLivingCreatures().size();
    }

    // Game is over when all creatures are killed or all adventurers are killed
    public Boolean isOver() {
        return !hasLivingCreatures() || !hasLivingAdventurers();
    }

    public Boolean hasLivingCreatures() {
        return maze.hasLivingCreatures();
    }

    public Boolean hasLivingAdventurers() {
        return maze.hasLivingAdventurers();
    }

    public APIPlayer getApiCharacter(){
        APIPlayer apiPlayer = (APIPlayer) getLivingAdventurers().stream().filter(Adventurer::isApiPlayer).findFirst().orElse(null);
        return apiPlayer;
    }

    public List<HumanStrategy.CommandOption> getApiPlayerOptions() {
        Character apiPlayer = getApiCharacter();

        if (apiPlayer.isAlive() && apiPlayer != null) {
            logger.info("returning available commands for API player {}", apiPlayer.getName());

            return apiPlayer.getOptions();
        }
        return List.of();
    }

    public void executeApiPlayerCommand(String command) {
        APIPlayer apiPlayer = getApiCharacter();
        if (apiPlayer.isAlive()) {
            apiPlayer.setLastCommand(command);
            apiPlayer.getAction().execute();
        }
    }

    public Boolean inMiddleOfTurn(){
        return turnPending;
    }

    private List<Character> getPendingCharacters(){
        if (!inMiddleOfTurn()){
            pendingCharacters = getLivingCharacters();
        }
        return pendingCharacters;
    }

    public void playTurn(String commandString) {
        if (isOver()) {
            return;
        }

        if (turnCount == 0) {
            logger.info("Starting play...");
        }
        turnCount += 1;

        if (inMiddleOfTurn()) {
            logger.info("Middle of turn");
            executeApiPlayerCommand(commandString);
        }

        logger.info("Starting turn " + turnCount + "...");
        pendingCharacters = getPendingCharacters();

        while (!pendingCharacters.isEmpty()) {
            int index = rand.nextInt(pendingCharacters.size());
            Character currentPlayer = pendingCharacters.get(index);

            // Make sure currentPlayer is still alive. It might have fought a Demon
            if (currentPlayer.isAlive()) {
                if (currentPlayer.isApiPlayer()) {
                    System.out.println("True!");
                    System.out.println(commandString);
                    if (commandString.equals("NULL")){
                        System.out.println("Yep I am null!");
                        turnPending = true;
                        pendingCharacters.remove(currentPlayer);
                    }
                    return;
                }

                Command command = currentPlayer.getAction();
                command.execute();
                logger.info("Turn " + turnCount + ": " + currentPlayer.getName() + " executed " + command.getName());

            }
            pendingCharacters.remove(currentPlayer);
            notifyObservers(status());
        }
    }

    public List<Character> getLivingCharacters() {
        return maze.getLivingCharacters();
    }

    public void postMessage(EventType eventType, String eventDescription) {
        logger.info(eventDescription);
        EventBus.getInstance().postMessage(eventType, eventDescription);
    }

    public void play() {
        while (!isOver()) {
            logger.info(this.toString());
            playTurn(null); //in this play, do not inject a command
        }
        postMessage(EventType.GameStart, getEndOfGameStatus());
    }

    public String getEndOfGameStatus() {
        String eventDescription = "The game ended after " + turnCount + " turns.\n";
        if (hasLivingAdventurers()) {
            eventDescription += "The adventurers won! Left standing are:\n" + getAdventurerNames() + "\n";
        } else if (hasLivingCreatures()) {
            eventDescription += "The creatures won! Left standing are:\n" + getCreatureNames() + "\n";
        } else {
            eventDescription += "No team won! Everyone died!\n";
        }
        return eventDescription;
    }

    String getAdventurerNames() {
        return String.join("\n ", getLivingCharacters().stream().map(Object::toString).toList());
    }

    String getCreatureNames() {
        return String.join("\n ", getLivingCreatures().stream().map(Object::toString).toList());
    }

    public List<Creature> getLivingCreatures() {
        return maze.getLivingCreatures();
    }

    public Character getWinner() {
        if (!isOver() || !hasLivingCharacters()) {
            // No one has won yet or no one won -- all died
            return null;
        }
        return getLivingCharacters().getFirst();
    }

    private boolean hasLivingCharacters() {
        return !getLivingCharacters().isEmpty();
    }

    @Override
    public IMaze getMaze() {
        return new MazeAdapter(maze);
    }

    public int getTurnNumber() {
        return turnCount;
    }

}