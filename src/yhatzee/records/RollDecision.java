package yhatzee.records;

import yhatzee.interfaces.*;

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
