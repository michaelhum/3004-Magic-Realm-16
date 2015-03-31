package ca.carleton.magicrealm.control;

import ca.carleton.magicrealm.GUI.board.BoardModel;
import ca.carleton.magicrealm.GUI.board.BoardWindow;
import ca.carleton.magicrealm.GUI.charactercreate.CharacterCreateMenu;
import ca.carleton.magicrealm.GUI.phaseselector.PhaseSelectorMenu;
import ca.carleton.magicrealm.network.AppClient;
import ca.carleton.magicrealm.network.Message;
import ca.carleton.magicrealm.entity.character.CharacterType;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.chit.ActionChit;
import ca.carleton.magicrealm.game.phase.AbstractPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Tony on 19/02/2015.
 */
public class GameController {

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    private final BoardWindow boardWindow;

    private BoardModel boardModel;

    private CharacterCreateMenu characterCreateMenu;

    private Player currentPlayer;

    private AppClient networkConnection = null;

    private final List<AbstractPhase> recordedPhasesForDay = new ArrayList<AbstractPhase>();

    private final List<CharacterType> availableCharacters = new ArrayList<CharacterType>(Arrays.asList(CharacterType.values()));

    public GameController() {
        this.boardWindow = new BoardWindow();
        this.currentPlayer = new Player();
    }

    /**
     * Handles a message from the server.
     *
     * @param obj the message.
     */
    public void handleMessage(Object obj) {

        if (obj instanceof Message) {
            Message m = (Message) obj;
            LOG.info("Received {} message from server. Payload: {}.", m.getMessageType(), m.getPayload());
            switch (m.getMessageType()) {
                case (Message.SELECT_CHARACTER):
                    this.removeFromAvailableCharacters(m.getPayload());
                    this.characterCreateMenu.updateAvailableCharacters();
                    break;
                case (Message.BIRDSONG_START):
                    this.updateFromBoard(m.getPayload());
                    this.refreshBoard();
                    // Process birdsong
                    this.selectPhasesForDay();
                    break;
                case (Message.DAYLIGHT_START):
                    this.updateFromBoard(m.getPayload());
                    // Process daylight
                    this.processUpdatedPhasesFromBoard();
                    this.processDaylight();
                    break;
                case (Message.COMBAT_FILL_OUT_MELEE_SHEET):
                    // Set new data
                    this.updateFromBoard(m.getPayload());
                    this.refreshBoard();
                    this.selectOptionsForCombat();
                default:
                    break;
            }

        } else if (obj instanceof String) {
            System.out.println("This is a string");
        }
    }

    /**
     * Called by the Character Select Menu to indicate that the character has been set.
     * This also sends the message to the server that the character has been selected, along with the player object.
     */
    public void characterSelected() {
        LOG.info("User selected {} as his/her character.", this.currentPlayer.getCharacter().getEntityInformation());
        this.networkConnection.sendMessage(Message.SELECT_CHARACTER, this.currentPlayer);
    }

    /**
     * Opens up phase selector dialog.
     */
    private void selectPhasesForDay() {
        LOG.info("Displayed birdsong action menu.");
        new PhaseSelectorMenu(this.currentPlayer, this.recordedPhasesForDay, 1, this).setVisible(true);

    }

    /**
     * Called by the selector menu when done entering phases.
     */
    public void doneEnteringPhasesForDay() {
        LOG.info("User finished entering phase data. Sending server message...");
        this.networkConnection.sendMessage(Message.BIRDSONG_DONE, null);
    }

    /**
     * Processes the daylight phases for this client.
     */
    private void processDaylight() {
        Daylight.processPhasesForPlayer(this.boardModel, this.currentPlayer, this.recordedPhasesForDay);
        this.updatePlayerInMap();
        this.refreshBoard();
        LOG.info("Executed daylight phase for player.");
        this.networkConnection.sendMessage(Message.DAYLIGHT_DONE, this.boardModel);
    }

    /**
     * Processes combat for the clearing of the current player.
     */
    private void processCombat() {
        LOG.info("Starting combat for clearing {}", this.boardModel.getClearingForPlayer(this.currentPlayer));
        Combat.doCombat(this.boardModel, this.currentPlayer, this.boardWindow);
        this.updatePlayerInMap();
        this.refreshBoard();
        LOG.info("Executed combat for clearing {}", this.boardModel.getClearingForPlayer(this.currentPlayer));
        this.networkConnection.sendMessage(Message.COMBAT_SEND_MELEE_SHEET, this.boardModel);
    }

    /**
     * Fills out the melee sheet for the user for combat.
     */
    private void selectOptionsForCombat() {
        LOG.info("Starting combat melee sheet step.");
        Combat.fillOutMeleeSheet(this.boardModel, this.currentPlayer, this.boardWindow);
        this.updatePlayerInMap();
       this.networkConnection.sendMessage(Message.COMBAT_SEND_MELEE_SHEET, this.boardModel);
    }

    /**
     * Perform updates necessary when receiving data from the board.
     *
     * @param boardModel the board.
     */
    private void updateFromBoard(final Object boardModel) {
        this.boardModel = (BoardModel) boardModel;
        this.updateCurrentPlayer();
    }

