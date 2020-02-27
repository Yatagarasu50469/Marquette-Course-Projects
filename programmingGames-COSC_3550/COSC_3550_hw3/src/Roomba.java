/*
 *  Class: Roomba.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 25 January 2018
 *  Date Last Modified: 12 February 2018
 *  Purpose: Provide methods for the player's roomba within the main program
 */
 
import java.util.LinkedList;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;

public class Roomba {
	double x, y, velocity, lastVelocity, winWidth, winHeight, angle, hx, hy, centerX, centerY, destAngle, remainDist;
	Circle roomba; //Circle describing the roomba
	Circle headlight; //Circle describing the headlight
	
	Bounds boundary; //Boundary conditions for the roomba
	int radius = 50; //Radius of the Roomba
	int headRadius = radius/4; //Radius of the headlight
	boolean velocityUpFlag = false; //Should the velocity be increasing
	boolean velocityDownFlag = false; //Should the velocity be decreasing
	boolean angleUpFlag = false; //Should the angle be increasing
	boolean angleDownFlag = false; //Should the angle be decreasing
	boolean fireFlag = false; //Should a missile be firing
	boolean boundaries = true; //Should the roomba be able to move offscreen
	boolean controls = true; //Should the user be able to control the roomba
	boolean turn = true; //Should the roomba continue turning (moveTo flag)
	boolean moveComplete = false; //Has the last moveTo command been completed
    public static LinkedList<Missile> missileList = new LinkedList<Missile>(); //Array holding the blasts
    int maxMissiles = 3; //Maximum number of blasts allowed on screen
    int maxVelocity = 300; //Maximum velocity that the ship can move at
    
	public Roomba(Group primaryGroup, double winWidth, double winHeight) {
		velocity = 0; //Set initial velocity to 0
		angle = 90; //Set initial angle that the ship points
		x = winWidth/2.0; //Set initial x position to center of the screen
		y = winHeight/2.0; //Set initial y position to center of the screen
		hx = x;
		hy = y+10;
		roomba = new Circle(radius); //Describe the roomba as a new circle
		boundary = roomba.getBoundsInParent(); //Save the boundary conditions for collision detection
		roomba.setStroke(Color.BLACK); //Set roomba stroke color
		roomba.setFill(Color.WHITE); //Set roomba stroke color
		roomba.setLayoutX(x); //Indicate where on the x layout the roomba is
		roomba.setLayoutY(y); //Indicate where on the y layout the roomba is
		primaryGroup.getChildren().add(roomba); //Add the roomba to the primary group

		headlight = new Circle(headRadius);
		headlight.setLayoutX(hx); //Indicate where on the x layout the roomba is
		headlight.setLayoutY(hy); //Indicate where on the y layout the roomba is
		headlight.setFill(Color.YELLOW);
		headlight.setStroke(Color.WHITE);
		primaryGroup.getChildren().add(headlight); //Add the roomba to the primary group
		
	}
	
	public void draw(Group primaryGroup, GraphicsContext gc, double winWidth, double winHeight, double frameRate, Image roombaImage, Image headlightImage) {
		
		if (controls) { //If user controls are allowed
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
					
	        		missileList.add(new Missile(primaryGroup, x+radius, y+radius, angle, velocity, winWidth, winHeight)); //Create a new missile at the location of the player's ship
	        	}
				fireFlag=false;
			}
		}
		
		if (angle >= 360) { 
			angle = 0;
		}
		if (angle < 0) {
			angle += 360;
		}
		
		//Update positions based on velocity
		x += (velocity/frameRate) * Math.cos(Math.toRadians(angle)); //Update the x coordinate based on the velocity and angle
		y -= (velocity/frameRate) * Math.sin(Math.toRadians(angle)); //Update the y coordinate based on the velocity and angle
		hx = x+radius-headRadius; //Move headlight to the x center of the roomba
		hy = y+radius-headRadius; //Move headlight to the y center of the roomba
		hx += (radius-headRadius) * Math.cos(Math.toRadians(angle)); //Move headlight to the x edge of the roomba, based on angle
		hy -= (radius-headRadius) * Math.sin(Math.toRadians(angle)); //Move headlight to the y edge of the roomba, based on angle
		
		if (boundaries) { //If boundaries are in effect
			if (x >= winWidth-(radius*2)) { //If the center of the roomba goes past the right of the screen
				x = winWidth-(radius*2); //Prevent movement
			}
			if (y >= winHeight-(radius*2)) { //If the center of the roomba goes past the bottom of the screen
				y = winHeight-(radius*2); //Prevent movement
			}
			if (x <= 0) { //If the center of the roomba goes past the left of the screen
				x = 0; //Prevent movement
			}
			if (y <= 0) { //If the center of the roomba goes past the top of the screen
				y = 0; //Prevent movement
			}	
		}
		//Move the roomba and headlight
		roomba.setRotate(-angle+90); //Rotate the roomba according to the current angle
		roomba.relocate(x,y); //Move the roomba to the appropriate x and y coordinates
		headlight.relocate(hx, hy); //Move the headlight to the appropriate x and y coordinates
		boundary = roomba.getBoundsInParent(); //Save the boundary conditions for collision detection

	}
	
	public void erase(Group primaryGroup) { //If the roomba needs to be erased
		primaryGroup.getChildren().remove(roomba); //Remove the roomba from the primary group	
	}

	public void moveToPoint(double destX, double destY) {
		controls=false; //Turn off user controls until the destination has been reached
		centerX = x + (double) radius; //Calculate the center x coordinate of the roomba
		centerY = y + (double) radius; //Calculate the center y coordinate of the roomba
		lastVelocity = velocity; //Make a backup of the velocity value 
		remainDist = Math.sqrt(Math.pow((destX - centerX), 2) + Math.pow((destY - centerY), 2));
		if (!moveComplete) {
			if (remainDist > 5.0){
				destAngle = 180 + Math.atan2(destY - centerY, centerX - destX) * (180 / Math.PI);

				if (((100.0 * Math.abs((angle-destAngle)/destAngle)) < 1)) {
					turn = false; //Once within range, don't bother turning
					angle = destAngle;
					velocity = lastVelocity;
				}
				if (turn) {
					angle+=1; //Continue rotating
					velocity = 0;
				}
				if (!turn) {
					angle = destAngle;
					velocity = lastVelocity;
				}	
			}
			else {
				moveComplete = true;
				controls = true; //Return user control
				velocity = lastVelocity;
				turn = true; //Reset turning flag
			}
		}
		
	}
}
