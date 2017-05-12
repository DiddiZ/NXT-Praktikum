import lejos.nxt.*;
import segoway.Segoway;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.Bot.WHEEL_DIAMETER;

import communication.PCCom;
import lejos.nxt.Sound;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.MotorController;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.SensorData;

public class SegwayMain 
{ 

	private static Segoway segWay;
	private static PCCom communicator;
	private static SegwayController segControl;
	int speed = 50;
   
 public static void main(String [] args) throws InterruptedException 
 {  
  System.out.println("Segway Controls:");
  System.out.println("");
  System.out.println("Balance    RIGHT");
  System.out.println("");
  System.out.println("Bluetooth   LEFT");
  System.out.println("");
  System.out.println("Exit      ESCAPE");
  
  MotorController motCtrl = new MotorController();
  
  communicator = new PCCom();
  communicator.setPriority(Thread.NORM_PRIORITY); 
  communicator.setDaemon(true);
  if (!communicator.registerCallback(motCtrl, 0))
	  System.out.println("failed to register 0");
  if (!communicator.registerCallback(motCtrl, 1))
	  System.out.println("failed to register 1");
  if (!communicator.registerCallback(motCtrl, 2))
	  System.out.println("failed to register 2");
  if (!communicator.registerCallback(motCtrl, 3))
	  System.out.println("failed to register 3");
 // communicator.registerCallback(f->{WEIGHT_MOTOR_DISTANCE = f * 360 / Math.PI / WHEEL_DIAMETER * 2;}, 0);
  
  
  
  while(Button.ESCAPE.isUp())
  { 
		  
	 if (Button.RIGHT.isDown()) {		 
		 SensorData.init();		 
		 playBeeps(3);
		 MotorController.run();
	 }
	 
	 if (Button.LEFT.isDown()) {
		 if (!communicator.getConnected() && !communicator.getConnecting()) 
			 communicator.connect();
	 }
  }
  communicator.disconnect();
 }//End main
 
	/**
	 * Plays a number of beeps and counts down. Each beep is one second.
	 */
	private static void playBeeps(int number) {
		System.out.println("About to start");
		for (int c = number; c > 0; c--) {
			System.out.print(c + " ");
			Sound.playTone(440, 100);
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {}
		}
		System.out.println("GO");
	}

 
}//NXTtr Class
