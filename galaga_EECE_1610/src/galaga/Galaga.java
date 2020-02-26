/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;

/**
 *
 * @author David Helminiak
 */
public class Galaga extends Canvas implements KeyListener, MouseListener, MouseMotionListener, Runnable {

    private static final long serialVersionUID = 1L;
    private JFrame frame = new JFrame("Galaga");
    private BufferStrategy bufferStrategy = null;
    private Thread mainThread;
    private Thread game;
    public static int windowWidth = 555;
    public static int windowHeight = 580;
    public static int score = 0;
    public static int missileCounter = 0;
    public static int shipSpeed = 0;
    public static int e1Column = 0;
    public static int e1Row = 0;
    public static int e2Column = -106;
    public static int e2Row = 200;
    public static int e3Column = 600; //Sets initial x position
    public static int e3Row = 0;
    public static int e4Column = 0; //Sets initial x position
    public static int e4lColumn = -50;
    public static int e4rColumn = 525 + 50;
    public static int e4Row = 0;
    public static int missileLimit = 5;
    private String highscore = getHighScore(); //Retrieves the present high score string and value
    public static boolean gameActive = true;
    public static boolean windowOpen = false;
    public static boolean playerNumberWindowVisibility = true;
    public static boolean mouseInWindow = false;
    public static boolean panicMode1Active = false;
    public static boolean panicMode2Active = false;
    private static boolean panicModeActive = false;
    public static Color panicColor;
    public static boolean gameOpeningMenu = false; //Identifies whether the user has yet bypassed the opening menu
    public static boolean gameOpeningMenuMusicCheck = false; //Identifies whether the Opening Menu's music has begun
    public static boolean gameEnd = false; //Identifies whether the end game requirments have been met
    public static boolean shipMovementLeft = false; //Left key pressed (True || False)
    public static boolean shipMovementRight = false; //Right key (True || False)
    public static boolean wave1 = false;
    public static boolean wave1active = false;
    public static boolean wave2 = false;
    public static boolean wave2active = false;
    public static boolean wave3 = false;
    public static boolean wave3active = false;
    public static boolean wave4 = false;
    public static boolean wave4active = false;
    public static boolean wave5 = false;
    public static boolean wave5active = false;
    public static boolean shipCaptured = false;
    public static boolean addFighter = false;
    Image image;
    public static Graphics graphics;
    public static LinkedList<Missile> missiles = new LinkedList<Missile>(); //Array holding missiles

    public static LinkedList<Missile> getMissileBounds() {
        return missiles;
    }
    public static LinkedList<HatakBeam> beams = new LinkedList<HatakBeam>(); //Array holding Hatak Beams

    public static LinkedList<HatakBeam> getHatakBeamBounds() {
        return beams;
    }
    public static LinkedList<TieFighter> e1 = new LinkedList<TieFighter>(); //Array holding Level 1 enemy

    public static LinkedList<TieFighter> getTieFighterBounds() {
        return e1;
    }
    public static LinkedList<Hatak> e2 = new LinkedList<Hatak>(); //Array holding Level 2 enemy

    public static LinkedList<Hatak> getHatakBounds() {
        return e2;
    }
    public static LinkedList<Banshee> e3 = new LinkedList<Banshee>(); //Array holding Level 2 enemy

    public static LinkedList<Banshee> getBansheeBounds() {
        return e3;
    }
    public static LinkedList<CylonRaider> e4 = new LinkedList<CylonRaider>(); //Array holding Level 2 enemy

    public static LinkedList<CylonRaider> getCylonRaiderBounds() {
        return e4;
    }
    public static LinkedList<Doomsday> e5 = new LinkedList<Doomsday>(); //Array holding Level 2 enemy

    public static LinkedList<Doomsday> getDoomsdayBounds() {
        return e5;
    }
    public static LinkedList<BansheePlasma> plasmaBalls = new LinkedList<BansheePlasma>(); //Array holding Hatak Beams

    public static LinkedList<BansheePlasma> getBansheePlasmaBounds() {
        return plasmaBalls;
    }
    public static LinkedList<Ship> ships = new LinkedList<Ship>();
    public static LinkedList<DoomsdayBeam> doomsdayBeams = new LinkedList<DoomsdayBeam>(); //Array holding missiles

