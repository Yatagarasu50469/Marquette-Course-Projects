/*
 *  Class: Ship.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 25 January 2018
 *  Date Last Modified: 9 March 2019
 *  Purpose: Provide methods for the player's avatar within the main program
 */
 
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Ship extends GameObj {
	
	public Ship(Group primaryGroup, double winWidth, double winHeight) {
		velocity = 300; //Set  velocity to 300
		angle = 90; //Set initial angle that the ship points
		x = winWidth/2.0; //Set initial x position to center of the screen
		y = winHeight; //Set initial y position to the bottom of the screen
		xSize = 25;
		ySize = 150;
		
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
}
