package ca.carleton.magicrealm.item.weapon;

import ca.carleton.magicrealm.item.Item;

/**
 * A weapon carried by entities or looted from the game board.
 * <p/>
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 8:53 AM
 */
public abstract class AbstractWeapon extends Item {

    protected AttackType attackType;

    protected int length;

    public AttackType getAttackType() {
        return this.attackType;
    }

    public int getLength() {
        return this.length;
    }
}
