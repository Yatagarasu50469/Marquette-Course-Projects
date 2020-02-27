/**
 *  Class: Bunny.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 21 January 2018
 *  Date Last Modified: 12 February 2018
 *  Purpose: Provide methods for dust bunnies generated and used within the primary program
 */

import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.LinkedList;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.*;

public class Bunny {
	double x, y, velocity, winWidth, winHeight, angle, radius;
	Circle bunny; //Circle describing the bunny
	boolean blue = false; //Is the bunny blue (true) or pink (false)
	Bounds boundary; //Boundary conditions for the bunny
	int randRadius = 20; //Range of additional radius for the bunny
	int minRadius = 10; //Minimum radius for the bunny
    int randVelocity = 100; //Range of additional velocity for the bunny
    int minVelocity = 50; //Minimum velocity for the bunny
    int maxVelocity = 300; //Maximum velocity that the bunny may travel
	Random randomNumber = new Random(); //Random number generator
	int collision = -1;
	int collisionFrameCounter = 0;
	boolean collisions;
	boolean babyGenerated = false; //Has this bunny produced a baby yet
	int frameCounter = 0;
	int babyTimer = 0; //How may seconds have elapsed since producing a baby bunny

	public Bunny(Group primaryGroup, double winWidth, double winHeight, boolean baby, double startX, double startY) {
		if (!baby) {
			x = randomNumber.nextInt((int) winWidth); //Set initial x position
			y = randomNumber.nextInt((int) winHeight); //Set initial y position
			collisions = true;
		}
		else {
			x = startX;
			y = startY;
			collisions = false; //Prevent collision checking
		}

		angle =  randomNumber.nextInt(360); //Set initial angle
		velocity = randomNumber.nextInt(randVelocity) + minVelocity; //Set initial velocity
		radius = randomNumber.nextInt(randRadius) + minRadius; //Set initial radius
		
		bunny = new Circle(radius); //Describe the bunny as a new circle
		boundary = bunny.getBoundsInParent(); //Save the boundary conditions for collision detection
		bunny.setStroke(Color.BLACK); //Set bunny stroke color
		if (randomNumber.nextDouble() < 0.5) { //Given a 50% chance
			blue = true; //The bunny is blue
			bunny.setFill(Color.BLUE); //Set bunny stroke color
		}
		else { //Otherwise
			bunny.setFill(Color.PINK); //Set bunny stroke color
		}
		bunny.setLayoutX(x); //Indicate where on the x layout the bunny is
		bunny.setLayoutY(y); //Indicate where on the y layout the bunny is
		primaryGroup.getChildren().add(bunny); //Add the bunny to the primary group		
	}
	
	public void draw(Group primaryGroup, GraphicsContext gc, double winWidth, double winHeight, double frameRate) {
		frameCounter++; //Increase the number of frames that have elapsed
		if (frameCounter==frameRate) { //If a second has passed
			frameCounter = 0; //Reset the frame counter
			collision = -1; //Reset the last collision id
			collisions = true; //enable collisions
			if (babyGenerated) { //If a baby has been generated
				babyTimer++; //Increase baby timer (seconds)
			}
		}
		if (babyTimer >= 5) { //After a set period of time
			babyGenerated = false; //Reset baby generated flag
			babyTimer = 0; //Reset baby seconds timer
		}
		
		//Update positions based on velocity
		x += (velocity/frameRate) * Math.cos(Math.toRadians(angle)); //Update the x coordinate based on the velocity and angle
		y -= (velocity/frameRate) * Math.sin(Math.toRadians(angle)); //Update the y coordinate based on the velocity and angle
		
		//Set window boundary conditions
		if ((x+radius) > winWidth) { //Right of the screen collision 
			x = 0;
		}
		else if ((y+radius) > winHeight) { //Bottom of the screen collision 
			y = 0;
		}
		else if (x <= -radius) { //Left of the screen collision
			x = winWidth-radius;
		}
		else if (y <= -radius) { //Top of the screen collision 
			y = winHeight-radius;
		}	

		//Move the object 
		bunny.setRotate(-angle+90); //Rotate the bunny according to the current angle
		bunny.relocate(x,y); //Move the bunny to the appropriate x and y coordinates
		boundary = bunny.getBoundsInParent(); //Save the boundary conditions for collision detection
	}
	
	public void erase(Group primaryGroup) { //If the object needs to be erased
		primaryGroup.getChildren().remove(bunny); //Remove the object from the primary group	
	}

}
