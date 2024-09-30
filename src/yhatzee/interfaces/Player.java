package yhatzee.interfaces;

import yhatzee.records.*;

public interface Player {
    public String getName();
    public void setScorecard(Scorecard scorecard);
    public Scorecard getScorecard();

    public Decision makeFirstRollDecision(DiceValues diceValues);
    public Decision makeSecondRollDecision(DiceValues diceValues);
    public ScorecardDecision makeThirdRollDecision(DiceValues diceValues);
}
