package org.enrime.thebrokencore.entity.custom;

import net.minecraft.util.math.random.Random;

import java.util.Arrays;
import java.util.Comparator;

public enum BateyeVariant {
    RED(0, 50),
    GREEN(1, 25),
    BLUE(2, 15),
    PURPLE(3, 7.5),
    LARTS_AND_MARRALD(4, 2.5);

    private static final BateyeVariant[] BY_ID = Arrays.stream(values())
            .sorted(Comparator.comparingInt(BateyeVariant::getId)).toArray(BateyeVariant[]::new);
    private final int id;
    private final double weight;

    BateyeVariant(int id, double weight) {
        this.id = id;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public static BateyeVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }

    public static BateyeVariant getRandomVariant(Random random) {
        double totalWeight = Arrays.stream(values()).mapToDouble(BateyeVariant::getWeight).sum();

        double r = random.nextDouble() * totalWeight;
        for (BateyeVariant variant : values()) {
            r -= variant.getWeight();
            if (r <= 0) {
                return variant;
            }
        }

        return values()[0];  // Fallback
    }
}
