package yhatzee.interfaces;

import java.util.*;

import yhatzee.records.DiceValues;

/**
 * The scorecard interface.  There are a number of boilerplate default methods here, including some static methods and constants.
 * There are only three methods which implementing classes need to override.  The rest provide out-of-the-box functionality, but
 * can be overriden for caching or testing purposes.
 */
public interface Scorecard {
    /**
     * Get the dice values for the given scorecard row, indexed from 1 (not 0)
     * @return null if not yet filled, or if filled the dice values present in the row
     */
    public DiceValues getRow(int rowNumber) throws IndexOutOfBoundsException;

    /**
     * @return the Yahtzee bonus score (50 points for each yahtzee rolled after the yahtzee row was full)
     */
    public int getYahtzeeBonus();

    /**
     * @param rowNumber the row number to set.  As with getRow, this is 1-indexed (not 0 indexed)
     * @param diceValues the dice values to set the row to.
     */
    public void setRow(int rowNumber, DiceValues diceValues);

    /*
     * IMPORTANT:
     * 
     * Everything below is boilerplate code.
     * Implentations only need to implement the above 3 methods, but may override the boilerplate
     * methods below for special cases (e.g. testing or rigging a game, caching scores to avoid repetative recalculations, etc)
     */


    public static final Map<String, Integer> ROWS_BY_NAME = Map.ofEntries(
        Map.entry("Aces", 1),
        Map.entry("Twos", 2),
        Map.entry("Threes", 3),
        Map.entry("Fours", 4),
        Map.entry("Fives", 5),
        Map.entry("Sixes", 6),
        Map.entry("3 of a kind", 7),
        Map.entry("4 of a kind", 8),
        Map.entry("Full House", 9),
        Map.entry("Small Straight", 10),
        Map.entry("Large Straight", 11),
        Map.entry("Yahtzee", 12),
        Map.entry("Chance", 13)
    );

    public static final Map<Integer, String> ROWS_BY_NUMBER = Map.ofEntries(
        Map.entry(1, "Aces"),
        Map.entry(2, "Twos"),
        Map.entry(3, "Threes"),
        Map.entry(4, "Fours"),
        Map.entry(5, "Fives"),
        Map.entry(6, "Sixes"),
        Map.entry(7, "3 of a kind"),
        Map.entry(8, "4 of a kind"),
        Map.entry(9, "Full House"),
        Map.entry(10, "Small Straight"),
        Map.entry(11, "Large Straight"),
        Map.entry(12, "Yahtzee"),
        Map.entry(13, "Chance")
    );

    public static final int YHATZEE_BONUS = 50; // bonus for rolling a yahtzee when the yahtzee row was alrady filled with a scoring roll


    /*
     * The below constants are used to assist with the textual printing of the scorecard on the terminal
     */

    public static final String JOKER = "(joker)";

    public static final int MAX_ROW_NAME_WIDTH = ROWS_BY_NAME.keySet().stream().mapToInt(String::length).max().getAsInt();
    public static final int NUMBER_COLUMN_WIDTH = 4;
    public static final int NAME_COLUMN_WIDTH = MAX_ROW_NAME_WIDTH + 2;
    public static final int DICE_VALUES_COLUMN_WIDTH = DiceValues.NULL_STRING.length();
    public static final int JOKER_COLUMN_WIDTH = JOKER.length();
    public static final int TOTALS_COLUMN_WIDTH = 4;
    public static final int ALL_COLUMNS_WIDTH = NUMBER_COLUMN_WIDTH + NAME_COLUMN_WIDTH + DICE_VALUES_COLUMN_WIDTH + JOKER_COLUMN_WIDTH + TOTALS_COLUMN_WIDTH;
    public static final String SEPERATOR = "-".repeat(ALL_COLUMNS_WIDTH) + "\n";


