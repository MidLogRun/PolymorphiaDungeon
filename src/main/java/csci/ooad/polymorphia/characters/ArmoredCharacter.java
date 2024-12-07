package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Armor;
import csci.ooad.polymorphia.Maze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ArmoredCharacter extends Adventurer {
    private static final Logger logger = LoggerFactory.getLogger(ArmoredCharacter.class);

    private final Character character;
    private final Armor armor;

    public ArmoredCharacter(Character character, Armor armor) {
        super(character.name);
        this.character = character;
        this.armor = armor;
    }

    @Override
    public String getName() {
        return armor.toString() + "ed " + character.getName();
    }

    @Override
    public Double getHealth() {
        return character.getHealth();
    }

    Strategy getStrategy() {
        return character.getStrategy();
    }

    @Override
    public Boolean isAlive() {
        return character.isAlive();
    }

    @Override
    protected boolean iAmHealthiestInRoom() {
        return character.iAmHealthiestInRoom();
    }

    @Override
    public void enterRoom(Maze.Room room) {
        character.enterRoom(room);
    }

    @Override
    public Maze.Room getCurrentLocation() {
        return character.getCurrentLocation();
    }

    @Override
    public int compareTo(Character otherCharacter) {
        return character.compareTo(otherCharacter);
    }

    @Override
    Boolean creatureInRoomWithMe() {
        return character.creatureInRoomWithMe();
    }

    @Override
    public Command getAction() {
        return character.getStrategy().generateCommand(this);
    }

    @Override
    public void gainHealth(double healthValue) {
        character.gainHealth(healthValue);
    }

    @Override
    public void loseFightDamage(Number fightDamage) {
        Double lostHealth = Math.max(0, fightDamage.doubleValue() - armor.strength());    // character cannot gain health here
        logger.info("{} absorbed {} damage", this.armor, armor.strength());
        character.loseFightDamage(lostHealth);
    }

    @Override
    protected Character getCharacter() {
        return character.getCharacter();
    }

    @Override
    public Boolean isAdventurer(){
        return getCharacter().isAdventurer();
    }

    @Override
    public Boolean isApiPlayer(){
        return getCharacter().isApiPlayer();
    }

    @Override
    public List<HumanStrategy.CommandOption> getOptions() {
        return getCharacter().strategy.getOptions(this);
    }

    @Override
    public void loseMovingHealth(Number healthPoints) {
        character.loseMovingHealth(healthPoints.doubleValue() + armor.movingCost());
        logger.info("{} caused {} to lose {} extra health in moving, due to the weight of the armor", this.armor, this.character, armor.movingCost());
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() == obj.getClass())
            return obj.equals(character);
        return character == obj;
    }

}
