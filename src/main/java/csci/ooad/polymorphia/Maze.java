package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.CharacterFactory;
import csci.ooad.polymorphia.characters.Creature;

import java.util.*;

import static csci.ooad.polymorphia.PolymorphiaNames.ROOM_NAMES;


public class Maze {
    private final Random rand = new Random();

    private List<Room> rooms = new ArrayList<>();

    public Maze() {
    }

    public int size() {
        return rooms.size();
    }

    @Override
    public String toString() {
        return String.join("\n\n", rooms.stream().map(Object::toString).toList());
    }

    public Boolean hasLivingCreatures() {
        return rooms.stream().anyMatch(Room::hasLivingCreatures);
    }

    public Boolean hasLivingAdventurers() {
        return rooms.stream().anyMatch(Room::hasLivingAdventurers);
    }

    private Room getRandomRoom() {
        return rooms.get(rand.nextInt(rooms.size()));
    }

    public List<Adventurer> getLivingAdventurers() {
        List<Adventurer> adventurers = new ArrayList<>();
        for (Room room : rooms) {
            adventurers.addAll(room.getLivingAdventurers());
        }
        return adventurers;
    }

    public List<Creature> getLivingCreatures() {
        List<Creature> creatures = new ArrayList<>();
        for (Room room : rooms) {
            creatures.addAll(room.getLivingCreatures());
        }
        return creatures;
    }

    public List<Character> getLivingCharacters() {
        List<Character> characters = new ArrayList<>();
        for (Room room : rooms) {
            characters.addAll(room.getLivingCharacters());
        }
        return characters;
    }

    public void addToRandomRoom(Character character) {
        getRandomRoom().add(character);
    }

    public void connectMaze(Maze neighbor) throws NoSuchRoomException {
        getGateRoom().connectCorrespondingGateRoom(neighbor.getGateRoom());
    }

    private GateRoom getGateRoom() throws NoSuchRoomException {
        return rooms.stream()
                .filter(room -> room instanceof GateRoom)
                .map(room -> (GateRoom) room) // Cast Room to GateRoom
                .findFirst()
                .orElseThrow(() -> new  NoSuchRoomException("No GateRoom found in the maze."));
    }

    public static Builder getNewBuilder() {
        return new Builder();
    }

    public static Builder getNewBuilder(CharacterFactory characterFactory, ArtifactFactory artifactFactory) {
        return new Builder(characterFactory, artifactFactory);
    }

    public Room getRoom(String roomName) throws NoSuchRoomException {
        Optional<Room> namedRoom = rooms.stream().filter(r -> r.getName().equals(roomName)).findFirst();
        if (namedRoom.isEmpty()) {
            throw new NoSuchRoomException(roomName);
        }
        return namedRoom.get();
    }

    public static class Room {
        private final String name;
        private final List<Room> neighbors = new ArrayList<>();
        private final List<Character> characters = new ArrayList<>();
        private final List<Food> foodItems = new ArrayList<>();
        private final List<Armor> armors = new ArrayList<>();