    /**
     * Interfaces don't allow default overrides of Object.toString().  Implementations should
     * call and return the value from this method in their own override of toString();
     * 
     * @return scorecard representation as a terminal-printable string
     */
    default public String string() {
        StringBuilder sb = new StringBuilder(SEPERATOR);
        sb.ensureCapacity(SEPERATOR.length() * 25);

        for (int i = 1; i <= 6; i++) {
            appendRow(sb, i, this.getRow(i), false, this.getRowScore(i));
        }

        sb.append(SEPERATOR);

        appendTotalsRow(sb, "Total Score", this.getUpperSubtotal());
        appendTotalsRow(sb, "Bonus", this.getUpperBonus());
        appendTotalsRow(sb, "Total of Upper Section", this.getUpperTotal());

        sb.append(SEPERATOR);

        for (int i = 7; i <= 13; i++) {
            DiceValues vals = this.getRow(i);
            int score = this.getRowScore(i);
            boolean joker = score > 0 && i >= 9 && i <= 11 && vals.isYahtzee();
            appendRow(sb, i, vals, joker, score);
        }
        
        sb.append(SEPERATOR);

        appendTotalsRow(sb, "Yahtzee bonus", this.getYahtzeeBonus());
        appendTotalsRow(sb, "Total of Lower Section", this.getLowerTotal());
        appendTotalsRow(sb, "Total of Upper Section", this.getUpperTotal());
        appendTotalsRow(sb, "Grand Total", this.getGrandTotal());

        sb.append(SEPERATOR);

        return sb.toString();
    }

    /**
     * Static utility method which appends spaces to a string builder
     * @param sb string builder
     * @param count the number of spaces to append
     */
    public static void appendSpaces(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
    }

    /**
     * Static utility method which appends a row from the scorecard to a string builder
     * @param sb string builder
     * @param row row number
     * @param vals dice values
     * @param joker whether the row is a joker
     * @param score the score for the row
     */
    public static void appendRow(StringBuilder sb, int row, DiceValues vals, boolean joker, int score) {
        String rowNumStr = Integer.toString(row);
        sb.append(rowNumStr);
        sb.append(')');
        appendSpaces(sb, NUMBER_COLUMN_WIDTH - rowNumStr.length() - 1);

        String rowName = ROWS_BY_NUMBER.get(row);
        sb.append(rowName);
        appendSpaces(sb, NAME_COLUMN_WIDTH - rowName.length());
        
        if (vals == null) {
            sb.append(DiceValues.NULL_STRING);
        } else {
            sb.append(vals.toString());
        }

        if (joker) {
            sb.append(JOKER);
        } else {
            appendSpaces(sb, JOKER_COLUMN_WIDTH);
        }

        String scoreStr = Integer.toString(score);
        appendSpaces(sb, TOTALS_COLUMN_WIDTH - scoreStr.length());
        sb.append(score);
        sb.append('\n');
    }

    /**
     * Static utility method which adds a "totals" row to the scorecard string builder.
     * @param sb the string builder
     * @param label the label for the totals row
     * @param total the total score for the row
     */
    public static void appendTotalsRow(StringBuilder sb, String label, int total) {
        String totalStr = Integer.toString(total);
        sb.append(label);
        appendSpaces(sb, ALL_COLUMNS_WIDTH - label.length() - totalStr.length());
        sb.append(totalStr);
        sb.append('\n');
    }


    /**
     * Default instance method for calculating the score of the passed row number
     * @param rowNumber the row number whose score to calculate
     * @return the score for rowNumber
     * @throws IllegalArgumentException if the rowNumber is less than 1 or greater than 13
     */
    default public int getRowScore(int rowNumber) throws IllegalArgumentException {
        if (rowNumber < 1 || rowNumber > 13) {
            throw new IllegalArgumentException("rowNumber must be between 1 and 13: " + rowNumber);
        }
        
        DiceValues vals = getRow(rowNumber);
        if (vals == null) return 0;

        if (rowNumber <= 6) {
            return vals.countValuesWhichEqual((byte)rowNumber) * rowNumber;

        } else switch (rowNumber) {
            case 7: // 3 of a kind
            case 8: // 4 of a kind
                if (vals.hasNOfAKind(rowNumber - 4)) {
                    return vals.sumValues();
                } else {
                    return 0;
                }

            case 9: // Full house (or joker)
                return vals.isFullHouse() || vals.isYahtzee() ? 25 : 0;
                
            case 10: // Small straight (or joker)
                return vals.isSmallStraight() || vals.isYahtzee() ? 30 : 0;

            case 11: // Large straight (or joker)
                return vals.isLargeStraight() || vals.isYahtzee() ? 40 : 0;
                
            case 12: // Yahtzee
                return vals.isYahtzee() ? 50 : 0;
            
            case 13: // Chance
                return vals.sumValues();

            default:
                throw new IllegalArgumentException("rowNumber must be between 1 and 13: " + rowNumber);
        }
    }

