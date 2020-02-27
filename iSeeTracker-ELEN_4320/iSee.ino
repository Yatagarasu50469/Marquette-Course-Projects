/*
  Program: EECE_4320_Project - MotorTrack
  Version: 1.0
  Author: David Helminiak
  Date Created: 4 March 2018
  Date Last Modified: 4 March 2018
  Purpose: Turn a pair of stepper motors based on serial input to track an object. 
 */

int time = 4; //Delay between impulses to the stepper motor; microseconds
//16 microseconds per step; 510 steps per revolution; 
boolean xc = false; //Should the x-axis motor turn clockwise 
boolean xcc = false; //Should the x-axis motor turn counter clockwise
boolean yc = false; //Should the y-axis motor turn clockwise 
boolean ycc = false; //Should the y-axis motor turn counter clockwise
int cmmd = 1; //Last serial command issued; default no movement
int turnStep = 1; //Which turn step is active

void setup() {
  
  Serial.begin(9600); //Define serial communication port
  Serial.setTimeout(10); //Reduce wait time for reading from serial port 
  //Set digital output pins for the motor control shield
  pinMode(2 , OUTPUT);
  pinMode(3 , OUTPUT);
  pinMode(4 , OUTPUT);
  pinMode(5 , OUTPUT); 
  pinMode(6 , OUTPUT);
  pinMode(7 , OUTPUT); 
  pinMode(8 , OUTPUT); 
  pinMode(9 , OUTPUT); 
  
  //Initialize all pins to have a low output
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
}

void loop () {
  if (Serial.available() > 0) { //If there is data avaliable in the serial port buffer
    cmmd = Serial.parseInt(); //Parse that command into an integer and store it
  }
  if (cmmd == 1) { //Stop x
    xc = false; //Do not turn x-axis clockwise
    xcc = false; //Do not turn x-axis counterclockwise
    //Reset x-axis motor pins to low
    digitalWrite(2, LOW);
    digitalWrite(3, LOW);
    digitalWrite(4, LOW);
    digitalWrite(5, LOW);
  }
  if (cmmd == 2) { //Turn clockwise
    xcc = false; //Do not turn x-axis counterclockwise
    xc = true; //Turn x-axis clockwise
  } 
  if (cmmd == 3) { //Turn x counterclockwise
    xc = false; //Do not turn x-axis clockwise
    xcc = true; //Turn x-axis counterclockwise
  }
  if (cmmd == 4) { //Stop y
    yc = false; //Do not turn y-axis clockwise
    ycc = false; //Do not turn y-axis counterclockwise
    //Reset y-axis motor pins to low
    digitalWrite(6, LOW);
    digitalWrite(7, LOW);
    digitalWrite(8, LOW);
    digitalWrite(9, LOW); 
  }
  if (cmmd == 5) { //Turn y clockwise
    ycc = false; //Do not turn y-axis counterclockwise
    yc = true; //Turn y-axis clockwise
  }
  if (cmmd == 6) { //Turn y counterclockwise
    yc = false; //Do not turn y-axis clockwise
    ycc = true; //Turn y-axis counterclockwise
  }
  
  if (xc) {
    digitalWrite(5, LOW);
    digitalWrite(2, HIGH);
    delay(time);
    digitalWrite(2, LOW);
    digitalWrite(3, HIGH);
    delay(time);      
    digitalWrite(3, LOW);
    digitalWrite(4, HIGH);
    delay(time);      
    digitalWrite(4, LOW);
    digitalWrite(5, HIGH);
    delay(time);
    Serial.write(0);
    cmmd = 1;
  }
  if (xcc) {
    digitalWrite(2, LOW);
    digitalWrite(5, HIGH);
    delay(time);
    digitalWrite(5, LOW);
    digitalWrite(4, HIGH);
    delay(time);
    digitalWrite(4, LOW);
    digitalWrite(3, HIGH);
    delay(time);
    digitalWrite(3, LOW);
    digitalWrite(2, HIGH);
    delay(time);
    Serial.write(0);
    cmmd = 1;
  }
  if (yc) {
    digitalWrite(9, LOW);
    digitalWrite(6, HIGH);
    delay(time);
    digitalWrite(6, LOW);
    digitalWrite(7, HIGH);
    delay(time);      
    digitalWrite(7, LOW);
    digitalWrite(8, HIGH);
    delay(time);      
    digitalWrite(8, LOW);
    digitalWrite(9, HIGH);
    delay(time);
    Serial.write(0);
    cmmd = 4;
  }
  if (ycc) {
    digitalWrite(6, LOW);
    digitalWrite(9, HIGH);
    delay(time);
    digitalWrite(9, LOW);
    digitalWrite(8, HIGH);
    delay(time);
    digitalWrite(8, LOW);
    digitalWrite(7, HIGH);
    delay(time);
    digitalWrite(7, LOW);
    digitalWrite(6, HIGH);
    delay(time);
    Serial.write(0);
    cmmd = 4;
  }  
  
  
}
