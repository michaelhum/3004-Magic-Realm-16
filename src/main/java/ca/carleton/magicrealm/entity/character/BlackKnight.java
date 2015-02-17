package ca.carleton.magicrealm.entity.character;

import ca.carleton.magicrealm.entity.EntityInformation;
import ca.carleton.magicrealm.entity.Relationship;
import ca.carleton.magicrealm.entity.Vulnerability;
import ca.carleton.magicrealm.entity.natives.NativeFaction;
import ca.carleton.magicrealm.item.armor.Shield;
import ca.carleton.magicrealm.item.armor.SuitOfArmor;
import ca.carleton.magicrealm.item.weapon.Mace;

/**
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 6:43 PM
 */
public class BlackKnight extends AbstractCharacter {

    protected BlackKnight() {
        this.vulnerability = Vulnerability.MEDIUM;

        this.addItem(new Mace());
        this.addItem(new SuitOfArmor());
        this.addItem(new Shield());

        this.addRelationship(NativeFaction.COMPANY, Relationship.ALLY);
        this.addRelationship(NativeFaction.SOLDIERS, Relationship.FRIENDLY);
        this.addRelationship(NativeFaction.LANCERS, Relationship.UNFRIENDLY);
        this.addRelationship(NativeFaction.GUARD, Relationship.ENEMY);

    }

    @Override
    public EntityInformation getEntityInformation() {
        return EntityInformation.CHARACTER_BLACK_KNIGHT;
    }
}
