/*
 *  Class: newWeaponType.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 10 March 2018
 *  Date Last Modified: 10 March 2018
 *  Purpose: Provide methods for allowing new weapon types
 */
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class newWeaponType {
	Text t1;
	Font font;
	int type;
	double x, y, xSize, ySize, velocity;
	double polyCoord[];
	String name;
	Random rand = new Random(); //Random number generator
	Polygon objectDescription; //Polygon describing the object
	Bounds boundary; //Boundary conditions for the object
	Group newWeaponGroup = new Group(); //Create a group to hold all nodes for this object
	boolean offBottom = false; //Has the object gone off the bottom of the screen

	newWeaponType(Group primaryGroup, double startX, double startY, double winWidth, double winHeight, int wType) {
		this.type = wType;
		this.x = startX;
		this.y = startY;
		xSize = 40;
		ySize = 40;
		velocity = 150; //Set the velocity
		name = "newWeaponType";
		
		x = rand.nextInt((int)winWidth); //Generate random x position
		y = -50; //Generate the object off screen
		
		t1 = new Text(); //New text node
		try {
			font = Font.loadFont(new FileInputStream(new File("data/PressStart2P.ttf")), 15);
		} catch (FileNotFoundException e) {
			if (primary.debugMode) { System.out.println("Font not found"); }
		}

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
		objectDescription.setStroke(Color.BLUE); //Set object stroke color
		objectDescription.setStrokeWidth(3);
		objectDescription.setFill(Color.BLACK);
		objectDescription.setLayoutX(x); //Indicate where on the x layout the object is
		objectDescription.setLayoutY(y); //Indicate where on the y layout the object is
		newWeaponGroup.getChildren().add(objectDescription); //Add the object to the object group
		objectDescription.relocate(x,y);
		
		t1.setFill(Color.WHITE); //Fill with white
		t1.setFont(font); //Set the font for the label
		t1.setLayoutX(x+8); //Indicate where on the x layout the object is
		t1.setLayoutX(y+15); //Indicate where on the y layout the object is
		if (type == 2) { //If weapon type 2
			t1.setText("W2"); //Set the text
		}
		else if (type == 3) { //If weapon type 3
			t1.setText("W3"); //Set the text
		}
		else if (type == 4) { //If weapon type 4
			t1.setText("W4"); //Set the text
		}
		newWeaponGroup.getChildren().add(t1); //Add the text node to the console group
		t1.relocate(x+8,y+15);
		primaryGroup.getChildren().add(newWeaponGroup); //Add the object's group to the primary group
		newWeaponGroup.relocate(x,y);
		
	}
	
	public void update(Group primaryGroup, double winWidth, double winHeight, double frameRate) {
		y+=velocity/frameRate; //Move the object down
		if (x >= winWidth) { //If the center of the object goes past the right of the screen
			x = winWidth; //Keep it on the right hand side of the screen
		}
		if (x <= 0) { //If the center of the object goes past the left of the screen
			x = 0; //Keep it on the left hand side of the screen
		}
		else if (y >= winHeight) { //If the center of the object goes past the bottom of the screen
			offBottom = true; //Indicate that the object has gone past the bottom of the screen
		}
		newWeaponGroup.relocate(x,y); //Move the object to the appropriate x and y coordinates
		boundary = newWeaponGroup.getBoundsInParent(); //Save the boundary conditions for collision detection		
	}
	
	public void erase(Group primaryGroup) { //If the object needs to be erased
		primaryGroup.getChildren().remove(newWeaponGroup); //Remove the object from the primary group	
	}
	
}
