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

    byte value;
    int weight1, weight2, weight3, weight4, weight5, weight6, totalWeight;

    public LoadedDie(int weight1, int weight2, int weight3, int weight4, int weight5, int weight6) {
        this.weight1 = weight1;
        this.weight2 = weight2;
        this.weight3 = weight3;
        this.weight4 = weight4;
        this.weight5 = weight5;
        this.weight6 = weight6;
        this.totalWeight = weight1 + weight2 + weight3 + weight4 + weight5 + weight6;

        if (weight1 < 0 || weight2 < 0 || weight3 < 0 || weight4 < 0 || weight5 < 0 || weight6 < 0 || totalWeight <= 0) {
            throw new IllegalArgumentException("Loaded die weights cannot be less than 0, and the sum of weights must be at least 1");
        }

        this.roll();
    }

    public void roll() {
        this.value = this.calcLoaded();
    }

    private byte calcLoaded() {
        int val = ThreadLocalRandom.current().nextInt(totalWeight);
        
        val -= weight1;
        if (val < 0) return 1;

        val -= weight2;
        if (val < 0) return 2;

        val -= weight3;
        if (val < 0) return 3;

        val -= weight4;
        if (val < 0) return 4;

        val -= weight5;
        if (val < 0) return 5;

        val -= weight6;
        if (val < 0) return 6;

        throw new IllegalStateException("Loaded die logic not working correctly");
    }

    public byte getValue() {
        return value;
    }
}
