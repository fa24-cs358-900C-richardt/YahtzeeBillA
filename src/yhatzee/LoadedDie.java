package yhatzee;
import java.util.concurrent.ThreadLocalRandom;

import yhatzee.interfaces.Die;

public class LoadedDie implements Die {
    public static Die[] makeDice(int weight1, int weight2, int weight3, int weight4, int weight5, int weight6) {
        return new Die[] {
            new LoadedDie(weight1, weight2, weight3, weight4, weight5, weight6),
            new LoadedDie(weight1, weight2, weight3, weight4, weight5, weight6),
            new LoadedDie(weight1, weight2, weight3, weight4, weight5, weight6),
            new LoadedDie(weight1, weight2, weight3, weight4, weight5, weight6),
            new LoadedDie(weight1, weight2, weight3, weight4, weight5, weight6)
        };
    }

    private byte value;
    private final int[] weights;
    private final int totalWeight;

    public LoadedDie(int ... weights) {
        if (weights == null || weights.length != 6) {
            throw new IllegalArgumentException("There must be 6 int arguments (or an array of 6 ints) to the loaded die constructor");
        }
        
        this.weights = weights.clone(); // encapsulation, so the array can't be modified from the outside
        int total = 0;
        for (int weight : this.weights) {
            if (weight < 0) throw new IllegalArgumentException("Loaded die weights cannot be less than 0");
            total += weight;
        }

        if (total <= 0) {
            throw new IllegalArgumentException("The sum of loaded die weights must be at least 1");
        }
        this.totalWeight = total;

        this.roll();
    }

    public void roll() {
        int val = ThreadLocalRandom.current().nextInt(this.totalWeight);

        for (int i = 0; i < 6; i++) {
            val -= weights[i];
            if (val < 0) {
                this.value = (byte)(i + 1);
                return;
            }
        }
        
        throw new IllegalStateException("Loaded die logic not working correctly");
    }

    public byte getValue() {
        return value;
    }
}
