package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm.CommunicatorNXT;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.sensors.SensorData;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.nxt.motorcontrol.MotorController;
import lejos.nxt.Button;

public class SegwayMain
{
	private static CommunicatorNXT communicator = new CommunicatorNXT();
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
				if (!communicator.isConnected() && !communicator.isConnecting())
					communicator.connect();
		}
		communicator.disconnect();
	}
}
