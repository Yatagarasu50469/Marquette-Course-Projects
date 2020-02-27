/*
 *  Class: UserConsole.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 8 March 2018
 *  Date Last Modified: 9 March 2018
 *  Purpose: Form the user console object. 
 */

import javafx.scene.paint.*;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.*;

public class UserConsole extends GameObj{
	Group consoleGroup = new Group(); //Create a group to hold all nodes for the console object
	Rectangle background, w1, w2, w3, w4, el1, el2, el3; //w: weapon; el: extra life
	boolean w2Allowed = true; //Is the second weapon type allowed
	boolean w3Allowed = true; //Is the third weapon type allowed
	boolean w4Allowed = true; //Is the fourth weapon type allowed
	double timeDiff = 1; //Time remaining for a level
	int minutesRemaining = 0; //Minutes remaining for a level
	int secondsRemaining = 0; //Seconds remaining for a level
	Text t1 = new Text(); //New text node label
	Text t2 = new Text(); //New text node
	Text t3 = new Text(); //New text node
	Text t4 = new Text(); //New text node
	Text timeRemaining = new Text(); //New text node indicating the remaining time in the level 
	Text playerScore = new Text(); //New text node indicating the player's current score 
	Font headerFont = Font.font("TimesRoman", 15.0); //Set a font for labels
	Font timeScoreFont = Font.font("TimesRoman", 30.0); //Set a font for the time remaining and score display
	
