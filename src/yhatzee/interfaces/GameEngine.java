package yhatzee.interfaces;

import yhatzee.records.*;
import java.util.*;

public interface GameEngine {
    default public void run() {
        List<Player> players = this.initPlayers();
        Pregame finishedPregame = this.runPregame(this.makePregame(players));
        Game game = makeGame(finishedPregame);
        this.runGame(game);
    }
    
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
    default void runGame(Game game) {
        for (Player player : game) {
            runPlayerRound(game, player);
        }
    }

    default void runPlayerRound(Game game, Player player) {
        game.rollAllDice();
        
        DiceValues diceVals = game.getDiceValues();
        Decision decision = player.makeFirstRollDecision(diceVals);
        int row = decision.getScorecardRowDecision();
        if (row > 0) {
            player.getScorecard().setRow(row, diceVals);
            return;
        }

        game.rollSomeDice(decision);
        diceVals = game.getDiceValues();
        decision = player.makeSecondRollDecision(diceVals);
        row = decision.getScorecardRowDecision();
        if (row > 0) {
            player.getScorecard().setRow(row, diceVals);
            return;
        }

        game.rollSomeDice(decision);
        diceVals = game.getDiceValues();
        decision = player.makeThirdRollDecision(diceVals);
        row = decision.getScorecardRowDecision();
        
        player.getScorecard().setRow(row, diceVals);
    }

}