    public static LinkedList<DoomsdayBeam> getDoomsdayBeamBounds() {
        return doomsdayBeams;
    }
    public static LinkedList<capturedShip> e6 = new LinkedList<capturedShip>(); //Array holding missiles

    public static LinkedList<capturedShip> getcapturedShipBounds() {
        return e6;
    }
    public static LinkedList<OtherShips> otherShips = new LinkedList<OtherShips>(); //Array holding missiles

    public static LinkedList<OtherShips> getOtherShipsBounds() {
        return otherShips;
    }

    public Galaga() throws IOException {

        if (!windowOpen) {
            frame.setResizable(false); //Prevents the window from being resized
            frame.setSize(windowWidth, windowHeight); //Sets the window's size
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Sets the JFrame to exit when closed
            frame.setVisible(true); //Sets the window as visible upon the program's start
            frame.add(this);
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
            Optionpanel();
            mainThread = new Thread(this);
            windowOpen = true;
        }
        resetGame();
        game = new Thread(new Game());
        game.start(); //Runs Game class
    }

    public void paint(Graphics g) { //Set program graphics
        if (bufferStrategy == null) {
            createBufferStrategy(2); //create a second buffer which will run in the backgground and then switch with the first buffer's visibility
            bufferStrategy = getBufferStrategy(); //sets the bufferStrategy equal to the new Strategy
            graphics = bufferStrategy.getDrawGraphics(); //sets the canvas graphics equal to the graphics within the bufferStrategy
            mainThread.start();
        }
    }

    private void gameRender() {
        graphics.clearRect(0, 0, windowWidth, windowHeight); //Upon each screen rendering clear the screen of all visible images

        if (!gameEnd) { //Provided the game has not ended
            if (!panicMode1Active && !panicMode2Active) { //If the user has not selected the panic modes
                graphics.setColor(Color.black); //Set the default color to black
                graphics.fillRect(0, 0, windowWidth, windowHeight); //Fill the background with the set color
            }

            if (panicMode1Active || panicMode2Active) { //If the user has selected either of the two panic modes

                if (!panicModeActive) { //If the panicMode thread has not yet started
                    (new Thread(new PanicMode())).start(); //Start the panicMode thread
                    panicModeActive = true; //Set the thread's boolean identifier as presently active
                }
                graphics.setColor(panicColor); //Set the background color as set by the panicMode thread
                graphics.fillRect(0, 0, windowWidth, windowHeight); //Fill the background with the set color
            }
        }

        gameOpeningMenu();

        if (gameOpeningMenu) {
            gameStartPaint();
        }
        if (gameEnd) {
            EndGame();
        }
    }

    private void draw() {
        for (int i = 0; i < otherShips.size(); i++) {
            otherShips.get(i).draw(graphics);
        }
        for (int i = 0; i < e6.size(); i++) {
            e6.get(i).draw(graphics);
        }
        for (int i = 0; i < e5.size(); i++) {
            e5.get(i).draw(graphics);
        }
        for (int i = 0; i < e4.size(); i++) {
            e4.get(i).draw(graphics);
        }
        for (int i = 0; i < plasmaBalls.size(); i++) {
            plasmaBalls.get(i).draw(graphics);
        }
        for (int i = 0; i < e3.size(); i++) {
            e3.get(i).draw(graphics);
        }
        for (int i = 0; i < beams.size(); i++) {
            beams.get(i).draw(graphics);
        }
        for (int i = 0; i < e2.size(); i++) {
            e2.get(i).draw(graphics);
        }
        for (int i = 0; i < e1.size(); i++) {
            e1.get(i).draw(graphics);
        }
        for (int i = 0; i < missiles.size(); i++) {
            missiles.get(i).draw(graphics);
        }
        for (int i = 0; i < ships.size(); i++) {
            ships.get(i).draw(graphics);
        }
        for (int i = 0; i < doomsdayBeams.size(); i++) {
            doomsdayBeams.get(i).draw(graphics);
        }
    }

