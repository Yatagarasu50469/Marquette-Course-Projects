/**
 *  Program: COSC_3550_hw3 - Great Dust Bunny Uprising
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 24 January 2018
 *  Date Last Modified: 12 February 2018
 *  Purpose: Use both animation and user input to form a game where a Roomba collects dust bunnies. 
 *  
 *  	==Requirements==			==Implemented==
 *  	Twelve bunnies				Yes
 *  	Random move. vect.			Yes
 *  	Wrap-Around effect			Yes
 *  	Roomba movement				Yes
 *  	Roomba headlight			Yes
 *  	Roomba collisions			Yes
 *  	Timer (elapsed)				Yes
 *  	
 *   	==Extra Goals==				==Implemented==
 *   	Bunny collisions			Yes
 *   	Baby bunnies				Yes
 *   	Roomba Speed				Yes
 *   	Scared bunnies				No
 *   	Bunny blaster				Yes (basic implementation)
 *   	Mouse set dest.				Yes
 *   	Images						Yes (only for background)
 *   	Resizable Window			Yes
 *   
 *   	==Bugs==					==Resolution==
 *   	Exponential bunnies!		Cooldown Timers, upward limit
 *
 *		==User Controls==			==Action==
 *		Mouse:						Click to set destination
 *		Key:	UP					Increase speed
 *				DOWN				Decrease speed
 *				LEFT				Rotate counterclockwise
 *				RIGHT				Rotate clockwise
 *				SPACE				Fire bunny blaster
 *
 *	References:
 *  https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
 *  https://stackoverflow.com/questions/15013913/checking-collision-of-shapes-with-javafx
 */

//Import statements
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import java.util.LinkedList;
import java.util.Random;
import javafx.scene.input.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.image.Image;

public class primary extends Application{
	//Global variables - Alterable
	String winTitle = "Great Bunny Dust Rising"; // Window title
	double winWidth = 1000; //Window screen width
	double winHeight = 1000; //Window screen height
	int frameCounter = 0; //Counter for how many frames have passed so far
	Color backgroundColor = Color.BLACK; //Base background color
	int maxBunnies = 50; //Maximum allowable number of bunnies
	
	//Global variables - Constant
	double frameRate = 60; //Animation frame rate; FPS
	Group primaryGroup; //Declare a new group
	Scene primaryScene; //Declare a new scene
	Canvas primaryCanvas; //Declare a new canvas
    GraphicsContext gc; //Declare a graphics context 
    KeyFrame keyFrame; //Declare a key frame for the animation
    Timeline primaryLoop; //Establish an animation timeline
    Roomba playerRoomba; //Initialize a ship variable for the player
	Random randomNumber = new Random(); //Random number generator
    public static LinkedList<Bunny> bunnies = new LinkedList<Bunny>(); //Array holding program bunnies
    double seconds = 0; //How many seconds have elapsed
    double genRate = 1; //How many bunnies can be generated per second
	int fCounter = 0; //How many frames have been generated
    boolean initial = true; //Flag to indicate if the program is just starting
    int globalTimer = 0; //Number of points achieved by the user
    double distance; //Distance between center of bunnies
    double distX, distY; //X and Y distances between the bunny centers
	Font font = Font.font("TimesRoman", FontPosture.ITALIC, 60.0);
	double moveToX, moveToY;
	boolean mouseMove;
	boolean gameOver = false;
	Image backgroundImage, roombaImage, headlightImage;
	

