/**
 *  Class: EnemyType5.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 10 March 2018
 *  Date Last Modified: 10 March 2018
 *  Purpose: Provide methods for objects used within the primary program
 */

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.*;

public class EnemyType5 extends GameObj {
	int size = 0;
	int maxVelChange = 30;
    int maxVelocity = 150; //Maximum velocity that the object can move
    int minVelocity = 50; //Minimum velocity that the object can move
    double minTimeToFire = 1; //Minimum time to fire
    int maxTimeToFire = 3; //Maximum additional time to fire
	
	EnemyType5(Group primaryGroup, double winWidth, double winHeight, double startX, double startY) {
		velocityDownFlag = true; //Enemy starts moving down
		name = "enemy5";
		weaponType = 4;
	    xSize = 80;
	    ySize = 100;
		
		y = rand.nextInt((int)winHeight) - ySize - 100;
		velocity = rand.nextInt(maxVelocity)+minVelocity; //Create random speed within a given tolerance
		newTimeToFire(); //Generate a time to fire the weapon
		angle = 270; //Indicate that the enemy is pointing downwards
		if (rand.nextDouble() > 0.5) {
			velocityUpFlag = true;
			velocityDownFlag = false;
		}
		else {
			velocityUpFlag = false;
			velocityDownFlag = true;
		}
		
		if (rand.nextDouble() > 0.5) { //Given half a chance
			x = -50; //This object starts on the left side
			polyCoord = new double[14];
			polyCoord[0] = (winWidth/2.0);
			polyCoord[1] = (winHeight/2.0)+ySize;
			polyCoord[2] = (winWidth/2.0)+(xSize/2); 
			polyCoord[3] = (winHeight/2.0)+ySize; 
			polyCoord[4] = (winWidth/2.0)+(xSize/2); 
			polyCoord[5] = (winHeight/2.0)+(ySize - ySize/3); 
			polyCoord[6] = (winWidth/2.0)+xSize; 
			polyCoord[7] = (winHeight/2.0)+(ySize/2); 
			polyCoord[8] = (winWidth/2.0)+(xSize/2); 
			polyCoord[9] = (winHeight/2.0)+(ySize/3); 
			polyCoord[10] = (winWidth/2.0)+(xSize/2); 
			polyCoord[11] = (winHeight/2.0); 
			polyCoord[12] = (winWidth/2.0); 
			polyCoord[13] = (winHeight/2.0); 
		}
		else { //Otherwise
			x = winWidth + xSize + 50; //This object starts on the right side
			polyCoord = new double[14];
			polyCoord[0] = (winWidth/2.0)-0;
			polyCoord[1] = (winHeight/2.0)+ySize;
			polyCoord[2] = (winWidth/2.0)-(xSize/2); 
			polyCoord[3] = (winHeight/2.0)+ySize; 
			polyCoord[4] = (winWidth/2.0)-(xSize/2); 
			polyCoord[5] = (winHeight/2.0)+(ySize - ySize/3); 
			polyCoord[6] = (winWidth/2.0)-xSize; 
			polyCoord[7] = (winHeight/2.0)+(ySize/2); 
			polyCoord[8] = (winWidth/2.0)-(xSize/2); 
			polyCoord[9] = (winHeight/2.0)+(ySize/3); 
			polyCoord[10] = (winWidth/2.0)-(xSize/2); 
			polyCoord[11] = (winHeight/2.0); 
			polyCoord[12] = (winWidth/2.0)-0; 
			polyCoord[13] = (winHeight/2.0); 
		}
		
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
