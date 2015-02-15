package ca.carleton.magicrealm.game;

import ca.carleton.magicrealm.entity.character.AbstractCharacter;

/**
 * Represents the player and their attributes.
 * <p/>
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 10:23 AM
 */
public class Player {

    private AbstractCharacter character;

    private VictoryCondition victoryCondition;

    public VictoryCondition getVictoryCondition() {
        return this.victoryCondition;
    }

}