    //Drawing method for object graphics; also updates object physics
	void addGraphics(GraphicsContext gc, Stage primaryStage) { //Add items to the graphics context
		if (!gameOver) {
			frameCounter++; //Increase the count of elapsed frames
			if (frameCounter == frameRate) { //If a second passes
				globalTimer++; //Increase the global timer
				frameCounter=0; //Reset the frame counter
			}
		}
		
		//Background
		gc.clearRect(0, 0, winWidth, winHeight); //Clear the canvas
		gc.setFill(backgroundColor); //Set the fill color to black
		gc.fillRect(0, 0, winWidth, winHeight); //Fill in the background
		gc.drawImage(backgroundImage, 0, 0, winWidth, winHeight); //Draw a background image
		
		primaryStage.setTitle(winTitle + " - Time Elapsed: " + Integer.toString(globalTimer)); //Set the window title
		if (bunnies.size() > 0) { //If there are bunnies to be drawn and updated
			for (int i = 0; i < bunnies.size(); i++) { //For each of the bunnies to be drawn
				bunnies.get(i).draw(primaryGroup, gc, winWidth, winHeight, frameRate); //Update and draw the object at its location
				for (int j = 0; j < bunnies.size(); j++) {
					//Calculate the distances between all of the bunnies
					distX = (bunnies.get(i).x + bunnies.get(i).radius) - (bunnies.get(j).x + bunnies.get(j).radius);
					distY = (bunnies.get(i).y + bunnies.get(i).radius) - (bunnies.get(j).y + bunnies.get(j).radius);
					distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
					if ((i != j) && (distance <= (bunnies.get(i).radius + bunnies.get(j).radius))) { //If a bunny collides with another
						if (!((bunnies.get(i).collision == j) && (bunnies.get(j).collision == i))) { //If this collision has not just occurred within the last second
							if (bunnies.get(i).collisions && bunnies.get(j).collisions) { //If collisions have been enabled
								//Perform physics on the massless bunnies
								double swap = bunnies.get(j).angle; 
								double swap2 = bunnies.get(i).angle;
								bunnies.get(i).angle = swap;
								bunnies.get(j).angle = swap2;
								bunnies.get(i).collision = j;
								bunnies.get(j).collision = i;
								if (bunnies.size() < maxBunnies) { //If the number of bunnies is less than the max allowable 
									if (!bunnies.get(i).babyGenerated && !bunnies.get(j).babyGenerated) { 
										if ((!bunnies.get(i).blue) && (bunnies.get(j).blue)) { //If i was a pink bunny and j was blue
							               bunnies.add(new Bunny(primaryGroup, winWidth, winHeight, true, bunnies.get(i).x, bunnies.get(i).y));
							               bunnies.get(i).babyGenerated = true;
							               bunnies.get(j).babyGenerated = true;
										}
										else if ((!bunnies.get(j).blue) && (bunnies.get(i).blue))  { //Otherwise if j was a pink bunny and i was blue
											bunnies.add(new Bunny(primaryGroup, winWidth, winHeight, true, bunnies.get(j).x, bunnies.get(j).y));
											bunnies.get(i).babyGenerated = true;
								           	bunnies.get(j).babyGenerated = true;
										}
									}
								}
							}
						}
					}
				}
				//Calculate the distances between the bunny and the roomba
				distX = (bunnies.get(i).x + bunnies.get(i).radius) - (playerRoomba.x + playerRoomba.radius);
				distY = (bunnies.get(i).y + bunnies.get(i).radius) - (playerRoomba.y + playerRoomba.radius);
				distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
				if (distance <= (bunnies.get(i).radius + playerRoomba.radius)) {
					bunnies.get(i).erase(primaryGroup);
					bunnies.remove(i);
				}
			}
		}
		else { //Otherwise there are no bunnies remaining
			gameOver = true; //Stop the timer
			//Move roomba offscreen
			if (!playerRoomba.moveComplete) {
				playerRoomba.boundaries = false; //Turn off roomba boundary conditions
				playerRoomba.velocity = 300; //Set roomba velocity
				playerRoomba.moveToPoint(-50,0);
			}
			else if (playerRoomba.moveComplete) {
				playerRoomba.velocity=0;
			}
			
			gc.setFill(Color.LIME);
			gc.setFont(font);
			gc.fillText("You won", winWidth/4, winHeight/4);
			gc.fillText("in " + globalTimer + " seconds!" ,winWidth/4,(winHeight/4)+50);
		}
		if (playerRoomba.missileList.size() > 0) { //If there are missiles to be drawn and updated
			for (int i = 0; i < playerRoomba.missileList.size(); i++) { //For each of the missiles to be drawn
				playerRoomba.missileList.get(i).draw(gc, winWidth, winHeight, frameRate); //Update and draw the missile at its location
				if (playerRoomba.missileList.get(i).outsideWin) { //If the missile is outside of the window
					playerRoomba.missileList.get(i).erase(primaryGroup); //Erase the missile
					playerRoomba.missileList.remove(i); //Remove the missile from the array
					break; //Exit the loop as the missile no longer exists
				}
				if (bunnies.size() > 0) { //If there are bunnies to be drawn and updated
					for (int j = 0; j < bunnies.size(); j++) {  //For each of the bunnies
						if (playerRoomba.missileList.get(i).boundary.intersects(bunnies.get(j).boundary)) { //If the missile intersects with an object
							playerRoomba.missileList.get(i).erase(primaryGroup); //Erase the missile
							playerRoomba.missileList.remove(i); //Remove the missile from the array
							bunnies.get(j).erase(primaryGroup); //Erase the object
							bunnies.remove(j); //Remove the object from the array
							break; //Exit the loop as the missile no longer exists
						}
					}
				}
			}
		}
		if (mouseMove) {
			if (!playerRoomba.moveComplete) {
				playerRoomba.controls = false; //Turn off roomba controls
				playerRoomba.velocity = 300; //Set roomba velocity
				playerRoomba.moveToPoint(moveToX,moveToY);
			}
			else if (playerRoomba.moveComplete) {
				playerRoomba.velocity=0;
				mouseMove = false;
				playerRoomba.controls = true;
				playerRoomba.moveComplete = false;
			}
		}
		playerRoomba.draw(primaryGroup, gc, winWidth, winHeight, frameRate, roombaImage, headlightImage); //Draw and update the player's ship
	}
	
