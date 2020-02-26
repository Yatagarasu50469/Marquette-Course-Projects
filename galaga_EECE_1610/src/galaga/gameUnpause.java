/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author Computer
 */
class gameUnpause implements Runnable, KeyListener {

    private JFrame pause = new JFrame("Game Paused");

    public gameUnpause() {
        pause.setSize(30, 30);
        pause.setVisible(true);
        pause.addKeyListener(this);
        Galaga.gameActive = false;
    }

    @Override
    public void run() {
    }

    @Override
    public void keyTyped(KeyEvent ae) {
    }

    @Override
    public void keyPressed(KeyEvent ae) {
        if (ae.getKeyCode() == KeyEvent.VK_P) {
            pause.setVisible(false);
            pause.dispose();
            Galaga.gameActive = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ae) {
    }
}
