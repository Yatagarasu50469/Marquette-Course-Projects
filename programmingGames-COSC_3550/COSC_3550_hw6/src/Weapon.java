/*
 *  Class: Weapon.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 8 March 2018
 *  Date Last Modified: 9 March 2018
 *  Purpose: Define the basic operation for all weapon types. 
 */

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

public class Weapon {
	double x, y, velocity, angle;
	double xCoord[], yCoord[], polyCoord[]; //Arrays to hold the x and y coordinates for the generated polygon
	double startX, startY, distLimit; //Starting coordinates and limited distance of travel
	int numVertices = 4; //How many vertices should the object have
	Polygon objectDescription; //Polygon describing the object
	Bounds boundary; //Boundary conditions for the object
	int xSize = 5; //Width of the object
	int ySize = 10; //Height of the object
	int durability; //Number of hits the object can take
	boolean outsideWin = false; //Indicate whether the missile is outside of the window
	boolean playerOwned; //Indicate whether the player created this weapon
	String name;
	
public void draw(GraphicsContext gc, double winWidth, double winHeight, double frameRate) {

		//Update velocity impact on position
		x += (velocity/frameRate) * Math.cos(Math.toRadians(angle)); //Update the x coordinate based on the velocity and angle
		y -= (velocity/frameRate) * Math.sin(Math.toRadians(angle)); //Update the y coordinate based on the velocity and angle
	
		//Set window boundary conditions
		if ((name.equals("weapon1")) || (name.equals("weapon3")) || (name.equals("weapon4"))) {
			if (x >= winWidth) { //If the center of the object goes past the right of the screen
				outsideWin = true; //Indicate that the weapon is out of the window boundary
			}
			else if (y >= winHeight) { //If the center of the object goes past the bottom of the screen
				outsideWin = true; //Indicate that the weapon is out of the window boundary
			}
			else if (x <= -1) { //If the center of the object goes past the left of the screen
				outsideWin = true; //Indicate that the missile is out of the window boundary
			}
			else if (y <= -1) { //If the center of the object goes past the top of the screen
				outsideWin = true; //Indicate that the weapon is out of the window boundary
			}		
		}
		
		if (name.equals("weapon2")) { //Additionally if this is weapon 2
			if (x >= startX+distLimit) { //If the center of the object goes past the limit
				outsideWin = true; //Indicate that the weapon is out of the window boundary
			}
			if (y >= startY+distLimit) { ///If the center of the object goes past the limit
				outsideWin = true; //Indicate that the weapon is out of the window boundary
			}
			else if (x <= startX-distLimit) { //If the center of the object goes past the limit
				outsideWin = true; //Indicate that the missile is out of the window boundary
			}
			else if (y <= startY-distLimit) { //If the center of the object goes past the limit
				outsideWin = true; //Indicate that the weapon is out of the window boundary
			}	
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
