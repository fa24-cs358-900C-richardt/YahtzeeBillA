package yhatzee;

import yhatzee.interfaces.DiceValues;
import yhatzee.interfaces.Scorecard;

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
        return this.rows[rowNumber];
    }

    @Override
    public int getYahtzeeBonus() {
        return this.yahtzeeBonus;
    }

    @Override
    public void setRow(int rowNumber, DiceValues diceValues) {
        if (this.rows[rowNumber] != null) {
            throw new IllegalArgumentException("Cannot overwrite an already established row");
        }
        if (diceValues == null)  {
            throw new NullPointerException("diceValues must be non-null");
        }

    
        if (diceValues.isYahtzee()) {
            // Forced joker rule checks
            if (this.getYahtzee() != null) {
                if (this.getRow(diceValues.get(0)) == null) {
                    if (rowNumber != diceValues.get(0)) {
                        throw new IllegalArgumentException("Forced Joker Rule: You must play this to the appropriate upper row if available");
                    }
                } else if (rowNumber < 6) {
                    //confirm that all the lower slots are taken
                    for (int i = 7; i <= 13; i++) {
                        if (this.rows[i] != null) {
                            throw new IllegalArgumentException("Force Joker Rule: If the appropriate upper section is taken, but a lower section is available, you must play this to a lower section as a joker");
                        }
                    }

                }
            }

            if (this.getYahtzeeScore() != 0) {
                this.yahtzeeBonus += Scorecard.YHATZEE_BONUS;
                this.lowerTotal = null; // invalidate the lower total cache, even if this is going to an upper row
            }
        }

        this.rows[rowNumber] = diceValues;

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
}
