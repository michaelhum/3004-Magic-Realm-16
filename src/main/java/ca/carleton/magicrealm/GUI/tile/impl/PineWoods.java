package ca.carleton.magicrealm.GUI.tile.impl;

import ca.carleton.magicrealm.GUI.tile.AbstractTile;
import ca.carleton.magicrealm.GUI.tile.Clearing;
import ca.carleton.magicrealm.GUI.tile.TileInformation;
import ca.carleton.magicrealm.GUI.tile.TileType;

/**
 * Created with IntelliJ IDEA.
 * Date: 17/02/15
 * Time: 1:06 PM
 */
public class PineWoods extends AbstractTile {

    public PineWoods() {
        this.clearings = Clearing.initializeClearingsArray(3,this);

        this.clearings[0].setX(117);
        this.clearings[0].setY(192);
        this.clearings[0].setName("2");

        this.clearings[1].setX(230);
        this.clearings[1].setY(86);
        this.clearings[1].setName("4");

        this.clearings[2].setX(363);
        this.clearings[2].setY(213);
        this.clearings[2].setName("5");
    }

    @Override
    public TileInformation getTileInformation() {
        return TileInformation.PINE_WOODS;
    }

    @Override
    public TileType getTileType() {
        return TileType.WOODS;
    }
}
