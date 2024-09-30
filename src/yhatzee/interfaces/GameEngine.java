package yhatzee.interfaces;

import yhatzee.records.*;
import java.util.*;

/**
 * The basic outline of a controller that will call all the methods in the Game interface, to actually run the game
 */
public interface GameEngine {
    public void run();
    
    /**
     * Initialize the list of players
     * @return
     */
    List<Player> initPlayers();
    
    /**
     * Construct a starting pre-game instance with the passed list of players
     * @param players
     * @return the pregame instance to run
     */
    Pregame makePregame(List<Player> players);

    /**
     * @param pregame 
     * @return The finished pre-game.  Normally this will be the pre-game passed in, but in the event of a tie-breaker
     * it will be the finished tie-breaker round with a single winner
     */
    Pregame runPregame(Pregame pregame);

    /**
     * Construct a game instance using the finishedPregame's list of players and pre-game winner to determine the starting player
     * @param finishedPregame
     * @return
     */
    Game makeGame(Pregame finishedPregame);

    /**
     * Run the full game
     * @param game
     */
    void runGame(Game game);

    default void runPlayerRound(Game game, Player player) {
        game.getDice().rollAllDice();
        
        DiceValues diceVals = game.getDice().getDiceValues();
        Decision decision = player.makeFirstRollDecision(diceVals);
        int row = decision.getScorecardRowDecision();
        if (row > 0) {
            player.getScorecard().setRow(row, diceVals);
            return;
        }

        game.getDice().rollSomeDice(decision);
        diceVals = game.getDice().getDiceValues();
        decision = player.makeSecondRollDecision(diceVals);
        row = decision.getScorecardRowDecision();
        if (row > 0) {
            player.getScorecard().setRow(row, diceVals);
            return;
        }

        game.getDice().rollSomeDice(decision);
        diceVals = game.getDice().getDiceValues();
        decision = player.makeThirdRollDecision(diceVals);
        row = decision.getScorecardRowDecision();
        
        player.getScorecard().setRow(row, diceVals);
    }

}