    /**
     * Because we send the entire board, we need to update the phases, since the references are now garbled.
     */
    private void processUpdatedPhasesFromBoard() {
        for (final AbstractPhase phase : this.recordedPhasesForDay) {
            phase.updateFromBoard(this.currentPlayer, this.boardModel);
        }
        LOG.info("Updated {} phases' data from the board before beginning daylight.", this.recordedPhasesForDay.size());
    }

    /**
     * Updates the current player status for use in the client's various methods after the board has been received from the server.
     */
    private void updateCurrentPlayer() {
        for (final Player player : this.boardModel.getPlayers()) {
            if (this.currentPlayer.getCharacter().getEntityInformation() == player.getCharacter().getEntityInformation()) {
                this.currentPlayer = player;
                break;
            }
        }

        LOG.info("Current player information has been updated from the server.");
    }

    /**
     * Replaces the current player stored on the board with the updated one.
     * IMPORTANT This needs to be called before sending sending the map, as it updates the board.
     */
    private void updatePlayerInMap() {
        final Iterator<Player> iterator = this.boardModel.getPlayers().iterator();

        final int sanityCheck = this.boardModel.getPlayers().size();
        while (iterator.hasNext()) {
            if (iterator.next().getCharacter().getEntityInformation() == this.currentPlayer.getCharacter().getEntityInformation()) {
                iterator.remove();
                break;
            }
        }
        // Sanity check - check that we actually removed someone.
        assertThat(this.boardModel.getPlayers().size(), is(sanityCheck - 1));

        this.boardModel.getPlayers().add(this.currentPlayer);

        LOG.info("Model has been updated with client player information.");
    }

    /**
     * Show the character create dialog.
     */
    private void showCharacterCreate() {
        this.characterCreateMenu = new CharacterCreateMenu(this.boardWindow, this.currentPlayer, this.availableCharacters, this);
        this.characterCreateMenu.displayWindow();
        LOG.info("Displayed character create.");
    }

    /**
     * Remove a character from the list of available characeters when choosing a character.
     *
     * @param player the player containing the character to remove.
     */
    private void removeFromAvailableCharacters(final Object player) {
        if (!(player instanceof Player)) {
            throw new IllegalArgumentException("Not a player object");
        }
        final CharacterType removed = ((Player) player).getCharacter().getEntityInformation().convertToCharacterType();
        this.availableCharacters.remove(removed);
        LOG.info("Removed {} from the list of available characters for character select, another user has chosen it.", removed);
    }

    /**
     * Refresh the board (re-draw).
     */
    private void refreshBoard() {
        this.boardWindow.refresh(this.boardModel, this.currentPlayer.getCharacter());
        this.boardWindow.setGameInfoText(this.createGameInfoString());
        LOG.info("Board refreshed.");
    }

    /**
     * Create the info display string.
     *
     * @return the string.
     */
    private String createGameInfoString() {
        String gameInfoText = "<html>";
        gameInfoText = gameInfoText.concat("Character: " + this.currentPlayer.getCharacter().toString() + "<br/>" +
                "Current gold: " + this.currentPlayer.getCharacter().getCurrentGold() + "<br/>" +
                "Current notoriety: " + this.currentPlayer.getCharacter().getCurrentNotoriety() + "<br/>" +
                "Current fame: " + this.currentPlayer.getCharacter().getCurrentFame() + "<br/>" +
                "Current spell count: " + this.currentPlayer.getCharacter().getCurrentSpellsCount() + "<br/>" +
                "Current great treasures count: " + this.currentPlayer.getCharacter().getCurrentGreatTreasuresCount() + "<br/>" +
                "Needed gold: " + this.currentPlayer.getVictoryCondition().getGold() * 30 + "<br/>" +
                "Needed notoriety: " + this.currentPlayer.getVictoryCondition().getNotoriety() * 20 + "<br/>" +
                "Needed fame: " + this.currentPlayer.getVictoryCondition().getFame() * 10 + "<br/>" +
                "Needed spell count: " + this.currentPlayer.getVictoryCondition().getSpellsCount() * 2 + "<br/>" +
                "Needed great treasures count: " + this.currentPlayer.getVictoryCondition().getGreatTreasuresCount() + "<br/><br/>" +
                "Action chits: <br/>");

        StringBuilder actionChits = new StringBuilder();
        for (final ActionChit chit : this.currentPlayer.getCharacter().getActionChits()) {
            actionChits.append(chit).append("<br/>");
        }
        gameInfoText = gameInfoText.concat(actionChits.toString());
        gameInfoText = gameInfoText.concat("</html>");

        return gameInfoText;
    }

    public void setBoardModel(BoardModel model) {
        this.boardModel = model;
    }

    public void setStatusText(final String text) {
        this.boardWindow.setStatusText(text);
    }

    public BoardWindow getParentWindow() {
        return this.boardWindow;
    }

    public BoardModel getBoardModel() {
        return this.boardModel;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setNetworkConnection(AppClient nC) {
        this.networkConnection = nC;
        LOG.info("Set controller network connection successfully.");
        this.showCharacterCreate();
    }
}
