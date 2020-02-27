/**
 *  Program: COSC_3550_hw1
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 20 January 2018
 *  Date Last Modified: 20 January 2018
 *  Purpose: Display a basic image with varied objects using JavaFX calls. 
 *  
 *  	==Requirements==			==Implemented==
 *  	Image Size 600x500			Yes
 *   	Two types of shapes			Yes
 *   	Two colors					Yes
 *   	For loop drawing			Yes
 *   	
 *   	==Extra Goals==				==Implemented==
 *   	Animation					Yes
 *   	Random ball positions		Yes
 *   	Random color changing		Yes
 *   	User control				Yes
 *   	Boundary conditions			Yes
 *		
 *		==User Controls==			==Action==
 *		Mouse:						NA
 *		Key:	UP					Lines move faster
 *				DOWN				Lines move slower
 *
 *	References:
 *  https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
 *  
 *
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
import java.util.Random;
import javafx.scene.shape.*;
import javafx.scene.input.KeyEvent;

public class primary extends Application{
	//Global variables - Alterable
	//Graphics
	String winTitle = "Homework 1"; // Window title
	double winWidth = 600; //Window screen width
	double winHeight = 500; //Window screen height
	double frameRate = 10; //Animation frame rate; FPS
	int frameCounter = 0; //Counter for how many frames have passed so far
	Color backgroundColor = Color.BLACK; //Base background color
	//Objects
	Random randomNumber = new Random(); //Random number generator
	int ovalWidth = 40; //Width of the generated ovals
	int ovalHeight = 40; //Height of the generated ovals
	int line1x = randomNumber.nextInt((int)winWidth); //Initial line 1 bottom x coordinate
	int line2x = randomNumber.nextInt((int)winWidth); //Initial line 2 bottom x coordinate
	int line3x = randomNumber.nextInt((int)winWidth); //Initial line 3 bottom x coordinate
	int line1Speed = 100; //Speed line 1 travels at
	int line2Speed = 70; //Speed line 2 travels at
	int line3Speed = 80; //Speed line 3 travels at
	boolean line1Back=false; //Line 1 should move back flag
	boolean line2Back=false; //Line 2 should move back flag
	boolean line3Back=false; //Line 3 should move back flag
	
	//Global variables - Constant
	//Graphics
	Group primaryGroup; //Declare a new group
	Scene primaryScene; //Declare a new scene
	Canvas primaryCanvas; //Declare a new canvas
    GraphicsContext gc; //Declare a graphics context 
    KeyFrame keyFrame; //Declare a key frame for the animation
    Timeline primaryLoop; //Establish an animation timeline

    //Drawing method for object graphics
	void addGraphics(GraphicsContext gc, Stage primaryStage) { //Add items to the graphics context
		int x, y; //Ball positions
		int red, green, blue; //Ball colors
		Line line1, line2, line3;
		
		gc.clearRect(0, 0, winWidth, winHeight); //Clear the canvas
		//Background
		gc.setFill(backgroundColor); //Set the fill color to black
		gc.fillRect(0, 0, winWidth, winHeight); //Fill in the background
		primaryStage.setTitle(winTitle + " - FPS: " + Integer.toString((int) frameRate));

	    //Rectangles
		
		red = randomNumber.nextInt(255); //Generate a random red value
		green = randomNumber.nextInt(255); //Generate a random green value
		blue = randomNumber.nextInt(255); //Generate a random blue value
		gc.setFill(Color.rgb(red, green, blue));
		gc.fillRect(0, 0, 180, winHeight);	
		
		for (int i = 0; i < 7; i++) { //For each of the rectangles to be drawn
			red = randomNumber.nextInt(255); //Generate a random red value
			green = randomNumber.nextInt(255); //Generate a random green value
			blue = randomNumber.nextInt(255); //Generate a random blue value
			gc.setFill(Color.rgb(red, green, blue));
			gc.fillRect((i*30), winHeight-30*(i+1), winWidth, 30);		
		}

		//Ovals
		for (int i = 0; i < 10; i++) { //For a the number of ovals to be generated
			x = randomNumber.nextInt(((int) winWidth)-ovalWidth); //Generate a random x position within the window boundaries 
			y = randomNumber.nextInt(((int) winHeight)-ovalHeight); //Generate a random y position within the window boundaries 
			red = randomNumber.nextInt(255); //Generate a random red value
			green = randomNumber.nextInt(255); //Generate a random green value
			blue = randomNumber.nextInt(255); //Generate a random blue value
		
			gc.setFill(Color.rgb(red, green, blue)); //Set the oval fill color
			gc.fillOval(x, y, ovalWidth, ovalHeight); //Fill in the oval
		}
		
		//Lines
		red = randomNumber.nextInt(255); //Generate a random red value
		green = randomNumber.nextInt(255); //Generate a random green value
		blue = randomNumber.nextInt(255); //Generate a random blue value
		gc.setStroke(Color.rgb(red, green, blue)); //Set the line color
		gc.setLineWidth(10);	
	    gc.beginPath();
	    gc.moveTo((winWidth/2), 0);
	    gc.lineTo(line1x, winHeight);
	    gc.closePath();
	    gc.stroke();
	    
		red = randomNumber.nextInt(255); //Generate a random red value
		green = randomNumber.nextInt(255); //Generate a random green value
		blue = randomNumber.nextInt(255); //Generate a random blue value
		gc.setStroke(Color.rgb(red, green, blue)); //Set the line color
		gc.setLineWidth(10);	
	    gc.beginPath();
	    gc.moveTo((winWidth/2), 0);
	    gc.lineTo(line2x, winHeight);
	    gc.closePath();
	    gc.stroke();
	    
		red = randomNumber.nextInt(255); //Generate a random red value
		green = randomNumber.nextInt(255); //Generate a random green value
		blue = randomNumber.nextInt(255); //Generate a random blue value
		gc.setStroke(Color.rgb(red, green, blue)); //Set the line color
		gc.setLineWidth(10);	
	    gc.beginPath();
	    gc.moveTo((winWidth/2), 0);
	    gc.lineTo(line3x, winHeight);
	    gc.closePath();
	    gc.stroke();
	      
	}
	
	//Graphics update method
    void update() { 
    	//Line 1
    	if ((line1x < winWidth) && (!line1Back)) { //if the end point of line 1 has not reached the edge of the screen and it is not moving back
    		line1x += Math.round(line1Speed/frameRate); //Move it further to the right
    		if (line1x >= winWidth) { //If the line has made it past the right edge
    			line1Back=true;
    		}
    	}
    	if (line1Back) { //If the line is moving back
    		line1x -= Math.round(line1Speed/frameRate); //Move it further to the left
    		if (line1x <= 0) { //If the line has made it past the left edge
    			line1Back=false; //Stop moving the line to the left
    		}
    	}
    	//Line 2
    	if ((line2x < winWidth) && (!line2Back)) { //if the end point of line 1 has not reached the edge of the screen and it is not moving back
    		line2x += Math.round(line2Speed/frameRate); //Move it further to the right
    		if (line2x >= winWidth) { //If the line has made it past the right edge
    			line2Back=true;
    		}
    	}
    	if (line2Back) { //If the line is moving back
    		line2x -= Math.round(line2Speed/frameRate); //Move it further to the left
    		if (line2x <= 0) { //If the line has made it past the left edge
    			line2Back=false; //Stop moving the line to the left
    		}
    	}
    	//Line3
    	if ((line3x < winWidth) && (!line3Back)) { //if the end point of line 1 has not reached the edge of the screen and it is not moving back
    		line3x += Math.round(line3Speed/frameRate); //Move it further to the right
    		if (line3x >= winWidth) { //If the line has made it past the right edge
    			line3Back=true;
    		}
    	}
    	if (line3Back) { //If the line is moving back
    		line3x -= Math.round(line3Speed/frameRate); //Move it further to the left
    		if (line3x <= 0) { //If the line has made it past the left edge
    			line3Back=false; //Stop moving the line to the left
    		}
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
                    	line1Speed+=10; 
                    	line2Speed+=10; 
                    	line3Speed+=10; 
                    	break;
                    case DOWN:	
                    	line1Speed-=10; 
                    	line2Speed-=10; 
                    	line3Speed-=10; 
                    	if (line1Speed <= 0) { line1Speed = 0; }
                    	if (line2Speed <= 0) { line2Speed = 0; }
                    	if (line3Speed <= 0) { line3Speed = 0; }
                    	break;
                }
            }
        });

		primaryScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
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
        		update(); //Update objects
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
