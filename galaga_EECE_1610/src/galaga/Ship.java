/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 *
 * @author David Helminiak
 */
public class Ship extends gameObjects {

    private LinkedList<TieFighter> e1 = Galaga.getTieFighterBounds();
    private LinkedList<Hatak> e2 = Galaga.getHatakBounds();
    private LinkedList<Banshee> e3 = Galaga.getBansheeBounds();
    private LinkedList<CylonRaider> e4 = Galaga.getCylonRaiderBounds();

    public Ship(int sxpos, int sypos, int swidth, int sheight, int slife, int sdeltaX, int sdeltaY, String img) {
        this.sxpos = sxpos;
        this.sypos = sypos;
        this.sheight = sheight;
        this.swidth = swidth;
        this.slife = slife;
        this.sdeltaX = sdeltaX;
        this.sdeltaY = sdeltaY;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        scollisionBox = new Rectangle(sxpos, sypos, swidth, sheight);

    }

    public Rectangle getBounds() {
        return new Rectangle(sxpos, sypos, swidth, sheight);
    }

    @Override
    void update(int id) {
        if (id == 1) {

            if (sxpos <= 0) {
                sxpos++;
            }

            if (Galaga.shipMovementLeft && sxpos > 0) {
                sxpos -= Galaga.shipSpeed;
                scollisionBox.x -= Galaga.shipSpeed;
            }
            if (Galaga.shipMovementRight && sxpos < (555 - swidth)) {
                sxpos += Galaga.shipSpeed;
                scollisionBox.x += Galaga.shipSpeed;
            }

            for (int i = 0; i < e1.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(e1.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.e1.remove(i);
                }
            }
            for (int i = 0; i < e2.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(e2.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.e2.remove(i);
                }
            }
            for (int i = 0; i < Galaga.beams.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(Galaga.beams.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.beams.remove(i);
                }
            }
            for (int i = 0; i < e3.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(e3.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.e3.remove(i);
                }
            }
            for (int i = 0; i < Galaga.plasmaBalls.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(Galaga.plasmaBalls.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                }
            }
            for (int i = 0; i < e4.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(e4.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.e4.remove(i);
                }
            }
            for (int i = 0; i < Galaga.doomsdayBeams.size(); i++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (getBounds().intersects(Galaga.doomsdayBeams.get(i).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.shipCaptured = true;
                    Ship.sxpos = -100;
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < Galaga.ships.size(); i++) {
            g.drawImage(img, Galaga.ships.get(i).sxpos, Galaga.ships.get(i).sypos, null);
        }
    }
}