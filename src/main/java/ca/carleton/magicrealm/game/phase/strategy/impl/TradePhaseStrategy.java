package ca.carleton.magicrealm.game.phase.strategy.impl;

import ca.carleton.magicrealm.entity.Relationship;
import ca.carleton.magicrealm.entity.character.AbstractCharacter;
import ca.carleton.magicrealm.entity.natives.AbstractNative;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.phase.AbstractPhase;
import ca.carleton.magicrealm.game.phase.PhaseType;
import ca.carleton.magicrealm.game.phase.impl.TradePhase;
import ca.carleton.magicrealm.game.phase.strategy.PhaseStrategy;
import ca.carleton.magicrealm.game.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Note that this does not do any error checking. That is, gold values are assumed to be correct. As with move phases,
 * it is up to the player to ensure they have the correct amount.
 * <p>
 * Created with IntelliJ IDEA.
 * Date: 11/03/15
 * Time: 4:43 PM
 */
public class TradePhaseStrategy implements PhaseStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(TradePhaseStrategy.class);

    @Override
    public boolean appliesTo(final AbstractPhase phase) {
        return phase.getPhaseType() == PhaseType.TRADE;
    }

    @Override
    public void doPhase(final Player player, final AbstractPhase phase) {
        final TradePhase trade = (TradePhase) phase;

        if (!trade.getCurrentClearing().getEntities().contains(trade.getTradeTarget())) {
            LOG.info("Played entered invalid target for his trade phase. Not executed. (Tried {}).", trade.getTradeTarget());
            return;
        }

        // If we're selling our item to the native.
        if (trade.isSelling()) {
            player.getCharacter().removeItem(trade.getItemToTrade());
            player.getCharacter().addGold(trade.getItemToTrade().getGoldValue());
            trade.getTradeTarget().addItem(trade.getItemToTrade());
            LOG.info("Removed {} from {}'s inventory.", trade.getItemToTrade(), player.getCharacter().getEntityInformation().convertToCharacterType());
            LOG.info("Added {} to {}'s inventory.", trade.getItemToTrade(), trade.getTradeTarget().getEntityInformation());
        } else {

            // whether or not to trade.
            boolean doTrade = false;
            // the basic gold value.
            int goldValueOfItem = trade.getItemToTrade().getGoldValue();

            // If trading with a character
            if (trade.getTradeTarget() instanceof AbstractCharacter) {
                doTrade = true;
            } else {

                // Get roll from the meeting table for trading with native or visitor...
                // Also allows cheating.
                int meetingTableRoll;
                if (trade.override == 0) {
                    meetingTableRoll = Table.MeetingTable.roll(player, trade.getCurrentClearing());
                    if (trade.isDrinksBought()) {
                        player.getCharacter().addGold(-1);
                    }
                } else {
                    meetingTableRoll = trade.override;
                }

                LOG.info("Rolled a {} on the meeting table. [bought drinks = {}]", meetingTableRoll, trade.isDrinksBought());

                if (trade.getTradeTarget() instanceof AbstractNative) {
                    final AbstractNative nativeTarget = (AbstractNative) trade.getTradeTarget();
                    Relationship relationship = player.getCharacter().getRelationshipWith(nativeTarget.getFaction());

                    if (trade.isDrinksBought()) {
                        relationship = Table.MeetingTable.getBoughtDrinksRelationship(relationship);
                    }

                    LOG.info("Characters relationship with the entity is {}.", relationship);

                    switch (relationship) {
                        case ENEMY:
                            switch (meetingTableRoll) {
                                case 1:
                                    // INSULT
                                    doTrade = true;
                                    goldValueOfItem = 0;
                                    player.getCharacter().addNotoriety(-5);
                                    break;
                                case 2:
                                    // CHALLENGE
                                    doTrade = true;
                                    goldValueOfItem = 0;
                                    player.getCharacter().addFame(-5);
                                    break;
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                    // BLOCK/BATTLE
                                    player.getCharacter().setBlocked(true);
                                    break;
                                default:
                                    LOG.error("Seriously how did we get this?");
                            }
                            break;
                        case UNFRIENDLY:
                            switch (meetingTableRoll) {
                                case 1:
                                    // PRICE X 4
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 4;
                                    break;
                                case 2:
                                    // NO DEAL
                                    break;
                                case 3:
                                    // NO DEAL
                                    break;
                                case 4:
                                    // INSULT
                                    doTrade = true;
                                    goldValueOfItem = 0;
                                    player.getCharacter().addNotoriety(-5);
                                    break;
                                case 5:
                                    // CHALLENGE
                                    doTrade = true;
                                    goldValueOfItem = 0;
                                    player.getCharacter().addFame(-5);
                                    break;
                                case 6:
                                    // BLOCK/BATTLE
                                    player.getCharacter().setBlocked(true);
                                    break;
                                default:
                                    LOG.error("Seriously how did we get this?");
                            }
                            break;
                        case NEUTRAL:
                            switch (meetingTableRoll) {
                                case 1:
                                    // OPPORTUNITY
                                    doTrade = true;
                                    break;
                                case 2:
                                    // PRICE X 4
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 3;
                                    break;
                                case 3:
                                    // PRICE X 4
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 4;
                                    break;
                                case 4:
                                    // NO DEAL
                                    break;
                                case 5:
                                    // NO DEAL
                                    break;
                                case 6:
                                    // Trouble TODO I don't know what this is.
                                    break;
                                default:
                                    LOG.error("Seriously how did we get this?");
                            }
                            break;
                        case FRIENDLY:
                            switch (meetingTableRoll) {
                                case 1:
                                    // OPPORTUNITY TODO I don't know what this is.
                                    doTrade = true;
                                    break;
                                case 2:
                                    // PRICE X 2
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 2;
                                    break;
                                case 3:
                                    // PRICE X 2
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 2;
                                    break;
                                case 4:
                                    // PRICE X 3
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 3;
                                    break;
                                case 5:
                                    // PRICE X 4
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 4;
                                    break;
                                case 6:
                                    // NO DEAL
                                    break;
                                default:
                                    LOG.error("Seriously how did we get this?");
                            }
                            break;
                        case ALLY:
                            switch (meetingTableRoll) {
                                case 1:
                                    // BOON TODO I don't know what it is.
                                    doTrade = true;
                                    goldValueOfItem = 0;
                                    break;
                                case 2:
                                    // PRICE X 1
                                    doTrade = true;
                                    break;
                                case 3:
                                    // PRICE X 2
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 2;
                                    break;
                                case 4:
                                    // PRICE X 3
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 3;
                                    break;
                                case 5:
                                    // PRICE X 4
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 2;
                                    break;
                                case 6:
                                    // PRICE X 4
                                    doTrade = true;
                                    goldValueOfItem = goldValueOfItem * 2;
                                    break;
                                default:
                                    LOG.error("Seriously how did we get this?");
                            }
                    }
                } else {
                    // Visitors not implemented yet
                    // TODO comeback when we have visitors.
                    LOG.error("We shouldn't of gotten here, as we don't have visitors.");
                }
            }
            if (doTrade) {
                LOG.info("Trade to native is allowed.");
                LOG.info("Cost to buy item (modified by rolls): {} Base was {}.", goldValueOfItem, trade.getItemToTrade().getGoldValue());
                if (player.getCharacter().getCurrentGold() - goldValueOfItem < 0) {
                    LOG.info("Character didn't have enough gold to pay for item. Cancelling trade.");
                    return;
                }
                trade.getTradeTarget().removeItem(trade.getItemToTrade());
                if (trade.getTradeTarget() instanceof AbstractCharacter) {
                    ((AbstractCharacter) trade.getTradeTarget()).addGold(goldValueOfItem);
                }
                player.getCharacter().addItem(trade.getItemToTrade());
                player.getCharacter().addGold(-goldValueOfItem);
                LOG.info("Added {} to {}'s inventory.", trade.getItemToTrade(), player.getCharacter().getEntityInformation().convertToCharacterType());
                LOG.info("Removed {} from {}'s inventory.", trade.getItemToTrade(), trade.getTradeTarget().getEntityInformation());
            } else {
                LOG.info("Meeting table result resulted in no trade.");
            }
        }
    }
}
