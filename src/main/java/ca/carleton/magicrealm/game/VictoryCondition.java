package ca.carleton.magicrealm.game;

import java.io.Serializable;

/**
 * A player's individual victory condition data.
 * <p/>
 * Created with IntelliJ IDEA.
 * Date: 14/02/15
 * Time: 10:27 AM
 */
public class VictoryCondition implements Serializable {

    private static final long serialVersionUID = 7930283393149953235L;

    public enum Conditions {
        GOLD,
        NOTORIETY,
        FAME,
        SPELLS_COUNT,
        GREAT_TREASURES_COUNT
    }

    private int gold;

    private int notoriety;

    private int fame;

    private int spellsCount;

    private int greatTreasuresCount;

    public void addGold(final int gold) {
        this.gold += gold;
    }

    public void addNotoriety(final int notoriety) {
        this.notoriety += notoriety;
    }

    public void addFame(final int fame) {
        this.fame += fame;
    }

    public void addSpells(final int spells) {
        this.spellsCount += spells;
    }

    public void addGreatTreasures(final int treasures) {
        this.greatTreasuresCount += treasures;
    }

    public int getGold() {
        return this.gold;
    }

    public int getNotoriety() {
        return this.notoriety;
    }

    public int getFame() {
        return this.fame;
    }

    public int getSpellsCount() {
        return this.spellsCount;
    }

    public int getGreatTreasuresCount() {
        return this.greatTreasuresCount;
    }

}