	UserConsole (Group primaryGroup, GraphicsContext gc, double winWidth, double winHeight) { //Constructor for a new user console
		this.winHeight = winHeight; //Store the current window height
		this.winWidth = winWidth; //Store the current window width
		velocity = 0; //Set initial velocity to 0
		angle = 90; //Set initial angle that the ship points
		x = 0; //Set the x position
		y = winHeight - 100; //Set the y position
		background = new Rectangle(x, y, winWidth, 100); //Create a background rectangle for the console
		background.setFill(Color.GRAY); //Set object fill
		background.setLayoutX(x); //Indicate where on the x layout the object is
		background.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(background); //Add the background node to the console group
		background.relocate(x,y); //Move the object to the appropriate x and y coordinates
		
		x = 50; //Advance the x position
		y = winHeight - 70; //Shift up the y position
		w1 = new Rectangle(x, y, 40, 40); //Create a new rectangle for weapon 1
		w1.setFill(Color.GRAY); //Fill with gray to indicate that the weapon is unavailable
		w1.setStroke(Color.BLACK); //Stroke the rectangle
		w1.setLayoutX(x); //Indicate where on the x layout the object is
		w1.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(w1); //Add the node to the console group
		w1.relocate(x,y); //Move the object to the appropriate x and y coordinates		
		
		t1.setFont(headerFont); //Set the font for the label
		t1.setText("WEAPONS"); //Set the text
		t1.setLayoutX(x); //Indicate where on the x layout the object is
		t1.setLayoutY(y-20); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(t1); //Add the text node to the console group
		t1.relocate(x, y-20); //Move the object to the appropriate x and y coordinates		
		
		x+=45; //Advance the x position
		w2 = new Rectangle(x, y, 40, 40); //Create a new rectangle for weapon 2
		w2.setFill(Color.GRAY); //Fill with gray to indicate that the weapon is unavailable
		w2.setStroke(Color.BLACK); //Stroke the rectangle
		w2.setLayoutX(x); //Indicate where on the x layout the object is
		w2.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(w2); //Add the node to the console group
		w2.relocate(x,y); //Move the object to the appropriate x and y coordinates		
				
		x+=45; //Advance the x position
		w3 = new Rectangle(x, y, 40, 40); //Create a new rectangle for weapon 3
		w3.setFill(Color.GRAY); //Fill with gray to indicate that the weapon is unavailable
		w3.setStroke(Color.BLACK); //Stroke the rectangle
		w3.setLayoutX(x); //Indicate where on the x layout the object is
		w3.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(w3); //Add the node to the console group
		w3.relocate(x,y); //Move the object to the appropriate x and y coordinates		
		
		x+=45; //Advance the x position
		w4 = new Rectangle(x, y, 40, 40); //Create a new rectangle for weapon 4
		w4.setFill(Color.GRAY); //Fill with gray to indicate that the weapon is unavailable
		w4.setStroke(Color.BLACK); //Stroke the rectangle 
		w4.setLayoutX(x); //Indicate where on the x layout the object is
		w4.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(w4); //Add the node to the console group
		w4.relocate(x,y); //Move the object to the appropriate x and y coordinates		
		
		x+=150; //Advance the x position
		el1 = new Rectangle(x, y, 40, 40); //Create a new rectangle for the first extra life
		el1.setFill(Color.BLUE); //Set the fill color
		el1.setStroke(Color.BLACK); //Stroke the rectangle
		el1.setLayoutX(x); //Indicate where on the x layout the object is
		el1.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(el1); //Add the rectangle node to the console group
		el1.relocate(x,y); //Move the object to the appropriate x and y coordinates		
				
		t2.setFont(headerFont); //Set the font for the label
		t2.setText("EXTRA LIVES"); //Set the text
		t2.setLayoutX(x); //Indicate where on the x layout the object is
		t2.setLayoutY(y-20); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(t2);
		t2.relocate(x, y-20); //Move the object to the appropriate x and y coordinates		
		
		x+=45; //Advance the x position
		el2 = new Rectangle(x, y, 40, 40);  //Create a new rectangle for the second extra life
		el2.setFill(Color.BLUE); //Set the fill color
		el2.setStroke(Color.BLACK); //Stroke the rectangle
		el2.setLayoutX(x); //Indicate where on the x layout the object is
		el2.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(el2); //Add the rectangle node to the console group
		el2.relocate(x,y); //Move the object to the appropriate x and y coordinates				

		x+=45; //Advance the x position
		el3 = new Rectangle(x, y, 40, 40); //Create a new rectangle for the third extra life
		el3.setFill(Color.BLUE); //Set the fill color
		el3.setStroke(Color.BLACK); //Stroke the rectangle
		el3.setLayoutX(x); //Indicate where on the x layout the object is
		el3.setLayoutY(y); //Indicate where on the y layout the object is
		consoleGroup.getChildren().add(el3); //Add the rectangle node to the console group
		el3.relocate(x,y); //Move the object to the appropriate x and y coordinates	
		
		x+=100; //Advance the x position
		t3.setFont(headerFont); //Set the font for the label
		t3.setText("TIME REMAINING"); //Set text
		t3.setLayoutX(x); //Set the text node's x layout position
		t3.setLayoutY(y-20); //Set the text node's y layout position
		consoleGroup.getChildren().add(t3); //Add the text node to the console group
		t3.relocate(x, y-20); //Move the object to the appropriate x and y coordinates		
		
		timeDiff = primary.maxLevelTime - primary.levelTimer; //Calculate the remaining seconds left for the level
		minutesRemaining = (int) (timeDiff/60.0); //Calculate then number of minutes remaining for the level
		secondsRemaining = (int) (timeDiff-(60.0 * minutesRemaining)); //Isolate the number of seconds less than a minute remaining for the level
		timeRemaining.setFont(timeScoreFont); //Set the font for the remaining time display
		timeRemaining.setText(minutesRemaining + ":" + String.format("%02d", secondsRemaining)); //Update the remaining time displayed in the console
		timeRemaining.setLayoutX(x); //Set the text node's x layout position
		timeRemaining.setLayoutY(y); //Set the text node's y layout position
		consoleGroup.getChildren().add(timeRemaining); //Add the text node to the console group
		timeRemaining.relocate(x, y); //Relocate the text node to the position on screen indicated
			
		x+=200; //Advance the x position
		t4.setFont(headerFont);  //Set the font for the label
		t4.setText("SCORE"); //Set text
		t4.setLayoutX(x); //Set the text node's x layout position
		t4.setLayoutY(y-15); //Set the text node's y layout position
		consoleGroup.getChildren().add(t4); //Add the text node to the console group
		t4.relocate(x, y-20); //Relocate the text node to the position on screen indicated
		
		playerScore.setFont(timeScoreFont); //Set the font for the player's current score
		playerScore.setText(String.format("%010d", primary.score)); //Set text
		playerScore.setLayoutX(x); //Set the text node's x layout position
		playerScore.setLayoutY(y); //Set the text node's y layout position
		consoleGroup.getChildren().add(playerScore); //Add the text node to the console group
		playerScore.relocate(x, y);	//Relocate the text node to the position on screen indicated
		primaryGroup.getChildren().add(consoleGroup); //Add the console group's children to the primary group

	}

