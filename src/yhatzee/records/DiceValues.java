package yhatzee.records;

import java.util.*;

import yhatzee.interfaces.*;

/**
 * Represents an immutable array of 5 dice values, that may be sorted or not.  If the "sorted" boolean
 * is passed to the constructor as true, the passed array of dice values will be cloned then sorted before
 * being stored.
 *  
 * Typically the scorecard values are stored in sorted order, while the values during the round are not.
 * 
 */
public record DiceValues(byte[] values, boolean sorted) {
    /**
     * replacement for toString when a DiceValues field is null
     */
    public static final String NULL_STRING = "- - - - - ";

    public DiceValues(byte[] values, boolean sorted) {
        if (values == null || values.length != 5) {
            throw new IllegalArgumentException("Illegal values array");
        }
        this.values = values.clone();
        if (sorted) {
            // sorting the array helps with comparison operations
            Arrays.sort(this.values);
        }
        this.sorted = sorted;
    }

    public DiceValues(byte[] values) {
        this(values, false);
    }

    public DiceValues(DiceValues diceValues, boolean sorted) {
        this(diceValues.values, sorted);
    }

    public DiceValues(DiceValues diceValues) {
        this(diceValues.values, diceValues.sorted);
    }

    public DiceValues(List<Die> dice, boolean sorted) {
        this(new byte[] {
            dice.get(0).getValue(),
            dice.get(1).getValue(),
            dice.get(2).getValue(),
            dice.get(3).getValue(),
            dice.get(4).getValue()
        }, sorted);
    }

    public DiceValues(List<Die> dice) {
        this(dice, false);
    }

    public byte get(int index) {
        return this.values[index];
    }

    public byte[] values() {
        return this.values.clone();
    }

    public String toString() {
        return values[0] + " " + values[1] + " " + values[2] + " "
                + values[3] + " " + values[4] + " ";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DiceValues other)) return false;
        return this.values[0] == other.values[0] && this.values[1] == other.values[1] && 
        this.values[2] == other.values[2] && this.values[3] == other.values[3] && 
        this.values[4] == other.values[4];
    }

    @Override
    public int hashCode() {
        int hash = (int)values[0];
        for (int i = 1; i < 5; i++) {
            hash <<= 3; // storing values from 1 - 6 only requires 3 bits
            hash |= values[i];
        }
        return hash;
    }

    /**
     * Used by upper section of scorecard
     * @param mustEqual
     * @return the number of dice which equal the passed value
     */
    public int countValuesWhichEqual(byte mustEqual) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (values[i] == mustEqual) count++;
        }
        return count;
    }

    /**
     * @return the sum of the dice values
     */
    public int sumValues() {
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += values[i];
        }
        return sum;
    }

    /**
     * Checks if values have at least N of a kind, where N is passed as a parameter.
     * For example, to check for 3 of a kind, the values would need at least 3
     * dice with the same value.
     * 
     * @param n
     * @return
     */
    public boolean hasNOfAKind(int n) {
        for (int i = 0; i < 6 - n; i++) {
            int count = 1;
            for (int j = i + 1; j < 5 && count < n; j++) {
                if (values[j] == values[i]) count++;
            }
            if (count >= n) return true;
        }
        return false;
    }

    /**
     * @return whether these dice values constitute a yahtzee
     */
    public boolean isYahtzee() {
        return this.hasNOfAKind(5);
    }

    /**
     * 
     * @return whether these dice values constitute a full house
     * @throws UnsupportedOperationException if the values array is not sorted
     */
    public boolean isFullHouse() throws UnsupportedOperationException {
        if (!this.sorted) {
            throw new UnsupportedOperationException("values array must be sorted for hasFullHouse() method");
        }
        if (values[0] == values[1] && values[3] == values[4]) {
            // xor on the middle comparisons ensures there are 2 of a kind AND 3 of a kind
            // and that they are distinct from each other
            if ((values[1] == values[2]) ^ (values[2] == values[3])) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether these dice values constitute a large straight
     * @throws UnsupportedOperationException if the values array is not sorted
     */
    public boolean isLargeStraight() {
        if (!this.sorted) {
            throw new UnsupportedOperationException("values array must be sorted for hasStraight() method");
        }
        for (int i = 0; i < 4; i++) {
            if (this.values[i] + 1 != this.values[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return whether these dice values constitute a small straight
     */
    public boolean isSmallStraight() {
        boolean hasNumber[] = new boolean[6];
        for (int i = 0; i < 5; i++) {
            hasNumber[this.values[i] - 1] = true;
        }

        int runLen = 0;
        for (int i = 0; i < 6; i++) {
            if (hasNumber[i]) {
                runLen++;
            } else if (runLen >= 4) {
                return true;
            } else {
                runLen = 0;
            }
        }
        return false;
    }
}