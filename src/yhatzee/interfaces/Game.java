package yhatzee.interfaces;

import java.util.*;

/**
 * 
 */
public interface Game extends TurnIterator {
    /**
     * @return The current round of the game
     */
    public int getCurrentRound();

    /**
     * @return the list of players in order of scores, from highest score to lowest score (descending order)
     * @throws IllegalStateException if the game is not over
     */
    public List<Player> getFinalRanking() throws IllegalStateException;
}
