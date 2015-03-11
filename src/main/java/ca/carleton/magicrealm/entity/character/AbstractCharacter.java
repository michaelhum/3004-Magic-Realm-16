package ca.carleton.magicrealm.entity.character;

import ca.carleton.magicrealm.entity.Entity;
import ca.carleton.magicrealm.entity.Interactable;
import ca.carleton.magicrealm.entity.Relationship;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a playable character within the Magic Realm.
 * <p/>
 * Created with IntelliJ IDEA.
 * Date: 10/02/15
 * Time: 8:25 AM
 */
public abstract class AbstractCharacter extends Entity implements Serializable {

    private int currentGold;

    private int currentNotoriety;

    private int currentFame;

    private int currentSpellsCount;

    private int currentGreatTreasuresCount;

    private boolean blocked;

    /**
     * All characters start hidden.
     */ {
        this.setHidden(true);
        this.setBlocked(false);
    }

    /**
     * The relationships this entity has with other entities.
     */
    protected Map<Interactable, Relationship> relationships = new HashMap<Interactable, Relationship>();

    /**
     * Find the relationship with another interactable entity.
     *
     * @param interactable The given entity to find the relationship with.
     * @return The relationship.
     */
    public Relationship getRelationshipWith(final Interactable interactable) {
        return this.relationships.get(interactable);
    }

    public void addRelationship(final Interactable entity, final Relationship relationship) {
        this.relationships.putIfAbsent(entity, relationship);
    }

    public int getCurrentGreatTreasuresCount() {
        return this.currentGreatTreasuresCount;
    }

    public void addGreatTreasure(int currentGreatTreasuresCount) {
        this.currentGreatTreasuresCount += currentGreatTreasuresCount;
    }

    public int getCurrentGold() {
        return this.currentGold;
    }

    public void addGold(int currentGold) {
        this.currentGold += currentGold;
    }

    public int getCurrentNotoriety() {
        return this.currentNotoriety;
    }

    public void addNotoriety(int currentNotoriety) {
        this.currentNotoriety += currentNotoriety;
    }

    public int getCurrentFame() {
        return this.currentFame;
    }

    public void addFame(int currentFame) {
        this.currentFame += currentFame;
    }

    public int getCurrentSpellsCount() {
        return this.currentSpellsCount;
    }

    public void addCurrentSpells(int currentSpellsCount) {
        this.currentSpellsCount += currentSpellsCount;
    }

    @Override
    public boolean equals(final Object rhs) {
        return rhs instanceof AbstractCharacter && this.getEntityInformation() == ((AbstractCharacter) rhs).getEntityInformation();
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    public void setBlocked(final boolean blocked) {
        this.blocked = blocked;
    }
}
