package ca.carleton.magicrealm.GUI.phaseselector;

import ca.carleton.magicrealm.GUI.tile.Clearing;
import ca.carleton.magicrealm.GUI.tile.TileType;
import ca.carleton.magicrealm.entity.Entity;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.chit.ActionChit;
import ca.carleton.magicrealm.game.phase.AbstractPhase;
import ca.carleton.magicrealm.game.phase.PhaseCountBean;
import ca.carleton.magicrealm.game.phase.PhaseType;
import ca.carleton.magicrealm.game.phase.impl.*;
import ca.carleton.magicrealm.item.Item;
import ca.carleton.magicrealm.item.weapon.AbstractWeapon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 22/02/15
 * Time: 7:02 PM
 */
public class PhaseSelectorModel {

    private static final Logger LOG = LoggerFactory.getLogger(PhaseSelectorModel.class);

    private final List<AbstractPhase> phases;

    private final PhaseSelectorMenu menu;

    private final PhaseCountBean extraPhases;

    public PhaseSelectorModel(final PhaseSelectorMenu menu, final List<AbstractPhase> phases, final PhaseCountBean phaseCountBean) {
        this.extraPhases = phaseCountBean;
        this.phases = phases;
        this.menu = menu;
    }

    public void updateInfoText() {
        String infoText = String.format("<html>Number of phases available: %s.<br/>", this.extraPhases.getNumberOfPhasesFOrDay());

        final StringBuilder extraPhases = new StringBuilder();
        extraPhases.append("Extras through treasure or traits:<br/>");

        this.extraPhases.getExtraPhases().stream().forEach(phase -> extraPhases.append(" ").append(phase).append("<br/></html>"));

        infoText += extraPhases.toString();

        this.menu.phaseSelectorPanel.updateInfoText(infoText);
    }

    public void addMovementPhase(final Clearing clearing) {

        if (clearing == null) {
            LOG.error("User entered null for their move target. Discarding.");
            return;
        }

        final MovePhase movePhase = new MovePhase();
        movePhase.setMoveTarget(clearing);

        // Need to check for mountain specially.
        if (movePhase.getMoveTarget().getParentTile().getTileType() == TileType.MOUNTAIN) {
            if (!this.extraPhases.canMakeMountainMove()) {
                // Refund the phase to the player, since we can't make the move.
                LOG.info("Unable to make move phase. Requires 2 to move through a mountain, but didn't have enough!.");
                this.extraPhases.refundLast();
                LOG.info("Refunded a phase to the player as they we're unable to make the move.");
                JOptionPane.showMessageDialog(this.menu, "Unable to make move. You don't have enough phases left! (You tried to make to a mountain)");
                return;
            } else {
                if (this.extraPhases.getExtraPhases().contains(PhaseType.MOVE)) {
                    this.extraPhases.removeExtraPhase(PhaseType.MOVE);
                } else {
                    this.extraPhases.removeOne();
                }
                LOG.info("Subtracted an extra phase for mountain penalty.");
                this.updateInfoText();
            }
        } else {
            LOG.info("{} was not on a tile of type mountain (actual --> {}). Continuing normally.", movePhase.getMoveTarget(), movePhase.getMoveTarget().getParentTile().getTileType());
        }

        this.phases.add(movePhase);
        LOG.info("Added move phase.");
    }

    public void addTradePhase(final Entity tradeTarget, final Item tradedItem, final boolean selling, final boolean isDrinksBought, final Clearing currentClearing) {
        final TradePhase tradePhase = new TradePhase();
        tradePhase.setTradeTarget(tradeTarget);
        tradePhase.setItemToTrade(tradedItem);
        tradePhase.setSelling(selling);
        tradePhase.setDrinksBought(isDrinksBought);
        tradePhase.setCurrentClearing(currentClearing);
        this.phases.add(tradePhase);
        LOG.info("Added trade phase.");
    }

    public void addHidePhase(final Player player) {
        final HidePhase hidePhase = new HidePhase();
        hidePhase.setPlayer(player);
        this.phases.add(hidePhase);
        LOG.info("Added hide phase.");
    }

    public void addAlertPhase(final AbstractWeapon weapon) {
        final AlertPhase alertPhase = new AlertPhase();
        alertPhase.setWeapon(weapon);
        this.phases.add(alertPhase);
        LOG.info("Added alert phase.");
    }

    public void addRestPhase(final ActionChit chit) {
        final RestPhase restPhase = new RestPhase();
        restPhase.setSelectedChit(chit);
        this.phases.add(restPhase);
        LOG.info("Added rest phase.");
    }

    public void addSpellEnchantPhase() {
        final SpellPhase spellPhase = new SpellPhase();
        this.phases.add(spellPhase);
        LOG.info("Added spell phase.");
    }

    public void addSearchPhase(final String action) {
        final SearchPhase searchPhase = new SearchPhase();
        searchPhase.setAction(action);
        this.phases.add(searchPhase);
        LOG.info("Added search phase.");
    }

    public void done() {
        this.menu.disposeWindow();
    }

    public PhaseCountBean getPhaseCount() {
        return this.extraPhases;
    }

}
