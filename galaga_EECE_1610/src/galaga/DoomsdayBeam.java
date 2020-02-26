/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import static galaga.gameObjects.e5height;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David Helminiak
 */
public class DoomsdayBeam extends gameObjects {

    private int mxpos;
    private int mypos;
    private static int beamWidth = 0;
    private static int beamHeight = 0;
    private static int beamXPos = Doomsday.e5xpos;
    private static int beamYPos = Doomsday.e5ypos + e5height;

    public DoomsdayBeam(int mxpos, int mypos, int mwidth, int mheight) {
        this.mxpos = mxpos;
        this.mypos = mypos;
        this.mheight = mheight;
        this.mwidth = mwidth;
        this.beamWidth = 15;
        this.beamHeight = 0;
        this.beamXPos = Doomsday.e5xpos;
        this.beamYPos = Doomsday.e5ypos + e5height;
    }

    public Rectangle getBounds() {
        return new Rectangle(beamXPos + 5, beamYPos - 8, beamWidth, beamHeight);
    }

    @Override
    void update(int id) {
        if (id == 10) {
            Galaga.graphics.setColor(Color.red);
            Galaga.graphics.fillRect(beamXPos + 5, beamYPos - 8, beamWidth, beamHeight);

            if ((beamHeight + beamYPos) <= Galaga.windowHeight) {
                beamHeight++;
            }
            if (beamHeight >= 280) {
                Doomsday.doomsdayMovementAllowed = true;
                beamYPos = Doomsday.e5ypos + e5height;
            }
            if (beamYPos >= Galaga.windowHeight) {
                Galaga.doomsdayBeams.remove(this);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
    }
}