	//Graphics update method; initialize stages and physics
    void update() { 
    	if (initial) { //During initialization
    		playerRoomba = new Roomba(primaryGroup, winWidth, winHeight);
    		for (int i = 1; i <= 12; i++) { //For the number of bunnies to be generated in this level
    			bunnies.add(new Bunny(primaryGroup, winWidth, winHeight, false, 0, 0));
    		}
    		initial=false; //Turn off level initialization
    	}
    }
    
    //Graphics initialization method
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Pull in image resources here
		backgroundImage = new Image("/images/background.jpg");
		roombaImage = new Image("/images/roombaImage.jpg");
		headlightImage = new Image("/images/headlightImage.jpg");
		
		//Set up graphical environment
		primaryGroup = new Group(); //Initialize a group of scene bunnies
		primaryScene = new Scene(primaryGroup); //Initialize the primary scene
		primaryCanvas = new Canvas(winWidth, winHeight); //Initialize the primary canvas
		primaryStage.setTitle(winTitle); //Set the stage title
		primaryScene.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	           mouseMove = true; //Set a flag indicating 
	           moveToX = event.getSceneX();
	           moveToY = event.getSceneY(); 
	        }
	    });
		
		primaryScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) { //Should a key press event occur
                switch (event.getCode()) { //Check the key pressed
                    case UP:
                    	playerRoomba.velocityUpFlag=true;
                    	break;
                    case DOWN:
                    	playerRoomba.velocityDownFlag=true;
                    	break;
                    case LEFT:
                    	playerRoomba.angleUpFlag=true;
                    	break;
                    case RIGHT:
                    	playerRoomba.angleDownFlag=true;
                    	break;
                    case SPACE:
                    	playerRoomba.fireFlag=true;
                    	break;
                }
            }
        });

		primaryScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                case UP:
                	playerRoomba.velocityUpFlag=false;
                	break;
                case DOWN:
                	playerRoomba.velocityDownFlag=false;
                	break;
                case LEFT:
                	playerRoomba.angleUpFlag=false;
                	break;
                case RIGHT:
                	playerRoomba.angleDownFlag=false;
                	break;
                case SPACE:
                	playerRoomba.fireFlag=false;
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
        		update(); //Update bunnies
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
