package ca.carleton.magicrealm.game.phase;

import ca.carleton.magicrealm.GUI.board.BoardModel;
import ca.carleton.magicrealm.GUI.tile.TileType;
import ca.carleton.magicrealm.entity.EntityInformation;
import ca.carleton.magicrealm.entity.character.AbstractCharacter;
import ca.carleton.magicrealm.game.Player;

/**
 * Created with IntelliJ IDEA.
 * Date: 01/04/2015
 * Time: 4:59 PM
 */
public class PhaseUtils {

    /**
     * Return the bean with info on how many phases the user has.
     *
     * @param player the player.
     * @param board  the board.
     * @return the bean.
     */
    public static PhaseCountBean getNumberOfPhasesForPlayer(final Player player, final BoardModel board) {

        final PhaseCountBean count = new PhaseCountBean();

        // Everyone gets two as a default.
        int numberOfPhases = 2;

        // If the player isn't currently in a cave, and isn't a dwarf, they get their sunlight phase.
        if (board.getClearingForPlayer(player).getParentTile().getTileType() != TileType.CAVE &&
                player.getCharacter().getEntityInformation() != EntityInformation.CHARACTER_DWARF) {
            numberOfPhases += 2;
        }


        // Begin checking for extra phases by treasures, or character traits.
        final AbstractCharacter character = player.getCharacter();

        // STAMINA - Amazon gets an extra MOVE phase.
        if (character.getEntityInformation() == EntityInformation.CHARACTER_AMAZON) {
            count.addExtraPhase(PhaseType.MOVE);
        }
        // ROBUST - Berserker gets an extra REST phase.
        if (character.getEntityInformation() == EntityInformation.CHARACTER_BERSERKER) {
            count.addExtraPhase(PhaseType.REST);
        }
        // REPUTATION - Captain gets an extra phase if they are at a dwelling
        if (character.getEntityInformation() == EntityInformation.CHARACTER_CAPTAIN && board.getClearingForPlayer(player).getDwelling() != null) {
            numberOfPhases += 1;
        }
        // ELUSIVENESS - Elf gets an extra HIDE phase.
        if (character.getEntityInformation() == EntityInformation.CHARACTER_ELF) {
            count.addExtraPhase(PhaseType.HIDE);
        }
        // HEALTH - White knight gets an extra REST phase.
        if (character.getEntityInformation() == EntityInformation.CHARACTER_WHITE_KNIGHT) {
            count.addExtraPhase(PhaseType.REST);
        }

        // Treasures



        count.setNumberOfPhasesFOrDay(numberOfPhases);

        return count;


    }

}
