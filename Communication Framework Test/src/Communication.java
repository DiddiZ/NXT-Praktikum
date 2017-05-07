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

	private static Segoway segWay;
	private static PCCom communicator;
	private static SegwayController segControl;
	int speed = 50;
   
 public static void main(String [] args) 
 {
  NXTMotor leftMotor = new NXTMotor(MotorPort.A);
  NXTMotor rightMotor = new NXTMotor(MotorPort.B);
  GyroSensor gyro = new GyroSensor(SensorPort.S2);
  
  double wheelDiameter = 5.6d;
  
  
  System.out.println("Segway Controls:");
  System.out.println("");
  System.out.println("Balance    RIGHT");
  System.out.println("");
  System.out.println("Bluetooth   LEFT");
  System.out.println("");
  System.out.println("Exit      ESCAPE");
  
  segControl = new SegwayController(segWay);
  
  communicator = new PCCom();
  communicator.setPriority(Thread.NORM_PRIORITY);  
  communicator.registerCallback(segControl, 0);
  
  
  
  
  
  while(Button.ESCAPE.isUp())
  { 
	  if (communicator.getConnected()) {
		  	  		
	  }
		  
	 if (Button.RIGHT.isDown()) {		 
		 segWay = new Segoway((EncoderMotor) leftMotor, (EncoderMotor) rightMotor, (Gyroscope) gyro, wheelDiameter);
		 segWay.setPriority(Thread.MAX_PRIORITY);		  
	 }
	 
	 if (Button.LEFT.isDown()) {
		 if (!communicator.getConnected() && !communicator.getConnecting()) 
			 communicator.connect();
	 }
  }
  communicator.disconnect();
 }//End main

 
}//NXTtr Class
