package ca.carleton.magicrealm.GUI.phaseselector;

import ca.carleton.magicrealm.GUI.tile.Clearing;
import ca.carleton.magicrealm.entity.Entity;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.chit.ActionChit;
import ca.carleton.magicrealm.game.phase.AbstractPhase;
import ca.carleton.magicrealm.game.phase.impl.*;
import ca.carleton.magicrealm.item.Item;
import ca.carleton.magicrealm.item.weapon.AbstractWeapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 22/02/15
 * Time: 7:02 PM
 */
public class PhaseSelectorModel {

    private final List<AbstractPhase> phases;
    private final ArrayList<String> extraPhases;
    private final PhaseSelectorMenu menu;

    public PhaseSelectorModel(final PhaseSelectorMenu menu, final List<AbstractPhase> phases, Player player) {
        this.extraPhases = player.getCharacter().getExtraPhases();
        this.phases = phases;
        this.menu = menu;
    }

    public void addMovementPhase(final Clearing clearing, final Clearing origin) {
        final MovePhase movePhase = new MovePhase();
        movePhase.setMoveTarget(clearing);
        movePhase.setOrigin(origin);
        this.phases.add(movePhase);
    }

    public void addTradePhase(final Entity tradeTarget, final Item tradedItem, final boolean selling, final boolean isDrinksBought, final Clearing currentClearing) {
        final TradePhase tradePhase = new TradePhase();
        tradePhase.setTradeTarget(tradeTarget);
        tradePhase.setItemToTrade(tradedItem);
        tradePhase.setSelling(selling);
        tradePhase.setDrinksBought(isDrinksBought);
        tradePhase.setCurrentClearing(currentClearing);
        this.phases.add(tradePhase);
    }

    public void addHidePhase() {
        final HidePhase hidePhase = new HidePhase();
        this.phases.add(hidePhase);
    }

    public void addAlertPhase(final AbstractWeapon weapon) {
        final AlertPhase alertPhase = new AlertPhase();
        alertPhase.setWeapon(weapon);
        this.phases.add(alertPhase);
    }

    public void addRestPhase(final ActionChit chit) {
        final RestPhase restPhase = new RestPhase();
        restPhase.setSelectedChit(chit);
        this.phases.add(restPhase);
    }

    public void done() {
        this.menu.disposeWindow();
    }

}
