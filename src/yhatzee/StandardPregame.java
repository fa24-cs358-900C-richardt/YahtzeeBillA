package yhatzee;

import java.util.*;

import yhatzee.interfaces.Die;
import yhatzee.interfaces.Game;
import yhatzee.interfaces.Player;
import yhatzee.interfaces.Pregame;

public class StandardPregame extends AbstractDiceRoller implements Pregame {
    protected final byte[] totals;
    protected List<Player> topRollers = null;

    protected StandardPregame(List<Player> players) {
        super(players);
        this.totals = new byte[this.players.size()];
    }

    protected StandardPregame(List<Player> players, List<Die> dice) {
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

    

    @Override
    public void rollAllDice() {
        if (this.currentPlayerIndex < 0) {
            throw new IllegalStateException("The first player has not yet been queued up.  Call next() first");

        } else if (totals[this.currentPlayerIndex] != 0) {
            throw new IllegalStateException("The current player has already rolled!");
        }

        super.rollAllDice();

        for (Die die : dice) {
            totals[this.currentPlayerIndex] += die.getValue();
        }
    }

    @Override
    public void rollSomeDice(boolean die1, boolean die2, boolean die3, boolean die4, boolean die5) {
        throw new UnsupportedOperationException("rollSomeDice not supported in Pregame");
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
    public Game startGame() throws IllegalStateException {
        return new StandardGame(this);
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
        private Tiebreaker(List<Player> tiedPlayers, List<Die> dice) {
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