	public void setWeapon(int weapon) { //If the player switches their active  weapon
		
		if ((weapon == 2) && (!w2Allowed)) { //If the weapon is not available 
			return; //Don't do anything
		}
		if ((weapon == 3) && (!w3Allowed)) { //If the weapon is not available 
			return; //Don't do anything
		}
		if ((weapon == 4) && (!w4Allowed)) { //If the weapon is not available 
			return; //Don't do anything
		}

		w1.setFill(Color.WHITE); //Weapon 1 is always available
		if (w2Allowed) { //If the second weapon is allowed
			w2.setFill(Color.WHITE); //Indicate this by turning the box white
		}
		else { //Otherwise
			w2.setFill(Color.GRAY); //Indicate that it is not available by turning it gray
		}
		if (w3Allowed) { //If the third weapon is allowed
			w3.setFill(Color.WHITE); //Indicate this by turning the box white
		}
		else { //Otherwise
			w3.setFill(Color.GRAY); //Indicate that it is not available by turning it gray
		}
		if (w4Allowed) { //If the fourth weapon is allowed
			w4.setFill(Color.WHITE); //Indicate this by turning the box white
		}
		else { //Otherwise
			w4.setFill(Color.GRAY); //Indicate that it is not available by turning it gray
		}
		if (weapon == 1) { //If the first weapon is being selected 
			w1.setFill(Color.RED); //Indicate this by turning the box red
			primary.objects.getFirst().weaponType = 1; //Change the ship's active weapon selection
		}
		if (weapon == 2) { //If the second weapon is being selected 
			w2.setFill(Color.RED); //Indicate this by turning the box red
			primary.objects.getFirst().weaponType = 2;  //Change the ship's active weapon selection
		}
		if (weapon == 3) { //If the third weapon is being selected 
			w3.setFill(Color.RED); //Indicate this by turning the box red
			primary.objects.getFirst().weaponType = 3;  //Change the ship's active weapon selection
		}
		if (weapon == 4) { //If the fourth weapon is being selected 
			w4.setFill(Color.RED); //Indicate this by turning the box red
			primary.objects.getFirst().weaponType = 4;  //Change the ship's active weapon selection
		}
		primary.weaponSwitch.play();
	}
	
	public void lostLife(int lives) { //Should the player have lost a life
		if (lives == 3) { //If the number of lives remaining is 3
			el3.setOpacity(0); //Hide the third extra life indicator
		}
		if (lives == 2) { //If the number of lives remaining is 2
			el2.setOpacity(0); //Hide the second extra life indicator
		}
		if (lives == 1) { //If the number of lives remaining is 1
			el1.setOpacity(0); //Hide the last extra life indicator
		}
	}
	
	public void updateData() { //Update the information displayed within the console
		timeDiff = primary.maxLevelTime - primary.levelTimer; //Calculate the remaining seconds left for the level
		minutesRemaining = (int) (timeDiff/60.0); //Calculate then number of minutes remaining for the level
		secondsRemaining = (int) (timeDiff-(60.0 * minutesRemaining)); //Isolate the number of seconds less than a minute remaining for the level
		timeRemaining.setText(minutesRemaining + ":" + String.format("%02d", secondsRemaining)); //Update the remaining time displayed in the console
		playerScore.setText(String.format("%010d", primary.score)); //Update the score displayed in the console
	
	}

	public void show() { //Show the console on screen
		consoleGroup.relocate(0, (winHeight-100)); //Relocate the console to the bottom of the screen
	}
	
	public void hide() { //Hide the console off screen
		consoleGroup.relocate(winWidth, -200); //Relocate the console to a location off screen
		
	}
}