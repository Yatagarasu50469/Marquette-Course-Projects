/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author Computer
 */
class doomsdayCreate implements Runnable {

    public static int doomsdayMinTimerTime = 10000;
    public static int doomsdayMaxTimerTime = 20000;
    private static int doomsdayRandomEstablishedTime;
    private static Random doomsdayRandomTime;
    private static Timer doomsdayTimer;
    private static boolean doomsdayActive = false;
    private static int minXpos = 0;
    private static int maxXpos = Galaga.windowWidth - 40;
    private static int randomXpos;
    private static Random doomsdayRandomXfactor;

    public doomsdayCreate() {
    }

    @Override
    public void run() {
        while (true) {
            if (!doomsdayActive) {
                doomsdayRandomTime = new Random();
                doomsdayRandomXfactor = new Random();
                doomsdayRandomEstablishedTime = (doomsdayMinTimerTime + doomsdayRandomTime.nextInt(doomsdayMaxTimerTime - doomsdayMinTimerTime + 1));
                randomXpos = (minXpos + doomsdayRandomXfactor.nextInt(maxXpos - minXpos + 1));
            }

            ActionListener doomsdayArrival = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    doomsdayActive = false;
                    doomsdayTimer.stop();
                    if (Galaga.e5.size() < 1) {
                        Galaga.e5.add(new Doomsday(randomXpos, -30, 27, 100, 1, 1, 0, "images/Doomsday_Machine.png"));
                    }
                }
            };
            if (!doomsdayActive) {
                doomsdayTimer = new Timer(doomsdayRandomEstablishedTime, doomsdayArrival);
                doomsdayActive = true;
                doomsdayTimer.start();
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
    }
}
