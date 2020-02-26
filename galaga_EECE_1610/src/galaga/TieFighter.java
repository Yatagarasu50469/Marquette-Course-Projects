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
class TieFighter extends gameObjects {

    private LinkedList<Missile> missiles = Galaga.getMissileBounds();
    private boolean Back = false;
    private int expos;
    private int eypos;
    private boolean bottomBaseReached = false;
    private boolean topBaseReached = true;

    public TieFighter(int expos, int eypos, int ewidth, int eheight, int elife, int edeltaX, int edeltaY, String img) {
        this.expos = expos;
        this.eypos = eypos;
        this.eheight = eheight;
        this.ewidth = ewidth;
        this.elife = elife;
        this.edeltaX = edeltaX;
        this.edeltaY = edeltaY;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        ecollisionBox = new Rectangle(expos, eypos, ewidth, eheight);
    }

    public Rectangle getBounds() {
        return new Rectangle(expos, eypos, ewidth, eheight);
    }

    @Override
    void update(int id) {

        if (id == 3) {
            for (int i = 0; i < missiles.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(missiles.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    if (true) {
                        Galaga.e1.remove(this);
                        Galaga.missiles.remove(i); //Given a collision with missile
                        Galaga.missileCounter--;
                        Galaga.score += 10;
                    }
                }
            }
        }
        if (eypos >= 555 - eheight) {
            bottomBaseReached = true;
            topBaseReached = false;
        }

        if (eypos <= 0) {
            bottomBaseReached = false;
            topBaseReached = true;
        }

        if (bottomBaseReached) {
            eypos--;
        }

        if (topBaseReached) {
            eypos++;
        }
        if (expos >= 525 - 25) {
            Back = true;
        }
        if (!Back) {
            expos++;
        }
        if (Back) {
            expos--;
        }
        if (expos <= 0) {
            Back = false;
        }
    }

    @Override
    void draw(Graphics g) {
        g.drawImage(img, expos, eypos, null);
    }
}