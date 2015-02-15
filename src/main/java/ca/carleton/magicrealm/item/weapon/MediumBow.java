package ca.carleton.magicrealm.item.weapon;

import ca.carleton.magicrealm.item.ItemInformation;

/**
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 8:55 AM
 */
public class MediumBow extends AbstractWeapon {

    public MediumBow() {
        this.goldValue = 8;
        this.length = 16;
        this.attackType = AttackType.MISSILE;
    }

    @Override
    public ItemInformation getItemInformation() {
        return ItemInformation.MEDIUM_BOW;
    }
}
