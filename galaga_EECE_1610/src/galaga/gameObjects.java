/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author David
 */
public abstract class gameObjects {

    protected Image img;
    protected static int sxpos; //position in the x direction
    protected static int sypos; //position in the y direction
    protected int swidth; //object width
    protected int sheight; //object height
    protected static int slife; //object's life count, or existence value
    protected int sdeltaX; //Directional value of travel in the x direction
    protected int sdeltaY; //Directional value of travel in the y direction       
    protected int mwidth; //object width
    protected int mheight; //object height
    protected int mlife; //object's life count, or existence value
    protected int mdeltaX; //Directional value of travel in the x direction
    protected int mdeltaY; //Directional value of travel in the y direction
    protected int ewidth; //object width
    protected int eheight; //object height
    protected int elife; //object's life count, or existence value
    protected int edeltaX; //Directional value of travel in the x direction
    protected int edeltaY; //Directional value of travel in the y direction
    protected int e2width; //object width
    protected int e2height; //object height
    protected int e2life; //object's life count, or existence value
    protected int e2deltaX; //Directional value of travel in the x direction
    protected int e2deltaY; //Directional value of travel in the y direction
    protected int e3width; //object width
    protected int e3height; //object height
    protected int e3life; //object's life count, or existence value
    protected int e3deltaX; //Directional value of travel in the x direction
    protected int e3deltaY; //Directional value of travel in the y direction
    protected int e4width; //object width
    protected int e4height; //object height
    protected int e4life; //object's life count, or existence value
    protected int e4deltaX; //Directional value of travel in the x direction
    protected int e4deltaY; //Directional value of travel in the y direction
    protected int e5width; //object width
    protected static int e5height; //object height
    protected int e5life; //object's life count, or existence value
    protected int e5deltaX; //Directional value of travel in the x direction
    protected int e5deltaY; //Directional value of travel in the y direction
    protected int e6width; //object width
    protected static int e6height; //object height
    public Rectangle scollisionBox, s2collisionBox, mcollisionBox, ecollisionBox, e2collisionBox, e3collisionBox, e4collisionBox, e5collisionBox;

    abstract void draw(Graphics g);

    abstract void update(int id);
}