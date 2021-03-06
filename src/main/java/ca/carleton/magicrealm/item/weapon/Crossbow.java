package ca.carleton.magicrealm.item.weapon;

import ca.carleton.magicrealm.game.combat.Harm;
import ca.carleton.magicrealm.item.ItemInformation;

/**
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 8:58 AM
 */
public class Crossbow extends AbstractWeapon {

    public Crossbow() {
        this.goldValue = 10;
        this.length = 12;
        this.attackType = AttackType.MISSILE;
        this.strength = Harm.HEAVY;
        this.unAlertedSharpness = 0;
        this.alertedSharpness = 1;
    }

    @Override
    public ItemInformation getItemInformation() {
        return ItemInformation.CROSSBOW;
    }
}
