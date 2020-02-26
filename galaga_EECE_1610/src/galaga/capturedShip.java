/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Graphics;
import java.awt.Toolkit;

/**
 *
 * @author Computer
 */
class capturedShip extends gameObjects {

    private static int e6xpos;
    private static int e6ypos;
    private static int s2xpos; //position in the x direction
    private static int s2ypos; //position in the y direction
    private int s2width; //object width
    private int s2height; //object height 

    public capturedShip(int e6width, int e6height, String img) {
        e6xpos = e6xpos = Doomsday.e5xpos - 10;
        e6ypos = e6ypos = Doomsday.e5ypos + Doomsday.e5height + 20;
        this.img = Toolkit.getDefaultToolkit().getImage(img);
    }

    @Override
    void update(int id) {
        if (id == 11) {
            e6xpos = Doomsday.e5xpos;
            e6ypos = Doomsday.e5ypos + Doomsday.e5height + 20;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, capturedShip.e6xpos, capturedShip.e6ypos, null);
    }
}
