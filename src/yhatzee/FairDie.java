package yhatzee;
import java.util.concurrent.ThreadLocalRandom;

import yhatzee.interfaces.*;
import yhatzee.records.*;
import java.util.*;

/**
 * A 6-sided fair die, which has an equal probably of turning
 * each value between 1 and 6
 */
public class FairDie implements Die {
    public static Dice makeDice() {
        return new StandardDice(List.of(
            new FairDie(),
            new FairDie(),
            new FairDie(),
            new FairDie(),
            new FairDie()
        ));
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
