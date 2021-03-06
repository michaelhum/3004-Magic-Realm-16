package ca.carleton.magicrealm.game;

import ca.carleton.magicrealm.GUI.tile.Discoverable;
import ca.carleton.magicrealm.entity.character.AbstractCharacter;
import ca.carleton.magicrealm.entity.chit.Dwelling;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player and their various attributes and decisions made within the game..
 * <p/>
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 10:23 AM
 */
public class Player implements Serializable {

    private static final long serialVersionUID = -4131876579835520249L;

    /**
     * The character the player is playing.
     */
    private AbstractCharacter character;

    /**
     * The victory conditions for the player.
     */
    private VictoryCondition victoryCondition;

    /**
     * Where the player starts. Set during character create..
     */
    private Dwelling startingLocation;

    /**
     * List of things that the player has found
     */
    private List<Discoverable> discoveredThings = new ArrayList<>();

    private int restarts = 0;

    public Player() {
        this.victoryCondition = new VictoryCondition();
    }

    public VictoryCondition getVictoryCondition() {
        return this.victoryCondition;
    }

    public AbstractCharacter getCharacter() {
        return this.character;
    }


    public void setCharacter(final AbstractCharacter character) {
        this.character = character;
    }

    public void setVictoryCondition(final VictoryCondition victoryCondition) {
        this.victoryCondition = victoryCondition;
    }

    @Override
    public boolean equals(Object rhs) {
        return rhs instanceof Player && this.getCharacter().getEntityInformation() == ((Player) rhs).getCharacter().getEntityInformation();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 13).append(this.character).append(this.victoryCondition).build();
    }

    public Dwelling getStartingLocation() {
        return this.startingLocation;
    }

    public void setStartingLocation(final Dwelling startingLocation) {
        this.startingLocation = startingLocation;
    }

    public int getRestarts() {
        return this.restarts;
    }

    public void restartNewLife() {
        this.restarts += 1;
    }

    public List<Discoverable> getDiscoveredThings() {
        return discoveredThings;
    }
}
