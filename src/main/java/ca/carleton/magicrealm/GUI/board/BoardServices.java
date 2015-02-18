package ca.carleton.magicrealm.GUI.board;

import ca.carleton.magicrealm.GUI.tile.AbstractTile;
import ca.carleton.magicrealm.GUI.tile.Clearing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Tony on 14/02/2015.
 */
public class BoardServices {

    public static final int ICON_WIDTH = 300;
    public static final int ICON_HEIGHT = 258;

    public static final int CHIT_WIDTH = 61;
    public static final int CHIT_HEIGHT = 52;

    public ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = this.getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public JLabel createTileIcon(AbstractTile tile) {
        JLabel newTile = new JLabel();
        newTile.setSize(ICON_WIDTH, ICON_HEIGHT);
        ImageIcon newIcon = createImageIcon(tile.getTileInformation().getPath());
        BufferedImage newImage = imageToBufferedImage(newIcon.getImage());
        //newImage = rotateBufferedImage(newImage, 120);

        newImage = resize(newImage, ICON_WIDTH, ICON_HEIGHT);
        newIcon.setImage(newImage);
        newTile.setIcon(newIcon);
        return newTile;
    }

    public ArrayList<JButton> createChitIconsForTile(AbstractTile tile) {
        ArrayList<JButton> iconList = new ArrayList<>();

        // TODO: Check with mike on getting clearings
        for (Clearing clearing : tile.getClearings()) {
            JButton newChit = null;
            ImageIcon newIcon = null;
            if (clearing.getDwelling() != null) {
                newChit = new JButton();
                newChit.setSize(CHIT_WIDTH, CHIT_HEIGHT);

                newIcon = createImageIcon(clearing.getDwelling().getPath());

            }
            if (newChit != null && newIcon != null) {
                BufferedImage newImage = imageToBufferedImage(newIcon.getImage());

                newImage = resize(newImage, CHIT_WIDTH, CHIT_HEIGHT);
                newIcon.setImage(newImage);
                newChit.setIcon(newIcon);
                newChit.setEnabled(true);
            }
            iconList.add(newChit);
        }

        return iconList;
    }

    /**
     * Method to resize a BufferedImage
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    /**
     * Convert an Image to a BufferedImage
     * @param im
     * @return
     */
    public static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage
                (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    /**
     * Rotate the Icon of a BufferedImage
     * @param bufferedImage
     * @param angle
     */
    public static BufferedImage rotateBufferedImage(BufferedImage bufferedImage, double angle) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(angle), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_BILINEAR);
        return op.filter(bufferedImage, null);
    }

    /**
     * Rotate a point around the center of an icon
     *
     * @param point
     */
    public static void rotatePoint(double[] point, double angle) {
        AffineTransform.getRotateInstance(Math.toRadians(angle), ICON_WIDTH/2, ICON_HEIGHT/2)
                .transform(point, 0, point, 0, 1); // specifying to use this double[] to hold coords
    }

}
