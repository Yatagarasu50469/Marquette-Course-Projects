/**
 *  Program: COSC_3550_hw5 - Romeo! Galactic Adventure
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 24 January 2018
 *  Date Last Modified: 10 March 2018
 *  Purpose: Use both animation and user input to form an interactive game.
 *  
 *  	==Requirements==			==Implemented==		==Note==
 * 		Splash Screen 				Yes					
 * 		Instructions				Yes					
 * 		Unique level structure		Yes					
 * 		Level change (timed)		Yes					Use "testCutsceneMode = true" to test easily
 * 		10 levels					Yes					Use "testCutsceneMode = true" to test easily
 * 		Generic object class		Yes
 * 		Basic Player movement		Yes
 * 		Player boundaries			Yes
 * 		Basic player console		Yes
 * 		Scoring mechanism			Yes
 * 		User console				Yes
 * 		Enemy boundaries			Yes
 * 		Object collision detection	Yes
 * 		Rand. enemy construction	Yes
 * 		Enemy 1 					Yes					Asteroid
 * 		Enemy 2						Yes					Mobile ship
 * 		Enemy 3						Yes					Mobile ship that fires a lot more often
 * 		Enemy 4						Yes					Bounces and fires along x axis
 * 		Enemy 5						Yes					Comes in from sides and fires homing weapon
 * 		Weapon 1					Yes					Basic two weapons
 * 		Weapon 2					Yes					Fires a spread that has limited lifetime
 * 		Weapon 3					Yes					Weapon that can hit 3 enemies before being destroyed
 * 		Weapon 4					Yes					Homing missile; towards nearest target at time of launch
 *		Weapons as slot upgrades	Yes
 * 		Switch  weapon types		Yes
 * 		Spawn diff. en. types		Yes
 * 		Player collision detection	Yes
 * 		Basic sound	effects			Yes
 * 		Good end game scenario		Yes					
 * 		Bad end game scenario		Yes			
 * 		High score screen			Yes	
 * 		Mouse controls				Yes
 * 		Reset Game					Yes
 *   	
 *   	==Extra Goals==				==Implemented==		==Note==
 *   	Breakable en. 1				Yes					Enemy 1 breaks up over time
 * 		Cutscenes					No
 * 		Collision animations		No
 *   	Finished screens			No
 *   	Full sound effects			No
 * 		Background music			Yes
 * 		Image overlay				No
 * 		Background scenery			Yes
 * 		Custom font					Yes
 * 		Pausing						No
 *		
 *		==User Controls==			==Action==
 *		Mouse:						Ship follows mouse movement when inside scene
 *				left click			Fire
 *				right click			Cycle weapons
 *		Key:	w					Move up
 *				s					Move down
 *				a					Move left
 *				d					Move right
 *				SPACE				Fire
 *				1					Switch to weapon 1
 *				2					Switch to weapon 2
 *				3					Switch to weapon 3
 *				4					Switch to weapon 4
 *
 *	References:
 *  https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
 *  https://github.com/zacstewart/Asteroids/blob/master/src/asteroids/Asteroid.java
 *  https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#setTranslateX-double-
 */

//Import statements
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.util.*;
import javafx.scene.input.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.media.AudioClip;
import javafx.scene.text.TextAlignment;

public class primary extends Application{
	//Global variables - Alterable
	String winTitle = "Romeo! Galactic Adventure"; // Window title
	double winWidth = 1000; //Window screen width
	double winHeight = 1000; //Window screen height
	int frameCounter = 0; //Counter for how many frames have passed so far
	Color backgroundColor = Color.BLACK; //Base background color
	static boolean debugMode = false; //Should the game output debug messages in the console
	boolean testCutsceneMode = false; //Shortens each level to run for 2 seconds
	
	//Global variables - Constant
	Group primaryGroup; //Declare a new group
	Scene primaryScene; //Declare a new scene
	Canvas primaryCanvas; //Declare a new canvas
    GraphicsContext gc; //Declare a graphics context 
    KeyFrame keyFrame; //Declare a key frame for the animation
    Timeline primaryLoop; //Establish an animation timeline
    int globalTimer = 0; //Total number of seconds the game has been running
    int fCounter = 0; // Number of frames elapsed out of the set FPS
    static int score = 0; //Player's current score
    int currLevel = 1; //The current level
    int introScene = 0; //Which scene in the introduction should be displayed
    int maxEnemyLevel = 0; //Maximum number of enemy types allowed for a particular level
    int numEnemies = 0; //How many enemies are currently in existence
    int maxEnemies = 0; //What is the maximum number of enemies that should be allowed on screen
    int enemyType = 0; //What type of enemy should be generated
    static int lives = 4; //How many extra lives does the player have remaining
    int numObjects; //How many objects are there; changes during loops
    int newScoreLineNum = -1; //What line number should a new high score be entered if at all
    int timeToNewWeapon = 100000; //How much time until a new weapon type is provided
    double background1Y = 0; //Starting background 1 Y position
    double background2Y = -winHeight; //Starting background 2 Y position
	double frameRate = 60; //Animation frame rate; FPS
	double mouseX = 0; //Current mouse x position
	double mouseY = 0; //Current mouse y position
    static double levelTimer = 0; //Level timer; may be started with startLevelTimer boolean
    double nextGenTime = 0; //How long until the next enemy should be generated
    double minGenTime = 0; //Minimum amount of time until the next enemy may be generated
    double enemyGenTimer = 0; //Timer to expire upon next enemy generation
    static double maxLevelTime = 0; //How much time should a level last
    boolean mouseMoved = false; //Has the mouse been moved
    boolean mouseInside = false; //Is the mouse inside of the window
    boolean startLevelTimer = false; //Timer for each level
    boolean genTimeEstablished = false; //Has the time until the next enemy is generated been determined
    boolean genEnemies = false; //Should enemies be generating
    boolean levelComplete = false; //Has the current level been completed
    boolean levelConstruction = false; //Has the current level construction been performed 
    boolean levelIntroComplete = false; //Has the introduction to the level concluded
    boolean waitingForUser = false; //Is the game waiting for user input to proceed
    boolean waitingForUserEnter = false; //Is the game waiting for the user to enter their name into the high score
    boolean playerExists = false; //Does the user's avatar exist yet
    boolean playerMovement = false; //Is the player allowed to control the ship
    boolean gameComplete = false; //Has the game been completed
    String[] dataLineParts; //String array to hold different data information
    String dataLine, nameString, scoreString, playerName; //Strings to hold the name and score of players
    static AudioClip weapon1Sound, weapon2Sound, weapon3Sound, weapon4Sound, explosionSound, advanceMenu, weaponSwitch, endGameGood, mainMenu, moveToPlanet, acquireNewWeapon, gamePlay; //Sound effects
    UserConsole console; //Create a console object
    Image splashImage, instructionsImage, badEndingImage, goodEndingImage, backgroundscrollImage1, backgroundscrollImage2; //Create image objects
    BufferedReader dataBuffReader = null;
    BufferedWriter dataBuffWriter = null;
    File dataFile = null;
    TextField textField = null;
    Button infoEnterButton = null;
    public static LinkedList<GameObj> objects = new LinkedList<GameObj>(); //Array holding all program objects
    public static LinkedList<Weapon> weapons = new LinkedList<Weapon>(); //Array holding all generated weapon objects
    public static LinkedList<String> scores = new LinkedList<String>(); //Array holding all player scores
    public static LinkedList<newWeaponType> newWeaponTypes = new LinkedList<newWeaponType>(); //Array holding all new Weapon Type items

