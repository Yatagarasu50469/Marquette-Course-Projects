/**
 *  Program: COSC_3550_hw2
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 24 January 2018
 *  Date Last Modified: 30 January 2018
 *  Purpose: Use both animation and user input to form a basic interactive program. User may move using the arrow keys
 *  and fire a weapon using the space bar. Weapon and polygon (planetoid) collisions will generate points for the user.
 *  The user is limited to 3 weapon blasts at a time which resets when they go off screen or collide with a planetoid. 
 *  
 *  	==Requirements==			==Implemented==
 *  	Animation					Yes
 *  	User Input					Yes
 *  	Control over animation		Yes
 *   	
 *   	==Extra Goals==				==Implemented==
 *   	Ship turning mechanics		Yes
 *   	Randomized planetoids		Yes
 *   	Randomized movement			Yes
 *   	Collision detection			Yes
 *   	State mach. movement		Yes
 *   	Boundary conditions			Yes
 *		Scoring						Yes
 *
 *		==User Controls==			==Action==
 *		Mouse:						NA
 *		Key:	UP					Increase speed
 *				DOWN				Decrease speed
 *				LEFT				Rotate counterclockwise
 *				RIGHT				Rotate clockwise
 *				SPACE				Fire
 *
 *	References:
 *  https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
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

public class primary extends Application{
	//Global variables - Alterable
	String winTitle = "Planetoid Defense"; // Window title
	double winWidth = 1000; //Window screen width
	double winHeight = 1000; //Window screen height
	int frameCounter = 0; //Counter for how many frames have passed so far
	Color backgroundColor = Color.BLACK; //Base background color
	
	//Global variables - Constant
	double frameRate = 60; //Animation frame rate; FPS
	Group primaryGroup; //Declare a new group
	Scene primaryScene; //Declare a new scene
	Canvas primaryCanvas; //Declare a new canvas
    GraphicsContext gc; //Declare a graphics context 
    KeyFrame keyFrame; //Declare a key frame for the animation
    Timeline primaryLoop; //Establish an animation timeline
    Ship playerShip; //Initialize a ship variable for the player
	Random randomNumber = new Random(); //Random number generator
    public static LinkedList<gObject> objects = new LinkedList<gObject>(); //Array holding program objects
    double seconds = 0; //How many seconds have elapsed
    double genRate = 1; //How many objects can be generated per second
	int fCounter = 0; //How many frames have been generated
    boolean initial = true; //Flag to indicate if the program is just starting
    int score = 0; //Number of points acheived by the user
	
    //Drawing method for object graphics; also updates object physics
	void addGraphics(GraphicsContext gc, Stage primaryStage) { //Add items to the graphics context
		
		//Background
		gc.clearRect(0, 0, winWidth, winHeight); //Clear the canvas
		gc.setFill(backgroundColor); //Set the fill color to black
		gc.fillRect(0, 0, winWidth, winHeight); //Fill in the background
		primaryStage.setTitle(winTitle + " - Score: " + Integer.toString((int) score)); //Set the window title

		if (objects.size() > 0) { //If there are objects to be drawn and updated
			for (int i = 0; i < objects.size(); i++) { //For each of the objects to be drawn
				objects.get(i).draw(gc, winWidth, winHeight, frameRate); //Update and draw the object at its location
			}
		}
		if (playerShip.missileList.size() > 0) { //If there are missiles to be drawn and updated
			for (int i = 0; i < playerShip.missileList.size(); i++) { //For each of the missiles to be drawn
				playerShip.missileList.get(i).draw(gc, winWidth, winHeight, frameRate); //Update and draw the missile at its location
				if (playerShip.missileList.get(i).outsideWin) { //If the missile is outside of the window
					playerShip.missileList.get(i).erase(primaryGroup); //Erase the missile
					playerShip.missileList.remove(i); //Remove the missile from the array
					break; //Exit the loop as the missile no longer exists
				}
				if (objects.size() > 0) { //If there are objects to be drawn and updated
					for (int j = 0; j < objects.size(); j++) {  //For each of the objects
						if (playerShip.missileList.get(i).boundary.intersects(objects.get(j).boundary)) { //If the missile intersects with an object
							playerShip.missileList.get(i).erase(primaryGroup); //Erase the missile
							playerShip.missileList.remove(i); //Remove the missile from the array
							objects.get(j).erase(primaryGroup); //Erase the object
							objects.remove(j); //Remove the object from the array
							score+=10; //Increase the user's score
							break; //Exit the loop as the missile no longer exists
						}
					}

				}
			}
		}
		playerShip.draw(primaryGroup, gc, winWidth, winHeight, frameRate); //Draw and update the player's ship
	}
	
	//Graphics update method; initialize stages and physics
    void update() { 
    	if (initial) { //During initialization
    		playerShip = new Ship(primaryGroup, winWidth, winHeight);
    		for (int i = 0; i <=5; i++) { //For the number of objects to be generated in this level
    			objects.add(new gObject(primaryGroup, winWidth, winHeight));
    		}
    		initial=false; //Turn off level initialization
    	}
    	
    }
    
    //Graphics initialization method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryGroup = new Group(); //Initialize a group of scene objects
		primaryScene = new Scene(primaryGroup); //Initialize the primary scene
		primaryCanvas = new Canvas(winWidth, winHeight); //Initialize the primary canvas
		
		primaryStage.setTitle(winTitle); //Set the stage title
		
		primaryScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) { //Should a key press event occur
                switch (event.getCode()) { //Check the key pressed
                    case UP:
                    	playerShip.velocityUpFlag=true;
                    	break;
                    case DOWN:
                    	playerShip.velocityDownFlag=true;
                    	break;
                    case LEFT:
                    	playerShip.angleUpFlag=true;
                    	break;
                    case RIGHT:
                    	playerShip.angleDownFlag=true;
                    	break;
                    case SPACE:
                    	playerShip.fireFlag=true;
                    	break;
                }
            }
        });

		primaryScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                case UP:
                	playerShip.velocityUpFlag=false;
                	break;
                case DOWN:
                	playerShip.velocityDownFlag=false;
                	break;
                case LEFT:
                	playerShip.angleUpFlag=false;
                	break;
                case RIGHT:
                	playerShip.angleDownFlag=false;
                	break;
                case SPACE:
                	playerShip.fireFlag=false;
                	break;
                }
            }
        });

		primaryStage.setScene(primaryScene); //Set the stage with the first scene
		primaryGroup.getChildren().add(primaryCanvas); //Add the canvas to the primary group
		GraphicsContext gc = primaryCanvas.getGraphicsContext2D(); //Push canvas draw calls to graphics context
        
        //Create animation loop
        keyFrame = new KeyFrame(Duration.seconds(1.0/frameRate), new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(javafx.event.ActionEvent event) { //Should an event occur
        		update(); //Update objects
        		winWidth=primaryStage.getWidth(); //Retrieve the width of the resized window
        		winHeight=primaryStage.getHeight(); //Retrieve the height of the resized window
        		primaryCanvas.setWidth(winWidth); //Set the canvas width to the new value
        		primaryCanvas.setHeight(winHeight); //Set the canvas height to the new value
        		addGraphics(gc, primaryStage); //Add items to the graphics context
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