    private void updateDrawings() {

        for (int i = 0; i < otherShips.size(); i++) {
            otherShips.get(i).update(12);
        }
        for (int i = 0; i < e6.size(); i++) {
            e6.get(i).update(11);
        }
        for (int i = 0; i < doomsdayBeams.size(); i++) {
            doomsdayBeams.get(i).update(10);
        }
        for (int i = 0; i < e5.size(); i++) {
            e5.get(i).update(9);
        }
        for (int i = 0; i < e4.size(); i++) {
            e4.get(i).update(8);
        }
        for (int i = 0; i < plasmaBalls.size(); i++) {
            plasmaBalls.get(i).update(7);
        }
        for (int i = 0; i < e3.size(); i++) {
            e3.get(i).update(6);
        }
        for (int i = 0; i < beams.size(); i++) {
            beams.get(i).update(5);
        }
        for (int i = 0; i < e2.size(); i++) {
            e2.get(i).update(4);
        }
        for (int i = 0; i < e1.size(); i++) {
            e1.get(i).update(3);
        }
        for (int i = 0; i < missiles.size(); i++) {
            missiles.get(i).update(2);
        }
        for (int i = 0; i < ships.size(); i++) {
            ships.get(i).update(1);
        }
    }

    private void Optionpanel() {
        new OpeningOptionPanel();
    }

    private void gameOpeningMenu() {
        if (!gameOpeningMenu) { //If the Menu has not yet been bypassed by the user
            graphics.drawImage(Toolkit.getDefaultToolkit().getImage("images/GalagaMainMenu.png"), 0, 0, null);
        }
    }

    private void gameStartPaint() {
        if (!gameEnd) {
            draw();
            if (gameActive) {
                updateDrawings();
            }
        }

        graphics.setColor(Color.yellow);
        graphics.drawString("Score " + score, 30, 30); //Shows the current player's score value during gameplay
        graphics.drawString("High Score: " + highscore, 100, 30); //Shows the game's highscore during gameplay
        try {
            if (ships.get(1).slife <= 0) {
                gameEnd = true;
            }
            graphics.drawString("Player 1 Lives: " + ships.get(1).slife, 350, 30); //Shows the number of lives remaining during gameplay

        } catch (Exception e) {
        }
    }

    public void EndGame() {
        if (ships.get(1).slife <= 0) { //Upon Failing the game
            checkHighScore();
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, windowWidth, windowHeight);
            graphics.setColor(Color.red);
            graphics.drawString("You lose", 250, 190);
            graphics.drawString("Your final score was: " + score, 250, 160);
            clearCurrentGame();
            graphics.drawString("Please press 'esc' to exit the game or 'r' to reset.", 250, 210);
        }

