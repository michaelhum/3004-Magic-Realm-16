package ca.carleton.magicrealm.item.weapon;

import ca.carleton.magicrealm.game.combat.Harm;
import ca.carleton.magicrealm.item.ItemInformation;

/**
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 9:22 AM
 */
public class Axe extends AbstractWeapon {

    public Axe() {
        this.goldValue = 4;
        this.length = 2;
        this.attackType = AttackType.STRIKING;
        this.strength = Harm.MEDIUM;
        this.unAlertedSharpness = 1;
        this.alertedSharpness = 1;
    }

    @Override
    public ItemInformation getItemInformation() {
        return ItemInformation.AXE;
    }
}
