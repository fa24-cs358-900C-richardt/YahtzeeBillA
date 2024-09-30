package yhatzee.interfaces;

import java.util.*;

public interface Game extends TurnIterator {
    public int getCurrentRound();
    public List<Player> getFinalRanking() throws IllegalStateException;
}
