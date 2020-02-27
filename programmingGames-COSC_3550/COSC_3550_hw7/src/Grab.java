/**
 *  Program: COSC_3550_hw7 - Grab
 *  Version: 1.0
 *  Original author: NetOthello (Black Art of Java Game Programming)
 *  Modified: Mike Slattery, April 2000; MSCS, application, March 2015; MSCS, JavaFX, March 2018
 *  Version Author: David Helminiak
 *  Date Created: April 2000
 *  Date Last Modified: 15 April 2018
 *  Purpose: Provide a network game experience using a pair of clients and game server. Players are tasked
 *  to grab all of the coins. Whoever has the most wins.
 *  
 *  	==Requirements==			==Implemented==		==Note==
 *  	Grab command				Yes					"G" key
 *  	Blast command				Yes					"B" key
 *  	Blast limits				Yes					4 explosives allowed per player
 *  	Score display				Yes
 *  	Explosion display			Yes
 *  	Synchronize displays		Yes
 *
 *   	==Extra Goals==				==Implemented==		==Note==
 *   	Unique explosive sounds		Yes
 *   	Unique coin sounds			Yes
 *   	Background sound			Yes
 *   	Unique end game scenarios	Yes					Win, Lose, tie scenarios with unique end sounds
 *   	Block explosion animation	Yes
 *   
 *		==User Controls==			==Action==
 *		Mouse:						NULL
 *		Key:	UP					Move forward
 *				LEFT				Turn to the left
 *				RIGHT				Turn to the right
 *				G					Grab coin in front of player
 *				B					Destroy block in front of player
 */

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.net.*;
import javafx.scene.image.Image;
import java.io.*;

public class Grab extends Application implements Runnable {
	final String appName = "Grab";
	GraphicsContext gc; // declare here to use in handlers
	boolean gameOver = false;
	int remainingCoins = 100; //How many coins are remaining on screen
	Font font = Font.font("TimesRoman", 30.0); //Define font to use
	AudioClip redCoinSound, blueCoinSound, redExplodeSound, blueExplodeSound, backgroundSound, goodGameEndSound, badGameEndSound; //Declare audio clips
	Image exp1, exp2, exp3; //Define holders for explosion sprites
	
	/* the Thread */
	Thread kicker;

	int grid[][] = new int[GameGroup.GWD][GameGroup.GHT]; // Game board
	public static final int CELLSIZE = 64;
	public static final int WIDTH = GameGroup.GWD * CELLSIZE;
	public static final int HEIGHT = GameGroup.GHT * CELLSIZE + 50; //Extend height by 100 for status area
	boolean setup = false; // record whether we've got the board yet
	Player blue = null, red = null;
	String my_name = "white"; //Initialize player name to unlikely value

	/* the network stuff */
	PrintWriter pw;
	Socket s = null;
	BufferedReader br = null;
	String name, theHost = "localhost";
	int thePort = 2001;

	void initialize() {
		gc.setFont(font); //Set the font to be used
		
		//Load in sounds to use
		redCoinSound = new AudioClip(ClassLoader.getSystemResource("sounds/redCoin.wav").toString());
		blueCoinSound = new AudioClip(ClassLoader.getSystemResource("sounds/blueCoin.wav").toString());
		redExplodeSound = new AudioClip(ClassLoader.getSystemResource("sounds/redExplode.wav").toString());
		blueExplodeSound = new AudioClip(ClassLoader.getSystemResource("sounds/blueExplode.wav").toString());
		goodGameEndSound = new AudioClip(ClassLoader.getSystemResource("sounds/goodGameEnd.wav").toString());
		badGameEndSound = new AudioClip(ClassLoader.getSystemResource("sounds/badGameEnd.wav").toString());
		backgroundSound = new AudioClip(ClassLoader.getSystemResource("sounds/background.wav").toString());

		//Load in images to use
		exp1 = new Image("/images/exp0.jpg");
		exp2 = new Image("/images/exp1.jpg");
		exp3 = new Image("/images/exp2.jpg");
		
		backgroundSound.setCycleCount(Animation.INDEFINITE); //Set background sounds to loop
		backgroundSound.play();
		
		makeContact();
		/* start a new game */
		/* start the thread */
		kicker = new Thread(this);
		kicker.setPriority(Thread.MIN_PRIORITY);
		kicker.setDaemon(true);
		kicker.start();
		render(gc);
	}

	private void makeContact()
	// contact the GameServer
	{
		/* ok, now make the socket connection */
		while (s == null)
			try {
				System.out.println("Attempting to make connection:" + theHost + ", " + thePort);
				s = new Socket(theHost, thePort);
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				pw = new PrintWriter(s.getOutputStream());
			} catch (Exception e) {
				System.out.println(e);
				try {
					Thread.sleep(7500);
				} catch (Exception ex) {
				}
				;
			}

		System.out.println("Connection established");

	} // end of makeContact()

