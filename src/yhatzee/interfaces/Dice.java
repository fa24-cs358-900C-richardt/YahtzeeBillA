package yhatzee.interfaces;

import yhatzee.records.DiceValues;

public interface Dice {
    /**
     * @return the array of current dice values, from the most recent roll
     */
    public DiceValues getDiceValues();

    /**
     * @param index the index of the die to get the value of
     * @return the value of the die at the given index
     */
    public byte getDieValue(int index);

    /**
     * Roll all 5 dice
     */
    public void rollAllDice();

    /**
     * Rolls only the dice specified by the Decision
     */
    public void rollSomeDice(Decision rollDecision);
}
