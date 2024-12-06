package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.EventBus;
import csci.ooad.polymorphia.EventType;
import csci.ooad.polymorphia.Maze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;

import static csci.ooad.polymorphia.EventBus.post;


public abstract class Character implements Comparable<Character> {
    private static final Logger logger = LoggerFactory.getLogger(Character.class);
    private static final DecimalFormat formatter = new DecimalFormat("0.00");

    static final Double DEFAULT_INITIAL_HEALTH = 5.0;
    static final Double HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME = 0.5;
    static final Double HEALTH_LOST_IN_MOVING_ROOMS = 0.25;
    static final Double EXTRA_HEALTH_LOST_IN_FLEEING_ROOM_WITH_CREATURE = 0.25;

    protected final String name;
    private Double health;

    private Maze.Room currentLocation;
    private final Strategy strategy;

    public Maze.Room getCurrentLocation() {
        return currentLocation;
    }

    public Character(String name) {
        this(name, DEFAULT_INITIAL_HEALTH);
    }

    public Character(String name, Double initialHealth) {
        this(name, initialHealth, new RandomStrategy());
    }

    public Character(String name, Double initialHealth, Strategy strategy) {
        this.name = name;
        this.health = initialHealth;
        this.strategy = strategy;
    }

    Strategy getStrategy() {
        return strategy;
    }

    @Override
    public int compareTo(Character otherCharacter) {
        return getHealth().compareTo(otherCharacter.getHealth());
    }

    public void enterRoom(Maze.Room room) {
        if (getCurrentLocation() != null) {
            if (getCurrentLocation().equals(room)) {
                return;
            }
        }
        this.currentLocation = room;
    }

    @Override
    public String toString() {
        return getName() + "(health: " + formatter.format(getHealth()) + ")";
    }

    public void loseFightDamage(Number healthPoints) {
        loseHealth(healthPoints);
    }

    public void loseMovingHealth(Number healthPoints) {
        loseHealth(healthPoints);
    }

    private void loseHealth(Number healthPoints) {
        if (health <= 0) {
            return;     // already dead, probably called for mandatory health loss for having a fight
        }

        health -= healthPoints.doubleValue();

        if (health <= 0) {
            String eventDescription = this.getName() + " just died!";
            logger.info(eventDescription);
            EventBus.getInstance().postMessage(EventType.Death, eventDescription);
        }
    }

    public void loseHealthForFighting() {
        loseHealth(HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME);
    }

    public Double getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public Boolean isAlive() {
        return getHealth() > 0;
    }

    protected boolean cannotMove() {
        return getCurrentLocation().getNeighbors().isEmpty();
    }

    Boolean creatureInRoomWithMe() {
        return getCurrentLocation().hasLivingCreatures();
    }

    public Boolean isAdventurer() {
        return false;
    }
    public Boolean isCreature() {
        return false;
    }
    public Boolean isApiPlayer() {return false;}

    public Command getAction() {
        return this.strategy.generateCommand(this);
    }

    public void move(Maze.Room nextLocation) {
        loseMovingHealth(HEALTH_LOST_IN_MOVING_ROOMS);
        if (creatureInRoomWithMe()) {
            // Loses a bit more health if there is a creature in the room to run faster!
            loseHealth(EXTRA_HEALTH_LOST_IN_FLEEING_ROOM_WITH_CREATURE);
        }
        String message = getName() + " moved from " + getCurrentLocation().getName() + " to " + nextLocation.getName();
        nextLocation.enter(this);
        logger.info(message);
        post(EventType.Moved, message);

    }
    public void gainHealth(double healthValue) {
        this.health += healthValue;
        logger.info("{} gained health: {}", getName(), formatter.format(getHealth()));
    }

    protected boolean iAmHealthiestInRoom() {
        return currentLocation.isHealthiestAdventurer(this);
    }

    public List<HumanStrategy.CommandOption> getOptions() {
        return strategy.getOptions(this);
    }
}
