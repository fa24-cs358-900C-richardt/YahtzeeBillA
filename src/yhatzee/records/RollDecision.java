package yhatzee.records;

import yhatzee.interfaces.*;

/**
 * Represents a player's decision to re-roll the dice, with certain dice to keep and others to re-roll
 */
public record RollDecision(boolean[] diceDecisions) implements Decision {
    public RollDecision(boolean[] diceDecisions) {
        if (diceDecisions == null || diceDecisions.length != 5) {
            throw new IllegalArgumentException("diceDecisions must be non-null and have 5");
        } else {
            this.diceDecisions = diceDecisions.clone();
        }
    }
    
    @Override
    public boolean getDieDecision(int dieNumber) throws IndexOutOfBoundsException {
        return this.diceDecisions[dieNumber];
    }
}
