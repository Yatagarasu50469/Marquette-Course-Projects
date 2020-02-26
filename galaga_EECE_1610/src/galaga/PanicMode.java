/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Computer
 */
public class PanicMode implements Runnable {

    @Override
    public void run() {
        if (Galaga.panicMode1Active) {
            while (true) {
                panicMode1Paint();
            }
        }
        if (Galaga.panicMode2Active) {
            while (true) {
                panicMode2Paint();
            }
        }

    }

    private void panicMode1Paint() {
        Galaga.panicColor = Color.red;
        panicMode1Pause();
        Galaga.panicColor = Color.orange;
        panicMode1Pause();
        Galaga.panicColor = Color.yellow;
        panicMode1Pause();
        Galaga.panicColor = Color.green;
        panicMode1Pause();
        Galaga.panicColor = Color.blue;
        panicMode1Pause();
        Galaga.panicColor = Color.magenta;
        panicMode1Pause();

    }

    private void panicMode2Paint() {
        Galaga.panicColor = Color.black;
        panicMode2Pause();
        Galaga.panicColor = Color.white;
        panicMode2Pause();
    }

    private void panicMode1Pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
    }

    private void panicMode2Pause() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
    }
}
