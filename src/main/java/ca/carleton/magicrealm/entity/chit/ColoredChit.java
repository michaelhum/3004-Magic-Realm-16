package ca.carleton.magicrealm.entity.chit;

/**
 * Represents one of the colored (gold, red, yellow) chits to be displayed on the map.
 *
 * Created with IntelliJ IDEA.
 * Date: 21/02/15
 * Time: 12:00 PM
 */
public abstract class ColoredChit {

    /**
     * The clearing number to place the chit in.
     */
    protected int clearingNumber;

    /**
     * The string in the rules that identifies the chit.
     */
    protected String description;

    /**
     * Whether or not it has been found.
     */
    protected boolean hidden = false;

    /**
     * The color of the chit.
     */
    public abstract ChitColor getChitColor();

    public int getClearingNumber() {
        return this.clearingNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void reveal() {
        this.hidden = true;
    }

}
