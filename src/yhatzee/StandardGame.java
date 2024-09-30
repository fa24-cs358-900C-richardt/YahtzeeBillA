package yhatzee;

import java.util.*;

import yhatzee.interfaces.Game;
import yhatzee.interfaces.Player;
import yhatzee.interfaces.Pregame;

/**
 * Concrete implementation of the Game interface and the BasicTurnIterator abstract class
 */
public class StandardGame extends BasicTurnIterator implements Game {
    private int currentRound = 0;
    private int startingPlayerIndex;

    public StandardGame(Pregame pregame) {
        super(pregame.getPlayers(), pregame.getDice());
        List<Player> topRollers = pregame.getTopRollers();
        if (topRollers == null || topRollers.size() != 1 || topRollers.get(0) == null) {
            throw new IllegalStateException("Pre-game was not propertly completed");
        }

        this.nextPlayerIndex = -1;
        this.startingPlayerIndex = -1;
        for (int i = 0; i < this.players.size(); i++) {
            if (topRollers.get(0) == this.players.get(i)) {
                this.nextPlayerIndex = i;
                this.startingPlayerIndex = i;
                break;
            }
        }
        if (this.nextPlayerIndex == -1) {
            throw new IllegalStateException("Could not find top rolling player in the players list");
        }
    }


    /**
     * Players go from left to right during the game
     */
    @Override
    public Player next() {
        this.currentPlayerIndex = this.nextPlayerIndex;
        this.currentPlayer = this.players.get(this.currentPlayerIndex);

        this.nextPlayerIndex--;
        if (this.nextPlayerIndex < 0) {
            this.nextPlayerIndex = this.players.size() - 1;
        }

        if (this.currentPlayerIndex == this.startingPlayerIndex) {
            this.currentRound++;

        }
        
        if (this.currentRound == 13 && this.nextPlayerIndex == this.startingPlayerIndex) {
            this.nextPlayerIndex = -1;
        }
        
        return this.currentPlayer;
    }


    @Override
    public int getCurrentRound() {
        return this.currentRound;
    }


    @Override
    public List<Player> getFinalRanking() throws IllegalStateException {
        if (this.nextPlayerIndex != -1) {
            throw new IllegalStateException("Cannot call getFinalRanking until the game is over!");
        }

        List<Player> ranking = new ArrayList<>(this.players);
        ranking.sort((a, b) -> b.getScorecard().getGrandTotal() - a.getScorecard().getGrandTotal());
        return ranking;
    }
}
