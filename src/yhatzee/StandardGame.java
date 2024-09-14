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

        this.nextPlayerIndex = -1;
        for (int i = 0; i < this.players.size(); i++) {
            if (topRollers.get(0) == this.players.get(i)) {
                this.nextPlayerIndex = i;
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
        this.nextPlayerIndex--;
        if (this.nextPlayerIndex < 0) {
            this.nextPlayerIndex = this.players.size() - 1;
        }
        
        this.currentPlayer = this.players.get(this.currentPlayerIndex);
        return this.currentPlayer;
    }
}
