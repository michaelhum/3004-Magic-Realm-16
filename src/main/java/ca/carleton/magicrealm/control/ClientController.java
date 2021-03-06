package ca.carleton.magicrealm.control;

import ca.carleton.magicrealm.GUI.board.BoardModel;
import ca.carleton.magicrealm.GUI.board.BoardWindow;
import ca.carleton.magicrealm.GUI.charactercreate.CharacterCreateMenu;
import ca.carleton.magicrealm.GUI.phaseselector.PhaseSelectorMenu;
import ca.carleton.magicrealm.entity.character.CharacterType;
import ca.carleton.magicrealm.game.GameResult;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.combat.chit.ActionChit;
import ca.carleton.magicrealm.game.phase.AbstractPhase;
import ca.carleton.magicrealm.game.phase.PhaseUtils;
import ca.carleton.magicrealm.log.LogWriter;
import ca.carleton.magicrealm.network.ClientNetwork;
import ca.carleton.magicrealm.network.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Main logic class for the client application. Contains the main game loop and supporting methods.
 * <p>
 * Created by Tony on 19/02/2015.
 */
public class ClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    private final BoardWindow boardWindow;

    private CharacterCreateMenu characterCreateMenu;

    private PhaseSelectorMenu phaseSelectorMenu;

    private BoardModel boardModel;

    private Player currentPlayer;

    private ClientNetwork networkConnection = null;

    private JFrame output;

    private final List<AbstractPhase> recordedPhasesForDay = new ArrayList<AbstractPhase>();

    private final List<CharacterType> availableCharacters = new ArrayList<CharacterType>(Arrays.asList(CharacterType.values()));;

    public ClientController() {
        this.boardWindow = new BoardWindow();
        this.currentPlayer = new Player();
        this.startOutputLogging();
        this.organizeDesktop();
    }

    /**
     * Handles a message from the server.
     *
     * @param message the message.
     */
    public void handleMessage(Message message) {

        LOG.info("Received {} message from server. Payload: {}.", message.getMessageType(), message.getPayload());
        switch (message.getMessageType()) {
            case (Message.START_GAME):
                this.showCharacterCreate();
                break;
            case (Message.SELECT_CHARACTER):
                this.removeFromAvailableCharacters(message.getPayload());
                this.characterCreateMenu.updateAvailableCharacters();
                break;
            case (Message.BIRDSONG_START):
                this.updateFromServer(message.getPayload());
                this.refreshBoard(false);
                // Process birdsong
                this.selectPhasesForDay();
                break;
            case (Message.DAYLIGHT_START):
                this.updateFromServer(message.getPayload());
                // Process daylight
                this.processDaylight();
                break;
            case (Message.SUNSET_UPDATE):
                // Only want to update the map... do nothing else.
                this.updateFromServer(message.getPayload());
                this.refreshBoard(true);
                break;
            case (Message.COMBAT_FILL_OUT_MELEE_SHEET):
                // Set new data
                this.updateFromServer(message.getPayload());
                this.refreshBoard(true);
                this.selectOptionsForCombat();
                break;
            case (Message.FATIGUE_FATIGUE_CHITS):
                // Set new data
                this.updateFromServer(message.getPayload());
                this.refreshBoard(false);
                this.selectChitsToFatigue();
                break;
            case (Message.GAME_OVER):
                this.gameOver(message.getPayload());
                break;
            default:
                break;
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
        this.phaseSelectorMenu = new PhaseSelectorMenu(this.currentPlayer, this.recordedPhasesForDay, PhaseUtils.getNumberOfPhasesForPlayer(this.currentPlayer, this.boardModel), this);
        this.phaseSelectorMenu.setVisible(true);
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
        Daylight.doDaylight(this.boardModel, this.currentPlayer, this.recordedPhasesForDay);
        this.updatePlayerInMap();
        this.refreshBoard(true);
        LOG.info("Executed daylight phase for player.");
        this.networkConnection.sendMessage(Message.DAYLIGHT_DONE, this.boardModel);
    }

    /**
     * Fills out the melee sheet for the user for combat.
     */
    private void selectOptionsForCombat() {
        LOG.info("Starting combat melee sheet step.");
        Combat.fillOutMeleeSheet(this.boardModel, this.currentPlayer, this.boardWindow);
        this.updatePlayerInMap();
        this.networkConnection.sendMessage(Message.COMBAT_SEND_MELEE_SHEET, this.boardModel);
        LOG.info("WARNING: OTHER PLAYERS MUST ENTER THEIR SHEET AS WELL - COMBAT WILL BE RESOLVED AUTOMATICALLY BY THE SERVER.");
    }

    /**
     * Figure out what chits to fatigue or wound.
     */
    private void selectChitsToFatigue() {
        LOG.info("Starting combat fatigue melee sheet step.");
        Combat.doFatigueStep(this.boardModel, this.currentPlayer, this.boardWindow);
        this.updatePlayerInMap();
        this.networkConnection.sendMessage(Message.FATIGUE_SUBMIT_UPDATED, this.boardModel);
    }

    /**
     * Perform updates necessary when receiving data from the board.
     *
     * @param boardModel the board.
     */
    private void updateFromServer(final Object boardModel) {
        this.boardModel = (BoardModel) boardModel;
        this.updateCurrentPlayer();
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
        // Note, need to invoke in another thread, since the previous method was calling from main() it worked out okay.
        SwingUtilities.invokeLater(() -> {
            ClientController.this.characterCreateMenu = new CharacterCreateMenu(
                    ClientController.this.boardWindow,
                    ClientController.this.currentPlayer,
                    ClientController.this.availableCharacters,
                    ClientController.this);
            LOG.info("Displayed character create.");
            ClientController.this.characterCreateMenu.displayWindow();
        });
    }

    /**
     * Game over... basically show a message and try and close gracefully..
     */
    private void gameOver(final Object payload) {
        final GameResult result = (GameResult) payload;

        switch (result) {
            case VICTOR:
                JOptionPane.showMessageDialog(this.boardWindow, "The server has reported game over and you're the victor! Congratulations!");
                break;
            case WINNER:
                JOptionPane.showMessageDialog(this.boardWindow, "The server has reported game over and you're a winner (>= 0 points)! Congratulations!");
                break;
            case LOSER:
                JOptionPane.showMessageDialog(this.boardWindow, "The server has reported game over, but you lost (< 0 points). Sorry, better luck next time!");
                break;
        }
        this.shutDown();
    }

    /**
     * Remove a character from the list of available characters when choosing a character.
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
    private void refreshBoard(final boolean isSunset) {
        this.boardWindow.refresh(this.boardModel, this.currentPlayer.getCharacter(), isSunset);
        this.boardWindow.setGameInfoText(this.createGameInfoString());
        LOG.info("Board refreshed.");
    }

    /**
     * Create the info display string.
     *
     * @return the string.
     */
    private String createGameInfoString() {

        String gameInfoText = "<html><br/>";
        gameInfoText = gameInfoText.concat("Character: " + this.currentPlayer.getCharacter().toString() + "<br/>" +
                "Hidden?: " + this.currentPlayer.getCharacter().isHidden() + "<br/>" +
                "Blocked?: " + this.currentPlayer.getCharacter().isBlocked() + "<br/>" +
                "Number of deaths: " + this.currentPlayer.getRestarts() + "<br/>" +
                "Vulnerability: " + this.currentPlayer.getCharacter().getVulnerability() + "<br/>" +
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

    private void startOutputLogging() {

        this.output = new JFrame("Logging output (See console for more detailed information).");
        this.output.setPreferredSize(new Dimension(550, 1000));
        this.output.setResizable(false);
        this.output.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JTextArea area = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setSize(new Dimension(550, 1000));
        area.setEditable(false);
        scrollPane.setVisible(true);
        this.output.add(scrollPane);
        this.output.pack();

        LogWriter writer = new LogWriter();
        Timer timer = new Timer(100, e -> writer.output(area));
        timer.start();

        this.output.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                writer.stopLogging = true;
                timer.stop();
                LOG.info("Ended log timer.");
            }
        });

        this.output.setVisible(true);
        LOG.info("Started logging timer and displayed logging output window.");
    }

    private void organizeDesktop() {
        this.output.setLocation(0, 0);
        this.boardWindow.setLocation(this.output.getX() + (int) this.output.getSize().getWidth() + this.output.getInsets().left + this.output.getInsets().right,
                this.output.getY());
    }

    public void setBoardModel(BoardModel model) {
        this.boardModel = model;
    }

    public BoardWindow getParentWindow() {
        return this.boardWindow;
    }

    public BoardModel getBoardModel() {
        return this.boardModel;
    }

    public void setNetworkConnection(ClientNetwork nC) {
        this.networkConnection = nC;
        LOG.info("Set controller network connection successfully. Waiting for server to send game start...");
    }

    /**
     * Called if necessary to shutdown gracefully.
     */
    public void shutDown() {
        LOG.info("Starting shutdown operations...");
        this.networkConnection.stop();
        this.output.dispose();
        this.boardWindow.dispose();
        if (this.characterCreateMenu != null) {
            this.characterCreateMenu.dispose();
        }
        if (this.phaseSelectorMenu != null) {
            this.phaseSelectorMenu.dispose();
        }
        LOG.info("Done - Client exiting.");
    }
}
