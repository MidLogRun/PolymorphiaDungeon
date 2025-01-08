package csci.ooad.polymorphia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ArtifactFactory {
    public final static String[] FOOD_NAMES = new String[]{"cupcake", "apple", "banana", "steak", "salad", "fries", "burger", "pizza", "eggs",
            "bacon", "muffin", "donut", "chicken", "pasta", "rice", "sushi", "taco", "burrito", "nachos", "chips"};

    public final static String[] ARMOR_NAMES = new String[] {
            "leather", "chainmail", "plate", "iron", "steel", "mithril", "dragon hide", "titanium", "platinum", "gold"};

    static final double DEFAULT_DELTA_ARMOR_STRENGTH = 1.0;
    static final double DEFAULT_DELTA_ARMOR_MOVING_COST = 0.1;
    public final static int DEFAULT_FOOD_HEALTH_VALUE = 1;


    private static final Random random = new Random();

    public Food createFood(String name) {
        return new Food(name, DEFAULT_FOOD_HEALTH_VALUE);
    }

    public List<Food> createFoodItems(Integer numItems) {
        return IntStream.range(0, numItems)
                .mapToObj(i -> new Food(FOOD_NAMES[i % FOOD_NAMES.length], random.nextDouble(1,2)))
                .map(Food.class::cast)
                .toList();
    }
    public Armor createArmorSuit(String name) {
        return new Armor(name, DEFAULT_DELTA_ARMOR_STRENGTH, DEFAULT_DELTA_ARMOR_MOVING_COST);
    }

    public List<Armor> createArmoredSuits(Integer numItems) {
        List<Armor> armors = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            int nameIndex = i % ARMOR_NAMES.length;
            armors.add(createArmorSuit(ARMOR_NAMES[nameIndex]));
        }
        return armors;
    }

    public RoomKey createRoomKey() {
        return new RoomKey();
    }
}
