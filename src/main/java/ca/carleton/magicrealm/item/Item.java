package ca.carleton.magicrealm.item;

import java.io.Serializable;

/**
 * Represents an item (weapon, armor, etc.) within the Magic Realm.
 * <p/>
 * Created with IntelliJ IDEA.
 * Date: 10/02/15
 * Time: 8:26 AM
 */
public abstract class Item implements Serializable {

    private static final long serialVersionUID = -6355859706461942882L;

    protected int goldValue;

    public int getGoldValue() {
        return this.goldValue;
    }

    public abstract ItemInformation getItemInformation();

    @Override
    public boolean equals(final Object rhs) {
             return rhs instanceof Item && ((Item) rhs).getItemInformation() == this.getItemInformation();
    }

    @Override
    public String toString() {
        return this.getItemInformation().getItemName();
    }

}