    /**
     * To be called only when submitting diceValues equivilent to a Yahtzee, to enforce the
     * "forced joker" rule
     * 
     * @param sharedDiceValue the value which all dice have in the current "Yahtzee" roll
     * @return the error string if forced joker rule was not obeyed, otherwise null
     */
    default public String forcedJokerCheck(int rowNumber, byte sharedDiceValue) {
        if (this.getYahtzee() == null) {
            return null;
        }
        
        if (this.getRow(sharedDiceValue) == null) {
            if (rowNumber != sharedDiceValue) {
                return "Forced Joker Rule: You must play a second Yahtzee to the appropriate upper row if available";
            }
        } else if (rowNumber < 6) {
            //confirm that all the lower slots are taken
            for (int i = 7; i <= 13; i++) {
                if (this.getRow(i) != null) {
                    return "Force Joker Rule: If the appropriate upper section is taken, but a lower section is available, you must play a second Yahtzee to a lower section as a joker";
                }
            }
        }

        return null;
    }

    /**
     * Default instance method for calculating the upper subtotal (before including the bonus line)
     * @return
     */
    default public int getUpperSubtotal() {
        int sum = 0;
        for (int i = 1; i <= 6; i++) {
            sum += getRowScore(i);
        }
        return sum;
    }

    /**
     * Default instance method for the upper half bonus
     * @return
     */
    default public int getUpperBonus() {
        return this.getUpperSubtotal() >= 63 ? 35 : 0;
    }

    /**
     * Default instance method for calculating the upper section total, including the bonus line
     * @return
     */
    default public int getUpperTotal() {
        return this.getUpperSubtotal() + this.getUpperBonus();
    }

    /**
     * Default instance method for calculating the lower section total
     * @return
     */
    default public int getLowerTotal() {
        int sum = 0;
        for (int i = 7; i <= 13; i++) {
            sum += getRowScore(i);
        }
        return sum;
    }

    /**
     * Default instance method for calculating the grand total of the entire scorecard
     * @return
     */
    default public int getGrandTotal() {
        return getUpperTotal() + getLowerTotal();
    }


    /*
     * The below are extra methods for getting the dice values and score of each of the rows, using the row name rather
     * than an index number.  These are not really used anywhere in the existing code, but I included them in case they
     * would be helpful.
     */

    default public DiceValues getAces() {
        return getRow(1);
    }

    default public DiceValues getTwos() {
        return getRow(2);
    }

    default public DiceValues getThrees() {
        return getRow(3);
    }

    default public DiceValues getFours() {
        return getRow(4);
    }

    default public DiceValues getFives() {
        return getRow(5);
    }

    default public DiceValues getSixes() {
        return getRow(6);
    }

    default public DiceValues getThreeOfAKind() {
        return getRow(7);
    }

    default public DiceValues getFourOfAKind() {
        return getRow(8);
    }

    default public DiceValues getFullHouse() {
        return getRow(9);
    }

    default public DiceValues getSmallStraight() {
        return getRow(10);
    }

    default public DiceValues getLargeStraight() {
        return getRow(11);
    }

    default public DiceValues getYahtzee() {
        return getRow(12);
    }

    default public DiceValues getChance() {
        return getRow(13);
    }

    default public int getAcesScore() {
        return getRowScore(1);
    }

    default public int getTwosScore() {
        return getRowScore(2);
    }

    default public int getThreesScore() {
        return getRowScore(3);
    }

    default public int getFoursScore() {
        return getRowScore(4);
    }

    default public int getFivesScore() {
        return getRowScore(5);
    }

    default public int getSixesScore() {
        return getRowScore(6);
    }

    default public int getThreeOfAKindScore() {
        return getRowScore(7);
    }

    default public int getFourOfAKindScore() {
        return getRowScore(8);
    }

    default public int getFullHouseScore() {
        return getRowScore(9);
    }

    default public int getSmallStraightScore() {
        return getRowScore(10);
    }

    default public int getLargeStraightScore() {
        return getRowScore(11);
    }

    default public int getYahtzeeScore() {
        return getRowScore(12);
    }

    default public int getChanceScore() {
        return getRowScore(13);
    }

    /**
     * @return whether the scorecard has been fully filled out
     */
    default public boolean isFinished() {
        for (int i = 1; i <= 13; i++) {
            if (this.getRow(i) == null) return false;
        }
        return true;
    }
}