        public Room(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<Adventurer> getLivingAdventurers() {
            return characters.stream()
                    .filter(Character::isAdventurer)
                    .filter(Character::isAlive)
                    .map(Adventurer.class::cast)
                    .sorted().toList();
        }

        public List<Creature> getLivingCreatures() {
            return characters.stream()
                    .filter(Character::isCreature)
                    .filter(Character::isAlive)
                    .map(Creature.class::cast)
                    .sorted()
                    .toList();
        }

        public List<String> getContents() {
            List<String> contents = new ArrayList<>(getLivingCharacters().stream()
                    .map(Object::toString)
                    .toList());
            contents.addAll(this.foodItems.stream()
                    .map(Object::toString)
                    .toList());
            contents.addAll(this.armors.stream()
                    .map(Object::toString)
                    .toList());
            return contents;
        }

        private void addNeighbor(Room neighbor) {
            // Make sure we are never a neighbor of ourselves
            assert this != neighbor;
            this.neighbors.add(neighbor);
        }

        Room connect(Room neighbor) {
            this.addNeighbor(neighbor);
            neighbor.addNeighbor(this);
            return this;
        }

        @Override
        public String toString() {
            String representation = "\t" + name + ":\n\t\t";
            representation += String.join("\n\t\t", getContents());
            return representation;
        }

        private void add(Character character) {
            characters.add(character);
            character.enterRoom(this);
        }

        public Boolean hasLivingCreatures() {
            return characters.stream()
                    .filter(Character::isCreature)
                    .filter(Character::isAlive)
                    .anyMatch(Character::isAlive);
        }

        public Boolean hasLivingAdventurers() {
            return characters.stream()
                    .filter(Character::isAdventurer)
                    .filter(Character::isAlive)
                    .anyMatch(Character::isAlive);
        }

        private void remove(Character character) {
            characters.remove(character);
        }

        public Room getRandomNeighbor() {
            if (neighbors.isEmpty()) {
                return null;
            }
            return neighbors.get(Die.randomLessThan(neighbors.size()));
        }

        public void replace(Character exisitingCharacter, Character wrappedCharacter) {
            // This method allows the room to replace an existing character with the same character that is decorated
            if (wrappedCharacter.equals(exisitingCharacter)) {
                remove(exisitingCharacter);
                add(wrappedCharacter);
            }
        }

        public void enter(Character character) {
            character.getCurrentLocation().remove(character);
            add(character);
        }

        public List<Character> getLivingCharacters() {
            return characters.stream()
                    .filter(Character::isAlive)
                    .toList();
        }

        public void add(Food foodItem) {
            foodItems.add(foodItem);
        }

        public void add(Armor armor) { armors.add(armor); }

        public boolean isHealthiestAdventurer(Character adventurer) {
            Optional<Adventurer> possibleAdventurer = getHealthiestAdventurer();
            return possibleAdventurer.isPresent() && possibleAdventurer.get().equals(adventurer);
        }

        public boolean isHealthiestCreature(Creature creature) {
            Optional<Creature> possibleCreature = getHealthiestCreature();
            return possibleCreature.isPresent() && possibleCreature.get().equals(creature);
        }

        public Optional<Adventurer> getHealthiestAdventurer() {
            return getLivingAdventurers().stream().max(Comparator.naturalOrder());
        }

        public Optional<Creature> getHealthiestCreature() {
            return getLivingCreatures().stream().max(Comparator.naturalOrder());
        }

        public boolean hasFood() {
            return !foodItems.isEmpty();
        }

        public boolean hasArmor() {
            return !armors.isEmpty();
        }

        public Food removeFoodItem() throws NotFoundInRoomException {
            if (foodItems.isEmpty()) {
                throw new NotFoundInRoomException("No food in room");
            }
            return foodItems.removeFirst();
        }

        public Food selectRandomFood() {
            return getFoodItems().get(Die.randomLessThan(foodItems.size()));
        }

        public Armor selectRandomArmor() {
            return armors.get(Die.randomLessThan(armors.size()));
        }

        public int numberOfNeighbors() {
            return neighbors.size();
        }

        public boolean hasNeighbors() {
            return !neighbors.isEmpty();
        }

        public boolean hasNeighbor(Room neighbor) {
            return neighbors.contains(neighbor);
        }

        public List<Room> getNeighbors() {
            return List.copyOf(neighbors);
        }

        public List<Food> getFoodItems() {
            return List.copyOf(foodItems);
        }

        public boolean contains(Food foodItem) {
            return foodItems.contains(foodItem);
        }

        public void removeItem(Food foodItem) {
            foodItems.remove(foodItem);
        }

        public boolean hasArmor(Armor armor) {
            return armors.contains(armor);
        }

        public void removeItem(Armor armor) {
            armors.remove(armor);
        }
    }

    public static class Builder {
        private final CharacterFactory characterFactory;
        private final ArtifactFactory artifactFactory;

        private Boolean distributeSequentially = false;
        private int currentRoomIndex = -1;  // This is incremented before any use

        private Room nextRoom() {
            if (distributeSequentially) {
                currentRoomIndex = (currentRoomIndex + 1) % maze.getRooms().size();
                return maze.getRooms().get(currentRoomIndex);
            }
            return maze.getRandomRoom();
        }

