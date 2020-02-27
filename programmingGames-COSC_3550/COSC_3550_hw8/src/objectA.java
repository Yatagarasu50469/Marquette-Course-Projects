/*
 *  Class: objectA.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 22 April 2018
 *  Date Last Modified: 24 April 2018
 *  Purpose: Provide methods for object type A
 */

import java.util.Random;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;


public class objectA extends GameObject {
	Box box;
	double rotation = 0;
	boolean rotFlag = false;
	Random random = new Random();
	int red, green, blue;
	double xaxis, yaxis, zaxis;
	int frames = 0;
	
	objectA(Group primaryGroup, double winWidth, double winHeight, double startX, double startY, double startZ, double startDX, double startDY, double startDZ, boolean startRot, PhongMaterial objMaterial) {
		x = startX;
		y = startY;
		z = startZ;
		material = objMaterial;
		rotFlag = startRot;
		sizeX = startDX;
		sizeY = startDY;
		sizeZ = startDZ;
		
		box = new Box(sizeX, sizeY, sizeZ); //Define new box
		box.setMaterial(material); //Set box material
		box.setTranslateX(x); //Set object starting x position
		box.setTranslateY(y); //Set object starting y position
		box.setTranslateZ(z); //Set object starting z position
		primaryGroup.getChildren().add(box); //Add the box to the primary group
		boundary = box.getBoundsInParent(); //Update boundary condition
		box.setRotationAxis(new Point3D(0.0, 1.0, 0.0)); //Set the initial rotation axis
	}
	
	@Override
	void update() {
		frames++;

		
		if (rotFlag) {
			if (rotation >= 360) {
				rotation = 0;
				red = random.nextInt(255);
				green = random.nextInt(255);
				blue = random.nextInt(255);
				material = new PhongMaterial();
				material.setDiffuseColor(Color.rgb(red, green, blue)); //Redefine material diffuse color
				material.setSpecularColor(Color.rgb(red, green, blue)); //Redefine material diffuse color
				box.setMaterial(material); //Set box material
				xaxis = random.nextDouble();
				yaxis = random.nextDouble();
				zaxis = random.nextDouble();
				box.setRotationAxis(new Point3D(xaxis, yaxis, zaxis)); //Set the rotation axis
			}
			box.setRotate(rotation); //Rotate the box
			rotation+=5; //Increase the amount the box should rotate
		}
		
		boundary = box.getBoundsInParent(); //Update boundary condition
	}
}
