/*
 *  Class: GameObj.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 22 April 2018
 *  Date Last Modified: 24 April 2018
 *  Purpose: Provide methods for most game objects
 */

import javafx.geometry.Bounds;
import javafx.scene.paint.PhongMaterial;

public class GameObject {
	double x, y, z, sizeX, sizeY, sizeZ, xAngle, yAngle; //Object position, angles, size within the game
	Bounds boundary; //Boundary conditions for the object
	PhongMaterial material; //Object material
	boolean collision = false;
	
	void update() { //Update objects
		
	}
}
