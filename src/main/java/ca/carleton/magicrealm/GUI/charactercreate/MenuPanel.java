package ca.carleton.magicrealm.GUI.charactercreate;

import ca.carleton.magicrealm.entity.character.CharacterType;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * Date: 17/02/15
 * Time: 8:51 PM
 */
public class MenuPanel extends JPanel {

    JButton submitButton;

    JTextField[] victoryPointsFields;

    JButton[][] victoryPointsButtons;

    JLabel[] victoryLabels;

    JLabel remainingPointsLabel;

    JLabel listInfoLabel;

    JLabel pointsInfoLabel;

    JList<CharacterType> characterSelectList;

    private MenuModel model;

    public MenuPanel(final MenuModel model) {
        this.setLayout(null);

        this.model = model;
        this.initializeLabels();
        this.initializeVictoryButtons();
        this.initializeVictoryFields();
        this.initializeList();
    }

    private void initializeList() {
        this.characterSelectList = new JList<CharacterType>(CharacterType.values());
        this.characterSelectList.setLocation(20, 40);
        this.characterSelectList.setSize(new Dimension(150, 110));
        this.characterSelectList.setToolTipText("Select a character.");
        this.characterSelectList.setVisible(true);
        this.add(this.characterSelectList);
    }

    private void initializeLabels() {
        this.victoryLabels = new JLabel[5];

        for (int i = 0; i < this.victoryLabels.length; i++) {
            this.victoryLabels[i] = new JLabel();
            this.victoryLabels[i].setSize(new Dimension(100, 20));
            this.victoryLabels[i].setLocation(180, 40 + (i * 20));
            this.victoryLabels[i].setVisible(true);
            this.victoryLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(this.victoryLabels[i]);
        }

        this.victoryLabels[0].setText("Gold");
        this.victoryLabels[1].setText("Notoriety");
        this.victoryLabels[2].setText("Fame");
        this.victoryLabels[3].setText("Spells");
        this.victoryLabels[4].setText("Great Treasures");

        this.remainingPointsLabel = new JLabel("Remaining points: 5");
        this.remainingPointsLabel.setSize(new Dimension(150, 20));
        this.remainingPointsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.remainingPointsLabel.setLocation(290, 140);
        this.remainingPointsLabel.setVisible(true);
        this.add(this.remainingPointsLabel);

        this.listInfoLabel = new JLabel("Choose your character:");
        this.listInfoLabel.setSize(new Dimension(150, 20));
        this.listInfoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.listInfoLabel.setLocation(20, 10);
        this.listInfoLabel.setVisible(true);
        this.add(this.listInfoLabel);

        this.pointsInfoLabel = new JLabel("Allocate your victory points:");
        this.pointsInfoLabel.setSize(new Dimension(180, 20));
        this.pointsInfoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.pointsInfoLabel.setLocation(265, 10);
        this.pointsInfoLabel.setVisible(true);
        this.add(this.pointsInfoLabel);


    }

    private void initializeVictoryFields() {
        this.victoryPointsFields = new JTextField[5];

        for (int i = 0; i < this.victoryPointsFields.length; i++) {
            this.victoryPointsFields[i] = new JTextField();
            this.victoryPointsFields[i].setSize(new Dimension(45, 20));
            this.victoryPointsFields[i].setLocation(345, 40 + (i * 20));
            this.victoryPointsFields[i].setVisible(true);
            this.victoryPointsFields[i].setEnabled(false);
            this.victoryPointsFields[i].setHorizontalAlignment(SwingConstants.CENTER);
            this.victoryPointsFields[i].setText("0");
            this.add(this.victoryPointsFields[i]);
        }
    }

    private void initializeVictoryButtons() {
        this.victoryPointsButtons = new JButton[5][2];

        for (int i = 0; i < this.victoryPointsButtons.length; i++) {
            for (int j = 0; j < this.victoryPointsButtons[i].length; j++) {
                this.victoryPointsButtons[i][j] = new JButton();
                if (j == 0) {
                    this.victoryPointsButtons[i][j].setText("<");
                } else {
                    this.victoryPointsButtons[i][j].setText(">");
                }

                this.victoryPointsButtons[i][j].setSize(new Dimension(45, 20));
                this.victoryPointsButtons[i][j].setLocation(295 + (j * 100), 40 + (i * 20));
                this.victoryPointsButtons[i][j].setVisible(true);
                this.add(this.victoryPointsButtons[i][j]);

            }
        }
    }
}
