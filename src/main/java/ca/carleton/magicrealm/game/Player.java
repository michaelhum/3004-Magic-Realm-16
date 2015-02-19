package ca.carleton.magicrealm.game;

import ca.carleton.magicrealm.GUI.tile.Clearing;
import ca.carleton.magicrealm.entity.character.AbstractCharacter;

/**
 * Represents the player and their various attributes and decisions made within the game..
 * <p/>
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 10:23 AM
 */
public class Player {

    /**
     * The character the player is playing.
     */
    private AbstractCharacter character;

    /**
     * The victory conditions for the player.
     */
    private VictoryCondition victoryCondition;

    /**
     * The clearing the player is currently located on.
     */
    private Clearing currentClearing;

    /**
     * The clearing the player may want to move to.
     */
    private Clearing moveTarget;

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

    public Clearing getCurrentClearing() {
        return this.currentClearing;
    }

    public void setCurrentClearing(final Clearing currentClearing) {
        this.currentClearing = currentClearing;
    }

    public Clearing getMoveTarget() {
        return this.moveTarget;
    }

    public void setMoveTarget(final Clearing moveTarget) {
        this.moveTarget = moveTarget;
    }

}
