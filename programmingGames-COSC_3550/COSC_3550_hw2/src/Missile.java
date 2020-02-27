/*
 *  Class: Missile.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 25 January 2018
 *  Date Last Modified: 25 January 2018
 *  Purpose: Provide methods for the player's avatar within the main program
 */
 
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Missile {
	double x, y, velocity, angle;
	double xCoord[], yCoord[], polyCoord[]; //Arrays to hold the x and y coordinates for the generated polygon
	int numVertices = 4; //How many vertices should the object have
	Polygon objectDescription; //Polygon describing the object
	Bounds boundary; //Boundary conditions for the object
	int xSize = 5; //Width of the object
	int ySize = 10; //Height of the object
	boolean outsideWin = false; //Indicate whether the missile is outside of the window
	
	Missile(Group primaryGroup, double x, double y, double angle, double shipVelocity, double winWidth, double winHeight) {
		this.x = x+8;
		this.y = y;
		this.angle = angle;
		this.velocity = shipVelocity;
		velocity += 200; //Set the missile velocity as the ship's + 200
		
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

	public void draw(GraphicsContext gc, double winWidth, double winHeight, double frameRate) {
		
		//Update velocity impact on position
		x += (velocity/frameRate) * Math.cos(Math.toRadians(angle)); //Update the x coordinate based on the velocity and angle
		y -= (velocity/frameRate) * Math.sin(Math.toRadians(angle)); //Update the y coordinate based on the velocity and angle
		//Set window boundary conditions
		if (x >= winWidth) { //If the center of the object goes past the right of the screen
			outsideWin = true; //Indicate that the missile is out of the window boundary
		}
		else if (y >= winHeight) { //If the center of the object goes past the bottom of the screen
			outsideWin = true; //Indicate that the missile is out of the window boundary
		}
		else if (x <= -1) { //If the center of the object goes past the left of the screen
			outsideWin = true; //Indicate that the missile is out of the window boundary
		}
		else if (y <= -1) { //If the center of the object goes past the top of the screen
			outsideWin = true; //Indicate that the missile is out of the window boundary
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
