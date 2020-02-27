/**
 *  Program: COSC_3550_hw5 - Romeo! Galactic Adventure
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 24 January 2018
 *  Date Last Modified: 9 March 2018
 *  Purpose: Use both animation and user input to form an interactive game draft. Basic game play is possible, just hit space to bypass
 *  placeholder screens. 
 *  
 *  	==Requirements==			==Implemented==		==Note==
 * 		Splash placeholder			Yes					Hit space to pass
 * 		Instructions placeholder	Yes					Hit space to pass
 * 		Cutscene placeholders		Yes					Hit space to pass
 * 		Unique level structure		Yes					All levels are setup differently (some elements not implemented yet)
 * 		Level change (timed)		Yes					Use "testCutsceneMode = true" to test easily
 * 		10 levels					Yes					Use "testCutsceneMode = true" to test easily
 * 		Generic object class		Yes
 * 		Basic Player movement		Yes
 * 		Player boundaries			Yes
 * 		Basic player console		Yes
 * 		Scoring mechanism			Yes
 * 		User console				Yes
 * 		Enemy boundaries			Yes
 * 		Rand. enemy construction	Yes					Multiple enemy types not implemented yet 
 * 		Enemy 1 					Yes
 * 		Weapon 1					Yes
 * 		Switch  weapon types		Yes					Currently 
 * 		Spawn diff. en. types		Yes					Other enemy types not implemented yet
 * 		Player collision detection	Yes
 * 		Basic sound	effects			Yes
 * 		Good end game scenario		Yes					Placeholder
 * 		Bad end game scenario		Yes					Placeholder
 *   	
 *   	==Extra Goals==				==Implemented==		==Note==
 * 		Enemy 2 					No
 * 		Enemy 3 					No
 * 		Enemy 4 					No
 * 		Enemy 5 					No
 * 		Weapon 2					No
 * 		Weapon 3					No
 * 		Weapon 4					No
 * 		Weapons as slot upgrades	No
 * 		High score screen			No
 * 		Cutscenes					No
 * 		Collision animations		No
 *   	Finished screens			No
 *   	Full sound effects			No
 * 		Background music			No
 * 		High score					No
 * 		Image overlay				No
 * 		Object collision detection	No
 * 		Background scenery			No
 * 		Custom font					No
 * 		Pausing						No
 * 		Game reset					No
 * 		Mouse controls				No
 *		
 *		==User Controls==			==Action==
 *		Mouse:						NA
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
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.util.*;
import java.util.LinkedList;
import java.util.Random;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.media.AudioClip;

public class primary extends Application{
	//Global variables - Alterable
	String winTitle = "Romeo! Galactic Adventure"; // Window title
	double winWidth = 1000; //Window screen width
	double winHeight = 1000; //Window screen height
	int frameCounter = 0; //Counter for how many frames have passed so far
	Color backgroundColor = Color.BLACK; //Base background color
	boolean debugMode = false; //Should the game output debug messages in the console
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
    int lives = 4; //How many extra lives does the player have remaining
    int numObjects; //How many objects are there; changes during loops
	double frameRate = 60; //Animation frame rate; FPS
    static double levelTimer = 0; //Level timer; may be started with startLevelTimer boolean
    double nextGenTime = 0; //How long until the next enemy should be generated
    double minGenTime = 0; //Minimum amount of time until the next enemy may be generated
    double enemyGenTimer = 0; //Timer to expire upon next enemy generation
    static double maxLevelTime = 0; //How much time should a level last
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
    static AudioClip weapon1Sound, weapon2Sound, weapon3Sound, weapon4Sound, explosionSound, advanceMenu, weaponSwitch, endGameGood, mainMenu, moveToPlanet; //Sound effects
    UserConsole console; //Create a console object
    public static LinkedList<GameObj> objects = new LinkedList<GameObj>(); //Array holding all program objects

    Random rand = new Random();
    Font font = Font.font("TimesRoman", 30.0);

    //Advance the level if appropriate and perform level introductions/conclusions as needed
    @SuppressWarnings("static-access")
	public void levelCheck(GraphicsContext gc, Stage primaryStage) {
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
        		maxEnemies = 5; //Set maximum number of enemies that may be on screen at a time for this level
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
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 10; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 1; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 25; //How many seconds should the level last
        		}
    		}
    		if (currLevel == 3) { //If the current level is 3
    			if (debugMode) { System.out.println("Level 3 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 7; //Set maximum number of enemies that may be on screen at a time for this level
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
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship//Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 2; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 35; //How many seconds should the level last
        		}
        		
    		}
    		if (currLevel == 5) { //If the current level is 5
    			if (debugMode) { System.out.println("Level 5 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 7; //Set maximum number of enemies that may be on screen at a time for this level
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
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 10; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 3; //Set maximum number of enemy types allowed in the level
        		if (testCutsceneMode) { maxLevelTime = 2; } else {
        		maxLevelTime = 45; //How many seconds should the level last
        		}
        		
    		}
    		if (currLevel == 7) { //If the current level is 7
    			if (debugMode) { System.out.println("Level 7 starting"); }
    			GameObj.boundaries = true; //Turn on window boundaries
    			objects.getFirst().x = winWidth/2; //Reset player's ship x position
    			objects.getFirst().y = winHeight; //Reset player's ship y position
    			console.show();
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 8; //Set maximum number of enemies that may be on screen at a time for this level
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
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 11; //Set maximum number of enemies that may be on screen at a time for this level
        		maxEnemyLevel = 4; //Set maximum number of enemy types allowed in the level
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
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 10; //Set maximum number of enemies that may be on screen at a time for this level
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
        		minGenTime = 1; //Set minimum amount of time until next enemy is generated
    			genEnemies = true; //Allow enemies to be generated
    			playerMovement = true; //Allow the player to move the ship
        		maxEnemies = 13; //Set maximum number of enemies that may be on screen at a time for this level
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
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("SPLASH - HIT SPACE", 20, winHeight/4);
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
    		}
    		else if (introScene == 1) { //For the second introduction scene
    			if (debugMode) { System.out.println("Instruction Screen"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("INSTRUCTIONS - HIT SPACE", 20, winHeight/4);
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
    		}
    		else if (introScene == 2) { //For the third introduction scene
    			mainMenu.stop();
    			if (debugMode) { System.out.println("Game Start Screen - Ready?"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("READY? - HIT SPACE", 20, winHeight/4);
        		objects.add(new Ship(primaryGroup, winWidth, winHeight)); //The player's ship is the first object in the objects array
        		objects.getFirst().name = "ship"; //Name the object to alter game object behavior
        		objects.getFirst().update(primaryGroup, winWidth, winHeight, frameRate); //Update the ship position on screen
        		playerExists = true; //Indicate that the player now exists
        		introScene++; //Advance the scene that should be displayed next
        		console = new UserConsole(primaryGroup, gc, winWidth, winHeight); //Add the user console
        		console.setWeapon(1); //Set the default weapon to active
        		console.show();
        		waitingForUser = true; //Indicate that user input is required before progressing
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
    		else {
    			if (debugMode) { System.out.println("Bad ending detected"); }
    			objects.getFirst().erase(primaryGroup); //Erase the player's ship
    			objects.remove(0); //Remove the player's ship from existence
    		}
    		if (introScene == 0) { //For the first introduction scene
    			if (debugMode) { System.out.println("Level " + currLevel + " introduction has started"); }
    			if (debugMode) { System.out.println("Aproaching planet"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("CUTSCENE PLANET - HIT SPACE", 20, winHeight/4);
    			moveToPlanet.play();
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
    		}
    		else if (introScene == 1) { //For the first introduction scene
    			if (debugMode) { System.out.println("Aproaching structure"); }
        		windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("CUTSCENE SURFACE - HIT SPACE", 20, winHeight/4);
        		introScene++; //Advance the scene that should be displayed next
        		waitingForUser = true; //Indicate that user input is required before progressing
    		}
    		else if (introScene == 2) { //For the first introduction scene
    			if (debugMode) { System.out.println("Aproaching Alien"); }
    			windowSetup(gc, primaryStage); //Clean and setup the window
    			gc.setFill(Color.WHITE);
    			gc.setFont(font);
    			gc.fillText("CUTSCENE ALIEN - HIT SPACE", 20, winHeight/4);
    			introScene++; //Advance the scene that should be displayed next
    			waitingForUser = true; //Indicate that user input is required before progressing   	
    		}
    		else if (introScene == 3) {  //When introduction scenes have concluded
    			if (currLevel == 11) { //If this was the last level
    				if (debugMode) { System.out.println("Running win game scenario"); }
        			windowSetup(gc, primaryStage); //Clean and setup the window
        			gc.setFill(Color.WHITE);
        			gc.setFont(font);
    				gc.fillText("Good High Score Ending", 20, winHeight/4);
    				endGameGood.cycleCountProperty().set(endGameGood.INDEFINITE);
    				endGameGood.play();
    				gameComplete = true; //Indicate that the game has completed
    				waitingForUser = true;
    				}
    			if (currLevel == 13) { //If the level jumps to 13
    				if (debugMode) { System.out.println("Running lose game scenario"); }
        			windowSetup(gc, primaryStage); //Clean and setup the window
        			gc.setFill(Color.WHITE);
        			gc.setFont(font);
    				gc.fillText("Bad High Score Ending", 20, winHeight/4);
    				gameComplete = true; //Indicate that the game has completed
    				waitingForUser = true;
    			}
    			if (currLevel < 11) {
    				levelIntroComplete = true; //Indicate that the introduction for the level is complete
    			}
    		}
    	}
    	
    	//Level completion check
    	if ((startLevelTimer) && (levelTimer >= (maxLevelTime-1))) {
    		if (debugMode) { System.out.println("Level completed"); }
    		startLevelTimer = false; //Stop counting the level timer
    		levelTimer = 0; //Reset the level timer
			levelComplete = true; //Indicate that the level is complete
			genEnemies = false; //Prevent further enemies from being generated
			playerMovement = false; //Disallow player's manual movement
			objects.getFirst().velocityUpFlag=true; //Force the player's ship upward movement on
			objects.getFirst().angleUpFlag=false; //Stop moving left
			objects.getFirst().angleDownFlag=false; //Stop moving right
			levelIntroComplete = false; 
			levelConstruction = false;
			waitingForUser = false;
			currLevel++; //Advance the level counter
			introScene = 0; //Reset cut scene counter
    		GameObj.boundaries = false; //Turn off window boundaries
    		console.hide(); //Hide the console
    		
			for (int i = objects.size(); i > 1; i--) { //For all of the objects in the game except for the ship
				objects.get(i-1).erase(primaryGroup);
				objects.remove(i-1);
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
			//Enemy generator
			if ((levelConstruction) && (genEnemies)) { //If the level construction has been completed
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
						enemyType = rand.nextInt(maxEnemyLevel) + 1;
						if (debugMode) { System.out.println("Enemy level will be: " + enemyType); }
						if (enemyType == 1) {
							if (debugMode) { System.out.println("Enemy type 1 generating"); }
							objects.add(new EnemyType1(primaryGroup, winWidth, winHeight, 3, true, 0.0, 0.0, 0.0, 0)); //Generate a new enemy of type 1
						}
						if (enemyType == 2) {
							
						}
						if (enemyType == 3) {
							
						}		
						if (enemyType == 4) {
							
						}
						if (enemyType == 5) {
							
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
				if (objects.get(i-1).weapons.size() > 0) { //If the object has an active weapon
					for (int j = objects.get(i-1).weapons.size(); j > 0; j--) { //For each of the active weapons
						objects.get(i-1).weapons.get(j-1).draw(gc, winWidth, winHeight, frameRate);
					}
				}
			}
			
			console.updateData(); //Update the data displayed in the console
			console.consoleGroup.toFront(); //Move the console to the top layer
			
		}
		if (objects.size() > 0) { //If the ship has been created
			objects.getFirst().update(primaryGroup, winWidth, winHeight, frameRate); //Update the ship positioning
			if (objects.getFirst().weapons.size() > 0) { //If the ship has an active weapon
				for (int j = objects.getFirst().weapons.size(); j > 0; j--) { //For each of the active weapons
					objects.getFirst().weapons.get(j-1).draw(gc, winWidth, winHeight, frameRate);
				}
			}
		}
    }
    
    //Check all objects for collisions and handle them if found
    public void collisionDetection(GraphicsContext gc, Stage primaryStage) throws IndexOutOfBoundsException {
    	if ((!levelComplete) && (objects.size() > 1)) { //If the level has not been completed and at least the player's ship and something else exists 
        	//Ship collision checks
    		for (int i = objects.size(); i > 1; i--) { //For all of the objects in the game except the player's ship
				if (objects.get(i-1).boundary.intersects(objects.getFirst().boundary)) { //If the object collides with the ship
        			if (debugMode) { System.out.println("Player's ship had a collision with an object"); }
    				objects.get(i-1).erase(primaryGroup); //Remove the object
    				objects.remove(i-1); //Remove the object from the objects array
    				objects.getFirst().collision = true; //Indicate that the player's ship was hit
    			}
    			else if (objects.get(i-1).weapons.size() > 0) { //If that object has launched one or more weapons
    				for (int j = objects.get(i-1).weapons.size(); j > 0; j--) { //For each of those weapons
        				if (objects.get(i-1).weapons.get(j-1).boundary.intersects(objects.getFirst().boundary)) {  //If there is a collision between the weapon and the player's ship
        					if (debugMode) { System.out.println("Player's ship had a collision with a weapon"); }
        					objects.getFirst().collision = true;
    						objects.get(i-1).weapons.get(j-1).erase(primaryGroup); //Erase the weapon
    						objects.get(i-1).weapons.remove(j-1); //Remove the weapon from that object's weapon array
        				}
        				if (objects.get(i-1).weapons.get(j-1).outsideWin) { //If the weapon is outside of the window
    						objects.get(i-1).weapons.get(j-1).erase(primaryGroup); //Erase the weapon
    						objects.get(i-1).weapons.remove(j-1); //Remove the weapon from that object's weapon array
        				}      				
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
    		
    		//Object collision checks
    		if (objects.getFirst().weapons.size() > 0) { //If the player has an active weapon
    			for (int i = objects.getFirst().weapons.size(); i > 0; i--) { //For each of those active
    				if (objects.getFirst().weapons.get(i-1).outsideWin) {
						objects.getFirst().weapons.get(i-1).erase(primaryGroup); //Erase the weapon
						objects.getFirst().weapons.remove(i-1); //Remove the weapon from the player's weapon array    					
    				}
    				else {
        				for (int j = objects.size(); j > 1; j--) { //For all objects in the object array
        					if (objects.getFirst().weapons.get(i-1).boundary.intersects(objects.get(j-1).boundary)) { //If there is a collision
        						objects.getFirst().weapons.get(i-1).erase(primaryGroup); //Erase the weapon
        						objects.getFirst().weapons.remove(i-1); //Remove the weapon from the player's weapon array
        						if (objects.get(j-1).name == "enemy1") {
        							score+=100;
        						}
        						objects.get(j-1).erase(primaryGroup); //Erase the object
        						objects.remove(j-1); //Remove the object from the objects array
        						explosionSound.play();
        					}
        				}
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
		
		//Read in necessary files from system
		weapon1Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon1.wav").toString());
		weapon2Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon2.wav").toString());
		weapon3Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon3.wav").toString());
		//weapon4Sound = new AudioClip(ClassLoader.getSystemResource("sounds/weapon4.wav").toString());
		explosionSound = new AudioClip(ClassLoader.getSystemResource("sounds/explosion.wav").toString());
		weaponSwitch = new AudioClip(ClassLoader.getSystemResource("sounds/weaponSwitch.wav").toString());
		advanceMenu = new AudioClip(ClassLoader.getSystemResource("sounds/advanceMenu.wav").toString());
		endGameGood  = new AudioClip(ClassLoader.getSystemResource("sounds/endGameGood.wav").toString());
		mainMenu  = new AudioClip(ClassLoader.getSystemResource("sounds/mainMenu.wav").toString());
		moveToPlanet  = new AudioClip(ClassLoader.getSystemResource("sounds/moveToPlanet.wav").toString());
		
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
                    	console.setWeapon(1);
                    	break;
                    case DIGIT2:
                    	console.setWeapon(2);
                    	break;
                    case DIGIT3:
                    	console.setWeapon(3);
                    	break;
                    case DIGIT4:
                    	console.setWeapon(4);
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
        		levelCheck(gc, primaryStage); //Advance the level if appropriate and perform level introductions/conclusions as needed
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
