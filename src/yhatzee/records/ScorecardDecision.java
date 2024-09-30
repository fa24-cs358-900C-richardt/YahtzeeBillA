package yhatzee.records;

import yhatzee.interfaces.*;

public record ScorecardDecision(int scorecardRow) implements Decision {
    public ScorecardDecision(int scorecardRow) {
        if (scorecardRow < 1 || scorecardRow > 13) {
            throw new IllegalArgumentException("scorecardRow must be between 1 and 13: " + scorecardRow);
        }
        this.scorecardRow = scorecardRow;
    }

    public int getScorecardRowDecision() {
        return this.scorecardRow;
    }
}
