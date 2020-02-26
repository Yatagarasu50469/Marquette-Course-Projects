/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.LinkedList;

/**
 *
 * @author David Helminiak
 */
class Doomsday extends gameObjects {

    private LinkedList<Missile> missiles = Galaga.getMissileBounds();
    private static boolean Back = false;
    public static int e5xpos;
    public static int e5ypos;
    public static boolean doomsdayMovementAllowed = true;
    private static boolean doomsdayFired = false;
    private static boolean beamCreated = false;
    public static boolean beamExists = false;
    private static boolean shipAdded = false;

    public Doomsday(int e5xpos, int e5ypos, int e5width, int e5height, int e5life, int e5deltaX, int e5deltaY, String img) {
        this.e5xpos = e5xpos;
        this.e5ypos = e5ypos;
        this.e5height = e5height;
        this.e5width = e5width;
        this.e5life = e5life;
        this.e5deltaX = e5deltaX;
        this.e5deltaY = e5deltaY;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        e5collisionBox = new Rectangle(e5xpos, e5ypos, e5width, e5height);
        doomsdayFired = false;
        doomsdayMovementAllowed = true;
        shipAdded = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(e5xpos, e5ypos, e5width, e5height);
    }

    @Override
    void update(int id) {
        if (id == 9) {
            for (int i = 0; i < missiles.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(missiles.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    if (true) {
                        e5xpos = -1000;
                        e5ypos = -1000;
                        Galaga.e5.remove(this);
                        Galaga.doomsdayBeams.remove(this);
                        Galaga.missiles.remove(i); //Given a collision with missile
                        Galaga.missileCounter--;
                        Galaga.score += 30;
                        if (Galaga.shipCaptured) {
                            Galaga.addFighter = true;
                        }
                        if (Galaga.addFighter) {
                            Galaga.otherShips.add(new OtherShips(Galaga.ships.getFirst().sxpos + (45 * (Galaga.otherShips.size() + 1)), Galaga.ships.getFirst().sypos, 45, 54, "images/NSEA_Protector.png"));
                            Galaga.addFighter = false;
                        }
                        Galaga.shipCaptured = false;
                    }
                }
                Galaga.e6.removeAll(Galaga.e6);
            }
        }

        if (e5ypos > Galaga.windowHeight) {
            Galaga.e5.remove(this);
            Galaga.doomsdayBeams.remove(this);
        }

        if (!doomsdayFired && !Galaga.shipCaptured) {
            if (e5ypos < 200 && doomsdayMovementAllowed) {
                e5ypos++;
            }
            if (e5ypos == 200 && doomsdayMovementAllowed) {
                e5ypos++;
                fire();
            }
        }
        if (doomsdayFired && !Galaga.shipCaptured && doomsdayMovementAllowed) {
            e5ypos += 4;
        }

        if (doomsdayFired && Galaga.shipCaptured && doomsdayMovementAllowed) {
            if (e5xpos >= Galaga.windowWidth - e5width - 10) {
                Back = true;
            }
            if (!Back) {
                e5xpos += 3;
            }
            if (Back) {
                e5xpos -= 3;
            }
            if (e5xpos <= 0) {
                Back = false;
            }

            Galaga.doomsdayBeams.removeAll(Galaga.doomsdayBeams);
            if (!shipAdded) {
                Galaga.e6.add(new capturedShip(45, 54, "images/Captured_Protector.png"));
            }
        }

        if (doomsdayFired && !doomsdayMovementAllowed) {
            if (!beamExists) {
                Galaga.doomsdayBeams.add(new DoomsdayBeam(0, 0, 600, 600));
            }
        }
    }

    private void fire() {
        doomsdayMovementAllowed = false;
        doomsdayFired = true;
    }

    @Override
    void draw(Graphics g) {
        g.drawImage(img, e5xpos, e5ypos, null);
    }
}