package ca.carleton.magicrealm.entity.monster;

import ca.carleton.magicrealm.entity.EntityInformation;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.Harm;

/**
 * Created by Tony on 31/03/2015.
 */
public class GiantBat extends AbstractMonster {

    public GiantBat() {
        this.vulnerability = Harm.HEAVY;
    }

    @Override
    public EntityInformation getEntityInformation() {
        return EntityInformation.GIANTBAT;
    }

    @Override
    public void addBountyToPlayer(final Player player) {
        player.getCharacter().addFame(3);
        player.getCharacter().addNotoriety(3);
    }
}
