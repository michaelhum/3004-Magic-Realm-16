package ca.carleton.magicrealm.entity.character;

import ca.carleton.magicrealm.entity.EntityInformation;
import ca.carleton.magicrealm.entity.Relationship;
import ca.carleton.magicrealm.entity.Vulnerability;
import ca.carleton.magicrealm.entity.natives.NativeFaction;
import ca.carleton.magicrealm.entity.natives.NativeType;
import ca.carleton.magicrealm.item.weapon.ThrustingSword;

/**
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 6:46 PM
 */
public class Swordsman extends AbstractCharacter {

    protected Swordsman() {
        this.vulnerability = Vulnerability.LIGHT;

        this.addItem(new ThrustingSword());

        this.addRelationship(NativeFaction.ROGUES, Relationship.FRIENDLY);
        this.addRelationship(NativeFaction.COMPANY, Relationship.FRIENDLY);
        this.addRelationship(NativeFaction.PATROL, Relationship.ENEMY);
    }

    @Override
    public EntityInformation getEntityInformation() {
        return EntityInformation.CHARACTER_SWORDSMAN;
    }
}
