package yhatzee;
import java.util.concurrent.ThreadLocalRandom;

import yhatzee.interfaces.Die;

public class FairDie implements Die {
    public static Die[] makeDice() {
        return new Die[] {
            new FairDie(),
            new FairDie(),
            new FairDie(),
            new FairDie(),
            new FairDie()
        };
    }

    byte value;

    public FairDie() {
        this.roll();
    }

    public void roll() {
        this.value = (byte)(ThreadLocalRandom.current().nextInt(6) + 1);
    }

    public byte getValue() {
        return value;
    }
}
