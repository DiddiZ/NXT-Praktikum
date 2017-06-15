package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;
/**
 * @author Gregor & Robin
 * 
 * This class is the main menu on the NXT. It allows to start the communication and the balancing threads.
 */
import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.COMMUNICATOR;

import java.io.IOException;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Button;

public class SegwayMain
{
	int speed = 50;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Segway Controls:");
		System.out.println("");
		System.out.println("Balance    RIGHT");
		System.out.println("");
		System.out.println("Bluetooth   LEFT");
		System.out.println("");
		System.out.println("Exit      ESCAPE");

		while (Button.ESCAPE.isUp()) {

			if (Button.RIGHT.isDown()) {
				SensorData.init();
				Audio.playBeeps(3);
				MotorController.run();
			}

			if (Button.LEFT.isDown())
				if (!COMMUNICATOR.isConnected() && !COMMUNICATOR.isConnecting())
					COMMUNICATOR.connect();
			
			if (Button.ENTER.isDown()) {
				if (COMMUNICATOR.isConnected())
					try {
						System.out.print(".");
						CommunicatorNXT.sendGetReturn((byte) 1, 100);
					} catch (IOException exc) {
						System.out.println("error");
					}
			}
		}
		COMMUNICATOR.disconnect();
	}
}
