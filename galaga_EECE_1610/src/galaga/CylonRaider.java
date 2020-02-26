/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.Timer;

/**
 *
 * @author David Helminiak
 */
class CylonRaider extends gameObjects {

    private LinkedList<Missile> missiles = Galaga.getMissileBounds();
    private int e4xpos;
    private int e4ypos;
    private boolean Left;
    private String imgLeft;
    private String imgRight;

    public CylonRaider(int e4xpos, int e4ypos, int e4width, int e4height, int e4life, int e4deltaX, int e4deltaY, boolean Left, String imgLeft, String imgRight) {
        this.e4xpos = e4xpos;
        this.e4ypos = e4ypos;
        this.e4height = e4height;
        this.e4width = e4width;
        this.e4life = e4life;
        this.e4deltaX = e4deltaX;
        this.e4deltaY = e4deltaY;
        this.Left = Left;
        this.imgLeft = imgLeft;
        this.imgRight = imgRight;
        e3collisionBox = new Rectangle(e4xpos, e4ypos, e4width, e4height);
    }

    public Rectangle getBounds() {
        return new Rectangle(e4xpos, e4ypos, e4width, e4height);
    }

    @Override
    void update(int id) {

        if (id == 8) {
            for (int i = 0; i < missiles.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(missiles.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    if (true) {
                        e4xpos = -1000;
                        e4ypos = -1000;
                        Galaga.e4.remove(this);
                        Galaga.missiles.remove(i); //Given a collision with missile
                        Galaga.missileCounter--;
                        Galaga.score += 30;
                    }
                }
            }
        }

        if (Left) {

            e4xpos += 1 + missiles.size();
            e4ypos += 1;
            this.img = Toolkit.getDefaultToolkit().getImage(imgLeft);
        } else {

            e4xpos -= 1 + missiles.size();
            e4ypos += 1;
            this.img = Toolkit.getDefaultToolkit().getImage(imgRight);
        }
    }

    @Override
    void draw(Graphics g) {
        g.drawImage(img, e4xpos, e4ypos, null);

    }
}