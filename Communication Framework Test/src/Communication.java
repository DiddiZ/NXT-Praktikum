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
import lejos.nxt.*;
import lejos.nxt.comm.*;
import segoway.Segoway;

import lejos.nxt.addon.GyroSensor;
import lejos.robotics.EncoderMotor;
import lejos.robotics.Gyroscope;


public class Communication
{ 
  public static DataOutputStream dataOut; 
  public static DataInputStream dataIn;
  public static USBConnection USBLink;
  public static BTConnection BTLink;
  public static BTConnection btLink;
  public static int transmitReceived;
  
  private static boolean isConnected = false;

   
 public static void main(String [] args) 
 {
  connect(); 
  
  NXTMotor leftMotor = new NXTMotor(MotorPort.A);
  NXTMotor rightMotor = new NXTMotor(MotorPort.B);
  GyroSensor gyro = new GyroSensor(SensorPort.S2);
  double wheelDiameter = 5.6d;
  int speed = 50;
  
  
  Segoway segWay = new Segoway((EncoderMotor) leftMotor, (EncoderMotor) rightMotor, (Gyroscope) gyro, wheelDiameter);
  
  
  while(Button.ESCAPE.isUp())
  { 
	  if (isConnected) {
		  checkCommand(); 
	  		switch (transmitReceived) {
	  		case 1:
	  			segWay.wheelDriver(speed, speed);
	  			break;
	  		case 2:
	  			segWay.wheelDriver(-speed, speed);
	  			break;
	  		case 3: segWay.wheelDriver(speed, -speed);
	  			break;
	  		default:
	  			segWay.wheelDriver(0, 0);
	  		}
	  }
	  else {
		  connect();
	  }
	  
	  if (Button.ENTER.isDown()) {
		  segWay = new Segoway((EncoderMotor) leftMotor, (EncoderMotor) rightMotor, (Gyroscope) gyro, wheelDiameter);
	  }
  }
 }//End main
 
 public static void checkCommand()//check input data
 {
    
    try {
    	transmitReceived = dataIn.read();
       System.out.print(transmitReceived);

    } catch (IOException ioe) {
       System.out.println("IO Exception readInt");
       USBLink.close();
       isConnected = false;
    }
    
    if (transmitReceived == -1) {
    	System.out.println("Connection lost. Disconnecting...");
    	USBLink.close();
        isConnected = false;
    }
    
 }//End checkCommand
 
 public static void connect()
 {  
    System.out.println("Listening");
    BTLink = Bluetooth.waitForConnection();    
    dataOut = BTLink.openDataOutputStream();
    dataIn = BTLink.openDataInputStream();
   //USBLink = USB.waitForConnection();
   isConnected = true;
   System.out.println("Connected");
   //dataOut = USBLink.openDataOutputStream();
   //dataIn = USBLink.openDataInputStream();
   System.out.print("Waiting for input");

 }//End connect
 
}//NXTtr Class
