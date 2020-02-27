/**
 *  Program: COSC_3550_hw8 - 3D World
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 22 April 2018
 *  Date Last Modified: 24 April 2018
 *  Purpose: Generate an interactive 3D world with multiple objects and shapes using 
 *  rotation and translation methods.
 *  
 *  	==Requirements==			==Implemented==			==Notes==
 *  	Six geometric shapes		Yes
 *  	Two types shapes			Yes						Spheres and boxes
 *  	Rotate or translate method	Yes						Rotate boxes, translate spheres
 *  	Basic user movement			Yes						Removed during extra goal implementation
 *  	
 *   	==Extra Goals==				==Implemented==
 *		Advanced movement			Yes						Go in the direction the camera is pointing
 *		Animation loop				Yes
 *		Collision detection			Yes						For spheres
 *   
 *
 *		==User Controls==			==Action==
 *		Mouse:						Sets camera angle
 *		Key:	UP					Angle up
 *				DOWN				Angle down
 *				LEFT				Angle left
 *				RIGHT				Angle right
 *				w					Increase speed
 *				s					Decrease speed
 *				i					Move up
 *				k					Move down
 *				j					Move left
 *				l					Move right
 *
 *	References:
 *  https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
 *  https://stackoverflow.com/questions/15013913/checking-collision-of-shapes-with-javafx
 */

