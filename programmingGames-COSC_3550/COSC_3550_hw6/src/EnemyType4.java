/**
 *  Class: EnemyType4.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 9 March 2018
 *  Date Last Modified: 9 March 2018
 *  Purpose: Provide methods for objects used within the primary program
 */

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.*;

public class EnemyType4 extends GameObj {
	int size = 0;
	int maxVelChange = 30;
    int maxVelocity = 100; //Maximum velocity that the object can move
    int minVelocity = 50; //Minimum velocity that the object can move
    double minTimeToFire = 1; //Minimum time to fire
    int maxTimeToFire = 2; //Maximum additional time to fire
	
	EnemyType4(Group primaryGroup, double winWidth, double winHeight, double startX, double startY) {
		velocityDownFlag = true; //Enemy is always moving down
		name = "enemy4";
	    xSize = 80;
	    ySize = 30;
		x = rand.nextInt((int)winWidth) - xSize;
		y = -50;
		velocity = rand.nextInt(maxVelocity)+minVelocity; //Create random speed within a given tolerance
		newTimeToFire(); //Generate a time to fire the weapon
		angle = 270; //Indicate that the enemy is pointing downwards
		if (rand.nextDouble() > 0.5) {
			angleUpFlag = true;
			angleDownFlag = false;
		}
		else {
			angleUpFlag = false;
			angleDownFlag = true;			
		}

		polyCoord = new double[12];
		polyCoord[0] = (winWidth/2.0);
		polyCoord[1] = (winHeight/2.0)-(ySize/2);
		
		polyCoord[2] = (winWidth/2.0)+(xSize/4); 
		polyCoord[3] = (winHeight/2.0); 
		
		polyCoord[4] = (winWidth/2.0)+(xSize-(xSize/4)); 
		polyCoord[5] = (winHeight/2.0); 
		
		polyCoord[6] = (winWidth/2.0)+xSize; 
		polyCoord[7] = (winHeight/2.0)-(ySize/2); 
		
		polyCoord[8] = (winWidth/2.0)+(xSize-(xSize/4)); 
		polyCoord[9] = (winHeight/2.0)-ySize; 
		
		polyCoord[10] = (winWidth/2.0)+(xSize/4); 
		polyCoord[11] = (winHeight/2.0)-ySize; 
		
		
		objectDescription = new Polygon(polyCoord); //Create a new polygon based on the polyArray 
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection
		objectDescription.setStroke(Color.WHITE); //Set object stroke color
		objectDescription.setLayoutX(x); //Indicate where on the x layout the object is
		objectDescription.setLayoutY(y); //Indicate where on the y layout the object is
		primaryGroup.getChildren().add(objectDescription); //Add the object to the primary group
	}
	
	@Override
	public double newTimeToFire() { //Generate a new time to fire
		timeToFire = ((double)(rand.nextInt(maxTimeToFire))) + minTimeToFire; //Create a random time to fire within a given tolerance
		return timeToFire;
	}

}
