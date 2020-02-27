/**
 *  Class: EnemyType1.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 21 January 2018
 *  Date Last Modified: 9 March 2018
 *  Purpose: Provide methods for objects used within the primary program
 */

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.*;

public class EnemyType1 extends GameObj {
	int maxVelChange = 50;
    int maxVelocity = 150; //Maximum velocity that the object can move
    int minVelocity = 50; //Minimum velocity that the object can mover
	EnemyType1(Group primaryGroup, double winWidth, double winHeight, int startSize, boolean newObject, double startX, double startY, double prevVelocity, int prevSize) {
		timeToFire = 1000000; //Should not fire a weapon
		this.sizeOption = startSize;
		numVertices = 4; //Indicate the number of vertices this object should have
		velocityDownFlag = true; //Enemy is always moving down
		name = "enemy1";
		if (!newObject) { //If this is not a brand new object
			this.x = startX;
			this.y = startY;	
			boundaries = false; 
			if (rand.nextDouble() < 0.5 ) { //Given a 50 percent chance
				this.velocity = prevVelocity + rand.nextInt(maxVelChange); //Positively impact the velocity
			}
			else { //Otherwise
				this.velocity = prevVelocity - rand.nextInt(maxVelChange); //Negatively impact the velocity
			}
			size = (int) (0.5 * (double) prevSize); //Cut the size of the object in half
			angle = (double) rand.nextInt(360);
		}
		else { //Otherwise, this is a new object
			//Create random spawning point
			velocity = rand.nextInt(maxVelocity)+minVelocity; //Create random speed within a given tolerance
			size = rand.nextInt(20) + 20*sizeOption; //Set the object size between 10 and 30
			x = rand.nextInt((int)winWidth-(size/2));
			y = -50;
			angle = 270;
		}
		
		numVertices = rand.nextInt(5) + 5; //Object has between 5 and 10 vertices
		xCoord = new double[numVertices]; //Create an appropriately sized array for the x coordinates
		yCoord = new double[numVertices]; //Create an appropriately sized array for the y coordinates
		polyCoord = new double[numVertices*2]; //Create  an array to hold all coordinate values in the style needed for the polygon object
		for (int i = 0; i < numVertices; i++) { //For the number of vertices needed for the object
			polyCoord[j]= x + rand.nextInt(20) + size * Math.cos(Math.toRadians(360/numVertices*i)); //Set a random x coordinate, centered about the object position
			polyCoord[j+1] = y + rand.nextInt(20) + size * Math.sin(Math.toRadians(360/numVertices*i)); //Set a random y coordinate, centered about the object position
			j+=2; //Advance the polyArray counter ahead by 2
		}
		
		objectDescription = new Polygon(polyCoord); //Create a new polygon based on the polyArray 
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection
		objectDescription.setStroke(Color.WHITE); //Set object stroke color
		objectDescription.setLayoutX(x); //Indicate where on the x layout the object is
		objectDescription.setLayoutY(y); //Indicate where on the y layout the object is
		primaryGroup.getChildren().add(objectDescription); //Add the object to the primary group
	}

}
