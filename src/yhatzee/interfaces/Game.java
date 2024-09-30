package yhatzee.interfaces;

import java.util.*;

public interface Game extends DiceEngine {
    public int getCurrentRound();
    public List<Player> getFinalRanking() throws IllegalStateException;
}
