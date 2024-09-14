package yhatzee;

import yhatzee.interfaces.Player;
import yhatzee.interfaces.Scorecard;

public class HumanPlayer implements Player {
    private final String name;
    private Scorecard scorecard;
    
    public HumanPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void newScorecard() {
        this.scorecard = new FairScorecard();
    }

    public Scorecard getScorecard() {
        return scorecard;
    }
}
