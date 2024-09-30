package yhatzee.interfaces;

import java.util.*;

/**
 * Parent interface for Pregame and Game interfaces.  Includes the basic player and
 * dice-rolling methods.  Implemented as an Iterator, and a 'self-referencing Iterable' which
 * returns itself as its own stateful Iterator, since a game (or pre-game) should only be fully
 * iterated over once.
 */
public interface TurnIterator extends Iterator<Player>, Iterable<Player> {

    /**
     * @return the dice to be used for this TurnIterator
     */
    Dice getDice();

    /**
     * Advances to the next player to roll
     * @throws NoSuchElementException if the end of game (or end of pre-game has been reached)
    */
    public Player next() throws NoSuchElementException;

    /**
     * @return whether all players who need to roll have rolled yet
     */
    public boolean hasNext();

    /**
     * @return the array of players
     */
    public List<Player> getPlayers();

    /**
     * @return the player whose turn it currently is, without advancing
     */
    public Player getCurrentPlayer();

    /**
     * For the implementation of Iterable interface, which simply returns self.  DiceRoller
     * is both an iterable and its own stateful iterator, since a game (or pre-game) can only
     * be fully iterated over once.
     * 
     * @return this
     */
    default TurnIterator iterator() {
        return this;
    }

    /**
     * Spliterator will not be implemented.  Throwing UnsupportedOperationException
     * @throws UnsupportedOperationException
     */
    @Override
    default Spliterator<Player> spliterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unimplemented method 'spliterator'");
    }

    /**
     * Iterator's 'forEachRemaining' and Iterable's 'forEach' will use the default implementation,
     * which simply iterates over remaining elements using the provided Consumer function.
     * 
     * Iterator's 'remove' will use the default implementation which throws an
     * UnsupportedOperationException
     */
}
