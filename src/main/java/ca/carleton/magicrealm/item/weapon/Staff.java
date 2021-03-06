package ca.carleton.magicrealm.item.weapon;

import ca.carleton.magicrealm.game.combat.Harm;
import ca.carleton.magicrealm.item.ItemInformation;

/**
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 9:14 AM
 */
public class Staff extends AbstractWeapon {

    public Staff() {
        this.goldValue = 1;
        this.length = 9;
        this.attackType = AttackType.STRIKING;
        this.strength = Harm.LIGHT;
        this.unAlertedSharpness = 0;
        this.alertedSharpness = 0;
    }

    @Override
    public ItemInformation getItemInformation() {
        return ItemInformation.STAFF;
    }
}
