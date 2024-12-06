package csci.ooad.polymorphia.characters;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static csci.ooad.polymorphia.PolymorphiaNames.*;

public class CharacterFactory {
    static final Double ADVENTURER_INITIAL_HEALTH = 5.0;
    static final Double KNIGHT_INITIAL_HEALTH = 8.0;
    static final Double DEMON_INITIAL_HEALTH = 15.0;

    private final Random random = new Random();

    public List<Adventurer> createNumberOfAdventurers(Integer numAdventurers) {
        return IntStream.range(0, numAdventurers)
                .mapToObj(i -> createAdventurer(ADVENTURER_NAMES[i % ADVENTURER_NAMES.length]))
                .map(Adventurer.class::cast)
                .toList();
    }

    public List<Creature> createNumberOfCreatures(Integer numCreatures) {
        return IntStream.range(0, numCreatures)
                .mapToObj(i -> createCreature(CREATURE_NAMES[i % CREATURE_NAMES.length]))
                .map(Creature.class::cast)
                .toList();
    }

    public List<Creature> createNumberOfDemons(Integer numDemons) {
        return IntStream.range(0, numDemons)
                .mapToObj(i -> createDemon(DEMON_NAMES[i % DEMON_NAMES.length]))
                .map(Creature.class::cast)
                .toList();
    }

    public List<Adventurer> createNumberOfKnights(Integer numAdventurers) {
        return IntStream.range(0, numAdventurers)
                .mapToObj(i -> createKnight(KNIGHT_NAMES[i % KNIGHT_NAMES.length]))
                .map(Adventurer.class::cast)
                .toList();
    }

    public List<Adventurer> createNumberOfCowards(Integer numAdventurers) {
        return IntStream.range(0, numAdventurers)
                .mapToObj(i -> createCoward(COWARD_NAMES[i % COWARD_NAMES.length]))
                .map(Adventurer.class::cast)
                .toList();
    }

    public List<Adventurer> createNumberOfGluttons(Integer numAdventurers) {
        return IntStream.range(0, numAdventurers)
                .mapToObj(i -> createGlutton(GLUTTON_NAMES[i % GLUTTON_NAMES.length]))
                .map(Adventurer.class::cast)
                .toList();
    }

    public Adventurer createAdventurer(String name) {
        return new Adventurer(name);
    }

    public Adventurer createHuman(String name) {
        return new Adventurer(name, KNIGHT_INITIAL_HEALTH, new HumanStrategy());
    }

    public Adventurer createApiPlayer(String name){
        return new APIPlayer(name, KNIGHT_INITIAL_HEALTH);
    }

    public Adventurer createKnight(String name) {
        return new Adventurer(name, KNIGHT_INITIAL_HEALTH, new FighterStrategy());
    }

    public Adventurer createCoward(String name) {
        return new Adventurer(name, ADVENTURER_INITIAL_HEALTH, new CowardlyStrategy());
    }

    public Adventurer createGlutton(String name) {
        return new Adventurer(name, ADVENTURER_INITIAL_HEALTH, new HungryStrategy());
    }

    public Creature createCreature(String name) {
        return new Creature(name);
    }

    public Creature createDemon(String name) {
        return new Creature(name, DEMON_INITIAL_HEALTH, new DemonStrategy());
    }

}
