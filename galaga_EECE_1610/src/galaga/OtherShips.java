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
public class OtherShips extends gameObjects {

    private LinkedList<TieFighter> e1 = Galaga.getTieFighterBounds();
    private LinkedList<Hatak> e2 = Galaga.getHatakBounds();
    private LinkedList<Banshee> e3 = Galaga.getBansheeBounds();
    private LinkedList<CylonRaider> e4 = Galaga.getCylonRaiderBounds();
    public static int s2xpos; //position in the x direction
    public static int s2ypos; //position in the y direction
    private int s2width; //object width
    private int s2height; //object height 

    public OtherShips(int s2xpos, int s2ypos, int s2width, int s2height, String img) {
        this.s2xpos = s2xpos;
        this.s2ypos = s2ypos;
        this.s2width = s2width;
        this.s2height = s2height;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        this.s2ypos = s2ypos;
        this.s2collisionBox = new Rectangle(s2xpos, s2ypos, s2width, s2height);

    }

    @Override
    void update(int id) {
        if (id == 12) {
            for (int i = 0; i < Galaga.otherShips.size(); i++) {
                s2collisionBox = new Rectangle(Galaga.ships.getFirst().sxpos + (45 * (i + 1)), s2ypos, s2width, s2height);
            }
            for (int j = 0; j < e1.size(); j++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (s2collisionBox.intersects(e1.get(j).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.otherShips.remove(this);
                    Galaga.e1.remove(j);
                }
            }
            for (int j = 0; j < e2.size(); j++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (s2collisionBox.intersects(e2.get(j).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.otherShips.remove(this);
                    Galaga.e2.remove(j);
                    Galaga.otherShips.remove(this);
                }
            }
            for (int j = 0; j < Galaga.beams.size(); j++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (s2collisionBox.intersects(Galaga.beams.get(j).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.otherShips.remove(this);
                }
            }
            for (int j = 0; j < e3.size(); j++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (s2collisionBox.intersects(e3.get(j).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.otherShips.remove(this);
                }
            }
            for (int j = 0; j < Galaga.plasmaBalls.size(); j++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (s2collisionBox.intersects(Galaga.plasmaBalls.get(j).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.otherShips.remove(this);
                }
            }
            for (int j = 0; j < e4.size(); j++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (s2collisionBox.intersects(e4.get(j).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.otherShips.remove(this);
                    Galaga.e4.remove(j);
                }
            }
            for (int j = 0; j < Galaga.doomsdayBeams.size(); j++) { //Takes how many enemies are present within the array and creates a boundary for each
                if (s2collisionBox.intersects(Galaga.doomsdayBeams.get(j).getBounds())) { //Retrieves the boundary of each enemy and compares to ship boundary
                    Ship.slife--;
                    Galaga.otherShips.remove(this);
                    Galaga.shipCaptured = true;
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (Galaga.otherShips.size() == 1) {
            g.drawImage(img, Galaga.ships.getFirst().sxpos + (45), this.s2ypos, null);
        } else {
            for (int i = 0; i < Galaga.otherShips.size(); i++) {
                g.drawImage(img, Galaga.ships.getFirst().sxpos + (45 * (i + 1)), this.s2ypos, null);
            }
        }
    }
}