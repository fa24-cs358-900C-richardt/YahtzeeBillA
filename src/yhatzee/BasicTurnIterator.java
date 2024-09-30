package yhatzee;

import java.util.*;

import yhatzee.interfaces.*;

/**
 * Abstract implementation of the TurnIterator interface, which defaults to using the fair die,
 * but allows any list of dice to be passed in provided the list is non-null and has a size
 * of 5.
 * 
 * Intended for concrete implementation by StandardGame and StandardPregame
 */
public abstract class BasicTurnIterator implements TurnIterator {
    protected Player currentPlayer;
    protected int currentPlayerIndex = -1;
    protected int nextPlayerIndex = 0;

    protected final List<Player> players;
    protected final Dice dice;

    protected BasicTurnIterator(List<Player> players) {
        this(players, FairDie.makeDice());
    }

    protected BasicTurnIterator(List<Player> players, Dice dice) {
        if (players == null || players.size() < 1 || players.size() > 6) {
            throw new IllegalArgumentException("Illegal players array");
        }
        if (dice == null) {
            throw new NullPointerException("dice must be non-null");
        }

        //shallow-copy the arrays to a local copy, to ensure encapsulation
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.dice = dice;
    }

    @Override
    public boolean hasNext() {
        return this.nextPlayerIndex >= 0 && this.nextPlayerIndex < players.size();
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Dice getDice() {
        return dice;
    }
}
