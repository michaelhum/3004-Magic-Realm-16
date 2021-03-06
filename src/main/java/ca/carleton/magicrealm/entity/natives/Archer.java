package ca.carleton.magicrealm.entity.natives;

import ca.carleton.magicrealm.entity.EntityInformation;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.Harm;
import ca.carleton.magicrealm.item.weapon.MediumBow;

/**
 * Created with IntelliJ IDEA.
 * Date: 16/02/15
 * Time: 12:45 PM
 */
public class Archer extends AbstractNative {

    protected Archer(final NativeFaction nativeType) {
        this.vulnerability = Harm.MEDIUM;
        this.weapon = new MediumBow();
        this.basicGoldWage = 2;

        this.faction = nativeType;
        this.moveStrength = Harm.MEDIUM;
    }

    @Override
    public void addBountyToPlayer(final Player player) {
        player.getCharacter().addGold(2);
        player.getCharacter().addNotoriety(4);
    }

    @Override
    public EntityInformation getEntityInformation() {
        return EntityInformation.NATIVE_ARCHER;
    }
}

