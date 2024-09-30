package yhatzee;

import java.io.IOException;

import yhatzee.interfaces.Decision;
import yhatzee.interfaces.Player;
import yhatzee.interfaces.Scorecard;
import yhatzee.records.DiceValues;
import yhatzee.records.RollDecision;
import yhatzee.records.ScorecardDecision;

public class HumanPlayer implements Player {
    private final String name;
    private Scorecard scorecard;
    
    public HumanPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    public Scorecard getScorecard() {
        return scorecard;
    }

    @Override
    public Decision makeFirstRollDecision(DiceValues diceValues) {
        System.out.println("First dice roll results:");
        System.out.println(diceValues.toString());
        System.out.println("You have two more rolls left for this round.");
        if (whatToDo()) {
            return enterDieDecision(diceValues);
        } else {
            return enterScorecardDecision(diceValues, true);
        }
    }

    @Override
    public Decision makeSecondRollDecision(DiceValues diceValues) {
        System.out.println("Second dice roll results:");
        System.out.println(diceValues.toString());
        System.out.println("You have one more roll left for this round.");
        if (whatToDo()) {
            return enterDieDecision(diceValues);
        } else {
            return enterScorecardDecision(diceValues, true);
        }
    }

    @Override
    public ScorecardDecision makeThirdRollDecision(DiceValues diceValues) {
        System.out.println("Third dice roll results:");
        System.out.println(diceValues.toString());
        System.out.println("That was your last roll left for this round.");
        return (ScorecardDecision)enterScorecardDecision(diceValues, false);
    }

    private static String readLine() {
        try {
            return StandardGameEngine.reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return true if the player wants to roll again, false if they want to record to scorecard
     */
    private boolean whatToDo() {
        while (true) {
            System.out.print("What would you like to do?  R to roll, S to record on Scorecard");

            String line = readLine();

            if (line.equals("R") || line.equals("r")) {
                return true;
            } else if (line.equals("S") || line.equals("s")) {
                return false;
            } else {
                System.out.print("Invalid input.  ");
            }
        }
    }

    private Decision enterDieDecision(DiceValues diceValues) {
        while(true) {
            System.out.println("For each die value, enter R for reroll, or K for keep (you may seperate by spaces).  S to enter into scorecard instead");
            System.out.println(diceValues.toString());

            String line = readLine();

            if (line.equals("S") || line.equals("s")) {
                return enterScorecardDecision(diceValues, true);
            }

            boolean[] decision = new boolean[5];
            int decisionIndex = 0;

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == ' ') continue;
                if (c == 'R' || c == 'r') {
                    if (decisionIndex < 4) {
                        decision[decisionIndex] = true;
                    }
                    decisionIndex++;

                } else if (c == 'K' || c == 'k') {
                    if (decisionIndex < 4) {
                        decision[decisionIndex] = false;
                    }
                    decisionIndex++;

                } else {
                    System.out.println("Invalid entry: " + String.valueOf(c));
                    decisionIndex = -1;
                    break;
                }
            }

            if (decisionIndex == 5) {
                return new RollDecision(decision);
            } else if (decisionIndex > 4) {
                System.out.println("Error: too many R/K entries.  Please only enter for the 5 dice displayed");
            }
        }
    }

    private Decision enterScorecardDecision(DiceValues diceValues, boolean allowReroll) {
        while (true) {
            System.out.println(this.scorecard);
            System.out.print("Please enter the row number you'd like to enter these dice values into.");
            if (allowReroll) {
                System.out.println("  R to reroll");
            } else {
                System.out.println();
            }
            System.out.println(diceValues.toString());

            String line = readLine();
            
            if (allowReroll && (line.equals("R") || line.equals("r"))) {
                return enterDieDecision(diceValues);
            }

            int row = 0;
            try {
                row = Integer.parseInt(line);

            } catch (NumberFormatException e) {
                // fall through to below check...
            }

            if (row < 1 || row > 13) {
                System.out.println("Invalid entry.  Must be an integer between 1 and 13.");

            } else if (this.scorecard.getRow(row) != null) {
                System.out.println("Invalid entry.  Row " + row + " is already taken.");

            } else if (diceValues.isYahtzee()) {
                String forcedJoker = this.scorecard.forcedJokerCheck(row, diceValues.get(0));
                if (forcedJoker != null) {
                    System.out.println(forcedJoker);
                } else {
                    return new ScorecardDecision(row);
                }

            } else {
                return new ScorecardDecision(row);
            }
        }
    }


}
