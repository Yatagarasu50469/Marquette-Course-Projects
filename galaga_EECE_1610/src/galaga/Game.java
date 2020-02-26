package galaga;

import static galaga.Galaga.e1;
import static galaga.Galaga.e2;
import static galaga.Galaga.e3;
import static galaga.Galaga.e4;
import static galaga.Galaga.wave1;
import static galaga.Galaga.wave2;
import static galaga.Galaga.wave3;
import static galaga.Galaga.wave4;
import static galaga.Galaga.wave5;
import static galaga.Galaga.wave1active;
import static galaga.Galaga.wave2active;
import static galaga.Galaga.wave3active;
import static galaga.Galaga.wave4active;
import static galaga.Galaga.wave5active;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Computer
 */
public class Game implements Runnable {

    private static boolean doomsdayCountdown = false;

    public static void main(String[] args) {
    }

    private void wavePause() { //Creates a pause between waves
        try {
            Thread.sleep(2000); //Pauses the thread for a set time
        } catch (InterruptedException ex) {
        }
    }

    private void wave1() {
        if (!wave1active) {
            wave1active = true;
            Galaga.e1Row = 250; //Specifies the initial row to be created
            for (int i = 0; i < 2; i++) { //Specifies number of columns to create

                for (int j = 0; j < 2; j++) { //Specifies number of rows to create
                    e1.add(new TieFighter(Galaga.e1Column, Galaga.e1Row, 46, 60, 1, 1, 0, "images/TieFighter.png"));
                    Galaga.e1Column += 60;
                }
                Galaga.e1Column = 0; //Resets Column number to original position
                Galaga.e1Row += 46; //Identifies and sets the position of the next row to be created
            }
        }
        if (e1.size() == 0) { //If the first wave is passed
            wave1 = true;
            wavePause();
            wave2();
        }
    }

    private void wave2() {
        if (!wave2active) {
            wave2active = true;

            Galaga.e1Column = 300;
            Galaga.e1Row = 300; //Specifies the initial row to be created
            for (int i = 0; i < 3; i++) { //Specifies number of columns to create

                for (int j = 0; j < 3; j++) { //Specifies number of rows to create
                    e1.add(new TieFighter(Galaga.e1Column, Galaga.e1Row, 46, 60, 1, 1, 0, "images/TieFighter.png"));
                    Galaga.e1Column += 60;
                }
                Galaga.e1Column = 300; //Resets Column number to original position
                Galaga.e1Row += 46; //Identifies and sets the position of the next row to be created
            }


            Galaga.e2Row = 100; //Specifies the initial row to be created
            for (int i = 0; i < 1; i++) { //Specifies number of rows to create

                for (int j = 0; j < 3; j++) { //Specifies number of columns to create
                    e2.add(new Hatak(Galaga.e2Column, Galaga.e2Row, 106, 60, 1, 1, 0, "images/Hatak.png"));
                    Galaga.e2Column -= 106;
                }
                Galaga.e2Column = 0; //Resets Column number to original position
                Galaga.e2Row += 70; //Identifies and sets the position of the next row to be created
            }
        }
        if (e1.size() == 0 && e2.size() == 0) {
            wave2 = true;
            wavePause();
            wave3();
        }
    }

    private void wave3() {
        if (!wave3active) {
            wave3active = true;
            Galaga.e2Row = 100; //Specifies the initial row to be created
            for (int i = 0; i < 1; i++) { //Specifies number of rows to create

                for (int j = 0; j < 3; j++) { //Specifies number of columns to create
                    e2.add(new Hatak(Galaga.e2Column + 600, Galaga.e2Row, 106, 60, 1, 1, 0, "images/Hatak.png"));
                    Galaga.e2Column += 106;
                }
                Galaga.e2Column = 0; //Resets Column number to original position
                Galaga.e2Row += 300; //Identifies and sets the position of the next row to be created
            }
            Galaga.e2Row = 400; //Specifies the initial row to be created
            for (int i = 0; i < 1; i++) { //Specifies number of rows to create

                for (int j = 0; j < 3; j++) { //Specifies number of columns to create
                    e2.add(new Hatak(Galaga.e2Column, Galaga.e2Row, 106, 60, 1, 1, 0, "images/Hatak.png"));
                    Galaga.e2Column -= 106;
                }
                Galaga.e2Column = 0; //Resets Column number to original position
                Galaga.e2Row += 300; //Identifies and sets the position of the next row to be created
            }
            Galaga.e1Row = 125; //Specifies the initial row to be created
            Galaga.e1Column = 200;
            for (int i = 0; i < 4; i++) { //Specifies number of rows to create

                for (int j = 0; j < 4; j++) { //Specifies number of columns to create
                    e1.add(new TieFighter(Galaga.e1Column, Galaga.e1Row, 46, 60, 1, 1, 0, "images/TieFighter.png"));
                    Galaga.e1Column += 90;
                }
                Galaga.e1Column = 200; //Resets Column number to original position
                Galaga.e1Row += 70; //Identifies and sets the position of the next row to be created
            }

        }

        if (e1.size() == 0 && e2.size() == 0) {
            wave3 = true;
            wavePause();
            wave4();
        }
    }

