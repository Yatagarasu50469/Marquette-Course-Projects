/*
 *  Class: Ship.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 25 January 2018
 *  Date Last Modified: 25 January 2018
 *  Purpose: Provide methods for the player's avatar within the main program
 */
 
import java.util.LinkedList;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Ship {
	double x, y, velocity, winWidth, winHeight, angle;
	double xCoord[], yCoord[], polyCoord[]; //Arrays to hold the x and y coordinates for the generated polygon
	int numVertices = 4; //How many vertices should the object have
	Polygon objectDescription; //Polygon describing the object
	Bounds boundary; //Boundary conditions for the object
	int xSize = 10; //Width of the object
	int ySize = 30; //Height of the object
	boolean velocityUpFlag = false; //Should the velocity be increasing
	boolean velocityDownFlag = false; //Should the velocity be decreasing
	boolean angleUpFlag = false; //Should the angle be increasing
	boolean angleDownFlag = false; //Should the angle be decreasing
	boolean fireFlag = false; //Should a missile be firing
    public static LinkedList<Missile> missileList = new LinkedList<Missile>(); //Array holding the missiles
    int maxMissiles = 3; //Maximum number of missiles allowed on screen
    int maxVelocity = 300; //Maximum velocity that the ship can move at
    
	public Ship(Group primaryGroup, double winWidth, double winHeight) {
		velocity = 0; //Set initial velocity to 0
		angle = 90; //Set initial angle that the ship points
		x = winWidth/2.0; //Set initial x position to center of the screen
		y = winHeight/2.0; //Set initial y position to center of the screen
		
		//Set polygon coordinates
		polyCoord = new double[8];
		polyCoord[0] = (winWidth/2.0);
		polyCoord[1] = (winHeight/2.0); 
		polyCoord[2] = (winWidth/2.0)+10; 
		polyCoord[3] = (winHeight/2.0)+10; 
		polyCoord[4] = (winWidth/2.0); 
		polyCoord[5] = (winHeight/2.0)-20; 
		polyCoord[6] = (winWidth/2.0)-10;
		polyCoord[7] = (winHeight/2.0)+10; 
		
		objectDescription = new Polygon(polyCoord); //Create a new polygon based on the polyArray 
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection
		objectDescription.setStroke(Color.WHITE); //Set object stroke color
		objectDescription.setLayoutX(x); //Indicate where on the x layout the object is
		objectDescription.setLayoutY(y); //Indicate where on the y layout the object is
		primaryGroup.getChildren().add(objectDescription); //Add the object to the primary group
	}
	
	public void draw(Group primaryGroup, GraphicsContext gc, double winWidth, double winHeight, double frameRate) {
		if (velocityUpFlag) {
			if (velocity < maxVelocity) {
				velocity+=1;
			}
		}
		if (velocityDownFlag) {
			if (velocity > 0) {
				velocity-=1;
			}
		}
		if (angleUpFlag) {
			angle+=1;
		}
		if (angleDownFlag) {
			angle-=1;
		}
		if (fireFlag) {
			if (missileList.size() < maxMissiles) { //If the number of missiles on screen is less than the maximum
        		missileList.add(new Missile(primaryGroup, x, y, angle, velocity, winWidth, winHeight)); //Create a new missile at the location of the player's ship
        	}
			fireFlag=false;
		}
		//Update velocity impact on position
		x += (velocity/frameRate) * Math.cos(Math.toRadians(angle)); //Update the x coordinate based on the velocity and angle
		y -= (velocity/frameRate) * Math.sin(Math.toRadians(angle)); //Update the y coordinate based on the velocity and angle
		//Set window boundary conditions
		if (x >= winWidth-40) { //If the center of the object goes past the right of the screen
			x = winWidth-40; //Prevent movement
		}
		else if (y >= winHeight-40) { //If the center of the object goes past the bottom of the screen
			y = winHeight-40; //Prevent movement
		}
		else if (x <= 0) { //If the center of the object goes past the left of the screen
			x = 0; //Prevent movement
		}
		else if (y <= 0) { //If the center of the object goes past the top of the screen
			y = 0; //Prevent movement
		}		
		//Move the object 
		objectDescription.setRotate(-angle+90); //Rotate the object according to the current angle
		objectDescription.relocate(x,y); //Move the object to the appropriate x and y coordinates
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection

	}
	
	public void erase(Group primaryGroup) { //If the object needs to be erased
		primaryGroup.getChildren().remove(objectDescription); //Remove the object from the primary group	
	}
}