	/* the main Thread loop */
	public void run() {

		/*
		 * Here is the main network loop Wait for messages from the server
		 */
		while (kicker != null) {
			String input = null;
			
			while (input == null)
				try {
					Thread.sleep(100);
					input = br.readLine();
				} catch (Exception e) {
					input = null;
				}

			System.out.println("Got input:" + input);

			// Chop up the message and see what to do
			String[] words = input.split(",");
			String cmd = words[0];

			/* if we are ready to start a game */
			if (cmd.equals("start")) {
				fillGrid(words[1]);
				setup = true;
				render(gc);
			} else if (cmd.equals("who")) {
				my_name = words[1];
				render(gc);
			} else if (cmd.equals("blue")) {
				try {
					if (blue == null)
						blue = new Player(0, 0, 0, Color.BLUE, 0, 0);
					blue.x = Integer.valueOf(words[1]).intValue();
					blue.y = Integer.valueOf(words[2]).intValue();
					blue.dir = Integer.valueOf(words[3]).intValue();
					blue.grab = Integer.valueOf(words[4]).intValue();
					blue.blast = Integer.valueOf(words[5]).intValue();
					blue.score = Integer.valueOf(words[6]).intValue();
					blue.sticks = Integer.valueOf(words[7]).intValue();
					if (blue.grab == 1) {
						blueCoinSound.play();
					}
					if (blue.blast == 1) {
						blueExplodeSound.play();
					}
				} catch (Exception e) {
				}
				; // if nonsense message, just ignore it
				render(gc);
			} else if (cmd.equals("red")) {
				try {
					if (red == null)
						red = new Player(0, 0, 0, Color.RED, 0, 0);
					red.x = Integer.valueOf(words[1]).intValue();
					red.y = Integer.valueOf(words[2]).intValue();
					red.dir = Integer.valueOf(words[3]).intValue();
					red.grab = Integer.valueOf(words[4]).intValue();
					red.blast = Integer.valueOf(words[5]).intValue();
					red.score = Integer.valueOf(words[6]).intValue();
					red.sticks = Integer.valueOf(words[7]).intValue();
					if (red.grab == 1) {
						redCoinSound.play();
					}
					if (red.blast == 1) {
						redExplodeSound.play();
					}
					
				} catch (Exception e) {
				}
				; // if nonsense message, just ignore it
				render(gc);
			} else if (cmd.equals("board")) {
				System.out.println("GRID CHANGE RECEIVED");
				if(Integer.valueOf(words[1]).intValue() == 1) { 
					grid[Integer.valueOf(words[2]).intValue()][Integer.valueOf(words[3]).intValue()] = 4; //Indicate there is an explosion in the space
					System.out.println("EXPLOSION SET");
				}
				else if (Integer.valueOf(words[1]).intValue() == 2) {
					grid[Integer.valueOf(words[2]).intValue()][Integer.valueOf(words[3]).intValue()] = 0; //Indicate a coin has been taken
					System.out.println("COIN GRAB INDICATED");
				}
				
			}
		} 
	}

	void fillGrid(String board) {
		// Fill in the grid array with the values
		// in the String board.
		int x, y, i = 0;
		char c;

		for (y = 0; y < GameGroup.GHT; y++)
			for (x = 0; x < GameGroup.GWD; x++) {
				c = board.charAt(i);
				i++;
				switch (c) {
				case '0':
					grid[x][y] = 0;
					break;
				case '1':
					grid[x][y] = 1;
					break;
				case '2':
					grid[x][y] = 2;
					break;
				}
			}
	}

	/* if the Thread stops, be sure to clean up! */
	public void finalize() {

		try {
			br.close();
			pw.close();
			s.close();
		} catch (Exception e) {
		}
		;
	}

