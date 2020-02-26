/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import static galaga.Galaga.ships;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.LinkedList;

/**
 *
 * @author David Helminiak
 */
public class BansheePlasma extends gameObjects {

    private int mxpos;
    private int mypos;

    public BansheePlasma(int mxpos, int mypos, int mwidth, int mheight, int mlife, int mdeltaX, int mdeltaY, String img) {
        this.mxpos = mxpos;
        this.mypos = mypos;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        this.mheight = mheight;
        this.mwidth = mwidth;
        this.mlife = mlife;
        this.mdeltaX = mdeltaX;
        this.mdeltaY = mdeltaY;
    }

    public Rectangle getBounds() {
        return new Rectangle(mxpos, mypos, mwidth, mheight);
    }

    @Override
    void update(int id) {
        if (id == 7) {
            if (mypos > 580 || getBounds().intersects(Galaga.ships.get(1).getBounds())) { //Given a collision with wall boundaries or ship
                Galaga.plasmaBalls.remove(this); //Remove the beam object from play
            } else {
                mypos -= -mdeltaY;
                mypos -= mdeltaX;
                if (mxpos < (Galaga.ships.getLast().sxpos)) {
                    mxpos++;
                }
                if (mxpos > (Galaga.ships.getLast().sxpos)) {
                    mxpos--;
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, mxpos, mypos, null);
    }
}