//Import statements
import java.util.LinkedList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class primary extends Application{
	//Global variables - Alterable
	String winTitle = "3D World"; // Window title
	double winWidth = 1000; //Window screen width
	double winHeight = 1000; //Window screen height
	Color backgroundColor = Color.BLACK; //Base background color
	
	//Global variables - Constant
	Group primaryGroup; //Declare a new group
	Scene primaryScene; //Declare a new scene
	KeyFrame keyFrame; //Declare a key frame for the animation
	Timeline primaryLoop; //Establish an animation timeline
	PerspectiveCamera camera1; //Declare new camera
	Group cameraDolly1; //Declare new camera dolly
	Rotate camera1XRotate, camera1YRotate; //Declare camera rotations
	Point3D cameraDolly1Pos; //Declare camera dolly position points
	PhongMaterial material1, material2; //Declare material types here
	double frameRate = 30; //Animation frame rate; FPS
	double cameraDelta = 10; //How much should the camera move
	double camera1XAngle = 0; //Initial camera 1 X angle
	double camera1YAngle = 0; //Initial camera 1 Y angle
	double speed1 = 0; //Camera 1 speed to move
	double theta1, phi1; //Camera 1 angle measurements in radians
	double distX1, distY1, distZ1; //Camera 1 distances to move
	double mouseX = 0; //Current mouse x position
	double mouseY = 0; //Current mouse y position
	boolean mouseMoved = false; //Has the mouse just moved
    public static LinkedList<GameObject> objects = new LinkedList<GameObject>(); //Array holding program objects
	
	private void update(Stage primaryStage) { //Update each frame
		
		if (mouseMoved) { //If the mouse is moved within the window
			camera1XAngle = ((mouseX/winWidth)*360)-180; //At x center of screen camera 1 x angle is set as 0
			camera1YAngle = ((-mouseY/winHeight)*360)-180; //At y center of screen camera 1 y angle is set as 0; moving up y goes down
			mouseMoved = false; //Reset flag
		}
		
		camera1XRotate.setAngle(camera1XAngle); //Rotate camera 1 x angle
		camera1YRotate.setAngle(camera1YAngle); //Rotate camera 1 y angle
		theta1 = Math.toRadians(camera1XAngle); //Change camera 1 x angle to radians for distance calculations
		phi1 = Math.toRadians(camera1YAngle); //Change camera 1 y angle to radians for distance calculations
		distX1 = Math.sin(theta1) * Math.cos(phi1); //Calculate x distance to move
		distY1 = Math.sin(phi1); //Calculate y distance to move
		distZ1 = Math.cos(theta1) + Math.sin(phi1); //Calculate z distance to move
		cameraDolly1.setTranslateX(cameraDolly1.getTranslateX() + distX1*speed1); //Move camera 1 x position at speed 1
		cameraDolly1.setTranslateY(cameraDolly1.getTranslateY() + distY1*-speed1); //Move camera 1 y position at inverse speed 1
		cameraDolly1.setTranslateZ(cameraDolly1.getTranslateZ() + distZ1*speed1); //Move camera 1 z position at speed 1

		for (int i = 0; i < objects.size(); i++) { //For all of the stored objects
			for (int j = 0; j < objects.size(); j++) { //For all of the stored objects
				if (i != j) { //If the object is not itself
					if (objects.get(i).boundary.intersects(objects.get(j).boundary)) { //Check for boundary intersection
						objects.get(i).collision = true; //Set collision event as true					
					}
				}
				
			}
			
			objects.get(i).update(); //Update the object
		}
		
		primaryStage.setTitle("3D World - X: " + String.valueOf(Math.round(cameraDolly1.getTranslateX())) +" Y: "+ String.valueOf(Math.round(cameraDolly1.getTranslateY())) + " Z: " + String.valueOf(Math.round(cameraDolly1.getTranslateZ())) + " XANGLE: " + String.valueOf(Math.round(camera1XAngle))+ " YANGLE: " + String.valueOf(Math.round(camera1YAngle)) + " SPEED: " + Math.round(speed1* 10.0)/10.0); //Set window title with camera positions
	}
	
	private void genWorld() { //Initialize the 3D world
		AmbientLight worldLight = new AmbientLight(Color.WHITE); //Generate a world light
		primaryGroup.getChildren().add(worldLight); //Add the world light to the primary group
		
		//Generate point lights at set position and add it to the primary group
		PointLight pointLight1 = new PointLight();
		pointLight1.setTranslateX(0);
		pointLight1.setTranslateY(-200);
		pointLight1.setTranslateZ(0);
		primaryGroup.getChildren().add(pointLight1);
		
		PointLight pointLight2 = new PointLight();
		pointLight2.setTranslateX(0);
		pointLight2.setTranslateY(200);
		pointLight2.setTranslateZ(0);
		primaryGroup.getChildren().add(pointLight2);
		
		//Define material types here
		material1 = new PhongMaterial(); //Create a new material type
		material1.setDiffuseColor(Color.GREEN); //Define material diffuse color
		material1.setSpecularColor(Color.BLUE); //Define material specular color

		//Define objects here
		
		//Create a bunch of spheres
		for (int i = 0; i < 100; i ++) {
			objects.add(new objectB(primaryGroup, winWidth, winHeight, 0, 0, 0, 2, 1, material1));
		}
		
		//Create a bunch of boxes
		for (int i = 0; i < 10; i ++) { //Number of columns
			for (int j = 0; j < 10; j ++) { //Number of rows
				objects.add(new objectA(primaryGroup, winWidth, winHeight, -100 + (15*i), -100 + 15*j, 100, 10, 10, 10, true, material1));
			}
			
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception { //Program startup and primary loop
		primaryGroup = new Group(); //Initialize primary group
		genWorld(); //Generate a new 3D world
		primaryScene = new Scene(primaryGroup, winWidth, winHeight, true);
		primaryScene.setFill(Color.BLACK); //Set the background color fill to black
		
		camera1 = new PerspectiveCamera(true); //Create camera 1 object
		camera1.setNearClip(0.1); //Set near clipping distance
		camera1.setFarClip(10000.0); //Set far clipping distances
		cameraDolly1 = new Group(); //Create a new camera dolly
		cameraDolly1.setTranslateX(0); //Set camera 1 dolly x position
		cameraDolly1.setTranslateY(-20); //Set camera 1 dolly y position
		cameraDolly1.setTranslateZ(-300); //Set camera 1 dolly z position
		
		primaryScene.setCamera(camera1); //Set the primary scene's camera as camera1
		cameraDolly1.getChildren().add(camera1); //Add camera 1 to the camera Dolly 1
		primaryGroup.getChildren().add(cameraDolly1);  //Add the camera1 dolly to the primary group
		camera1XRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS); //Rotate camera 1 x-axis; angle, pivot_x, pivot_y, pivot_z, axis
		camera1YRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS); //Rotate camera 1 y-axis; angle, pivot_x, pivot_y, pivot_z, axis
		cameraDolly1Pos = new Point3D(0, 0, 0);
		camera1.getTransforms().addAll(camera1XRotate, camera1YRotate); //Apply rotation scheme to camera 1
		
		primaryScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) { //Should a key press event occur
                switch (event.getCode()) { //Check the key pressed
	                case UP: //Angle up
	                	camera1YAngle++;
	                	break;
	                case DOWN: //Angle down
	                	camera1YAngle--;
	                	break;
	                case LEFT: //Angle left
	                	camera1XAngle--;
	                	break;
	                case RIGHT: //Angle right
	                	camera1XAngle++;
	                	break;
	                case W: //Increase speed
	                	speed1+=0.1;
	                	break;
	                case S: //Decrease speed
	                	speed1-=0.1;
	                	break;
	                case I: //Move up
	                	cameraDolly1.setTranslateY(cameraDolly1.getTranslateY() - 1);
	                	break;
	                case K: //Move down
	                	cameraDolly1.setTranslateY(cameraDolly1.getTranslateY() + 1);
	                	break;	   
	                case J: //Move left
	                	cameraDolly1.setTranslateX(cameraDolly1.getTranslateX() - 1);
	                	break;		                	
	                case L: //Move right
	                	cameraDolly1.setTranslateX(cameraDolly1.getTranslateX() + 1);
	                	break;	     	
	                default:
	                	break;
                }
            }	
		});

	    primaryScene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> { //Whenever the mouse is moved
            mouseX = e.getSceneX(); //Retrieve the current mouse x position
            mouseY = e.getSceneY(); //Retrieve the current mouse y position
            mouseMoved = true; //Indicate that the mouse moved
        });
    
		
		primaryStage.setTitle("3D World"); //Set initial window title
		primaryStage.setScene(primaryScene); //Set the primary scene to the window
		
        //Create animation loop
        keyFrame = new KeyFrame(Duration.seconds(1.0/frameRate), new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(javafx.event.ActionEvent event) { //Should an event occur
        		update(primaryStage); //Update the key frame
        	}
        });
        primaryLoop = new Timeline(keyFrame); //Create a timeline using the keyframe  
        primaryLoop.setCycleCount(Animation.INDEFINITE); //Have the timeline continue to run forever
        primaryLoop.play(); //Play the animation at the set rate
		primaryStage.show(); //Show the window
	}

	//Main method
	public static void main(String[] args) {
		launch(args);
	}
}
