package ca.carleton.magicrealm.control;

import ca.carleton.magicrealm.GUI.board.BoardGUIModel;
import ca.carleton.magicrealm.GUI.board.BoardWindow;
import ca.carleton.magicrealm.GUI.charactercreate.CharacterCreateMenu;
import ca.carleton.magicrealm.GUI.tile.Clearing;
import ca.carleton.magicrealm.Networking.AppClient;
import ca.carleton.magicrealm.Networking.Message;
import ca.carleton.magicrealm.entity.character.CharacterType;
import ca.carleton.magicrealm.game.Player;
import ca.carleton.magicrealm.game.phase.AbstractPhase;
import ca.carleton.magicrealm.game.phase.impl.MovePhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tony on 19/02/2015.
 */
public class GameController {

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    private BoardWindow boardWindow;

    private BoardGUIModel boardModel;

    private CharacterCreateMenu characterCreateMenu;

    private Player currentPlayer;

    private AppClient networkConnection = null;

    private List<AbstractPhase> recordedPhasesForDay = new ArrayList<AbstractPhase>();

    private List<CharacterType> availableCharacters = new ArrayList<CharacterType>(Arrays.asList(CharacterType.values()));

    public GameController() {

        // Build window.
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
            System.out.println("Game Controller:This is a Message Object");
            Message m = (Message) obj;
            System.out.println("This is a :" + m.getMessageType() + " Message Type");
            switch (m.getMessageType()) {
                case (Message.SELECT_CHARACTER):
                    this.removeFromAvailableCharacters(m.getMessageObject());
                    this.characterCreateMenu.updateAvailableCharacters();
                    break;
                case (Message.MOVE):
                    //Insert move character functionality here
                    this.handleMove((BoardGUIModel) m.getMessageObject());
                    break;
                case (Message.ALL_PARTICIPATED):
                    //Insert Stage incrementing functionality here
                    System.out.println("ALL PARTICIPATED MESSAGE RECEIVED");
                    //this.boardWindow.hideStatusLabel();
                    //TODO Update label with message saying waiting for board or some shit.
                    break;
                case (Message.SET_MAP):
                    //SETTING MAP MODEL
                    this.setBoardModel((BoardGUIModel) m.getMessageObject());
                    this.updateCurrentPlayer();
                    this.boardWindow.refresh(this.boardModel);

                default:
                    break;
            }

        } else if (obj instanceof String) {
            System.out.println("This is a string");
        }
    }

    public void characterSelected() {
        System.out.println("CHARACTER SELECTED IN GAME CONTROLLER");
        this.networkConnection.sendMessage(Message.SELECT_CHARACTER, this.currentPlayer);
        //this.setupStatusLabel();
        //this.setStatusText("SELECTED CHARACTER, WAITING FOR OTHER PLAYERS");
    }

    //Update Map
    public void handleMove(final BoardGUIModel newBoardModel) {
        this.boardModel = newBoardModel;
    }

    /**
     * Methods to set up a move phase *
     */
    private void setupMovePhaseForPlayer() {
        this.boardWindow.setupMoveButtons(this.createMoveButtonsForClearing(this.currentPlayer.getCurrentClearing())); // display move buttons now? or later
    }

    public void movePlayerToClearing(Clearing clearing) {
        // Record a move phase
        MovePhase movement = new MovePhase();
        movement.setMoveTarget(clearing);
        this.recordedPhasesForDay.add(movement);
        Daylight.processPhasesForPlayer(this.currentPlayer, this.recordedPhasesForDay);
        this.recordedPhasesForDay.clear();
        // Update
        this.updatePlayerInMap();
        // Send the new board to the server.
        Message m = new Message(this.networkConnection.getId(), Message.MOVE, this.boardModel);
        this.networkConnection.sendMessage(Message.MOVE, m);
    }

    public ArrayList<JButton> createMoveButtonsForClearing(Clearing clearing) {
        ArrayList<JButton> buttons = new ArrayList<>();

        for (final Clearing adjacentClearing : clearing.getAdjacentClearings()) {
            JButton newButton = new JButton();
            newButton.setSize(30, 30);
            newButton.setLocation(adjacentClearing.getX(), adjacentClearing.getY());

            newButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameController.this.movePlayerToClearing(adjacentClearing);
                }
            });
            buttons.add(newButton);
        }
        return buttons;
    }

    /**
     * Replaces the current player stored on the board with the updated one.
     */
    public void updatePlayerInMap() {
        final Iterator<Player> iterator = this.boardModel.getPlayers().iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getCharacter().getEntityInformation() == this.currentPlayer.getCharacter().getEntityInformation()) {
                iterator.remove();
            }
        }

        this.boardModel.getPlayers().add(this.currentPlayer);
    }

    /**
     * Updates the current player status for use in the client's various methods..
     */
    public void updateCurrentPlayer() {
        for (final Player player : this.boardModel.getPlayers()) {
            if (this.currentPlayer.getCharacter().getEntityInformation() == player.getCharacter().getEntityInformation()) {
                this.currentPlayer = player;
            }
        }
    }

    public void setNetworkConnection(AppClient nC) {
        this.networkConnection = nC;
        LOG.info("Set controller network connection succesfully. Showing character create for the user.");
        this.showCharacterCreate();
    }

    private void showCharacterCreate() {
        this.characterCreateMenu = new CharacterCreateMenu(this.boardWindow, this.currentPlayer, this.availableCharacters, this);
        this.characterCreateMenu.displayWindow();
    }

    private void removeFromAvailableCharacters(final Object player) {
        if (!(player instanceof Player)) {
            throw new IllegalArgumentException("Not a player object");
        }

        this.availableCharacters.remove(((Player) player).getCharacter().getEntityInformation().convertToCharacterType());
    }

    /**
     * Methods to set up the window to select a player's phase for the day *
     */
    public void setupPhaseSelection() {
        // PhaseSelectorMenu phaseSelectorMenu = new PhaseSelectorMenu();

        JButton confirmFirstPhaseButton = new JButton("ENTER"); // TODO: make unhardcoded later

        confirmFirstPhaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  String selectedPhase = (String)phaseSelectorMenu.getPhaseSelectorPanel().getFirstPhaseBox().getSelectedItem();

              /*  if (selectedPhase.equals("Move")) {
                    setupMovePhaseForPlayer();
                }*/
            }
        });
    }

    public void setBoardModel(BoardGUIModel model) {
        this.boardModel = model;
    }

    public void setupStatusLabel() {
        this.boardWindow.setupStatusLabel();
    }

    public void setStatusText(final String text) {
        this.boardWindow.setStatusText(text);
    }
}
