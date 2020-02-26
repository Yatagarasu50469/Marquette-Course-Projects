/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.LinkedList;

/**
 *
 * @author David Helminiak
 */
public class Missile extends gameObjects {

    private int mxpos;
    private int mypos;

    public Missile(int mxpos, int mypos, int mwidth, int mheight, int mlife, int mdeltaX, int mdeltaY, String img) {
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
        if (id == 2) {
            if (mypos < -mheight) { //Given a collision with wall boundaries
                Galaga.missiles.remove(this); //Remove the Missile object from play
                Galaga.missileCounter--;
            } else {
                mypos += mdeltaY;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, mxpos, mypos, null);
    }
}