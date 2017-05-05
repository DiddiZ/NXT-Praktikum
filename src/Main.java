import lejos.Segoway;
import lejos.nxt.*;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

import lejos.robotics.EncoderMotor;
import lejos.robotics.Gyroscope;

public class Main {
  public static void main (String[] args) {
	  
	  NXTMotor leftMotor = new NXTMotor(MotorPort.A);
	  NXTMotor rightMotor = new NXTMotor(MotorPort.B);
	  GyroSensor gyro = new GyroSensor(SensorPort.S2);
	  double wheelDiameter = 5.6d;
	  
	  
	Segoway segWay = new Segoway((EncoderMotor) leftMotor, (EncoderMotor) rightMotor, (Gyroscope) gyro, wheelDiameter);
  
	while(Button.ESCAPE.isUp())
		if (Button.ENTER.isDown())
			segWay = new Segoway((EncoderMotor) leftMotor, (EncoderMotor) rightMotor, (Gyroscope) gyro, wheelDiameter);
	
  }
}