    Random rand = new Random(); //Random number generator
    static Font font; //Font used to print any text on screen

    //Advance the level if appropriate and perform level introductions/conclusions as needed
    @SuppressWarnings("static-access")
	public void levelCheck(GraphicsContext gc, Stage primaryStage) throws FileNotFoundException {
    	if (lives == 0) { //If the player is out of lives
    		if (debugMode) { System.out.println("Player is out of lives"); }
    		lives = -1; //Alter lives to prevent loop back to this statement
    		currLevel = 13; //Set failed level criteria
    		startLevelTimer = false; //Stop counting the level timer
    		levelTimer = 0; //Reset the level timer
			levelComplete = true; //Indicate that the level is complete
			genEnemies = false; //Prevent further enemies from being generated
			playerMovement = false; //Disallow player's manual movement
			levelIntroComplete = false; 
			levelConstruction = false;
			waitingForUser = false;
			introScene = 3; //Jump cut scene counter to the end scene
    		GameObj.boundaries = false; //Turn off window boundaries
    		console.hide(); //Hide the console
    	}
    	//Level construction
    	if ((levelIntroComplete) && (!levelConstruction)) { //If the level introduction is complete and construction is not yet complete
    		if (debugMode) { System.out.println("Level construction has begun"); }
    		levelComplete = false; //Reset the level completion flag
    		startLevelTimer = true; //Start a timer for the level
    		
    		if (currLevel == 1) { //If the current level is 1

    			if (debugMode) { System.out.println("Level 1 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 10; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 1; //Set maximum number of enemy types allowed in the level
        		minGenTime = 2; //Set minimum amount of time until next enemy is generated
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 20; //How many seconds should the level last
        		}
    		}
    		if (currLevel == 2) { //If the current level is 2
    			if (debugMode) { System.out.println("Level 2 starting"); } 
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.9; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 25; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 1; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 25;
        		}
        		timeToNewWeapon = (int) (rand.nextInt((int) (maxLevelTime/2)) + (maxLevelTime/8));
        		if (debugMode) { System.out.println("Time to new weapon type: " + timeToNewWeapon); }
    		}
    		if (currLevel == 3) { //If the current level is 3
    			if (debugMode) { System.out.println("Level 3 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.8; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 20; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 2; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 30; //How many seconds should the level last
        		}
    		}
    		if (currLevel == 4) { //If the current level is 4
    			if (debugMode) { System.out.println("Level 4 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.7; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship//Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 2; //Set maximum number of enemy types allowed in the level
        		maxEnemies = 25; //Set maximum number of enemies that may be on screen at a time for this level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 35; //How many seconds should the level last
        		}
        		timeToNewWeapon = (int) (rand.nextInt((int) (maxLevelTime/2)) + (maxLevelTime/8));
        		if (debugMode) { System.out.println("Time to new weapon type: " + timeToNewWeapon); }
    		}
    		if (currLevel == 5) { //If the current level is 5
    			if (debugMode) { System.out.println("Level 5 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.6; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
    			maxEnemies = 10; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 3; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 40; //How many seconds should the level last
        		}
        		
    		}
    		if (currLevel == 6) { //If the current level is 6
    			if (debugMode) { System.out.println("Level 6 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.7; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 15; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 3; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 45; //How many seconds should the level last
        		}
        		timeToNewWeapon = (int) (rand.nextInt((int) (maxLevelTime/2)) + (maxLevelTime/8));
        		if (debugMode) { System.out.println("Time to new weapon type: " + timeToNewWeapon); }
    		}
    		if (currLevel == 7) { //If the current level is 7
    			if (debugMode) { System.out.println("Level 7 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.6; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 10; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 4; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 50; //How many seconds should the level last
        		}
        		
    		}
    		if (currLevel == 8) { //If the current level is 8
    			if (debugMode) { System.out.println("Level 8 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.5; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 15; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 5; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 55; //How many seconds should the level last
        		}
        		
    		}
    		if (currLevel == 9) { //If the current level is 9
    			if (debugMode) { System.out.println("Level 9 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.4; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 25; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 5; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 60; //How many seconds should the level last
        		}
        		
    		}
    		if (currLevel == 10) { //If the current level is 10
    			if (debugMode) { System.out.println("Level 10 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 0.2; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 30; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 5; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 90; //How many seconds should the level last
        		}
        		
    		}	
    		levelConstruction = true; //Indicate that the level has been constructed
    	}
    	
    	//Introduction/conclusion construction
    	if ((currLevel == 1) && (!levelIntroComplete) && (!waitingForUser)) { //If the current level is 1 and the level introduction is not complete and the user input is not yet needed
    		if (introScene == 0) { //For the first introduction scene
        		//Display introduction screen
    			
    			mainMenu.cycleCountProperty().set(mainMenu.INDEFINITE);
    			mainMenu.play();
    			if (debugMode) { System.out.println("Program introduction has started"); } 
    			if (debugMode) { System.out.println("Splash Screen"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
        		gc.drawImage(splashImage, 0, 0, winWidth, winHeight); //Draw a background image
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
    		}
    		else if (introScene == 1) { //For the second introduction scene
    			if (debugMode) { System.out.println("Instruction Screen"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
        		gc.drawImage(instructionsImage, 0, 0, winWidth, winHeight); //Draw a background image
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
    		}
    		else if (introScene == 2) { //For the third introduction scene
    			mainMenu.stop();
    			if (debugMode) { System.out.println("Game Start Screen - Ready?"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
        		gc.drawImage(backgroundscrollImage1, 0, background1Y, winWidth, winHeight); //Draw background image 1
    			gc.drawImage(backgroundscrollImage2, 0, background2Y, winWidth, winHeight); //Draw background image 2	
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.setTextAlign(TextAlignment.CENTER);
    	        gc.setTextBaseline(VPos.CENTER);
    			gc.fillText("READY? - SPACE/CLICK", Math.round(primaryCanvas.getWidth()/2), winHeight/4);
        		objects.add(new Ship(primaryGroup, winWidth, winHeight)); //The player's ship is the first object in the objects array
        		objects.getFirst().name = "ship"; //Name the object to alter game object behavior
        		objects.getFirst().update(primaryGroup, winWidth, winHeight, frameRate); //Update the ship position on screen
        		playerExists = true; //Indicate that the player now exists
        		introScene++; //Advance the scene that should be displayed next
        		console = new UserConsole(primaryGroup, gc, winWidth, winHeight); //Add the user console
        		console.setWeapon(1); //Set the default weapon to active
    			gamePlay.cycleCountProperty().set(mainMenu.INDEFINITE);
    			gamePlay.play();
        		console.show();
        		waitingForUser = true; //Indicate that user input is required before progressing
    			if (currLevel == 11) { //If the current level is 11
    				waitingForUser = false; //Then don't bother waiting for input
    			}
    		}
    		else if (introScene == 3) {  //When introduction scenes have concluded
    			levelIntroComplete = true; //Indicate that the introduction for the level is complete
    		}
    	}
    	
    	if ((currLevel >= 2) && (!levelIntroComplete) && (!waitingForUser) && (!waitingForUserEnter)) { //If the current level is 2 or above and the level introduction is not complete and the user input is not yet needed
    		if (currLevel != 13) { //If this is not a bad ending 
        		if (!(objects.getFirst().y < -50)) { //If the player's ship is not yet off the top of the screen
        			return; //Do not run yet
        		}
        		else {
        			objects.getFirst().velocityUpFlag = false; //Stop moving upwards
        		}
    		}
    		else if (currLevel == 13) {
    			gamePlay.stop();
    			if (debugMode) { System.out.println("Bad ending detected"); }
        		mouseMoved = false; //Turn off mouse movement flag
        		playerMovement = false; //Disallow player's manual movement
        		startLevelTimer = false; //Stop counting the level timer
        		levelTimer = 0; //Reset the level timer
    			genEnemies = false; //Prevent further enemies from being generated		
        		GameObj.boundaries = false; //Turn off window boundaries
        		console.hide(); //Hide the console
    		}
    		if (introScene == 0) { //For the first introduction scene
/*    			if (debugMode) { System.out.println("Level " + currLevel + " introduction has started"); }
    			if (debugMode) { System.out.println("Aproaching planet"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("CUTSCENE PLANET - HIT SPACE", 20, winHeight/4);
    			moveToPlanet.play();
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
*/    		
    			introScene++; //Advance the scene that should be displayed next
    			}
    		else if (introScene == 1) { //For the first introduction scene
/*    			if (debugMode) { System.out.println("Aproaching structure"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("CUTSCENE SURFACE - HIT SPACE", 20, winHeight/4);
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
*/    		
    			introScene++; //Advance the scene that should be displayed next	
    		}
    		else if (introScene == 2) { //For the first introduction scene
/*    			if (debugMode) { System.out.println("Approaching Alien"); }
    			windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("CUTSCENE ALIEN - HIT SPACE", 20, winHeight/4);
    			introScene++; //Advance the scene that should be displayed next
    			waitingForUser = true; //Indicate that user input is required before progressing   	
*/
    			if (debugMode) { System.out.println("Level Start Screen"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
        		
    			gc.drawImage(backgroundscrollImage1, 0, background1Y, winWidth, winHeight); //Draw background image 1
    			gc.drawImage(backgroundscrollImage2, 0, background2Y, winWidth, winHeight); //Draw background image 2	
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.setTextAlign(TextAlignment.CENTER);
    	        gc.setTextBaseline(VPos.CENTER);
    			gc.fillText("READY? - SPACE/CLICK", Math.round(primaryCanvas.getWidth()/2), winHeight/4);
        		objects.getFirst().update(primaryGroup, winWidth, winHeight, frameRate); //Update the ship position on screen
        		introScene++; //Advance the scene that should be displayed next
        		console.show();
        		waitingForUser = true; //Indicate that user input is required before progressing
    		}
    		else if (introScene == 3) {  //When introduction scenes have concluded
    			if (currLevel == 11) { //If this was the last level
    				gamePlay.stop();
    				if (debugMode) { System.out.println("Running win game scenario"); }
        			windowSetup(gc, primaryStage); //Clean and setup the window
    				gameComplete = true; //Indicate that the game has completed
    				introScene++; //Advance the scene that should be displayed next
    				waitingForUser = true;
    				console.hide(); //Hide the console from view
        			gc.setFill(Color.WHITE);
        			gc.setFont(font);
    				endGameGood.cycleCountProperty().set(endGameGood.INDEFINITE);
    				endGameGood.play();
    				gc.drawImage(goodEndingImage, 0, 0, winWidth, winHeight); //Draw a background image
    				for (int i = 0; i < 5; i++) { 
			           	dataLineParts = scores.get(i).split(":");
			           	gc.fillText(dataLineParts[0], 225, 300+(i*50));
			           	gc.fillText(dataLineParts[1], 775, 300+(i*50));
    				}  				
    				textField = new TextField();
    				primaryGroup.getChildren().add(textField);
    				textField.relocate(100, 750); 
    				textField.setMinWidth(700);
    				textField.setAlignment(Pos.CENTER);
    				textField.setFont(font);
    				infoEnterButton = new Button("Enter");
    				infoEnterButton.setFont(font);
    				primaryGroup.getChildren().add(infoEnterButton);
    				infoEnterButton.relocate(800, 750);
    				infoEnterButton.setOnAction(new EventHandler<ActionEvent>() {
    					@Override
    					public void handle(ActionEvent event) {
    						dataFile = new File("data/highscore.dat");
    						try {
								dataBuffWriter = new BufferedWriter(new FileWriter(dataFile));
							} catch (IOException e1) {
								if (debugMode) { System.out.println("FileWriter error"); }
							}

    				        for (int i = 0; i < scores.size(); i++) { 
    				           	dataLineParts = scores.get(i).split(":");
    				       			try {
    				       				if ((score >= Integer.parseInt(dataLineParts[1])) && (score != -1)) {
    				       					dataBuffWriter.write(textField.getText() + ":" + Integer.toString(score));
    										dataBuffWriter.newLine();
    				       					dataBuffWriter.write(scores.get(i));
    				       					dataBuffWriter.newLine();
    				       					score = -1; //Reset the player's score
    				       				}
    				       				else {
    				       					dataBuffWriter.write(scores.get(i));
    				       					dataBuffWriter.newLine();
    				       				}
    								} catch (IOException e) {
    									if (debugMode) { System.out.println("Writer error"); }
    								}
    				        }
    						try {
    							if (debugMode) { System.out.println("Moving to Reset Function"); }
    							dataBuffWriter.close(); //Close the file writer
    							introScene = 4; //Move to reset the game
    							waitingForUser = false; //Indicate that the user has performed an action
    						} catch (IOException e) {
    							if (debugMode) { System.out.println("Buffer writer closing error"); }
    						}
    	    			}
    		        });
    			}
    				
    			if (currLevel == 13) { //If the level jumps to 13
    				if (debugMode) { System.out.println("Running lose game scenario"); }
        			windowSetup(gc, primaryStage); //Clean and setup the window
        			for (int i = objects.size(); i > 1; i--) { //For all of the objects in the game except for the ship
        				objects.get(i-1).erase(primaryGroup);
        				objects.remove(i-1);
        			}
        			for (int i = newWeaponTypes.size(); i > 0; i--) { //For all of the objects in the game except for the ship
        				newWeaponTypes.get(i-1).erase(primaryGroup);
        				newWeaponTypes.remove(i-1);
        			}
        			for (int i = weapons.size(); i > 0; i--) { //For all active weapons
        				if (weapons.get(i-1).playerOwned) {
        					objects.getFirst().numWeapons--;
        				}
        				weapons.get(i-1).erase(primaryGroup);
        				weapons.remove(i-1);
        			}
        			if (objects.size() > 0) {
            			objects.getFirst().erase(primaryGroup); //Erase the player's ship
            			objects.remove(0); //Remove the player's ship from existence
        			}
        			gc.setFill(Color.WHITE);
        			gc.setFont(font);
    				gc.drawImage(badEndingImage, 0, 0, winWidth, winHeight); //Draw a background image
    				for (int i = 0; i < 5; i++) { 
			           	dataLineParts = scores.get(i).split(":");
			           	gc.fillText(dataLineParts[0], 225, 375+(i*50));
			           	gc.fillText(dataLineParts[1], 775, 375+(i*50));
    				}
    				gameComplete = true; //Indicate that the game has completed
    				waitingForUser = true;
    				introScene++; //Advance the scene that should be displayed next
    			}
    			if (currLevel < 11) {
    				levelIntroComplete = true; //Indicate that the introduction for the level is complete
    			}
    		}
    		else if (introScene == 4) {  //When introduction scenes have concluded
    			if (debugMode) { System.out.println("RESET GAME"); }
    			endGameGood.stop();
    			for (int i = objects.size(); i > 1; i--) { //For all of the objects in the game except for the ship
    				objects.get(i-1).erase(primaryGroup);
    				objects.remove(i-1);
    			}
    			for (int i = weapons.size(); i > 0; i--) { //For all active weapons
    				if (weapons.get(i-1).playerOwned) {
    					objects.getFirst().numWeapons--;
    				}
    				weapons.get(i-1).erase(primaryGroup);
    				weapons.remove(i-1);
    			}
    			if (objects.size() > 0) {
        			objects.getFirst().erase(primaryGroup); //Erase the player's ship
        			objects.remove(0); //Remove the player's ship from existence
    			}
    			for (int i = newWeaponTypes.size(); i > 0; i--) { //For all of the objects in the game except for the ship
    				newWeaponTypes.get(i-1).erase(primaryGroup);
    				newWeaponTypes.remove(i-1);
    			}
    			primaryGroup.getChildren().remove(textField);
    			primaryGroup.getChildren().remove(infoEnterButton);
    		    globalTimer = 0; //Total number of seconds the game has been running
    		    fCounter = 0; // Number of frames elapsed out of the set FPS
    		    score = 0; //Player's current score
    		    currLevel = 1; //The current level
    		    introScene = 0; //Which scene in the introduction should be displayed
    		    maxEnemyLevel = 0; //Maximum number of enemy types allowed for a particular level
    		    numEnemies = 0; //How many enemies are currently in existence
    		    maxEnemies = 0; //What is the maximum number of enemies that should be allowed on screen
    		    enemyType = 0; //What type of enemy should be generated
    		    lives = 4; //How many extra lives does the player have remaining
    		    newScoreLineNum = -1; //What line number should a new high score be entered if at all
    		    background1Y = 0; //Starting background 1 Y position
    		    background2Y = -winHeight; //Starting background 2 Y position
    		    levelTimer = 0; //Level timer; may be started with startLevelTimer boolean
    		    nextGenTime = 0; //How long until the next enemy should be generated
    		    minGenTime = 0; //Minimum amount of time until the next enemy may be generated
    		    enemyGenTimer = 0; //Timer to expire upon next enemy generation
    		    maxLevelTime = 0; //How much time should a level last
    		    startLevelTimer = false; //Timer for each level
    		    genTimeEstablished = false; //Has the time until the next enemy is generated been determined
    		    genEnemies = false; //Should enemies be generating
    		    levelComplete = false; //Has the current level been completed
    		    levelConstruction = false; //Has the current level construction been performed 
    		    levelIntroComplete = false; //Has the introduction to the level concluded
    		    waitingForUser = false; //Is the game waiting for user input to proceed
    		    waitingForUserEnter = false; //Is the game waiting for the user to enter their name into the high score
    		    playerExists = false; //Does the user's avatar exist yet
    		    playerMovement = false; //Is the player allowed to control the ship
    		    gameComplete = false; //Has the game been completed
    		}
    	}
    	
    	//Level completion check
    	if ((startLevelTimer) && (levelTimer >= (maxLevelTime-1))) {
    		if (debugMode) { System.out.println("Level completed"); }
    		mouseMoved = false; //Turn off mouse movement flag
    		playerMovement = false; //Disallow player's manual movement
    		startLevelTimer = false; //Stop counting the level timer
    		levelTimer = 0; //Reset the level timer
			levelComplete = true; //Indicate that the level is complete
			genEnemies = false; //Prevent further enemies from being generated
			objects.getFirst().fireFlag=false; //Indicate the ship is not firing
			objects.getFirst().velocityUpFlag=true; //Force the player's ship upward movement on
			objects.getFirst().velocityDownFlag=false; //Force the player's ship downward movement off
			objects.getFirst().angleUpFlag=false; //Stop moving left
			objects.getFirst().angleDownFlag=false; //Stop moving right			
			levelIntroComplete = false; 
			levelConstruction = false;
			waitingForUser = false;
			currLevel++; //Advance the level counter
			introScene = 0; //Reset cut scene counter
    		GameObj.boundaries = false; //Turn off window boundaries
    		console.hide(); //Hide the console
    		moveToPlanet.play();
			for (int i = newWeaponTypes.size(); i > 0; i--) { //For all of the objects in the game except for the ship
				newWeaponTypes.get(i-1).erase(primaryGroup);
				newWeaponTypes.remove(i-1);
			}
			for (int i = objects.size(); i > 1; i--) { //For all of the objects in the game except for the ship
				objects.get(i-1).erase(primaryGroup);
				objects.remove(i-1);
			}
			for (int i = weapons.size(); i > 0; i--) { //For all active weapons
				if (weapons.get(i-1).playerOwned) {
					objects.getFirst().numWeapons--;
				}
				weapons.get(i-1).erase(primaryGroup);
				weapons.remove(i-1);
			}
    	}
    }

	//Update all objects
    public void objectUpdate(GraphicsContext gc, Stage primaryStage) {
    	
    	//Advance Timers
		if (startLevelTimer) {
			levelTimer += 1.0/((double)frameRate); //Increase the time elapsed in the level timer
		}
		if (genTimeEstablished) { //If the time until the next enemy generation has been established
			enemyGenTimer += 1.0/((double)frameRate); //Increase the time elapsed for the enemy generation timer 
		}
		if (fCounter >= frameRate) { //If the number of frames that have elapsed is greater than the set FPS
			globalTimer++; //Increase the number of seconds that have elapsed in the game so far
			fCounter = 0; //Reset the frame counter
		}
		else { //Otherwise
			fCounter++; //Increase the frame counter
		}
		if (levelIntroComplete) { //If there is not a cut scene or introduction in play
			windowSetup(gc, primaryStage); //Clean and setup the window
			gc.drawImage(backgroundscrollImage1, 0, background1Y, winWidth, winHeight); //Draw background image 1
			gc.drawImage(backgroundscrollImage2, 0, background2Y, winWidth, winHeight); //Draw background image 2			
			background1Y++; //Advance the background 1 y position
			background2Y++; //Advance the background 1 y position
			if (background1Y == 1) { //If background 1 has reached it starting position
				background2Y = -winHeight; //Move background 2 to the top
			}
			if (background2Y == 0) { //If background 2 has reached it starting position
				background1Y = -winHeight; //Move background 1 to the top
			}
			//Enemy and newWeapon generator
			if ((levelConstruction) && (genEnemies)) { //If the level construction has been completed
				if (fCounter == 0) { //If a second has passed
					timeToNewWeapon--; //Decrease the number of seconds until the next weapon type is generated
				}
				if (timeToNewWeapon <= 0) { //If the time until a new weapon type expires
					if (debugMode) { System.out.println("Generating next weapon type"); }
					if (!console.w2Allowed) { //If weapon 2 is not yet allowed
						newWeaponTypes.add(new newWeaponType(primaryGroup, 0.0, 00, winWidth, winHeight, 2)); //Generate upgrade box
					}
					else if (!console.w3Allowed) { //If weapon 3 is not yet allowed
						newWeaponTypes.add(new newWeaponType(primaryGroup, 0.0, 00, winWidth, winHeight, 3)); //Generate upgrade box
					}
					else if (!console.w4Allowed) { //If weapon 4 is not yet allowed
						newWeaponTypes.add(new newWeaponType(primaryGroup, 0.0, 00, winWidth, winHeight, 4)); //Generate upgrade box
					}
					timeToNewWeapon = 1000000; //Set next weapon timer to unreasonable value
				}
				for (int i = 0; i < newWeaponTypes.size(); i++ ) { //For any new weapon type items
					newWeaponTypes.get(i).update(primaryGroup, winWidth, winHeight, frameRate); //Update their positions
					if (objects.getFirst().boundary.intersects(newWeaponTypes.get(i).boundary)) {
						acquireNewWeapon.play();
						if (newWeaponTypes.get(i).type == 2) { //If weapon 2 item was collided with
							console.allowWeapon(2); //Tell the console that the weapon is now allowed
							newWeaponTypes.get(i).erase(primaryGroup); //Erase the new weapon type item from the screen
							newWeaponTypes.remove(i); //Remove the new weapon type item from the array
							break;
						}
						else if (newWeaponTypes.get(i).type == 3) { //If weapon 3 item was collided with
							console.allowWeapon(3); //Tell the console that the weapon is now allowed
							newWeaponTypes.get(i).erase(primaryGroup); //Erase the new weapon type item from the screen
							newWeaponTypes.remove(i); //Remove the new weapon type item from the array
							break;
						}
						else if (newWeaponTypes.get(i).type == 4) { //If weapon 4 item was collided with
							console.allowWeapon(4); //Tell the console that the weapon is now allowed
							newWeaponTypes.get(i).erase(primaryGroup); //Erase the new weapon type item from the screen
							newWeaponTypes.remove(i); //Remove the new weapon type item from the array
							break;
						}
					}
				}
				
				if ((!genTimeEstablished) && (numEnemies <= maxEnemies)) { //If next enemy generation time has not been computed and the number of enemies is less than the maximum
					nextGenTime = rand.nextDouble() + minGenTime; //How much time until the next enemy is created
					genTimeEstablished = true; //Indicate if the next enemy generation time has been set 
					if (debugMode) { System.out.println("Time to next enemy generation determined as: " + nextGenTime); }
				}
				if (genTimeEstablished) {
					
					if (enemyGenTimer >= nextGenTime) { //If the timer expires
						if (debugMode) { System.out.println("Generating an Enemy"); }
						genTimeEstablished = false; //Reset flag to establish next generation time
						enemyGenTimer = 0; //Reset enemy generation timer
						enemyType = rand.nextInt(maxEnemyLevel) + 1; //Select a random enemy type out of those allowable to generate
						if (debugMode) { System.out.println("Enemy level will be: " + enemyType + " out of " + maxEnemyLevel); }
						if (enemyType == 1) {
							if (debugMode) { System.out.println("Enemy type 1 generating"); }
							objects.add(new EnemyType1(primaryGroup, winWidth, winHeight, rand.nextInt(3), true, 0.0, 0.0, 0.0, 0)); //Generate a new enemy of type 1
						}
						if (enemyType == 2) {
							if (debugMode) { System.out.println("Enemy type 2 generating"); }
							objects.add(new EnemyType2(primaryGroup, winWidth, winHeight, 0.0, 0.0)); //Generate a new enemy of type 2
						}
						if (enemyType == 3) {
							if (debugMode) { System.out.println("Enemy type 3 generating"); }
							objects.add(new EnemyType3(primaryGroup, winWidth, winHeight, 0.0, 0.0)); //Generate a new enemy of type 3
						}		
						if (enemyType == 4) {
							if (debugMode) { System.out.println("Enemy type 4 generating"); }
							objects.add(new EnemyType4(primaryGroup, winWidth, winHeight, 0.0, 0.0)); //Generate a new enemy of type 4
						}
						if (enemyType == 5) {
							if (debugMode) { System.out.println("Enemy type 5 generating"); }
							objects.add(new EnemyType5(primaryGroup, winWidth, winHeight, 0.0, 0.0)); //Generate a new enemy of type 5
						}
					}
				}
			}
			for (int i = objects.size(); i > 1; i--) { //For all of the objects in the game except for the ship
				objects.get(i-1).update(primaryGroup, winWidth, winHeight, frameRate); //Update their positioning
				if (objects.get(i-1).offBottom) { //If the object goes off the bottom of the screen
					objects.get(i-1).erase(primaryGroup); //Erase the object
					objects.remove(i-1); //Delete the object from the objects array
				}
				if (objects.get(i-1).fireFlag == true) { //If the object has previously fired a weapon
					objects.get(i-1).fireFlag = false; //Prevent continuous firing by stopping the previous command
				}
				if ((objects.get(i-1).timeToFire > 0)) { //If the object's time to fire is greater than 0
					objects.get(i-1).timeToFire-=(1/frameRate); //Subtract a frame's time from the time to fire counter
				}
				else if ((fCounter == 0) && (objects.get(i-1).timeToFire <= 0)) { //If a second has passed and it is time for an object to fire
					objects.get(i-1).fireFlag = true; //Indicate that it should fire this frame
					objects.get(i-1).timeToFire = objects.get(i-1).newTimeToFire(); //Generate a new time for that object to fire		
				}
			}
			for (int i = weapons.size(); i > 0; i--) { //For all weapons that have been fired
				weapons.get(i-1).draw(gc, winWidth, winHeight, frameRate); //Draw the weapon
			}
			console.updateData(); //Update the data displayed in the console
			console.consoleGroup.toFront(); //Move the console to the top layer
		}
		if (objects.size() > 0) { //If the ship has been created
			if (mouseMoved && mouseInside && (levelIntroComplete) && (playerMovement)) { //If the mouse moved and it is still inside of the window and it is allowable
	            if ((mouseX-20 <= objects.getFirst().x) && (objects.getFirst().x <= mouseX+20) && (mouseY-10 <= objects.getFirst().y) && (objects.getFirst().y <= mouseY+10)) {
	            	mouseMoved = false; //Reset the mouse moved flag
	            	objects.getFirst().angleUpFlag = false;  //Turn off the movement flag
	            	objects.getFirst().angleDownFlag = false; //Turn off the movement flag
	            	objects.getFirst().velocityUpFlag = false; //Turn off the movement flag
	            	objects.getFirst().velocityDownFlag = false; //Turn off the movement flag
	            }
	            else if ((mouseX-20 <= objects.getFirst().x) && (objects.getFirst().x <= mouseX+20)) {
	            	objects.getFirst().angleUpFlag = false;  //Turn off the movement flag
	            	objects.getFirst().angleDownFlag = false; //Turn off the movement flag
	            }
	            else if ((objects.getFirst().x <= mouseX+30) && (mouseY-10 <= objects.getFirst().y) && (objects.getFirst().y <= mouseY+10)) {
	            	objects.getFirst().velocityUpFlag = false; //Turn off the movement flag
	            	objects.getFirst().velocityDownFlag = false; //Turn off the movement flag
	            }
	            else { 
	            	if (mouseY < objects.getFirst().y) {
	            		objects.getFirst().velocityUpFlag = true;
	            		objects.getFirst().velocityDownFlag = false; 
	            	} 
	            	else if (mouseY > objects.getFirst().y) {
	            		objects.getFirst().velocityDownFlag = true; 
	            		objects.getFirst().velocityUpFlag = false;
	            	}
	            	if (mouseX < objects.getFirst().x) {
	            		objects.getFirst().angleUpFlag = true; 
	            		objects.getFirst().angleDownFlag = false; 
	            	}
	            	else if (mouseX > objects.getFirst().x) {
	            		objects.getFirst().angleDownFlag = true;
	            		objects.getFirst().angleUpFlag = false; 
	            	}
	            }
			}
			objects.getFirst().update(primaryGroup, winWidth, winHeight, frameRate); //Update the ship positioning			
		}
    }
    
    //Check all objects for collisions and handle them if found
    public void collisionDetection(GraphicsContext gc, Stage primaryStage) throws IndexOutOfBoundsException {
    	if ((!levelComplete) && (objects.size() > 1)) { //If the level has not been completed and at least the player's ship and something else exists 
    		//Off screen check
    		for (int i = weapons.size(); i > 0; i--) { //For each of the active weapons
				if (weapons.get(i-1).outsideWin) { //If a weapon goes outside of the window
					if (weapons.get(i-1).playerOwned) { //If the weapon is owned by the player
						objects.getFirst().numWeapons--; //Indicate that the player can fire an additional weapon again
					}
					weapons.get(i-1).erase(primaryGroup); //Erase the weapon
					weapons.remove(i-1); //Remove the weapon
				}
    		}
    		
    		//Ship collision checks
    		for (int i = objects.size(); i > 1; i--) { //For all of the objects in the game except the player's ship
				if (objects.get(i-1).boundary.intersects(objects.getFirst().boundary)) { //If the object collides with the ship
        			if (debugMode) { System.out.println("Player's ship had a collision with an object"); }
    				objects.get(i-1).erase(primaryGroup); //Remove the object
    				objects.remove(i-1); //Remove the object from the objects array
    				objects.getFirst().collision = true; //Indicate that the player's ship was hit
    			}
    		}
    		if (weapons.size() > 0) { //If there are active weapons
				for (int i = weapons.size(); i > 0; i--) { //Then for each of those weapons
					if (weapons.get(i-1).boundary.intersects(objects.getFirst().boundary) && !weapons.get(i-1).playerOwned) { //If there is a collision between the weapon and the player's ship
						if (debugMode) { System.out.println("Player's ship had a collision with a weapon"); }
        				objects.getFirst().collision = true; //Indicate that the player's ship had a collision
    					weapons.get(i-1).erase(primaryGroup); //Erase the weapon
    					weapons.remove(i-1); //Remove the weapon from the weapon array
					}
				}
			}	
    		if (objects.getFirst().collision) { //If there was a collision with the ship
    			if (debugMode) { System.out.println("Player's ship had a collision"); }
    			objects.getFirst().collision = false; //Reset the collision flag
    			lives--; //Decrease the number of lives 
    			console.lostLife(lives); //Update the console as to the number of lives the player has remaining
    			explosionSound.play();
    		}
    		
			for (int i = objects.size(); i > 1; i--) { //For all of the objects in the object array except the player's ship
				for (int j = weapons.size(); j > 0; j--) { //For all of the active weapons
					if (objects.size() <= 1) { //If the number of objects goes below the limit
						break; //break out of the loop
					}
					if (objects.get(i-1).boundary.intersects(weapons.get(j-1).boundary) && (weapons.get(j-1).playerOwned)) { //If there is a collision between object and the player's weapon
						if (objects.get(i-1).name == "enemy1") {
							score+=100;
							if (objects.get(i-1).sizeOption == 3) { //If enemy 1 had a size option of 3
								objects.add(new EnemyType1(primaryGroup, winWidth, winHeight, 2, false, objects.get(i-1).x+5, objects.get(i-1).y+5, objects.get(i-1).velocity, objects.get(i-1).size)); //Generate a new enemy of type 1
								objects.add(new EnemyType1(primaryGroup, winWidth, winHeight, 2, false, objects.get(i-1).x-5, objects.get(i-1).y+5, objects.get(i-1).velocity, objects.get(i-1).size)); //Generate a new enemy of type 1
							}
							else if (objects.get(i-1).sizeOption == 2) { //If enemy 1 had a size option of 2
								objects.add(new EnemyType1(primaryGroup, winWidth, winHeight, 1, false, objects.get(i-1).x+5, objects.get(i-1).y+5, objects.get(i-1).velocity, objects.get(i-1).size)); //Generate a new enemy of type 1
								objects.add(new EnemyType1(primaryGroup, winWidth, winHeight, 1, false, objects.get(i-1).x-5, objects.get(i-1).y+5, objects.get(i-1).velocity, objects.get(i-1).size)); //Generate a new enemy of type 1
							}
						}
						if (objects.get(i-1).name == "enemy2") {
							score+=500;
						}
						if (objects.get(i-1).name == "enemy3") {
							score+=1000;
						}
						if (objects.get(i-1).name == "enemy4") {
							score+=1500;
						}
						if (objects.get(i-1).name == "enemy5") {
						score+=2000;
						}
						if (weapons.get(j-1).name.equals("weapon3") && (weapons.get(j-1).durability <= 0)) { //If this weapon is out of durability
							weapons.get(j-1).erase(primaryGroup); //Erase the weapon
							weapons.remove(j-1); //Remove the weapon from the array
						}
						else if ((weapons.get(j-1).name.equals("weapon3") && (weapons.get(j-1).durability > 0))) { //If this weapon still has durability
							weapons.get(j-1).durability--; //Detract from the weapon's durability
						}
						else { //This is not a weapon with durability
							weapons.get(j-1).erase(primaryGroup); //Erase the weapon
							weapons.remove(j-1); //Remove the weapon from the array
						}
						objects.getFirst().numWeapons--; //Allow the player the ability to fire another weapon again
						objects.get(i-1).erase(primaryGroup); //Erase the object
						objects.remove(i-1); //Remove the object from the objects array
						explosionSound.play(); //Play an explosion sound				
					}
				}
			}
		}
   	}

    //Display a basic window
    public void windowSetup(GraphicsContext gc, Stage primaryStage) {
		//Clear the frame and setup the next
		gc.clearRect(0, 0, winWidth, winHeight); //Clear the canvas
		gc.setFill(backgroundColor); //Set the fill color to black
		gc.fillRect(0, 0, winWidth, winHeight); //Fill in the background
		if (currLevel < 11) {
			primaryStage.setTitle(winTitle + " - Level " + currLevel + "/10"); //Set the window title
		}
		else {
			primaryStage.setTitle(winTitle); //Set the window title
		}
    }
    
    //Graphics initialization method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryGroup = new Group(); //Initialize a group of scene objects
		primaryScene = new Scene(primaryGroup); //Initialize the primary scene
		primaryCanvas = new Canvas(winWidth, winHeight); //Initialize the primary canvas
		primaryStage.setTitle(winTitle); //Set the stage title
		
		//Read in necessary sound files from system
		weapon1Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon1.wav").toString());
		weapon2Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon2.wav").toString());
		weapon3Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon3.wav").toString());
		weapon4Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon4.wav").toString());
		explosionSound = new AudioClip(ClassLoader.getSystemResource("sounds/explosion.wav").toString());
		weaponSwitch = new AudioClip(ClassLoader.getSystemResource("sounds/weaponSwitch.wav").toString());
		advanceMenu = new AudioClip(ClassLoader.getSystemResource("sounds/advanceMenu.wav").toString());
		endGameGood  = new AudioClip(ClassLoader.getSystemResource("sounds/endGameGood.wav").toString());
		mainMenu  = new AudioClip(ClassLoader.getSystemResource("sounds/mainMenu.wav").toString());
		moveToPlanet  = new AudioClip(ClassLoader.getSystemResource("sounds/moveToPlanet.wav").toString());
		acquireNewWeapon = new AudioClip(ClassLoader.getSystemResource("sounds/acquireNewWeapon.wav").toString());
		gamePlay = new AudioClip(ClassLoader.getSystemResource("sounds/gamePlay.wav").toString());
		
		//Read in necessary image files from system
		splashImage = new Image("/images/splashImage.png");
		instructionsImage = new Image("/images/instructionsImage.png");
		badEndingImage = new Image("/images/badEndingImage.png");
		goodEndingImage = new Image("/images/goodEndingImage.png");
		backgroundscrollImage1 = new Image("/images/backgroundscrollImage1.jpg");
		backgroundscrollImage2 = new Image("/images/backgroundscrollImage2.jpg");
		
		//Read in necessary data from the system
		dataFile = new File("data/highscore.dat");
		dataBuffReader = new BufferedReader(new FileReader(dataFile));
		try {
			while ((dataLine = dataBuffReader.readLine()) != null) {
				scores.add(dataLine); //Read in all the score data
				if (debugMode) { System.out.println("READ: " + dataLine); }
			}
		} catch (IOException e1) {
			if (debugMode) { System.out.println("Read data error"); }
		}
		dataBuffReader.close(); //Close the buffer reader
        
		//Read in manual font
		font = Font.loadFont(new FileInputStream(new File("data/PressStart2P.ttf")), 20); //Import font
		
		primaryScene.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
			mouseInside = true; //Indicate that the mouse is inside the scene
        });

		primaryScene.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
			if (mouseMoved && playerExists && levelIntroComplete && playerMovement) { //If the mouse had moved discontinue the action
            	objects.getFirst().angleUpFlag = false;  //Turn off the movement flag
            	objects.getFirst().angleDownFlag = false; //Turn off the movement flag
            	objects.getFirst().velocityUpFlag = false; //Turn off the movement flag
            	objects.getFirst().velocityDownFlag = false; //Turn off the movement flag
			}
			mouseInside = false; //Indicate that the mouse has left the scene
        });
		
	    primaryScene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> { //Whenever the mouse is moved
            mouseX = e.getX(); //Retrieve the current mouse x position
            mouseY = e.getY(); //Retrieve the current mouse y position
            mouseMoved = true; //Indicate that the mouse moved
        });
    
	    primaryScene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> { //Whenever the mouse is pressed
	    	if (e.getButton() == MouseButton.PRIMARY) {
            	if (waitingForUser && (currLevel != 11)) {
            		waitingForUser = false; //Indicate that the user has acknowledged the prompt
            		if (debugMode) { System.out.println("Level: " + currLevel + " done waiting"); }
            		advanceMenu.play();
            	}
            	else if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
            		objects.getFirst().fireFlag=true;
            	}
	    	}
	    	if (e.getButton() == MouseButton.SECONDARY) {
	    		if (objects.getFirst().weaponType >= 4) {
	    			objects.getFirst().weaponType = 1;
	    			console.setWeapon(objects.getFirst().weaponType);
	    		}
	    		else {
	    			objects.getFirst().weaponType++;
	    			console.setWeapon(objects.getFirst().weaponType);
	    		}
	    	}
	    });
    
		primaryScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) { //Should a key press event occur
                switch (event.getCode()) { //Check the key pressed
                	
                    case W:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		objects.getFirst().velocityUpFlag=true;
                    	}
                    	break;
                    case S:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		objects.getFirst().velocityDownFlag=true;
                    	}
                    	break;
                    case A:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		objects.getFirst().angleUpFlag=true;
                    	}
                    	break;
                    case D:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)){
                    		objects.getFirst().angleDownFlag=true;
                    	}
                    	break;
                    case SPACE:
                    	if (waitingForUser) {
                    		waitingForUser = false; //Indicate that the user has acknowledged the prompt
                    		advanceMenu.play();
                    	}
                    	else if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		objects.getFirst().fireFlag=true;
                    	}
                    	break;
                    case DIGIT1:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		console.setWeapon(1);
                    	}
                    	break;
                    case DIGIT2:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		console.setWeapon(2);
                    	}
                    	break;
                    case DIGIT3:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		console.setWeapon(3);
                    	}
                    	break;
                    case DIGIT4:
                    	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                    		console.setWeapon(4);
                    	}
                    	break;	
                }
            }
        });
		
		primaryScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                case W:
                	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                		objects.getFirst().velocityUpFlag=false;
                	}
                	break;
                case S:
                	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                		objects.getFirst().velocityDownFlag=false;
                	}
                	break;
                case A:
                	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                		objects.getFirst().angleUpFlag=false;
                	}
                	break;
                case D:
                	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                		objects.getFirst().angleDownFlag=false;
                	}
                	break;
                case SPACE:
                	if ((playerExists) && (levelIntroComplete) && (playerMovement)) {
                		objects.getFirst().fireFlag=false;
                	}
                	break;
                }
            }
        });
	
		primaryStage.setScene(primaryScene); //Set the stage with the first scene
		primaryGroup.getChildren().add(primaryCanvas); //Add the canvas to the primary group
		GraphicsContext gc = primaryCanvas.getGraphicsContext2D(); //Push canvas draw calls to graphics context
        
        //Create a variable keyframe for the display
        keyFrame = new KeyFrame(Duration.seconds(1.0/frameRate), new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(javafx.event.ActionEvent event) { //Should an event occur
        		winWidth=primaryStage.getWidth(); //Retrieve the width of the resized window
        		winHeight=primaryStage.getHeight(); //Retrieve the height of the resized window
        		primaryCanvas.setWidth(winWidth); //Set the canvas width to the new value
        		primaryCanvas.setHeight(winHeight); //Set the canvas height to the new value
        		try {
					levelCheck(gc, primaryStage);
				} catch (FileNotFoundException e) {
					if(debugMode) { System.out.println("Font file not found"); }
				} //Advance the level if appropriate and perform level introductions/conclusions as needed
            		collisionDetection(gc, primaryStage); //Check all objects for collisions and handle them if found
            		objectUpdate(gc, primaryStage); //Update objects
        	}
        });
        primaryLoop = new Timeline(keyFrame); //Create a timeline using the keyframe  
        primaryLoop.setCycleCount(Animation.INDEFINITE); //Have the timeline continue to run forever
        primaryLoop.play(); //Play the animation at the set rate
        primaryStage.show(); //Display the stage
	}

	//Main method
	public static void main(String[] args) {
		launch(args);
	}
}