	public void render(GraphicsContext gc) {
		int x, y;
		remainingCoins = 0; //Reset remaining coin count
		
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		
		if (!setup) {
			gc.setFill(Color.BLACK);
			gc.fillText("Waiting...", 50, 50);
		} else if (!gameOver){
			// Draw static board
			for (x = 0; x < GameGroup.GWD; x++)
				for (y = 0; y < GameGroup.GHT; y++) {
					if (grid[x][y] == 1) {
						gc.setFill(Color.GRAY);
						gc.fillRect(CELLSIZE * x, CELLSIZE * y, CELLSIZE - 1, CELLSIZE - 1);
					} else if (grid[x][y] == 2) {
						remainingCoins++; //Augment the remaining coin count
						gc.setFill(Color.ORANGE);
						gc.fillOval(CELLSIZE * x + 2, CELLSIZE * y + 2, CELLSIZE - 4, CELLSIZE - 4);
					} 
				}
			
			gc.setStroke(Color.BLACK);
			gc.strokeRect(0, 0, WIDTH, HEIGHT);
			gc.strokeLine(0, HEIGHT-50, WIDTH, HEIGHT-50); //Separate game area from stats area
			
			// Add the players if they're there
			if (blue != null) {
				blue.render(gc);
				if (my_name.equals("blue")) {
					gc.setFill(Color.BLACK);
					gc.fillText(("Player: " + my_name), 20, HEIGHT-20);
					gc.fillText(("SCORE: " + blue.score), 220, HEIGHT-20);
					gc.fillText(("EXPLOSIVES: " + blue.sticks), 420, HEIGHT-20);
				}
			}
			if (red != null) {
				red.render(gc);
				if (my_name.equals("red")) {
					gc.fillText(("Player: " + my_name), 20, HEIGHT-20);
					gc.fillText(("SCORE: " + red.score), 220, HEIGHT-20);
					gc.fillText(("EXPLOSIVES: " + red.sticks), 420, HEIGHT-20);
				}
			}
			
			
			//Now check for animations
			for (x = 0; x < GameGroup.GWD; x++) 
				for (y = 0; y < GameGroup.GHT; y++) {
					 if (grid[x][y] == 4) {
						try {
							gc.drawImage(exp1, CELLSIZE * x, CELLSIZE * y, CELLSIZE - 1, CELLSIZE - 1);
							Thread.sleep(100);
							gc.drawImage(exp2, CELLSIZE * x, CELLSIZE * y, CELLSIZE - 1, CELLSIZE - 1);
							Thread.sleep(100);
							gc.drawImage(exp3, CELLSIZE * x, CELLSIZE * y, CELLSIZE - 1, CELLSIZE - 1);
							Thread.sleep(100);
						} catch (InterruptedException e) { System.out.println("Explosion animation error"); }
						gc.setFill(Color.WHITE);
						gc.fillRect(CELLSIZE * x, CELLSIZE * y, CELLSIZE - 1, CELLSIZE - 1);
						grid[x][y] = 0; //Indicate that the block has been removed
					}
				}
			
			if (remainingCoins == 0) { //If the number of coins on screen have all been depleted
				gameOver=true; //Indicate that the game should end
			}
			
		}
		if (gameOver) { //If a game over state occurs
			System.out.println("Game has been completed");
			backgroundSound.stop();
			
			gc.setFill(Color.WHITE);
			gc.fillRect(0, 0, WIDTH, HEIGHT);
			if (red.score > blue.score) {
				if (my_name.equals("red")) {
					System.out.println("Playing good end game song");
					goodGameEndSound.play();
				}
				else {
					System.out.println("Playing bad end game song");
					badGameEndSound.play();
				}
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setTextBaseline(VPos.CENTER);
				gc.setFill(Color.BLACK);
				gc.fillText(("RED WAS THE WINNER" + "\nRED SCORE: " + red.score + "\nBLUE SCORE: " + blue.score), WIDTH/2, HEIGHT/2);
			}
			else if (blue.score > red.score) {
				if (my_name.equals("blue")) {
					System.out.println("Playing good end game song");
					goodGameEndSound.play();
				}
				else {
					System.out.println("Playing bad end game song");
					badGameEndSound.play();
				}
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setTextBaseline(VPos.CENTER);
				gc.setFill(Color.BLACK);
				gc.fillText(("BLUE WAS THE WINNER" + "\nBLUE SCORE: " + blue.score + "\nRED SCORE: " + red.score), WIDTH/2, HEIGHT/2);
			}
			else if (blue.score == red.score) {
				goodGameEndSound.play();
				System.out.println("Playing good end game song");
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setTextBaseline(VPos.CENTER);		
				gc.setFill(Color.BLACK);
				gc.fillText(("TIED GAME!" + "\nSCORE: " + red.score), WIDTH/2, HEIGHT/2);		
			}
			else {
				System.out.println("ERROR: Something has gone wrong with the scoring mechanism");
			}
		}
	}

	public void tellServer(String msg) {
		/* send a message to the server */
		boolean flag = false;
		while (!flag) // we keep trying until it's sent
			try {
				pw.println(msg);
				pw.flush();
				flag = true;
			} catch (Exception e1) {
				flag = false;
			}
	}

	void setHandlers(Scene scene) {
		scene.setOnKeyPressed(e -> {
			KeyCode c = e.getCode();
			switch (c) {
			case J:
			case LEFT:
				tellServer("turnleft," + my_name);
				break;
			case L:
			case RIGHT:
				tellServer("turnright," + my_name);
				break;
			case K:
			case UP:
				tellServer("step," + my_name);
				break;
			case G:
				tellServer("grab," + my_name);
				System.out.println(my_name + " told server to grab a coin");
				break;
			case B:
				tellServer("blast," + my_name);
				System.out.println(my_name + " told server to blast");
				break;
			default: /* Do Nothing */
				break;
			}
		});
	}

	/*
	 * Begin boiler-plate code... [Events with initialization]
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		theStage.setTitle(appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		// Initial setup
		initialize();

		setHandlers(theScene);

		theStage.show();
	}
	/*
	 * ... End boiler-plate code
	 */
}
