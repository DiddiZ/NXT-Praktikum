package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx;

import static de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.NXT.COMMUNICATOR;
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
		}
		COMMUNICATOR.disconnect();
	}
}
