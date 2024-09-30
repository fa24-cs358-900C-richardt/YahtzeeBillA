package yhatzee;

import java.util.*;

import yhatzee.interfaces.*;
import yhatzee.records.DiceValues;

public class StandardPregame extends AbstractDiceEngine implements Pregame {
    protected final byte[] totals;
    protected List<Player> topRollers = null;

    public StandardPregame(List<Player> players) {
        super(players);
        this.totals = new byte[this.players.size()];
    }

    public StandardPregame(List<Player> players, Dice dice) {
        super(players, dice);
        this.totals = new byte[this.players.size()];
    }

    /**
     * Players go in the order they were entered.  This method should be called AFTER each
     * player rolls, including after the final player.  Then check done() to determine
     * whether to keep iterating
     */
    @Override
    public Player next() throws IllegalStateException, NoSuchElementException {
        if (this.nextPlayerIndex >= this.players.size()) {
            throw new NoSuchElementException("All players have already rolled!");
        }

        if (this.currentPlayerIndex >= 0 && this.totals[this.currentPlayerIndex] == 0) {
            throw new IllegalStateException("The current player has not rolled yet");
        }

        this.currentPlayerIndex = this.nextPlayerIndex;
        this.nextPlayerIndex++;

        this.currentPlayer = this.players.get(this.currentPlayerIndex);
        return this.currentPlayer;
    }

    /**
     * @return a clone of the player totals.  Zero indicates the player has not rolled yet.
     * A clone of the totals array is returned to protect encapsulation, so the original
     * cannot be modified
     */
    @Override
    public byte[] getPlayerTotals() {
        return totals.clone();
    }

    @Override
    public void setCurrentPlayerTotal(DiceValues diceValues) {
        this.totals[this.currentPlayerIndex] = (byte)diceValues.sumValues();
    }

    @Override
    public byte getCurrentPlayerTotal() throws IndexOutOfBoundsException {
        return this.totals[this.currentPlayerIndex];
    }

    @Override
    public List<Player> getTopRollers() throws IllegalStateException {
        if (this.topRollers == null) {
            this.determineTopRollers();
        }
        return this.topRollers;
    }

    private void determineTopRollers() throws IllegalStateException {
        byte top = 0;
        this.topRollers = new ArrayList<>();
        for (int i = 0; i < totals.length; i++) {
            if (totals[i] == 0) {
                this.topRollers = null;
                throw new IllegalStateException("Pre-game is not done!");
            }
            if (totals[i] > top) {
                top = totals[i];
                topRollers.clear();
                topRollers.add(this.players.get(i));

            } else if (totals[i] == top) {
                topRollers.add(this.players.get(i));
            }
        }
        this.topRollers = Collections.unmodifiableList(this.topRollers);
    }

    @Override
    public Tiebreaker getTiebreaker() throws IllegalStateException {
        if (this.topRollers == null) {
            throw new IllegalStateException("Top roller hasn't been determined yet!");
        } else if (this.topRollers.size() < 2) {
            throw new IllegalStateException("There is no tie to break!");
        }

        return new Tiebreaker(this.topRollers, this.dice);
    }
    
    public class Tiebreaker extends StandardPregame implements Pregame.Tiebreaker {
        private Tiebreaker(List<Player> tiedPlayers, Dice dice) {
            super(tiedPlayers, dice);
        }

        @Override
        public List<Player> getPlayers() {
            return StandardPregame.this.getPlayers();
        }

        @Override
        public List<Player> getTiedPlayers() {
            return this.players;
        }
        
    }
}
