package ca.carleton.magicrealm.entity.natives;

import ca.carleton.magicrealm.entity.EntityInformation;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.Harm;
import ca.carleton.magicrealm.item.weapon.ShortSword;

/**
 * Created with IntelliJ IDEA.
 * Date: 16/02/15
 * Time: 12:45 PM
 */
public class Raider extends AbstractNative {

    protected Raider(final NativeFaction nativeType) {
        this.vulnerability = Harm.LIGHT;
        this.weapon = new ShortSword();
        this.basicGoldWage = 2;

        this.faction = nativeType;
        this.moveStrength = Harm.LIGHT;
    }

    @Override
    public void addBountyToPlayer(final Player player) {
        player.getCharacter().addGold(2);
        player.getCharacter().addNotoriety(4);
    }

    @Override
    public EntityInformation getEntityInformation() {
        return EntityInformation.NATIVE_RAIDER;
    }
}

