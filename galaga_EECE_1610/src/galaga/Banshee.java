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
class Banshee extends gameObjects {

    private LinkedList<Missile> missiles = Galaga.getMissileBounds();
    private boolean Back = false;
    private boolean Down = false;
    private boolean Up = false;
    private boolean Entered = false;
    public int e3xpos;
    private int e3ypos;
    private int minTimerTime = 3000;
    private int maxTimerTime = 6000;
    private int randomEstablishedTime;
    private Random randomTime;
    private static Timer timer;

    public Banshee(int e3xpos, int e3ypos, int e3width, int e3height, int e3life, int e3deltaX, int e3deltaY, String img) {
        this.e3xpos = e3xpos;
        this.e3ypos = e3ypos;
        this.e3height = e3height;
        this.e3width = e3width;
        this.e3life = e3life;
        this.e3deltaX = e3deltaX;
        this.e3deltaY = e3deltaY;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        e3collisionBox = new Rectangle(e3xpos, e3ypos, e3width, e3height);
        randomTime = new Random();
        randomEstablishedTime = (minTimerTime + randomTime.nextInt(maxTimerTime - minTimerTime + 1));
        ActionListener fired = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                fire(); //Initiates the creation of a projectile
            }
        };
        timer = new Timer(randomEstablishedTime, fired);
        timer.start();
    }

    public Rectangle getBounds() {
        return new Rectangle(e3xpos, e3ypos, e3width, e3height);
    }

    @Override
    void update(int id) {

        if (id == 6) {
            for (int i = 0; i < missiles.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(missiles.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    if (true) {
                        e3xpos = -1000;
                        e3ypos = -1000;
                        Galaga.e3.remove(this);
                        Banshee.timer.stop();
                        Galaga.plasmaBalls.remove(this);
                        randomEstablishedTime = 0;
                        Galaga.missiles.remove(i); //Given a collision with missile
                        Galaga.missileCounter--;
                        Galaga.score += 15;
                    }
                }
            }
        }
        if (!Entered) {
            e3xpos--;
        }
        if (e3xpos == (525 - 30)) {
            Entered = true;
        }

        if (e3xpos >= 0 && Entered && e3ypos < 400) {
            Back = true;
        }

        if (Back && Entered && !Down && e3ypos <= 400 && !Up) {
            e3xpos--;
        }

        if (e3xpos == 0 && Entered) {
            Down = true;
            Back = false;
        }

        if (Down && Entered && e3ypos < 400) {
            e3ypos++;
        }

        if (e3ypos >= 400 && Entered) {
            Down = false;
        }

        if (!Back && Entered) {
            e3xpos++;
            if (e3xpos == (525 - 40)) {
                Up = true;
            }
        }
        if (Up) {
            e3ypos--;
            if (e3ypos == 200) {
                Up = false;
            }
        }
    }

    private void fire() {
        if (e3xpos < 263) {
            BansheePlasma plasma = new BansheePlasma(e3xpos, e3ypos + 20, 30, 24, 1, -2, 1, "images/Plasma.png");
            Galaga.plasmaBalls.add(plasma); //Creates and adds a new instance of a plasma ballto array
        } else {
            BansheePlasma plasma = new BansheePlasma(e3xpos, e3ypos + 20, 30, 24, 1, -2, 1, "images/Plasma.png");
            Galaga.plasmaBalls.add(plasma); //Creates and adds a new instance of a plasma ballto array   
        }
    }

    @Override
    void draw(Graphics g) {
        g.drawImage(img, e3xpos, e3ypos, null);
    }
}