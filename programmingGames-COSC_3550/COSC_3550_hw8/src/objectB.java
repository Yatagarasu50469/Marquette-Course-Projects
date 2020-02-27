/*
 *  Class: objectB.java
 *  Version: 1.0
 *  Author: David Helminiak
 *  Date Created: 22 April 2018
 *  Date Last Modified: 24 April 2018
 *  Purpose: Provide methods for object type B
 */

import java.util.Random;

import javafx.scene.Group;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class objectB extends GameObject {
	double radius; //Radius of the sphere
	double mass; //Mass of the sphere
	Sphere sphere; //Object type
	double vx = 0, vy = 0, vz = 0; //Velocities
	double dx = 0, dy = 0, dz = 0; //Amount to move
	double gravity = 0.1; //Amount to move down each frame
	double minVelocity = 0.1;
	double maxVelocityMultiplier = 1.2;
	Random random = new Random();
	int red, green, blue;
	
	objectB(Group primaryGroup, double winWidth, double winHeight, double startX, double startY, double startZ, double startRad, double startMass, PhongMaterial objMaterial) {
		x = startX;
		y = startY;
		z = startZ;
		//material = objMaterial;
		radius = startRad;
		mass = startMass;
		red = random.nextInt(255);
		green = random.nextInt(255);
		blue = random.nextInt(255);
		material = new PhongMaterial();
		material.setDiffuseColor(Color.rgb(red, green, blue)); //Redefine material diffuse color
		material.setSpecularColor(Color.rgb(red, green, blue)); //Redefine material diffuse color
		
		vx = random.nextDouble()*2;
		vy = random.nextDouble()*2;
		vz = random.nextDouble()*2;
		
		if (random.nextDouble() < 0.5) {
			vx = -vx;
		}
		if (random.nextDouble() < 0.5) {
			vy = -vy;
		}
		if (random.nextDouble() < 0.5) {
			vz = -vz;
		}
		sphere = new Sphere(radius); //Define new box
		sphere.setMaterial(material); //Set box material
		sphere.setTranslateX(x); //Set object starting x position
		sphere.setTranslateY(y); //Set object starting y position
		sphere.setTranslateZ(z); //Set object starting z position
		primaryGroup.getChildren().add(sphere); //Add the sphere to the primary group
		boundary = sphere.getBoundsInParent(); //Update boundary condition
	}
	
	@Override
	void update() {
		x = sphere.getTranslateX(); //Update internal x position
		y = sphere.getTranslateY(); //Update internal y position
		z = sphere.getTranslateZ(); //Update internal z position
		
		if (x >= 100 || x <= -100 || collision) {
			vx = -vx;
			if (collision) {
				collision = false;
			}
		}
		
		if (y >= 100 || y <= -100 || collision) {
			vy = -vy;
			if (collision) {
				collision = false;
			}
		}
		
		if (z >= 100 || z <= -100 || collision) {
			vz = -vz;
			if (collision) {
				collision = false;
			}
		}
		
		sphere.setTranslateX(x + vx);
		sphere.setTranslateY(y + vy);
		sphere.setTranslateZ(z + vz);
		boundary = sphere.getBoundsInParent(); //Update boundary condition
	}
	
	
}
