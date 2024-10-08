package yhatzee;

import java.io.*;
import java.util.*;

import yhatzee.interfaces.*;
import yhatzee.records.DiceValues;

/**
 * Concrete implementation of the GameEngine interface, which acts as the controller calling the various methods
 * of the Game.
 */
public class StandardGameEngine implements GameEngine {
    public static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        new StandardGameEngine().run();
    }

    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt + " > ");
            String result;
            try {
                result = reader.readLine();

            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if (result != null && !result.isEmpty()) {
                return result;
            }
        }
    }

    public static int getInt(int lowerBound, int upperBound, String prompt) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound must be lower than upper bound: " + lowerBound + " vs " + upperBound);
        }

        while(true) {
            String input = getString(prompt);
            int result;

            try {
                result = Integer.parseInt(input);
                if (result >= lowerBound && result <= upperBound) {
                    return result;
                }

            } catch (NumberFormatException e) {
                System.out.print("Invalid input, not an integer.  ");
            }

            System.out.println("You must enter an integer between " + lowerBound + " and " + upperBound);
        }
    }

    private TurnIterator currentGame;

    public TurnIterator getCurrentGame() {
        return this.currentGame;
    }

    @Override
    public void run() {
        List<Player> players = this.initPlayers();
        Pregame finishedPregame = this.runPregame(this.makePregame(players));
        Game game = makeGame(finishedPregame);
        this.runGame(game);
        this.printResult(game);
    }

    @Override
    public Pregame makePregame(List<Player> players) {
        return new StandardPregame(players, FairDie.makeDice()); //LoadedDie.makeDice(1, 1, 1, 1, 1, 200));
    }

    @Override
    public Game makeGame(Pregame finishedPregame) {
        return new StandardGame(finishedPregame);
    }

    @Override
    public void runGame(Game game) {
        this.currentGame = game;
        for (Player player : game) {
            runPlayerRound(game, player);
        }
    }

    public void printResult(Game game) {
        System.out.println();
        System.out.println("FINAL SCORECARDS");
        System.out.println();

        for (Player player : game.getPlayers()) {
            System.out.println(player.getName());
            System.out.println(player.getScorecard());
            System.out.println();
        }

        List<Player> ranking = game.getFinalRanking();

        System.out.println("Final ranking:");
        for (Player player : ranking) {
            System.out.println(player.getName() + ": " + player.getScorecard().getGrandTotal());
        }
    }

    

    public List<Player> initPlayers() {
        int numPlayers = getInt(1, 6, "How many players? (1-6)");

        System.out.println("Enter player's names, from left to right (each name must be unique)");
        List<Player> players = new ArrayList<Player>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            boolean duplicate;
            String name;
            do {
                name = getString("What is player #" + (i + 1) + "'s name?");
                duplicate = false;
                for (int j = 0; j < i; j++) {
                    if (players.get(j).getName().equals(name)) {
                        System.out.println("Error: Player #" + (j + 1) + " already has that name.  Please enter a unique name.");
                        duplicate = true;
                        break;
                    }
                }
            } while (duplicate);

            players.add(new HumanPlayer(name));
        }

        return players;
    }

    public Pregame runPregame(Pregame pregame) {
        this.currentGame = pregame;

        if (pregame instanceof Pregame.Tiebreaker) {
            System.out.println("Rolling a tiebreaker to see who takes the first turn...");
        } else {
            System.out.println("Rolling to see who takes the first turn...");
        }
        
        for (Player player : pregame) {
            System.out.print("Rolling dice for " + player.getName() + " : ");
            pregame.getDice().rollAllDice();
            DiceValues diceValues = pregame.getDice().getDiceValues();
            pregame.setCurrentPlayerTotal(diceValues);
            System.out.println(diceValues + "Total: " + pregame.getCurrentPlayerTotal());
        }

        List<Player> topRollers = pregame.getTopRollers();

        if (topRollers.size() > 1) {
            System.out.print("We have a tie between ");
            if (topRollers.size() == 2) {
                System.out.println(topRollers.get(0).getName() + " and " + topRollers.get(1).getName());

            } else {
                for (int i = 0; i < topRollers.size() - 1; i++) {
                    System.out.print(topRollers.get(i).getName() + ", ");
                }
                System.out.println("and " + topRollers.get(topRollers.size() - 1).getName());
            }
            
            return runPregame(pregame.getTiebreaker());

        } else {
            System.out.println("The first turn goes to " + topRollers.get(0).getName());
            return pregame;
        }
    }
    
    
}
