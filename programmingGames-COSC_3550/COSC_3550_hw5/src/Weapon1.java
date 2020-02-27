/*
 *  Class: Weapon1.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 25 January 2018
 *  Date Last Modified: 9 March 2018
 *  Purpose: Provide methods for the first weapon type
 */
 
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Weapon1 extends Weapon {

	Weapon1(Group primaryGroup, double x, double y, double angle, double shipVelocity, double winWidth, double winHeight) {
		this.x = x+8;
		this.y = y;
		this.angle = angle;
		this.velocity = shipVelocity;
		velocity += 200; //Set the weapon1 velocity as the ship's + 200
		
		//Set polygon coordinates
		polyCoord = new double[4];
		polyCoord[0] = (winWidth/2.0);
		polyCoord[1] = (winHeight/2.0);
		polyCoord[2] = (winWidth/2.0); 
		polyCoord[3] = (winHeight/2.0)+15; 
		
		objectDescription = new Polygon(polyCoord); //Create a new polygon based on the polyArray 
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection
		objectDescription.setStroke(Color.WHITE); //Set object stroke color
		objectDescription.setStrokeWidth(5);
		objectDescription.setLayoutX(x); //Indicate where on the x layout the object is
		objectDescription.setLayoutY(y); //Indicate where on the y layout the object is
		primaryGroup.getChildren().add(objectDescription); //Add the object to the primary group
	}

}
