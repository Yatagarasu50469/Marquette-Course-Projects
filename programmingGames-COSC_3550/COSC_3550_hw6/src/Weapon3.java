/*
 *  Class: Weapon2.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 9 March 2018
 *  Date Last Modified: 9 March 2018
 *  Purpose: Provide methods for the third weapon type
 */
 
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Weapon3 extends Weapon {

	Weapon3(Group primaryGroup, double x, double y, double angle, double shipVelocity, double winWidth, double winHeight, boolean player) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.velocity = shipVelocity;
		name = "weapon3";
		velocity += 200; //Set the weapon1 velocity as the ship's + 200
		playerOwned = player; //Indicate whether this object is owned by the player
		durability = 3;
		xSize = 10;
		ySize = 40;
		
		//Set polygon coordinates
		polyCoord = new double[8];
		polyCoord[0] = (winWidth/2.0);
		polyCoord[1] = (winHeight/2.0)-ySize;
		
		polyCoord[2] = (winWidth/2.0)+xSize; 
		polyCoord[3] = (winHeight/2.0)-ySize; 
		
		polyCoord[4] = (winWidth/2.0)+xSize;
		polyCoord[5] = (winHeight/2.0);
		
		polyCoord[6] = (winWidth/2.0);
		polyCoord[7] = (winHeight/2.0);
		
		objectDescription = new Polygon(polyCoord); //Create a new polygon based on the polyArray 
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection
		objectDescription.setStroke(Color.RED); //Set object stroke color
		objectDescription.setStrokeWidth(3);
		objectDescription.setLayoutX(x); //Indicate where on the x layout the object is
		objectDescription.setLayoutY(y); //Indicate where on the y layout the object is
		primaryGroup.getChildren().add(objectDescription); //Add the object to the primary group
	}

}
