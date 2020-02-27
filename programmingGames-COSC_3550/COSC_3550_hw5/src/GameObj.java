/*
 *  Class: Ship.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 8 March 2018
 *  Date Last Modified: 9 March 2019
 *  Purpose: Provide methods for the game objects
 */


import java.util.LinkedList;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;

public class GameObj {
	String name; //Name of the object
	int xSize, ySize; //Width and height of the object as used for window conditions; NOT ACCURATE FOR COLLISIONS
    int maxVelocity = 500; //Maximum velocity that the object can move
    int maxWeapons = 6; //Maximum number of fired weapons allowed on screen
    int weaponType = 1; //What type of weapon is currently engaged
	int numVertices = 4; //How many vertices should the object have
	int j = 0; //Counter for polyArray formation
	double x, y, velocity, winWidth, winHeight, angle;
	double xCoord[], yCoord[], polyCoord[]; //Arrays to hold the x and y coordinates for the generated polygon
	boolean velocityUpFlag = false; //Should the velocity be increasing
	boolean velocityDownFlag = false; //Should the velocity be decreasing
	boolean angleUpFlag = false; //Should the angle be increasing
	boolean angleDownFlag = false; //Should the angle be decreasing
	boolean offBottom = false; //Has the object gone off the bottom of the screen
	boolean fireFlag = false; //Is the object firing a weapon
	boolean angleAlterable = false; //Is the object allowed to alter its angle
	boolean speedAlterable = false; //Is the object allowed to alter its speed
	boolean collision = false; //Has the object been hit by another object or weapon
	static boolean boundaries = true; //Are objects allowed to move outside their traditional boundaries
	Polygon objectDescription; //Polygon describing the object
	Bounds boundary; //Boundary conditions for the object
	Random rand = new Random(); //Random number generator
    public LinkedList<Weapon> weapons = new LinkedList<Weapon>(); //Array holding generated weapons for the object
	
	public void update(Group primaryGroup, double winWidth, double winHeight, double frameRate) {
		
		if (speedAlterable) { //If the speed of the object is able to be altered
			if (velocityUpFlag) { //If the object should move up
				if (velocity < maxVelocity) { //If the current velocity is less than the maximum allowable
					velocity+=1; //Increase the current velocity
				}
			}
			if (velocityDownFlag) { //If the object should move down
				if (velocity > 0) { //If the velocity is greater than 0
					velocity-=1;  //Decrease the velocity
				}
			}
		}
		else { //Speed of the object is not able to be altered
			if (velocityUpFlag) { //If the object should move up
				y-=velocity/frameRate; //Move the object down
			}
			if (velocityDownFlag) { //If the object should move down
				y+=velocity/frameRate; //Move the object up
			}				
			if (angleUpFlag) { //If the object should move to the left
				x-=velocity/frameRate; //Move the object left
			}			
			if (angleDownFlag) { //If the object should move to the right
				x+=velocity/frameRate; //Move the object right
			}					

		}
		if (angleAlterable) { //If the angle of the object is able to be altered
			if (angleUpFlag) { //If the angle should be increased
				angle+=1; //Increase the current angle
			}
			if (angleDownFlag) { //If the angle should be decreased
				angle-=1; //Decrease the current angle
			}
		}
		
		if (fireFlag) { //If the object is firing a weapon
			if (weapons.size() < maxWeapons) { //If the number of weapons on screen is less than the maximum
        		if (weaponType == 1) { //If the first weapon type is selected
        			primary.weapon1Sound.play();
        			weapons.add(new Weapon1(primaryGroup, x-10, y, angle, velocity, winWidth, winHeight)); //Create a new weapon1 at the location of the player's ship
        			weapons.add(new Weapon1(primaryGroup, x+10, y, angle, velocity, winWidth, winHeight)); //Create a new weapon1 at the location of the player's ship
        		}
        		if (weaponType == 2) { //If the second weapon type is selected
        			primary.weapon2Sound.play();
        			
        		}
        		if (weaponType == 3) { //If the third weapon type is selected
        			primary.weapon3Sound.play();
        			
        		}
        		if (weaponType == 4) { //If the fourth weapon type is selected
        			//primary.weapon4Sound.play();
        			
        		}
        	}
			fireFlag=false; //Reset the indicator flag for whether the object is firing a weapon
		}

		//Update velocity impact on position
		if (speedAlterable) { //If the speed is allowed to be variable
			x += (velocity/frameRate) * Math.cos(Math.toRadians(angle)); //Update the x coordinate based on the velocity and angle
			y -= (velocity/frameRate) * Math.sin(Math.toRadians(angle)); //Update the y coordinate based on the velocity and angle
		}
		
		//Set window boundary conditions
		if (boundaries) { //If boundaries are being enforced
			if (name == "ship") { //If the object is a ship
				if (x >= winWidth-xSize) { //If the center of the object goes past the right of the screen
					x = winWidth-xSize; //Keep it on the right hand side of the screen
				}
				if (x <= 0) { //If the center of the object goes past the left of the screen
					x = 0; //Keep it on the left hand side of the screen
				}
				if (y >= winHeight-ySize) { //If the center of the object goes past the bottom of the screen
					y = winHeight-ySize; //Prevent the ship from going further down
				}
				if (y <= 0) { //If the center of the object goes past the top of the screen
					y = 0; //Prevent the ship from going further up
				}
			}
			else { //For all other objects
				if (x >= winWidth) { //If the center of the object goes past the right of the screen
					x = winWidth; //Keep it on the right hand side of the screen
				}
				if (x <= 0) { //If the center of the object goes past the left of the screen
					x = 0; //Keep it on the left hand side of the screen
				}
				else if (y >= winHeight) { //If the center of the object goes past the bottom of the screen
					offBottom = true; //Indicate that the object has gone past the bottom of the screen
				}
		
			}
		}

		//Move the object
		if (angleAlterable) { //If the angle of the object is able to be altered
			objectDescription.setRotate(-angle+90); //Rotate the object according to the current angle
		}
		objectDescription.relocate(x,y); //Move the object to the appropriate x and y coordinates
		boundary = objectDescription.getBoundsInParent(); //Save the boundary conditions for collision detection

	}
		
	public void erase(Group primaryGroup) { //If the object needs to be erased
		primaryGroup.getChildren().remove(objectDescription); //Remove the object from the primary group	
	}
    
}
