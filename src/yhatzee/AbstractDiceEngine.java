package yhatzee;

import java.util.*;

import yhatzee.interfaces.*;

/**
 * Abstract implementation of DiceRoller interface, which defaults to using the fair die,
 * but allows any list of dice to be passed in provided the list is non-null and has a size
 * of 5.
 * 
 * Intended for concrete implementation by StandardGame and StandardPregame
 */
public abstract class AbstractDiceEngine implements DiceEngine {
    protected Player currentPlayer;
    protected int currentPlayerIndex = -1;
    protected int nextPlayerIndex = 0;

    protected final List<Player> players;
    protected final List<Die> dice;

    protected AbstractDiceEngine(List<Player> players) {
        this(players, Arrays.asList(FairDie.makeDice()));
    }

    protected AbstractDiceEngine(List<Player> players, List<Die> dice) {
        if (players == null || players.size() < 1 || players.size() > 6) {
            throw new IllegalArgumentException("Illegal players array");
        }
        if (dice == null || dice.size() != 5) {
            throw new IllegalArgumentException("Illegal dice array");
        }

        //shallow-copy the arrays to a local copy, to ensure encapsulation
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.dice = Collections.unmodifiableList(new ArrayList<>(dice));
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

    public DiceValues getDiceValues() {
        return new DiceValues(dice);
    }

    public byte getDieValue(int index) throws IndexOutOfBoundsException {
        return dice.get(index).getValue();
    }

    public void rollAllDice() {
        for (Die die : dice) {
            die.roll();
        }
    }

    public void rollSomeDice(boolean die1, boolean die2, boolean die3, boolean die4, boolean die5) {
        if (die1) dice.get(0).roll();
        if (die2) dice.get(1).roll();
        if (die3) dice.get(2).roll();
        if (die4) dice.get(3).roll();
        if (die5) dice.get(4).roll();
    }
}