    private void wave4() {
        if (!wave4active) {
            wave4active = true;

            Galaga.e2Row = 100; //Specifies the initial row to be created
            for (int i = 0; i < 2; i++) { //Specifies number of rows to create

                for (int j = 0; j < 3; j++) { //Specifies number of columns to create
                    e2.add(new Hatak(Galaga.e2Column, Galaga.e2Row, 106, 60, 1, 1, 0, "images/Hatak.png"));
                    Galaga.e2Column -= 106;
                }
                Galaga.e2Column = 0; //Resets Column number to original position
                Galaga.e2Row += 300; //Identifies and sets the position of the next row to be created
            }
            Galaga.e3Row = 200; //Specifies the initial row to be created
            for (int i = 0; i < 1; i++) { //Specifies number of rows to create

                for (int j = 0; j < 3; j++) { //Specifies number of columns to create
                    e3.add(new Banshee(Galaga.e3Column, Galaga.e3Row, 40, 30, 1, 1, 0, "images/Banshee.png"));
                    Galaga.e3Column += 35; //Distance between characters;
                }
            }
        }

        if (e2.size() == 0 && e3.size() == 0) {
            wave4 = true;
            wavePause();
            wave5();
        }
    }

    private void wave5() {
        if (!wave5active) {
            wave5active = true;

            Galaga.e4Row = 100; //Specifies the initial row to be created
            for (int i = 0; i < 3; i++) { //Specifies number of rows to create

                for (int j = 0; j < 1; j++) { //Specifies number of columns to create
                    e4.add(new CylonRaider(Galaga.e4lColumn, Galaga.e4Row, 106, 60, 1, 1, 0, true, "images/CylonRaiderFlipped.png", "images/CylonRaider.png"));
                    Galaga.e4Column -= 0;
                }
                Galaga.e4Column = 0; //Resets Column number to original position
                Galaga.e4Row += 50; //Identifies and sets the position of the next row to be created
            }

            Galaga.e4Row = 100; //Specifies the initial row to be created        
            for (int i = 0; i < 3; i++) { //Specifies number of rows to create

                for (int j = 0; j < 1; j++) { //Specifies number of columns to create
                    e4.add(new CylonRaider(Galaga.e4rColumn, Galaga.e4Row, 106, 60, 1, 1, 0, false, "images/CylonRaiderFlipped.png", "images/CylonRaider.png"));
                    Galaga.e4Column += 50;
                }
                Galaga.e4Column = 0; //Resets Column number to original position
                Galaga.e4Row += 50; //Identifies and sets the position of the next row to be created
            }

            Galaga.e3Row = 200; //Specifies the initial row to be created
            for (int i = 0; i < 1; i++) { //Specifies number of rows to create

                for (int j = 0; j < 3; j++) { //Specifies number of columns to create
                    e3.add(new Banshee(Galaga.e3Column, Galaga.e3Row, 40, 30, 1, 1, 0, "images/Banshee.png"));
                    Galaga.e3Column += 35; //Distance between characters;
                }
            }
            Galaga.e3Row = 100; //Specifies the initial row to be created
            for (int i = 0; i < 1; i++) { //Specifies number of rows to create

                for (int j = 0; j < 3; j++) { //Specifies number of columns to create
                    e3.add(new Banshee(Galaga.e3Column, Galaga.e3Row, 40, 30, 1, 1, 0, "images/Banshee.png"));
                    Galaga.e3Column += 35; //Distance between characters;
                }
            }
        }

        if (e3.size() == 0) {
            wave5 = true;
            wavePause();
            Galaga.gameEnd = true;
        }
    }

    @Override
    public void run() {
        if (!doomsdayCountdown) {
            Thread doomsdayCreate = new Thread(new doomsdayCreate());
            doomsdayCreate.start();
            doomsdayCountdown = true;
        }

        while (!Galaga.gameEnd) {
            wave1();
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
            }
        }
    }
}