        Builder() {
            this(new CharacterFactory(), new ArtifactFactory());
        }

        final Maze maze = new Maze();
        Map<String, Room> roomMap = new HashMap<>();

        public Builder(CharacterFactory characterFactory, ArtifactFactory artifactFactory) {
            this.characterFactory = characterFactory;
            this.artifactFactory = artifactFactory;
        }

        public Builder createRoom(String name) {
            Room initialRoom = new Room(name);
            maze.addRoom(initialRoom);
            return this;
        }

        public Builder createGridOfRooms(int rows, int columns, String[][] roomNames) {
            Room[][] roomGrid = new Room[rows][columns];
            // Notice -- don't use i and j. Use row and column -- they are better
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    Room newRoom = new Room(roomNames[row][column]);
                    roomGrid[row][column] = newRoom;
                    maze.rooms.add(newRoom);
                }
            }

            // Now connect the rooms
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    Room currentRoom = roomGrid[row][column];
                    if (row > 0) {
                        currentRoom.connect(roomGrid[row - 1][column]);
                    }
                    if (column > 0) {
                        currentRoom.connect(roomGrid[row][column - 1]);
                    }
                }
            }
            return this;
        }

        public Builder create2x2Grid() {
            String[][] roomNames = new String[][]{{"Northwest", "Northeast"}, {"Southwest", "Southeast"}};
            return createGridOfRooms(2, 2, roomNames);
        }

        public Builder create3x3Grid() {
            String[][] roomNames = new String[][]{{"Northwest", "North", "Northeast"}, {"West", "Center", "East"}, {"Southwest", "South", "Southeast"}};
            return createGridOfRooms(3, 3, roomNames);
        }

        public Builder createFullyConnectedRooms(Integer numRooms) {
            String[] roomNames = createRoomNames(numRooms);
            return createFullyConnectedRooms(roomNames);
        }

        private static String[] createRoomNames(Integer numRooms) {
            // Room names must be unique, as the maze drawer uses them as keys to connect rooms
            String[] roomNames = new String[numRooms];
            int roomNameIndex = (new Random()).nextInt(ROOM_NAMES.length);
            for (int i = 0; i < numRooms; i++) {
                roomNames[i] = ROOM_NAMES[(roomNameIndex + i) % PolymorphiaNames.ROOM_NAMES.length];
            }
            return roomNames;
        }

        public Builder createFullyConnectedRooms(String... roomNames) {
            for (String roomName : roomNames) {
                Room currentRoom = new Room(roomName);
                roomMap.put(roomName, currentRoom);
                for (Room otherRoom : maze.rooms) {
                    currentRoom.connect(otherRoom);
                }
                maze.rooms.add(currentRoom);
            }
            return this;
        }

        public Builder createConnectedRooms(Integer minConnections, Integer numRooms) {
            return createConnectedRooms(minConnections, createRoomNames(numRooms));
        }

        public Builder createConnectedRooms(Integer minConnections, String... roomNames) {
            for (String roomName : roomNames) {
                Room currentRoom = new Room(roomName);
                roomMap.put(roomName, currentRoom);
                maze.rooms.add(currentRoom);
            }

            Boolean oldDistributionSetting = this.distributeSequentially;
            distributeSequentially();
            int realMinimumConnections = Math.min(minConnections, Math.max(maze.rooms.size() - 1, 1));
            for (Room room : maze.rooms) {
                while (room.numberOfNeighbors() < realMinimumConnections) {
                    Room neighbor = nextRoom();
                    if (!room.equals(neighbor) && !room.hasNeighbor(neighbor)) {
                        room.connect(neighbor);
                    }
                }
            }
            this.distributeSequentially = oldDistributionSetting;
            return this;
        }

        public Builder createAndAddAdventurers(String... adventurerNames) {
            for (String adventurerName : adventurerNames) {
                nextRoom().add(characterFactory.createAdventurer(adventurerName));
            }
            return this;
        }

        public Builder addAdventurers(List<Adventurer> adventurers) {
            for (Adventurer adventurer : adventurers) {
                nextRoom().add(adventurer);
            }
            return this;
        }

        public Builder createAndAddAdventurers(Integer numAdventurers) {
            return addAdventurers(characterFactory.createNumberOfAdventurers(numAdventurers));
        }

        public Builder createAndAddKnights(Integer numKnights) {
            addAdventurers(characterFactory.createNumberOfKnights(numKnights));
            return this;
        }

        public Builder createAndAddGluttons(Integer numGluttons) {
            addAdventurers(characterFactory.createNumberOfGluttons(numGluttons));
            return this;
        }

        public Builder createAndAddCowards(Integer numCowards) {
            addAdventurers(characterFactory.createNumberOfCowards(numCowards));
            return this;
        }

        public Builder addAdventurers(Adventurer... adventurers) {
            for (Adventurer adventurer : adventurers) {
                nextRoom().add(adventurer);
            }
            return this;
        }

        public Builder addCreatures(List<Creature> creatures) {
            for (Creature creature : creatures) {
                nextRoom().add(creature);
            }
            return this;
        }

        public Builder createAndAddCreatures(Integer numCreatures) {
            return addCreatures(characterFactory.createNumberOfCreatures(numCreatures));
        }


        public Builder createAndAddCreatures(String... names) {
            for (String name : names) {
                nextRoom().add(characterFactory.createCreature(name));
            }
            return this;
        }

        public Builder createAndAddDemons(Integer numDemons) {
            addCreatures(characterFactory.createNumberOfDemons(numDemons));
            return this;
        }

        public Builder createAndAddDemons(String... names) {
            for (String name : names) {
                nextRoom().add(characterFactory.createDemon(name));
            }
            return this;
        }

        public Maze build() {
            return maze;
        }

        public Builder createAndAddFoodItems(String... foodNames) {
            for (String foodName : foodNames) {
                nextRoom().add(artifactFactory.createFood(foodName));
            }
            return this;
        }

        public Builder createAndAddArmor(Integer numArmor) {
            List<Armor> armors = artifactFactory.createArmoredSuits(numArmor);
            for (Armor armor : armors) {
                nextRoom().add(armor);
            }
            return this;        }

        public Builder createAndAddFoodItems(Integer numItems) {
            List<Food> foodItems = artifactFactory.createFoodItems(numItems);
            for (Food food : foodItems) {
                nextRoom().add(food);
            }
            return this;
        }

        public Builder addCreatures(Creature... creatures) {
            for (Creature creature : creatures) {
                nextRoom().add(creature);
            }
            return this;
        }

        public Builder addToRoom(String roomName, Character character) {
            roomMap.get(roomName).add(character);
            return this;
        }

        public Builder addToRoom(String roomName, Food foodItem) {
            roomMap.get(roomName).add(foodItem);
            return this;
        }

        public Builder createAndAddKnights(String... names) {
            for (String name : names) {
                nextRoom().add(characterFactory.createKnight(name));
            }
            return this;
        }

        public Builder createAndAddGluttons(String... names) {
            for (String name : names) {
                nextRoom().add(characterFactory.createGlutton(name));
            }
            return this;
        }

        public Builder createAndAddCowards(String... names) {
            for (String name : names) {
                nextRoom().add(characterFactory.createCoward(name));
            }
            return this;
        }

        public Builder distributeSequentially() {
            distributeSequentially = true;
            return this;
        }

        public Builder distributeRandomly() {
            distributeSequentially = false;
            return this;
        }

        public Builder addToRoom(String roomName, Armor armor) {
            roomMap.get(roomName).add(armor);
            return this;
        }

        public Builder createAndAddHumanPlayer(String humanPlayerName) {
            nextRoom().add(characterFactory.createHuman(humanPlayerName));
            return this;
        }

        public Builder createAndAddAPIPlayer(String apiPlayerName){
            nextRoom().add(characterFactory.createApiPlayer(apiPlayerName));
            return this;
        }

        public Builder createAndAddGateRoom(String name) {
            Room room = new GateRoom(name);
            maze.addRoom(room);
            return this;
        }
    }

    private void addRoom(Room aRoom) {
        rooms.add(aRoom);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }
}