        if (ships.get(1).slife > 0) { //Upon successfull completion of the game; 
            checkHighScore();
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, windowWidth, windowHeight);
            graphics.setColor(Color.white);
            graphics.drawString("Congrulation! Yove Suckcesssfly complete this thing!", 250, 160);
            graphics.drawString("Your final score was: " + score, 250, 190);
            clearCurrentGame();
            graphics.drawString("Please press 'esc' to exit the game or 'r' to reset.", 250, 210);
        }
    }

    public String getHighScore() {
        FileReader readFile = null; //initializes a file reader
        BufferedReader reader = null; //initializes a buffer reader to read the data from the file
        try {
            readFile = new FileReader("data/highscore.dat");
            reader = new BufferedReader(readFile);
            return reader.readLine(); //Reads the first line of the specified file and return values
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                reader.close(); //closes reader upon completion of returning the specified data
            } catch (IOException e) {
            }
        }
    }

    private void checkHighScore() {
        if (score > Integer.parseInt(highscore.split(":")[1])) { //If the current player's score has exceeded the previous highscore
            String nameInput = JOptionPane.showInputDialog("Congratulations! You have set a a new high score. \nPlease enter your name to protect your legacy of high acheivment: ");//Retreives user identification via a java option panel
            highscore = nameInput + ":" + score; //Changes the official highscore of the game to that of the current player

            File highscoreData = new File("data/highscore.dat");
            if (!highscoreData.exists()) { //If the file does not exist
                try {
                    highscoreData.createNewFile(); //Create the file
                } catch (IOException e) {
                } //In the event of an error
            }
            FileWriter writeFile = null; //Initialize FileWriter
            BufferedWriter writer = null; //Initialize BufferedWriter

            try {
                writeFile = new FileWriter(highscoreData); //Open file specified by highscoreData, to write to it
                writer = new BufferedWriter(writeFile);
                writer.write(this.highscore); //Write the new highscore string to the file

            } catch (Exception e) {
            } finally {
                try {
                    if (writer != null) { //If the data supposed to be written by the writer is confirmed
                        writer.close(); //Close the writer
                    }
                } catch (Exception e) {
                } //In the event of an error
            }
        }
    }

    private void clearCurrentGame() {
        wave1 = false;
        wave2 = false;
        wave3 = false;
        wave4 = false;
        wave5 = false;
        e1.removeAll(e1);
        e2.removeAll(e2);
        e3.removeAll(e3);
        e4.removeAll(e4);
        e5.removeAll(e5);
        e6.removeAll(e6);
        doomsdayBeams.removeAll(doomsdayBeams);
        beams.removeAll(beams);
        plasmaBalls.removeAll(plasmaBalls);
        otherShips.removeAll(otherShips);
        missiles.removeAll(missiles);
    }

    private void resetGame() { //Resets the main game variables
        clearCurrentGame();
        score = 0; //Resets the user's score to zero in case of restart
        gameEnd = false;
        ships.add(new Ship(520, 500, 45, 54, 10, 0, 0, "images/NSEA_Protector.png"));  //Creates an instance of a ship
        wave1active = false;
        wave2active = false;
        wave3active = false;
        wave4active = false;
        wave5active = false;
    }

    private void close() {
        System.exit(0);
    }

    @Override
    public void run() {
        while (true) {
            bufferStrategy.show();
            gameRender();

            Thread.currentThread();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            try {
                new Galaga();
            } catch (IOException ex) {
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && shipSpeed <= 5) {
            shipSpeed += 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            Thread paused = new Thread(new gameUnpause());
            paused.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && shipSpeed > 0) {
            shipSpeed -= 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            shipMovementLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            shipMovementRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && missileCounter < missileLimit) {
            if (!gameOpeningMenu) {
                gameOpeningMenu = true;
            }
            fireMissile();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            close();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            close();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            shipMovementRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            shipMovementLeft = false;
        }

    }

    /**
     * @param args the command line arguments
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        if (!gameOpeningMenu) {
            gameOpeningMenu = true;
        }
        try {
            if (missileCounter < 5) {
                Missile missile = new Missile(ships.get(1).sxpos + 18, ships.get(1).sypos - 18, 10, 26, 1, 0, -3, "images/missile.png");
                missiles.add(missile); //Creates and adds a new instance of a Missle to array
                missileCounter++;
            }
        } catch (java.lang.IndexOutOfBoundsException a) {
        } //Catches an exception when missiles are created on the main menu
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        mouseInWindow = true;
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TRANSLUCENT), new Point(0, 0), "blank")); //Creates a blank translucent image with width: 1, height 1, to a point x:0, y:0 with the name: blank as the present cursor type
    }

    @Override
    public void mouseExited(MouseEvent me) {
        mouseInWindow = false;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (mouseInWindow && gameActive) {
            Ship.sxpos = me.getX();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        if (!gameOpeningMenuMusicCheck) { //If the Opening Menu Music is not presently on then enact the following code
            try {
                Sounds.gameOpeningMenuMusic(); //Attempt to open method containing Opening sound files
            } catch (IOException ex) {
            }
        }
        new Galaga();
    }

    private void fireMissile() {
        try {
            try {
                Missile missile = new Missile(Galaga.ships.getFirst().sxpos + 18, Galaga.ships.getFirst().sypos - 18, 10, 26, 1, 0, -3, "images/missile.png");
                missiles.add(missile);
                missileCounter++;
                for (int i = 0; i < otherShips.size(); i++) {
                    Missile missile2 = new Missile(Galaga.ships.getFirst().sxpos + (45 * (i + 1) + 18), otherShips.getFirst().s2ypos - 18, 10, 26, 1, 0, -3, "images/missile.png");
                    missiles.add(missile2);
                    missileCounter++;
                }
            } catch (NoSuchElementException f) {
            }
        } catch (java.lang.IndexOutOfBoundsException a) {
        } //Catches an exception when missiles are created on the main menu

        if (otherShips.size() == 0) {
            missileLimit = 5;
        }
        if (otherShips.size() == 1) {
            missileLimit = 10;
        }
        if (otherShips.size() >= 2) {
            missileLimit = 20;
        }
    }
}