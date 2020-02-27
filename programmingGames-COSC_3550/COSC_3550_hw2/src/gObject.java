/**
 *  Class: gObject.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 21 January 2018
 *  Date Last Modified: 25 January 2018
 *  Purpose: Provide methods for planetoids generated and used within the primary program
 */

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.*;

public class gObject {
	Node view;
	double x, y, xv, yv, size;
	int numVertices; //How many vertices should the object have
	double xCoord[], yCoord[], polyCoord[]; //Arrays to hold the x and y coordinates for the generated polygon
	int maxVelocity = 30; //Maximum velocity that the object may move
	int j = 0; //Counter for polyArray formation
	Polygon objectDescription; //Polygon describing the object
	Bounds boundary; //Boundary conditions for the object
	Random randomNumber = new Random(); //Random number generator
	
	gObject(Group primaryGroup, double winWidth, double winHeight) {
		
		//Create random spawning point
		x = randomNumber.nextInt((int)winWidth);
		y = randomNumber.nextInt((int)winHeight);
		xv = randomNumber.nextInt(maxVelocity)+30; //Create random speeds within a given tolerance
		yv = randomNumber.nextInt(maxVelocity)+30; //Create random speeds within a given tolerance

		
		if (randomNumber.nextDouble() > 0.5) { //Given a 50 percent chance
			xv = -xv; //Reverse the x velocity
		}
		if (randomNumber.nextDouble() > 0.5) { //Given a 50 percent chance
			yv = -yv; //Reverse the y velocity
		}
		
		size = randomNumber.nextInt(20) + 20 * 3; //Set the object size between 10 and 30
		
		numVertices = randomNumber.nextInt(5) + 5; //Object has between 5 and 10 vertices
		xCoord = new double[numVertices]; //Create an appropriately sized array for the x coordinates
		yCoord = new double[numVertices]; //Create an appropriately sized array for the y coordinates
		polyCoord = new double[numVertices*2]; //Create  an array to hold all coordinate values in the style needed for the polygon object

		for (int i = 0; i < numVertices; i++) { //For the number of vertices needed for the object
			polyCoord[j]= x + randomNumber.nextInt(20) + size * Math.cos(Math.toRadians(360/numVertices*i)); //Set a random x coordinate, centered about the object position
			polyCoord[j+1] = y + randomNumber.nextInt(20) + size * Math.sin(Math.toRadians(360/numVertices*i)); //Set a random y coordinate, centered about the object position
			j+=2; //Advance the polyArray counter ahead by 2
		}
		objectDescription = new Polygon(polyCoord); //Create a new polygon based on the polyArray 
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection
		objectDescription.setStroke(Color.WHITE); //Set object stroke color
		objectDescription.setLayoutX(x); //Indicate where on the x layout the object is
		objectDescription.setLayoutY(y); //Indicate where on the y layout the object is
		primaryGroup.getChildren().add(objectDescription); //Add the object to the primary group
	}

	public void draw(GraphicsContext gc, double winWidth, double winHeight, double frameRate) {
		//Update velocity impact on position
		x += (xv/frameRate); //Move the central x position
		y += (yv/frameRate); //Move the central y position

		//Set window boundary conditions
		if (x >= winWidth) { //If the center of the object goes past the right of the screen
			xv= -xv; //Reverse direction
		}
		else if (y >= winHeight) { //If the center of the object goes past the bottom of the screen
			yv = -yv; //Reverse direction
		}
		else if (x <= -1) { //If the center of the object goes past the left of the screen
			xv= -xv; //Reverse direction
		}
		else if (y <= -1) { //If the center of the object goes past the top of the screen
			yv = -yv; //Reverse direction
		}		

		//Move the object 
		objectDescription.relocate(x-size, y-size); //Translate the object to its new x position
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection
	}
	
	public void erase(Group primaryGroup) { //If the object needs to be erased
		primaryGroup.getChildren().remove(objectDescription); //Remove the object from the primary group	
	}

}
