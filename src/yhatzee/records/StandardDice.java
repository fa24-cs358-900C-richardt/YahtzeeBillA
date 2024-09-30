package yhatzee.records;

import yhatzee.interfaces.*;
import java.util.*;

/**
 * Concrete implementation of the Dice interface, using an immutable List of 5 dice
 */
public record StandardDice (List<Die> dice) implements Dice {
    public StandardDice(List<Die> dice) {
        if (dice == null || dice.size() != 5) {
            throw new IllegalArgumentException("Illegal dice array");
        }
        this.dice = List.of(dice.get(0), dice.get(1), dice.get(2), dice.get(3), dice.get(4));
    }

    @Override
    public void rollAllDice() {
        for (Die die : dice) {
            die.roll();
        }
    }

    @Override
    public void rollSomeDice(Decision decision) {
        if (decision.getDieDecision(0)) dice.get(0).roll();
        if (decision.getDieDecision(1)) dice.get(1).roll();
        if (decision.getDieDecision(2)) dice.get(2).roll();
        if (decision.getDieDecision(3)) dice.get(3).roll();
        if (decision.getDieDecision(4)) dice.get(4).roll();
    }

    @Override
    public DiceValues getDiceValues() {
        return new DiceValues(this.dice);
    }

    @Override
    public byte getDieValue(int index) {
        return this.dice.get(index).getValue();
    }    
}
