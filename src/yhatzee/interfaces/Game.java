package yhatzee.interfaces;

import java.util.*;

public interface Game extends TurnEngine {
    public int getCurrentRound();
    public List<Player> getFinalRanking() throws IllegalStateException;
}
