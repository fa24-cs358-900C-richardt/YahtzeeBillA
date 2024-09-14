package yhatzee.interfaces;

import java.util.List;

/**
 * Parent interface for Pregame and Game interfaces.  Includes the basic player and
 * dice-rolling methods
 */
public interface DiceRoller {
    /**
     * @return the array of players
     */
    public List<Player> getPlayers();

    /**
     * @return the player whose turn it current is
     */
    public Player getCurrentPlayer();

    /**
     * Advances to the next player to roll
     * @throws IllegalStateException if the end of game (or pre-game has been reached)
    */
    public void nextPlayer() throws IllegalStateException;

    /**
     * @return whether all players who need to roll have rolled yet
     */
    public boolean done();

    /**
     * @return the array of current dice values, from the most recent roll
     */
    public byte[] getDiceValues();

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
     * Rolls only the dice specified by the boolean arguments.  True means roll that die, while
     * false means do not roll it.
     * 
     * @param die1 whether to roll die 1 (index 0)
     * @param die2 whether to roll die 2 (index 1)
     * @param die3 whether to roll die 3 (index 2)
     * @param die4 whether to roll die 4 (index 3)
     * @param die5 whether to roll die 5 (index 4)
     */
    public void rollSomeDice(boolean die1, boolean die2, boolean die3, boolean die4, boolean die5);
}
