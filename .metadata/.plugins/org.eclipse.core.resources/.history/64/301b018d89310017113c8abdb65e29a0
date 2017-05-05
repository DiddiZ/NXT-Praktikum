/*
 * September 21, 2009
 * Author by Tawat Atigarbodee
 *
 * Install this program on to NXT brick and use it with NXTremoteControl_TA.java
 *
 * To use this program.
 *  -   Install Lejos 0.8.5
 *  -   Include Lejos_nxj library to the project path
 *  -   Upload the program using lejosdl.bat (I use Eclipse)
 *  -   To exit the program, restart NXT brick (remove battery)
 * 
 * NXT setup
 *  -  Port A for right wheel
 *  -  Port C for left wheel
 *  -  No sensor is needed
 *  
 * Note: This program is a partial of my project file. 
 * I use “USBSend” and “USBReceive” created by Lawrie Griffiths 
 * as a pattern for creating USB communication between PC and NXT. 
 */

import java.io.*;

import lejos.addon.keyboard.KeyEvent;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import segoway.Segoway;
import communication.PCCom;

import lejos.nxt.addon.GyroSensor;
import lejos.robotics.EncoderMotor;
import lejos.robotics.Gyroscope;


public class Communication
{ 



   
 public static void main(String [] args) 
 {
  NXTMotor leftMotor = new NXTMotor(MotorPort.A);
  NXTMotor rightMotor = new NXTMotor(MotorPort.B);
  GyroSensor gyro = new GyroSensor(SensorPort.S2);
  double wheelDiameter = 5.6d;
  int speed = 100;
  
  
  PCCom communicator = new PCCom();
  communicator.setPriority(Thread.NORM_PRIORITY);
  Segoway segWay = new Segoway((EncoderMotor) leftMotor, (EncoderMotor) rightMotor, (Gyroscope) gyro, wheelDiameter);
  segWay.setPriority(Thread.MAX_PRIORITY);
  
  
  while(Button.ESCAPE.isUp())
  { 
	  if (communicator.getConnected()) {
		  int receivedKey = communicator.getNewData();
		  if (receivedKey != -1) {
			  setDriveDirection(segWay, receivedKey  ,speed);  
		  }
		  	  		
	  } else {
		  communicator = new PCCom();
		  communicator.setPriority(Thread.NORM_PRIORITY);
	  }
		  
	 if (Button.ENTER.isDown()) {
		  segWay = new Segoway((EncoderMotor) leftMotor, (EncoderMotor) rightMotor, (Gyroscope) gyro, wheelDiameter);
		  segWay.setPriority(Thread.MAX_PRIORITY);
	 }
  }
 }//End main
 

 
 public static void setDriveDirection(Segoway segWay, int keyCode, int speed) {
	 switch (keyCode) {
		case KeyEvent.VK_W:
			segWay.wheelDriver(speed, speed);
			break;
		case KeyEvent.VK_L:
			segWay.wheelDriver(-speed/4, speed/4);
			break;
		case KeyEvent.VK_R: segWay.wheelDriver(speed/4, -speed/4);
			break;
		case KeyEvent.VK_D: segWay.wheelDriver(-speed, -speed);
		default:
			segWay.wheelDriver(0, 0);
		}
 }
 

 
}//NXTtr Class
