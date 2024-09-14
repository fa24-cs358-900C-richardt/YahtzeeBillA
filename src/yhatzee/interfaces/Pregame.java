package yhatzee.interfaces;

import java.util.List;

/**
 * Represents the pre-game stage at which players roll dice to see who goes first
 */

public interface Pregame extends DiceRoller {
    /**
     * 
     * @return the total dice roll for the current player.  0 if they have not rolled yet
     * @throws IndexOutOfBoundsException if the pregame has reached "done" (aka nextPlayer has been called since the last player rolled)
     */
    public byte getCurrentPlayerTotal() throws IndexOutOfBoundsException;

    /**
     * @return the array of player totals.  Players who haven't rolled yet will have a total
     * of zero
     */
    public byte[] getPlayerTotals();

    /**
     * @return an array representing all players who rolled the top total (there may be more
     * than 1 if there was a tie!)
     * @throws IllegalStateException if not all players have rolled
     */
    public List<Player> getTopRollers() throws IllegalStateException;

    /**
     * @return a game ready to start with the player who rolled the highest total
     * @throws IllegalStateException if not all players have rolled, or if there is a tie that
     * needs to be broken
     */
    public Game startGame() throws IllegalStateException;

    /**
     * @return a Pregame Tiebreaker instance to break any ties over who goes first
     * @throws IllegalStateException if there were no ties to be broken
     */
    public Tiebreaker getTiebreaker() throws IllegalStateException;

    public interface Tiebreaker extends Pregame {
        /**
         * @return the array of players who are rolling to break the tie
         */
        public List<Player> getTiedPlayers();
    }
}
