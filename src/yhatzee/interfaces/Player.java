package yhatzee.interfaces;

import yhatzee.records.*;

/**
 * A player of the game, with an interface to store their scorecard, make decisions about which dice to re-roll and which to keep,
 * and which row of the scorecard to place the dice values into after they are done rolling.
 */
public interface Player {
    /**
     * @return the player's name
     */
    public String getName();

    /**
     * @param scorecard the player's scorecard to set
     */
    public void setScorecard(Scorecard scorecard);

    /**
     * @return the player's scorecard for the current game
     */
    public Scorecard getScorecard();

    /**
     * Have the player make a decision after the first role, with the given dice values
     * @param diceValues the dice values from the first roll
     * @return the player's decision about what to do
     */
    public Decision makeFirstRollDecision(DiceValues diceValues);

    /**
     * Have the player make a decision after the second role, with the given dice values
     * @param diceValues the dice values from the second roll
     * @return the player's decision about what to do
     */
    public Decision makeSecondRollDecision(DiceValues diceValues);

    /**
     * Have the player make a decision after the third role, with the given dice values
     * @param diceValues the dice values from the third roll
     * @return the player's decision about what to do.  This will neccessarily need to be a decision
     * about which row in the scorecard to place the dice values, since they are out of re-rolls at this point
     */
    public ScorecardDecision makeThirdRollDecision(DiceValues diceValues);
}
