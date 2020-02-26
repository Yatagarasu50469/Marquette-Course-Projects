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
class Hatak extends gameObjects {

    private LinkedList<Missile> missiles = Galaga.getMissileBounds();
    private boolean Back = false;
    private int e2xpos;
    private int e2ypos;
    private boolean bottomBaseReached = false;
    private boolean topBaseReached = true;
    public static boolean hatakFired = false;
    private int minTimerTime = 3000;
    private int maxTimerTime = 7000;
    private int randomEstablishedTime;
    private Random randomTime;
    private static Timer timer;

    public Hatak(int e2xpos, int e2ypos, int e2width, int e2height, int e2life, int e2deltaX, int e2deltaY, String img) {
        this.e2xpos = e2xpos;
        this.e2ypos = e2ypos;
        this.e2height = e2height;
        this.e2width = e2width;
        this.e2life = e2life;
        this.e2deltaX = e2deltaX;
        this.e2deltaY = e2deltaY;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        e2collisionBox = new Rectangle(e2xpos, e2ypos, e2width, e2height);
        randomTime = new Random();
        randomEstablishedTime = (minTimerTime + randomTime.nextInt(maxTimerTime - minTimerTime + 1));
        ActionListener fired = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                hatakFired = true; //Ceases movement during fire
                fire(); //Initiates the creation of a projectile
            }
        };
        timer = new Timer(randomEstablishedTime, fired);
        timer.start();
    }

    public Rectangle getBounds() {
        return new Rectangle(e2xpos, e2ypos, e2width, e2height);
    }

    @Override
    void update(int id) {

        if (id == 4) {
            for (int i = 0; i < missiles.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(missiles.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    if (true) {
                        e2xpos = -1000;
                        e2ypos = -1000;
                        Galaga.e2.remove(this);
                        Hatak.timer.stop();
                        Galaga.beams.remove(this);
                        randomEstablishedTime = 0;
                        Galaga.missiles.remove(i); //Given a collision with missile
                        Galaga.missileCounter--;
                        Galaga.score += 20;
                    }
                }
            }
        }
        if (!hatakFired) {
            if (e2xpos >= 525 - 70) {
                Back = true;
            }
            if (!Back) {
                e2xpos += 1 + missiles.size();
            }
            if (Back) {
                e2xpos -= 1 + missiles.size();
            }
            if (e2xpos <= 0) {
                Back = false;
            }
        }
    }

    private void fire() {
        HatakBeam beam = new HatakBeam(e2xpos, e2ypos + 20, 106, 56, 1, 0, 1, "images/HatakBeam.png");
        Galaga.beams.add(beam); //Creates and adds a new instance of a Hatak Beam to array
        hatakFired = false; //Returns to normal movmenent
    }

    @Override
    void draw(Graphics g) {
        g.drawImage(img, e2xpos, e2ypos, null);
    }
}