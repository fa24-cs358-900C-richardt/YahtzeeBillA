package yhatzee;

import yhatzee.interfaces.Scorecard;
import yhatzee.records.DiceValues;

public class FairScorecard implements Scorecard {
    private DiceValues[] rows = new DiceValues[13];
    private int yahtzeeBonus = 0;

    // caches to prevent needless recalculation
    private Integer upperSubtotal;
    private Integer upperTotal;
    private Integer lowerTotal;
    private Integer grandTotal;

    @Override
    public DiceValues getRow(int rowNumber) throws IndexOutOfBoundsException {
        return this.rows[rowNumber - 1]; // row numbers are 1-indexed
    }

    @Override
    public int getYahtzeeBonus() {
        return this.yahtzeeBonus;
    }

    @Override
    public void setRow(int rowNumber, DiceValues diceValues) {
        if (this.rows[rowNumber - 1] != null) {
            throw new IllegalArgumentException("Cannot overwrite an already established row");
        }
        if (diceValues == null)  {
            throw new NullPointerException("diceValues must be non-null");
        }

    
        if (diceValues.isYahtzee()) {
            // Forced joker rule checks
            String forcedJokerCheck = forcedJokerCheck(rowNumber, diceValues.get(0));
            if (forcedJokerCheck != null) throw new IllegalArgumentException(forcedJokerCheck);


            if (this.getYahtzeeScore() != 0) {
                this.yahtzeeBonus += Scorecard.YHATZEE_BONUS;
                this.lowerTotal = null; // invalidate the lower total cache, even if this is going to an upper row
            }
        }

        this.rows[rowNumber - 1] = new DiceValues(diceValues, true);

        //invalidate the cached totals
        if (rowNumber <= 6) {
            this.upperSubtotal = null;
            this.upperTotal = null;

        } else {
            this.lowerTotal = null;
        }

        this.grandTotal = null;
    }

    @Override
    public int getUpperSubtotal() {
        if (this.upperSubtotal == null) {
            this.upperSubtotal = Scorecard.super.getUpperSubtotal();
        }
        return this.upperSubtotal;
    }
    

    @Override
    public int getUpperTotal() {
        if (this.upperTotal == null) {
            this.upperTotal = Scorecard.super.getUpperTotal();
        }
        return this.upperTotal;
    }

    @Override
    public int getLowerTotal() {
        if (this.lowerTotal == null) {
            this.lowerTotal = Scorecard.super.getLowerTotal();
        }
        return this.lowerTotal;
    }

    @Override
    public int getGrandTotal() {
        if (this.grandTotal == null) {
            this.grandTotal = Scorecard.super.getGrandTotal();
        }
        return this.grandTotal;
    }

    public String toString() {
        return this.string();
    }
}
