package yhatzee.interfaces;

/**
 * Represent's a players decision as to whether to continue rolling dice (if available), which dice to
 * re-roll and which to keep, OR which row in the scorecard to place the current dice values.
 */
public interface Decision {
    /**
     * @return -1 if the player wants to roll the dice again,
     * otherwise the row number to assign the existing dice rolls to (between 1 and 13, inclusive)
     * 
     * NOTE: If getDieDecision returns false for all dieNumbers (0-4), then getScorecardRowDecision should
     * return a number between 1 and 13
     */
    default public int getScorecardRowDecision() {
        return -1;
    }

    /**
     * @param dieNumber the index of the die number being queried (0-4, inclusive)
     * @return whether the player wishes to re-roll the die at index dieNumber
     * 
     * NOTE: If getScorecardRowDecision returns a number other than -1 (aka a number between 1 and 13)
     * then getDieDecision should return false for all dieNumbers
     */
    default public boolean getDieDecision(int dieNumber) throws IndexOutOfBoundsException {
        return false;
    }
}
