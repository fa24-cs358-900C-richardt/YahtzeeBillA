package yhatzee;

import java.util.*;

import yhatzee.interfaces.Game;
import yhatzee.interfaces.Player;
import yhatzee.interfaces.Pregame;

public class StandardGame extends AbstractDiceRoller implements Game {
    public StandardGame(Pregame pregame) {
        super(pregame.getPlayers());
        List<Player> topRollers = pregame.getTopRollers();
        if (topRollers == null || topRollers.size() != 1 || topRollers.get(0) == null) {
            throw new IllegalStateException("Pre-game was not propertly completed");
        }

        this.currentPlayer = -1;
        for (int i = 0; i < this.players.size(); i++) {
            if (topRollers.get(0) == this.players.get(i)) {
                this.currentPlayer = i;
            }
        }
        if (this.currentPlayer == -1) {
            throw new IllegalStateException("Could not find top rolling player in the players list");
        }
    }


    /**
     * Players go from left to right during the game
     */
    @Override
    public void nextPlayer() {
        this.currentPlayer--;
        if (this.currentPlayer < 0) {
            this.currentPlayer = this.players.size() - 1;
        }
    }

    @Override
    public boolean done() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'done'");
    }
}
