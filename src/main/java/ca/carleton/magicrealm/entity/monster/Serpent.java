package ca.carleton.magicrealm.entity.monster;

import ca.carleton.magicrealm.entity.EntityInformation;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.Harm;

/**
 * Created by Tony on 31/03/2015.
 */
public class Serpent extends AbstractMonster {

    public Serpent() {
        this.vulnerability = Harm.HEAVY;
        this.isArmored = true;
    }

    @Override
    public EntityInformation getEntityInformation() {
        return EntityInformation.SERPENT;
    }

    @Override
    public void addBountyToPlayer(final Player player) {
        player.getCharacter().addFame(4);
        player.getCharacter().addNotoriety(4);
    }
}
