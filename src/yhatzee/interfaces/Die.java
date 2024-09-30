package yhatzee.interfaces;

/**
 * Represents a single Die
 */
public interface Die {
    /**
     * Roll the die
     */
    public void roll();

    /**
     * @return the value from the most recent roll
     */
    public byte getValue();
}
