package yhatzee.interfaces;

import java.util.*;

/**
 * Parent interface for Pregame and Game interfaces.  Includes the basic player and
 * dice-rolling methods.  Implemented as an Iterator, and a 'self-referencing Iterable' which
 * returns itself as its own stateful Iterator, since a game (or pre-game) can only be fully
 * iterated over once.
 */
public interface DiceEngine extends Iterator<Player>, Iterable<Player> {

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



    /**
     * For the implementation of Iterable interface, which simply returns self.  DiceRoller
     * is both an iterable and its own stateful iterator, since a game (or pre-game) can only
     * be fully iterated over once.
     * 
     * @return this
     */
    default DiceEngine iterator() {
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
